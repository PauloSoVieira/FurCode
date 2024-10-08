package org.mindera.fur.code.mapper.pet;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.pet.PetRecordCreateDTO;
import org.mindera.fur.code.dto.pet.PetRecordDTO;
import org.mindera.fur.code.model.pet.PetRecord;

/**
 * A mapper class for mapping pet records.
 */
@Mapper
public interface PetRecordMapper {

    PetRecordMapper INSTANCE = Mappers.getMapper(PetRecordMapper.class);

    @Mapping(source = "pet.id", target = "petId")
    PetRecordDTO toDTO(PetRecord petRecord);

    @Mapping(source = "petId", target = "pet.id")
    PetRecord toModel(PetRecordDTO petRecordDTO);

    PetRecord toModel(PetRecordCreateDTO petRecordCreateDTO);
}
