package org.mindera.fur.code.mapper.external_apis;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.external_apis.dog_api.DogBreedDTO;
import org.mindera.fur.code.model.external_apis.dog_api.DogBreed;

/**
 * A mapper class for mapping dog breeds.
 */
@Mapper
public interface DogBreedMapper {

    DogBreedMapper INSTANCE = Mappers.getMapper(DogBreedMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "attributes.name", target = "name")
    @Mapping(source = "attributes.description", target = "description")
    DogBreedDTO toBreedDTO(DogBreed dogBreed);
}
