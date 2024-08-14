package org.mindera.fur.code.service;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.dto.donation.DonationCreateDTO;
import org.mindera.fur.code.dto.donation.DonationDTO;
import org.mindera.fur.code.dto.form.FormDTO;
import org.mindera.fur.code.exceptions.donation.DonationNotFoundException;
import org.mindera.fur.code.exceptions.donation.InvalidDonationAmountException;
import org.mindera.fur.code.mapper.DonationMapper;
import org.mindera.fur.code.messages.donation.DonationMessages;
import org.mindera.fur.code.messages.person.PersonMessages;
import org.mindera.fur.code.messages.shelter.ShelterMessages;
import org.mindera.fur.code.model.Donation;
import org.mindera.fur.code.model.form.Form;
import org.mindera.fur.code.repository.DonationRepository;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mindera.fur.code.repository.form.FormRepository;
import org.mindera.fur.code.repository.pet.PetRepository;
import org.mindera.fur.code.service.form.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Schema(description = "The donation service")
public class DonationService {
    private static final Integer DONATION_AMOUNT_MAX = 999999;
    private static final Integer DONATION_AMOUNT_MIN = 0;
    private final DonationRepository donationRepository;
    private final ShelterRepository shelterRepository;
    private final PersonRepository personRepository;
    private final FormService formService;
    private final FormRepository formRepository;

    /**
     * Constructor for the DonationService.
     *
     * @param donationRepository the donationRepository
     * @param personRepository   the personRepository
     * @param shelterRepository  the shelterRepository
     * @param petRepository      the petRepository
     * @param formService        the formService
     * @param formRepository     the formRepository
     */
    @Autowired
    public DonationService(DonationRepository donationRepository, PersonRepository personRepository, ShelterRepository shelterRepository, PetRepository petRepository, FormService formService, FormRepository formRepository) {
        this.donationRepository = donationRepository;
        this.shelterRepository = shelterRepository;
        this.personRepository = personRepository;
        this.formService = formService;
        this.formRepository = formRepository;
    }

    /**
     * Validates the donation creation details.
     *
     * @param donationCreateDTO the donation creation details
     */
    private static void donationValidations(DonationCreateDTO donationCreateDTO) {
        if (donationCreateDTO.getTotal() <= DONATION_AMOUNT_MIN ||
                donationCreateDTO.getDate() == null ||
                donationCreateDTO.getShelterId() == null ||
                donationCreateDTO.getPersonId() == null) {
            throw new IllegalArgumentException(DonationMessages.DONATION_HAS_INVALID_DATA_FIELDS);
        }
        if (donationCreateDTO.getTotal() >= DONATION_AMOUNT_MAX) {
            throw new InvalidDonationAmountException("Donation total must be less than 999999");
        }
    }

    /**
     * Creates a new donation based on the provided donation creation details.
     *
     * @param donationCreateDTO the data transfer object containing the details for creating a donation.
     *                          This includes information such as the donor's ID, the shelter's ID, and donation specifics.
     * @return DonationDTO the data transfer object representing the saved donation, including the associated form details.
     * @throws IOException              if there is an issue during the donation validation or form creation process.
     * @throws IllegalArgumentException if the specified person or shelter cannot be found.
     * @throws RuntimeException         if the form associated with the donation cannot be found.
     * @apiNote This method performs several operations:
     * - Validates the donation creation details.
     * - Generates a donation form from the "donation-template".
     * - Maps the donation creation DTO to a new Donation entity.
     * - Associates the Donation entity with the specified Person and Shelter.
     * - Links the Donation with the generated Form.
     * - Saves the Donation entity and returns the corresponding DTO.
     * @operationId createDonation
     */
    public DonationDTO createDonation(DonationCreateDTO donationCreateDTO) {
        donationValidations(donationCreateDTO);
        FormDTO formDTO = formService.createFormFromTemplate("donation-template");
        Donation newDonation = DonationMapper.INSTANCE.toModel(donationCreateDTO);
        newDonation.setPerson(personRepository.findById(donationCreateDTO.getPersonId())
                .orElseThrow(() -> new IllegalArgumentException(PersonMessages.PERSON_NOT_FOUND)));
        newDonation.setShelter(shelterRepository.findById(donationCreateDTO.getShelterId())
                .orElseThrow(() -> new IllegalArgumentException(ShelterMessages.SHELTER_NOT_FOUND)));
        Form form = formRepository.findById(formDTO.getId())
                .orElseThrow(() -> new RuntimeException("Form not found"));
        newDonation.setForm(form);
        Donation savedDonation = donationRepository.save(newDonation);
        return DonationMapper.INSTANCE.toDTO(savedDonation);
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
