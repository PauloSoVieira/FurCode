package org.mindera.fur.code.service;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.dto.donation.DonationCreateDTO;
import org.mindera.fur.code.dto.donation.DonationDTO;
import org.mindera.fur.code.exceptions.donation.DonationNotFoundException;
import org.mindera.fur.code.exceptions.donation.InvalidDonationAmountException;
import org.mindera.fur.code.mapper.DonationMapper;
import org.mindera.fur.code.messages.donation.DonationMessages;
import org.mindera.fur.code.model.Donation;
import org.mindera.fur.code.model.Person;
import org.mindera.fur.code.model.Shelter;
import org.mindera.fur.code.repository.DonationRepository;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.ShelterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Schema(description = "The donation service")
public class DonationService {
    private static final Logger logger = LoggerFactory.getLogger(DonationService.class);
    private static final BigDecimal DONATION_AMOUNT_MAX = new BigDecimal("999999");
    private static final BigDecimal DONATION_AMOUNT_MIN = BigDecimal.ZERO;
    private final DonationRepository donationRepository;
    private final ShelterRepository shelterRepository;
    private final PersonRepository personRepository;

    @Autowired
    public DonationService(DonationRepository donationRepository, PersonRepository personRepository, ShelterRepository shelterRepository) {
        this.donationRepository = donationRepository;
        this.shelterRepository = shelterRepository;
        this.personRepository = personRepository;
    }

    private static void donationValidations(DonationCreateDTO donationCreateDTO) {
        if (donationCreateDTO.getTotal().compareTo(DONATION_AMOUNT_MIN) <= 0 ||
                donationCreateDTO.getShelterId() == null ||
                donationCreateDTO.getPersonId() == null) {
            throw new IllegalArgumentException(DonationMessages.DONATION_HAS_INVALID_DATA_FIELDS);
        }
        if (donationCreateDTO.getTotal().compareTo(DONATION_AMOUNT_MAX) >= 0) {
            throw new InvalidDonationAmountException(DonationMessages.DONATION_AMOUNT_EXCEEDS_MAXIMUM);
        }
    }

    public DonationDTO createDonation(DonationCreateDTO donationCreateDTO) {
        logger.info("Creating donation: {}", donationCreateDTO);
        donationValidations(donationCreateDTO);

        Person person = personRepository.findById(donationCreateDTO.getPersonId())
                .orElseThrow(() -> new RuntimeException("Person not found"));
        Shelter shelter = shelterRepository.findById(donationCreateDTO.getShelterId())
                .orElseThrow(() -> new RuntimeException("Shelter not found"));

        Donation donation = new Donation();
        donation.setTotal(donationCreateDTO.getTotal());
        donation.setCurrency(donationCreateDTO.getCurrency());
        donation.setDate(LocalDateTime.now());
        donation.setPerson(person);
        donation.setShelter(shelter);
        donation.setPaymentIntentId(donationCreateDTO.getPaymentIntentId());
        donation.setPaymentMethod(donationCreateDTO.getPaymentMethod());

        Donation savedDonation = donationRepository.save(donation);
        logger.info("Donation created successfully: {}", savedDonation);
        return DonationMapper.INSTANCE.toDTO(savedDonation);
    }

    public DonationDTO getDonationById(Long id) {
        logger.info("Fetching donation with id: {}", id);
        idValidation(id);
        Optional<Donation> donation = donationRepository.findById(id);
        if (donation.isEmpty()) {
            logger.warn("Donation not found with id: {}", id);
            throw new DonationNotFoundException();
        }
        return DonationMapper.INSTANCE.toDTO(donation.get());
    }

    private void idValidation(Long id) {
        if (id == null || id <= 0) {
            logger.warn("Invalid donation id: {}", id);
            throw new DonationNotFoundException(DonationMessages.DONATION_NOT_FOUND);
        }
    }

    public List<DonationDTO> getAllDonations() {
        logger.info("Fetching all donations");
        List<DonationDTO> donations = new ArrayList<>();
        donationRepository.findAll().forEach(donation -> donations.add(DonationMapper.INSTANCE.toDTO(donation)));
        return donations;
    }

    public List<DonationDTO> getAllDonationsByPersonId(Long id) {
        logger.info("Fetching all donations for person id: {}", id);
        List<DonationDTO> donations = new ArrayList<>();
        donationRepository.findAll().forEach(donation -> {
            if (Objects.equals(donation.getPerson().getId(), id)) {
                donations.add(DonationMapper.INSTANCE.toDTO(donation));
            }
        });
        return donations;
    }

    public List<DonationDTO> getAllDonationsByShelterId(Long id) {
        logger.info("Fetching all donations for shelter id: {}", id);
        List<DonationDTO> donations = new ArrayList<>();
        donationRepository.findAll().forEach(donation -> {
            if (Objects.equals(donation.getShelter().getId(), id)) {
                donations.add(DonationMapper.INSTANCE.toDTO(donation));
            }
        });
        return donations;
    }
}