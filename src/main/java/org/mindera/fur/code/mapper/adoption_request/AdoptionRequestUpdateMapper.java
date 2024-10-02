package org.mindera.fur.code.mapper.adoption_request;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.adoptionRequest.AdoptionRequestUpdateDTO;
import org.mindera.fur.code.model.AdoptionRequest;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AdoptionRequestUpdateMapper {

    AdoptionRequestUpdateMapper INSTANCE = Mappers.getMapper(AdoptionRequestUpdateMapper.class);

    AdoptionRequest updateAdoptionRequestFromDto(AdoptionRequestUpdateDTO adoptionRequestUpdateDTO, @MappingTarget AdoptionRequest adoptionRequest);
}
