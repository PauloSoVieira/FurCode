package org.mindera.fur.code.mapper.pet;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.pet.PetBreedCreateDTO;
import org.mindera.fur.code.dto.pet.PetBreedDTO;
import org.mindera.fur.code.model.pet.PetBreed;

/**
 * A mapper class for mapping pet breeds.
 */
@Mapper
public interface PetBreedMapper {

    PetBreedMapper INSTANCE = Mappers.getMapper(PetBreedMapper.class);

    PetBreedDTO toDTO(PetBreed petBreed);

    PetBreed toModel(PetBreedDTO dto);

    PetBreed toModel(PetBreedCreateDTO dto);
}
