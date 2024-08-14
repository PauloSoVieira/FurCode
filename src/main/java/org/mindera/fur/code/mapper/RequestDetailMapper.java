package org.mindera.fur.code.mapper;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.requestDetail.RequestDetailCreationDTO;
import org.mindera.fur.code.dto.requestDetail.RequestDetailDTO;
import org.mindera.fur.code.model.RequestDetail;

import java.util.List;

/**
 * Class mapping the RequestDetail.
 */
@Mapper
@Schema(description = "The request detail mapper")
public interface RequestDetailMapper {

    /**
     * Instance of the RequestDetailMapper.
     */
    RequestDetailMapper INSTANCE = Mappers.getMapper(RequestDetailMapper.class);

    /**
     * Method to map a RequestDetail to a RequestDetailDTO.
     *
     * @param requestDetail The RequestDetail to map.
     * @return The RequestDetailDTO.
     */
    @Mapping(source = "personId", target = "personId")
    RequestDetailDTO toDTO(RequestDetail requestDetail);

    /**
     * Method to map a RequestDetailDTO to a RequestDetail.
     *
     * @param requestDetailDTO The RequestDetailDTO to map.
     * @return The RequestDetail.
     */
    RequestDetail toModel(RequestDetailDTO requestDetailDTO);

    /**
     * Method to map a RequestDetailCreationDTO to a RequestDetail.
     *
     * @param requestDetailCreationDTO The RequestDetailCreationDTO to map.
     * @return The RequestDetail.
     */
    @Mapping(source = "personId", target = "personId")
    RequestDetail toModel(RequestDetailCreationDTO requestDetailCreationDTO);

    /**
     * Method to map a list of RequestDetailDTO to a list of RequestDetail.
     *
     * @param requestDetails The list of RequestDetailDTO to map.
     * @return The list of RequestDetail.
     */
    List<RequestDetailDTO> toDTO(List<RequestDetail> requestDetails);

    /**
     * Method to map a list of RequestDetail to a list of RequestDetailDTO.
     *
     * @param requestDetailDTOs The list of RequestDetail to map.
     * @return The list of RequestDetailDTO.
     */
    List<RequestDetail> toModel(List<RequestDetailDTO> requestDetailDTOs);
}
