package org.mindera.fur.code.service;

import org.mindera.fur.code.dto.donation.DonationDTO;
import org.mindera.fur.code.exceptions.donation.DonationNotFoundException;
import org.mindera.fur.code.exceptions.donation.InvalidDonationAmountException;
import org.mindera.fur.code.exceptions.donation.InvalidDonationDateException;
import org.mindera.fur.code.mapper.DonationMapper;
import org.mindera.fur.code.model.Donation;
import org.mindera.fur.code.repository.DonationRepository;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.pet.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class DonationService {

    private final DonationRepository donationRepository;
    private final PersonRepository personRepository;
    private final PetRepository petRepository;

    @Autowired
    public DonationService(DonationRepository donationRepository, PersonRepository personRepository, PetRepository petRepository) {
        this.donationRepository = donationRepository;
        this.personRepository = personRepository;
        this.petRepository = petRepository;
    }

    /**
     * Creates a new donation record based on the provided DonationDTO.
     *
     * <p>This method performs several validation checks on the provided DonationDTO:
     * <ul>
     *   <li>Ensures that the donation ID, total amount, date, pet ID, and person ID
     *       are not null and that the total amount is greater than 0.</li>
     *   <li>Validates that the donation date is in the future.</li>
     *   <li>Ensures that the total donation amount is less than 999999.</li>
     * </ul>
     * If any of these conditions are not met, an appropriate exception is thrown.
     *
     * <p>After successful validation, the donation is mapped to a Donation model object
     * and saved to the repository.
     *
     * @param donation the DonationDTO containing the donation details
     * @return the saved Donation object
     * @throws IllegalArgumentException       if any required fields are null or invalid
     * @throws InvalidDonationDateException   if the donation date is not in the future
     * @throws InvalidDonationAmountException if the donation amount is too large
     */
    public Donation createDonation(DonationDTO donation) {
        //TODO
        // change strings to enum

        if (donation.getId() == null ||
                donation.getId() <= 0 ||
                donation.getTotal() <= 0 ||
                donation.getDate() == null ||
                donation.getPetId() == null ||
                donation.getPersonId() == null) {
            throw new IllegalArgumentException("Donation has invalid data fields");
        }

        // TODO: this is not working, please fix
        //if (donation.getDate().getTime() < System.currentTimeMillis()) {
        //    throw new InvalidDonationDateException("Donation date must be in the future");
        // }

        if (donation.getTotal() >= 999999) {
            throw new InvalidDonationAmountException("Donation total must be less than 999999");
        }

        Donation newDonation = DonationMapper.INSTANCE.toModel(donation);

        newDonation.setPerson(personRepository.findById
                (donation.getPersonId()).orElseThrow(() -> new IllegalArgumentException("Person not found")));
        newDonation.setPet(petRepository.findById(
                donation.getPetId()).orElseThrow(() -> new IllegalArgumentException("Pet not found")));

        return donationRepository.save(newDonation);
    }

    /**
     * Retrieves a donation by its ID.
     *
     * <p>This method attempts to find a donation record in the repository using the provided ID.
     * If the donation is found, it is mapped to a DonationDTO and returned.
     * If the donation is not found, a {@code DonationNotFoundException} is thrown.
     *
     * @param id the ID of the donation to retrieve
     * @return the DonationDTO representing the donation details
     * @throws DonationNotFoundException if no donation with the specified ID is found
     */
    public DonationDTO getDonationById(long id) {
        Optional<Donation> donation = donationRepository.findById(id);
        if (donation.isEmpty()) {
            throw new DonationNotFoundException();
        }
        return DonationMapper.INSTANCE.toDTO(donation.get());
    }

    /**
     * Retrieves all donations from the repository.
     *
     * <p>This method fetches all donation records from the repository, maps each one to a
     * DonationDTO, and collects them in an ArrayList which is then returned.
     *
     * @return an ArrayList of DonationDTO objects representing all donations
     */
    public ArrayList<DonationDTO> getAllDonations() {
        ArrayList<DonationDTO> donations = new ArrayList<>();
        donationRepository.findAll().forEach(donation -> donations.add(DonationMapper.INSTANCE.toDTO(donation)));
        return donations;
    }

    /**
     * Retrieves all donations associated with a specific person ID from the repository.
     *
     * <p>This method first validates the provided person ID to ensure it is greater than zero.
     * It then fetches all donation records from the repository, filters them by the provided
     * person ID, maps each matching donation to a DonationDTO, and collects these DTOs in an
     * ArrayList which is then returned.
     *
     * @param id the ID of the person whose donations are to be retrieved
     * @return an ArrayList of DonationDTO objects representing all donations for the specified person
     * @throws IllegalArgumentException if the provided ID is not valid
     */
    public ArrayList<DonationDTO> getAllDonationsById(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id must be valid");
        }

        ArrayList<DonationDTO> donations = new ArrayList<>();
        donationRepository.findAll().forEach(donation -> {
            if (donation.getPerson().getId() == id) {
                donations.add(DonationMapper.INSTANCE.toDTO(donation));
            }
        });
        return donations;
    }
}
