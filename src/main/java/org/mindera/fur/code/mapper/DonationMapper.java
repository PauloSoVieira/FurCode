package org.mindera.fur.code.mapper;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.donation.DonationCreateDTO;
import org.mindera.fur.code.dto.donation.DonationDTO;
import org.mindera.fur.code.model.Donation;

@Mapper
@Schema(description = "The donation mapper")
public interface DonationMapper {

    /**
     * Singleton instance of the mapper
     */
    DonationMapper INSTANCE = Mappers.getMapper(DonationMapper.class);

    /**
     * Converts a Donation to a DonationDTO
     **/
    DonationDTO toDTO(Donation donation);

    /**
     * Converts a DonationDTO to a Donation
     **/
    Donation toModel(DonationDTO donationDTO);

    /**
     * Converts a DonationCreateDTO to a Donation
     **/
    Donation toModel(DonationCreateDTO donationCreateDTO);
}
