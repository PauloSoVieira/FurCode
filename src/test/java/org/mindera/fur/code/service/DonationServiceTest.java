//package org.mindera.fur.code.service;
//
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mindera.fur.code.dto.donation.DonationCreateDTO;
//import org.mindera.fur.code.dto.donation.DonationDTO;
//import org.mindera.fur.code.exceptions.donation.DonationNotFoundException;
//import org.mindera.fur.code.exceptions.donation.InvalidDonationAmountException;
//import org.mindera.fur.code.mapper.DonationMapper;
//import org.mindera.fur.code.model.Donation;
//import org.mindera.fur.code.model.Person;
//import org.mindera.fur.code.model.Shelter;
//import org.mindera.fur.code.repository.DonationRepository;
//import org.mindera.fur.code.repository.PersonRepository;
//import org.mindera.fur.code.repository.ShelterRepository;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.lenient;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class DonationServiceTest {
//
//    @Mock
//    private DonationRepository donationRepository;
//    @Mock
//    private PersonRepository personRepository;
//    @Mock
//    private ShelterRepository shelterRepository;
//
//    @InjectMocks
//    private DonationService donationService;
//
//    @Nested
//    class createDonationUnitTests {
//        @Test
//        void createDonation_withValidData_shouldSucceed() {
//            DonationMapper mapper = DonationMapper.INSTANCE;
//            Date date = new Date();
//
//            DonationCreateDTO newDonation = new DonationCreateDTO();
//            newDonation.setTotal(100.0);
//            newDonation.setDate(date);
//            newDonation.setShelterId(1L);
//            newDonation.setPersonId(1L);
//
//
//            Donation expectedDonation = mapper.toModel(newDonation);
//
//            when(shelterRepository.findById(1L)).thenReturn(Optional.of(new Shelter()));
//            when(personRepository.findById(1L)).thenReturn(Optional.of(new Person()));
//            when(donationRepository.save(any(Donation.class))).thenReturn(expectedDonation);
//            DonationDTO savedDonation = donationService.createDonation(newDonation);
//
//
//            assertEquals(expectedDonation, mapper.toModel(savedDonation));
//        }
//
//        @Test
//        void createDonation_withNullDate_shouldFail() {
//            DonationCreateDTO newDonation = new DonationCreateDTO();
//            newDonation.setTotal(100.0);
//            newDonation.setDate(null);
//            newDonation.setShelterId(1L);
//            newDonation.setPersonId(1L);
//
//            assertThrows(IllegalArgumentException.class, () -> donationService.createDonation(newDonation));
//        }
//
//        @Test
//        void createDonation_withFutureDate_shouldSucceed() {
//            Date futureDate = new Date(System.currentTimeMillis() + 1000000L);
//
//            DonationCreateDTO newDonation = new DonationCreateDTO();
//            newDonation.setTotal(100.0);
//            newDonation.setDate(futureDate);
//            newDonation.setShelterId(1L);
//            newDonation.setPersonId(1L);
//            Donation expectedDonation = DonationMapper.INSTANCE.toModel(newDonation);
//
//            when(shelterRepository.findById(1L)).thenReturn(Optional.of(new Shelter()));
//            when(personRepository.findById(1L)).thenReturn(Optional.of(new Person()));
//            when(donationRepository.save(any(Donation.class))).thenReturn(expectedDonation);
//
//            DonationDTO savedDonation = donationService.createDonation(newDonation);
//            assertEquals(expectedDonation, DonationMapper.INSTANCE.toModel(savedDonation));
//        }
//
//        // TODO: fix this test
//        @Test
//        @Disabled
//        void createDonation_withPastDate_shouldFail() {
//            Date pastDate = new Date(System.currentTimeMillis() - 1000000L);
//
//            DonationCreateDTO newDonation = new DonationCreateDTO();
//            newDonation.setTotal(100.0);
//            newDonation.setDate(pastDate);
//            newDonation.setShelterId(1L);
//            newDonation.setPersonId(1L);
//
//            assertThrows(IllegalArgumentException.class, () -> donationService.createDonation(newDonation));
//        }
//
//        @Test
//        void createDonation_withZeroTotal_shouldFail() {
//            Date date = new Date();
//
//            DonationCreateDTO newDonation = new DonationCreateDTO();
//            newDonation.setTotal(0.0);
//            newDonation.setDate(date);
//            newDonation.setShelterId(1L);
//            newDonation.setPersonId(1L);
//
//            assertThrows(IllegalArgumentException.class, () -> donationService.createDonation(newDonation));
//        }
//
//        @Test
//        void createDonation_withNegativeTotal_shouldFail() {
//            Date date = new Date();
//
//            DonationCreateDTO newDonation = new DonationCreateDTO();
//            newDonation.setTotal(-1.0);
//            newDonation.setDate(date);
//            newDonation.setShelterId(1L);
//            newDonation.setPersonId(1L);
//
//            assertThrows(IllegalArgumentException.class, () -> donationService.createDonation(newDonation));
//        }
//
//        @Test
//        void createDonation_withExceededTotal_shouldFail() {
//            Date date = new Date();
//
//            DonationCreateDTO newDonation = new DonationCreateDTO();
//            newDonation.setTotal(1000000.0);
//            newDonation.setDate(date);
//            newDonation.setShelterId(1L);
//            newDonation.setPersonId(1L);
//
//            assertThrows(InvalidDonationAmountException.class, () -> donationService.createDonation(newDonation));
//        }
//
//        @Test
//        void createDonation_withNullPetId_shouldFail() {
//            Date date = new Date();
//
//            DonationCreateDTO newDonation = new DonationCreateDTO();
//            newDonation.setTotal(100.0);
//            newDonation.setDate(date);
//            newDonation.setShelterId(1L);
//            newDonation.setPersonId(1L);
//
//            assertThrows(IllegalArgumentException.class, () -> donationService.createDonation(newDonation));
//        }
//
//        @Test
//        void createDonation_withNullPersonId_shouldFail() {
//            Date date = new Date();
//
//            DonationCreateDTO newDonation = new DonationCreateDTO();
//            newDonation.setTotal(100.0);
//            newDonation.setDate(date);
//            newDonation.setShelterId(1L);
//            newDonation.setPersonId(null);
//
//            assertThrows(IllegalArgumentException.class, () -> donationService.createDonation(newDonation));
//        }
//    }
//
//    @Nested
//    class getDonationUnitTests {
//        @Test
//        void getDonationById_withValidId_shouldSucceed() {
//            DonationMapper mapper = DonationMapper.INSTANCE;
//            Date date = new Date();
//
//            DonationCreateDTO newDonation = new DonationCreateDTO();
//            newDonation.setTotal(100.0);
//            newDonation.setDate(date);
//            newDonation.setShelterId(1L);
//            newDonation.setPersonId(1L);
//            Donation expectedDonation = mapper.toModel(newDonation);
//
//            when(donationRepository.findById(any(Long.class))).thenReturn(Optional.of(expectedDonation));
//            DonationDTO savedDonation = donationService.getDonationById(1L);
//
//            assertEquals(expectedDonation, mapper.toModel(savedDonation));
//        }
//
//        @Test
//        void getDonationById_withIdZero_shouldFail() {
//            assertThrows(DonationNotFoundException.class, () -> donationService.getDonationById(0L));
//        }
//
//        @Test
//        void getDonationById_withNegativeId_shouldFail() {
//            assertThrows(DonationNotFoundException.class, () -> donationService.getDonationById(0L));
//        }
//
//        @Test
//        void getDonationById_withNonexistentId_shouldFail() {
//            lenient().when(donationRepository.findById(any(Long.class))).thenReturn(Optional.empty());
//            assertThrows(DonationNotFoundException.class, () -> donationService.getDonationById(99L));
//        }
//
//        @Test
//        void getDonationById_withIdLargerThanMax_shouldFail() {
//            assertThrows(DonationNotFoundException.class, () -> donationService.getDonationById(Long.MAX_VALUE));
//        }
//    }
//
//    @Nested
//    class getAllDonationsUnitTests {
//        @Test
//        void getAllDonations_withValidData_shouldSucceed() {
//            DonationMapper mapper = DonationMapper.INSTANCE;
//            Date date = new Date();
//
//            DonationDTO newDonation1 = new DonationDTO();
//            newDonation1.setId(1L);
//            Donation expectedDonation1 = mapper.toModel(newDonation1);
//
//            DonationDTO newDonation2 = new DonationDTO();
//            newDonation2.setId(2L);
//            Donation expectedDonation2 = mapper.toModel(newDonation2);
//
//            when(donationRepository.findAll()).thenReturn(List.of(expectedDonation1, expectedDonation2));
//            List<DonationDTO> savedDonations = donationService.getAllDonations();
//
//            assertEquals(2, savedDonations.size());
//            assertEquals(newDonation1, savedDonations.get(0));
//            assertEquals(newDonation2, savedDonations.get(1));
//        }
//
//        @Test
//        void getAllDonationsById_withIdZero_shouldFail() {
//            long id = 0L;
//            lenient().when(donationRepository.findById(id)).thenReturn(Optional.empty());
//            assertThrows(DonationNotFoundException.class, () -> donationService.getDonationById(id));
//        }
//
//        @Test
//        void getAllDonationsById_withNegativeId_shouldFail() {
//            Long id = -1L;
//            lenient().when(donationRepository.findById(id)).thenReturn(Optional.empty());
//            assertThrows(DonationNotFoundException.class, () -> donationService.getDonationById(id));
//        }
//
//        @Test
//        void getDonationsById_withNonexistentId_shouldFail() {
//            long id = 99L;
//            lenient().when(donationRepository.findById(id)).thenReturn(Optional.empty());
//            assertThrows(DonationNotFoundException.class, () -> donationService.getDonationById(id));
//        }
//
//
//    }
//}