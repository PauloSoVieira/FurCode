package org.mindera.fur.code.service;

import org.mindera.fur.code.dto.donation.DonationCreateDTO;
import org.mindera.fur.code.dto.donation.DonationDTO;
import org.mindera.fur.code.exceptions.donation.DonationNotFoundException;
import org.mindera.fur.code.exceptions.donation.InvalidDonationAmountException;
import org.mindera.fur.code.exceptions.donation.InvalidDonationDateException;
import org.mindera.fur.code.mapper.DonationMapper;
import org.mindera.fur.code.messages.donation.DonationMessages;
import org.mindera.fur.code.messages.person.PersonMessages;
import org.mindera.fur.code.messages.shelter.ShelterMessages;
import org.mindera.fur.code.model.Donation;
import org.mindera.fur.code.repository.DonationRepository;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mindera.fur.code.repository.pet.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DonationService {

    private final DonationRepository donationRepository;
    private final ShelterRepository shelterRepository;
    private final PersonRepository personRepository;

    @Autowired
    public DonationService(DonationRepository donationRepository, PersonRepository personRepository, ShelterRepository shelterRepository, PetRepository petRepository) {
        this.donationRepository = donationRepository;
        this.shelterRepository = shelterRepository;
        this.personRepository = personRepository;
    }

    private static void donationValidations(DonationCreateDTO donationCreateDTO) {
        if (donationCreateDTO.getTotal() <= 0 ||
                donationCreateDTO.getDate() == null ||
                donationCreateDTO.getShelterId() == null ||
                donationCreateDTO.getPersonId() == null) {
            throw new IllegalArgumentException(DonationMessages.DONATION_HAS_INVALID_DATA_FIELDS);
        }

        // TODO: this is not working, please fix
//        if (donation.getDate().getTime() < donation.getShelterId()) {
//            throw new InvalidDonationDateException("Donation date must be in the future");
//         }

        if (donationCreateDTO.getTotal() >= 999999) {
            throw new InvalidDonationAmountException("Donation total must be less than 999999");
        }
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
     * @param donationCreateDTO the DonationDTO containing the donation details
     * @return the saved Donation object
     * @throws IllegalArgumentException       if any required fields are null or invalid
     * @throws InvalidDonationDateException   if the donation date is not in the future
     * @throws InvalidDonationAmountException if the donation amount is too large
     */
    public DonationDTO createDonation(DonationCreateDTO donationCreateDTO) {
        donationValidations(donationCreateDTO); // TODO: this is not working, please fix

        Donation newDonation = DonationMapper.INSTANCE.toModel(donationCreateDTO);

        newDonation.setPerson(personRepository.findById
                (donationCreateDTO.getPersonId()).orElseThrow(() -> new IllegalArgumentException(PersonMessages.PERSON_NOT_FOUND)));
        newDonation.setShelter(shelterRepository.findById(donationCreateDTO.getShelterId()).orElseThrow(() ->
                new IllegalArgumentException(ShelterMessages.SHELTER_NOT_FOUND)));

        donationRepository.save(newDonation);
        return DonationMapper.INSTANCE.toDTO(newDonation);
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
    public DonationDTO getDonationById(Long id) {
        idValidation(id);
        Optional<Donation> donation = donationRepository.findById(id);
        if (donation.isEmpty()) {
            throw new DonationNotFoundException();
        }
        return DonationMapper.INSTANCE.toDTO(donation.get());
    }

    private void idValidation(Long id) {
        if (id == null) {
            throw new DonationNotFoundException(DonationMessages.DONATION_NOT_FOUND);
        }
        if (id <= 0) {
            throw new DonationNotFoundException(DonationMessages.DONATION_NOT_FOUND);
        }
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
     * <p>It fetches all donation records from the repository, filters them by the provided
     * person ID, maps each matching donation to a DonationDTO, and collects these DTOs in an
     * ArrayList which is then returned.
     *
     * @param id the ID of the person whose donations are to be retrieved
     * @return an ArrayList of DonationDTO objects representing all donations for the specified person
     */
    public ArrayList<DonationDTO> getAllDonationsByPersonId(Long id) {

        ArrayList<DonationDTO> donations = new ArrayList<>();
        donationRepository.findAll().forEach(donation -> {
            if (Objects.equals(donation.getPerson().getId(), id)) {
                donations.add(DonationMapper.INSTANCE.toDTO(donation));
            }
        });
        return donations;
    }


    /**
     * Retrieves all donations associated with a specific shelter ID from the repository.
     *
     * <p>It fetches all donation records from the repository, filters them by the provided
     * person ID, maps each matching donation to a DonationDTO, and collects these DTOs in an
     * ArrayList which is then returned.
     *
     * @param id the ID of the shelter whose donations are to be retrieved
     * @return an ArrayList of DonationDTO objects representing all donations for the specified shelter
     */
    public List<DonationDTO> getAllDonationsByShelterId(Long id) {
        ArrayList<DonationDTO> donations = new ArrayList<>();
        donationRepository.findAll().forEach(donation -> {
            if (Objects.equals(donation.getShelter().getId(), id)) {
                donations.add(DonationMapper.INSTANCE.toDTO(donation));
            }
        });
        return donations;
    }
}
