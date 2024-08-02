package org.mindera.fur.code.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.donation.DonationCreateDTO;
import org.mindera.fur.code.dto.donation.DonationDTO;
import org.mindera.fur.code.model.Donation;

@Mapper
public interface DonationMapper {

    DonationMapper INSTANCE = Mappers.getMapper(DonationMapper.class);

    DonationDTO toDTO(Donation donation);

    Donation toModel(DonationDTO donationDTO);

    Donation toModel(DonationCreateDTO donationCreateDTO);
}
