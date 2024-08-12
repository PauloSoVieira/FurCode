package org.mindera.fur.code.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.requestDetail.RequestDetailCreationDTO;
import org.mindera.fur.code.dto.requestDetail.RequestDetailDTO;
import org.mindera.fur.code.model.RequestDetail;

import java.util.List;

@Mapper
public interface RequestDetailMapper {

    RequestDetailMapper INSTANCE = Mappers.getMapper(RequestDetailMapper.class);

    RequestDetailDTO toDTO(RequestDetail requestDetail);

    RequestDetail toModel(RequestDetailDTO requestDetailDTO);

    RequestDetail toModel(RequestDetailCreationDTO requestDetailCreationDTO);

    List<RequestDetailDTO> toDTO(List<RequestDetail> requestDetails);

    List<RequestDetail> toModel(List<RequestDetailDTO> requestDetailDTOs);
}
