package org.mindera.fur.code.service.external_apis;

import jakarta.validation.Valid;
import org.mindera.fur.code.dto.external_apis.dog_api.DogBreedDTO;
import org.mindera.fur.code.mapper.external_apis.DogBreedMapper;
import org.mindera.fur.code.model.external_apis.dog_api.DogBreedByIdResponse;
import org.mindera.fur.code.model.external_apis.dog_api.DogBreedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Validated
@Service
public class DogApiService {
    private final String breedsUrl = "/breeds";
    private final RestTemplate restTemplate;

    @Value("${dog_api.base.url}")
    private String apiBaseUrl;

    @Autowired
    public DogApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public DogBreedDTO fetchBreedById(@Valid String id) {
        String url = apiBaseUrl + breedsUrl + id;
        try {
            DogBreedByIdResponse response = restTemplate.getForObject(url, DogBreedByIdResponse.class);

            if (response != null && response.getData() != null) {
                return DogBreedMapper.INSTANCE.toBreedDTO(response.getData());
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Breed not found with ID: " + id);
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Breed not found with ID: " + id, e);
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch breed from the external API", e);
            }
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while communicating with the external API", e);
        }
    }

    public List<String> fetchAllBreedsNames() {
        List<String> allBreedsNames = new ArrayList<>();
        String url = apiBaseUrl + breedsUrl;
        boolean hasNextPage = true;

        try {
            while (hasNextPage) {
                DogBreedResponse response = restTemplate.getForObject(url, DogBreedResponse.class);

                if (response != null && response.getData() != null) {
                    // Add the breed names from the current page to the list
                    response.getData().forEach(dogBreed -> allBreedsNames.add(dogBreed.getAttributes().getName()));

                    // Check if there is a next page
                    if (response.getLinks() != null && response.getLinks().getNext() != null) {
                        url = response.getLinks().getNext();
                    } else {
                        hasNextPage = false;
                    }
                } else {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch breeds names from the external API");
                }
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Breeds not found", e);
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Client error occurred while fetching breed names from the external API", e);
            }
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while communicating with the external API", e);
        }

        return allBreedsNames;
    }

    public DogBreedDTO getBreedByName(@Valid String breedName) {
        String url = apiBaseUrl + breedsUrl;
        boolean hasNextPage = true;

        try {
            while (hasNextPage) {
                DogBreedResponse response = restTemplate.getForObject(url, DogBreedResponse.class);

                if (response != null && response.getData() != null) {
                    DogBreedDTO foundBreed = response.getData().stream()
                            .filter(dogBreed -> dogBreed.getAttributes().getName().equalsIgnoreCase(breedName))
                            .map(DogBreedMapper.INSTANCE::toBreedDTO)
                            .findFirst()
                            .orElse(null);

                    if (foundBreed != null) {
                        return foundBreed;
                    }
                    // Check if there is a next page
                    if (response.getLinks() != null && response.getLinks().getNext() != null) {
                        url = response.getLinks().getNext();
                    } else {
                        hasNextPage = false;
                    }
                } else {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch breeds from the external API");
                }
            }
            // If no breed is found after all pages are processed
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Breed not found with name: " + breedName);

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Breed not found: " + breedName, e);
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Client error occurred while fetching breed from the external API", e);
            }
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while communicating with the external API", e);
        }
    }
}
