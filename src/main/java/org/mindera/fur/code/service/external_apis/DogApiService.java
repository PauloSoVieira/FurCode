package org.mindera.fur.code.service.external_apis;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.mindera.fur.code.dto.external_apis.dog_api.DogBreedDTO;
import org.mindera.fur.code.dto.external_apis.dog_api.DogBreedsNamesDTO;
import org.mindera.fur.code.exceptions.external_apis.DogApiException;
import org.mindera.fur.code.mapper.external_apis.DogBreedMapper;
import org.mindera.fur.code.messages.external_apis.DogApiMessages;
import org.mindera.fur.code.model.external_apis.dog_api.DogBreedByIdResponse;
import org.mindera.fur.code.model.external_apis.dog_api.DogBreedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Dog API service class for making requests to the Dog API.
 */
@Validated
@Service
public class DogApiService {
    private static final String BREEDS_URL = "/breeds";
    private final RestTemplate restTemplate;

    @Value("${dog_api.base.url}")
    private String apiBaseUrl;

    @Autowired
    public DogApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Fetches a dog breed by its ID.
     *
     * @param id the ID of the dog breed and description
     * @return the dog breed
     */
    @Cacheable(value = "dogBreeds", key = "#id")
    public DogBreedDTO fetchBreedById(
            @Valid
            @NotBlank(message = "Dog breed ID is required")
            @Size(min = 1, max = 255, message = "Dog breed ID must be between 1 and 255 characters")
            String id
    ) {
        String url = buildUrlWithId(id);
        DogBreedByIdResponse response = executeGetRequest(url, DogBreedByIdResponse.class);
        return mapToBreedDTO(response, id);
    }

    /**
     * Fetches all dog breed names.
     *
     * @return the list of dog breed names
     */
    @Cacheable(value = "allBreedsNames")
    public DogBreedsNamesDTO fetchAllBreedsNames() {
        List<String> allBreedsNames = new ArrayList<>();
        String url = apiBaseUrl + BREEDS_URL;
        boolean hasNextPage = true;

        while (hasNextPage) {
            DogBreedResponse response = executeGetRequest(url, DogBreedResponse.class);
            addBreedNamesToList(response, allBreedsNames);
            url = getNextPageUrl(response);
            hasNextPage = (url != null);
        }
        return new DogBreedsNamesDTO(allBreedsNames);
    }

    /**
     * Fetches a dog breed by its name.
     *
     * @param breedName the name of the dog breed
     * @return the dog breed
     */
    @Cacheable(value = "dogBreedCache", key = "#breedName")
    public DogBreedDTO getBreedByName(
            @Valid
            @NotBlank(message = "Dog breed name is required")
            @Size(min = 1, max = 255, message = "Dog breed name must be between 1 and 255 characters")
            String breedName
    ) {
        String url = apiBaseUrl + BREEDS_URL;
        boolean hasNextPage = true;

        while (hasNextPage) {
            DogBreedResponse response = executeGetRequest(url, DogBreedResponse.class);
            DogBreedDTO foundBreed = findBreedByName(response, breedName);
            if (foundBreed != null) {
                return foundBreed;
            }
            url = getNextPageUrl(response);
            hasNextPage = (url != null);
        }
        throw new DogApiException(DogApiMessages.DOG_API_BREED_NOT_FOUND_WITH_NAME + breedName, NOT_FOUND);
    }

    // Helper Methods
    private String buildUrlWithId(String id) {
        return apiBaseUrl + BREEDS_URL + "/" + id;
    }

    private <T> T executeGetRequest(String url, Class<T> responseType) {
        try {
            return restTemplate.getForObject(url, responseType);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == NOT_FOUND) {
                throw new DogApiException(DogApiMessages.DOG_API_BREED_NOT_FOUND_WITH_URL + url, NOT_FOUND);
            } else {
                throw new DogApiException(DogApiMessages.DOG_API_BREEDS_FAILURE, INTERNAL_SERVER_ERROR);
            }
        } catch (RestClientException e) {
            throw new DogApiException(DogApiMessages.DOG_API_COMMUNICATION_FAILURE, INTERNAL_SERVER_ERROR);
        }
    }

    private DogBreedDTO mapToBreedDTO(DogBreedByIdResponse response, String id) {
        if (response != null && response.getData() != null) {
            return DogBreedMapper.INSTANCE.toBreedDTO(response.getData());
        } else {
            throw new DogApiException(DogApiMessages.DOG_API_BREED_NOT_FOUND_WITH_ID + id, NOT_FOUND);
        }
    }

    private void addBreedNamesToList(DogBreedResponse response, List<String> allBreedsNames) {
        if (response != null && response.getData() != null) {
            response.getData().forEach(dogBreed -> allBreedsNames.add(dogBreed.getAttributes().getName()));
        } else {
            throw new DogApiException(DogApiMessages.DOG_API_BREEDS_NAMES_FAILURE, INTERNAL_SERVER_ERROR);
        }
    }

    private String getNextPageUrl(DogBreedResponse response) {
        return (response.getLinks() != null && response.getLinks().getNext() != null) ? response.getLinks().getNext() : null;
    }

    private DogBreedDTO findBreedByName(DogBreedResponse response, String breedName) {
        if (response != null && response.getData() != null) {
            return response.getData().stream()
                    .filter(dogBreed -> dogBreed.getAttributes().getName().equalsIgnoreCase(breedName))
                    .map(DogBreedMapper.INSTANCE::toBreedDTO)
                    .findFirst()
                    .orElse(null);
        } else {
            throw new DogApiException(DogApiMessages.DOG_API_BREEDS_FAILURE, INTERNAL_SERVER_ERROR);
        }
    }
}
