package org.mindera.fur.code.service;

import org.mindera.fur.code.exceptions.DonationNotFoundException;
import org.mindera.fur.code.model.Donation;
import org.mindera.fur.code.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Optional;

public class DonationService {

    private final DonationRepository donationRepository;

    @Autowired
    public DonationService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    public Donation save(Donation donation) {
        //TODO
        // change to DTO
        // change strings to other file

        if (donation.getId() == null ||
                donation.getTotal() <= 0 ||
                donation.getDate() == null ||
                donation.getPet() == null ||
                donation.getPerson() == null) {
            throw new IllegalArgumentException("Donation has invalid data fields");
        }

        if (donation.getDate().getTime() < System.currentTimeMillis()) {
            throw new IllegalArgumentException("Donation date must be in the future");
        }

        if (donation.getTotal() >= 999999) {
            throw new IllegalArgumentException("Donation total must be less than 999999");
        }

        Donation savedDonation = donationRepository.save(donation);
        return savedDonation;

    }

    public Optional<Donation> get(long id) {
        Optional<Donation> donation = donationRepository.findById(id);
        if (donation.isEmpty()) {
            throw new DonationNotFoundException();
        }
        return donation;
    }

    public ArrayList<Donation> list() {
        ArrayList<Donation> donations = new ArrayList<>();
        donationRepository.findAll().forEach(donations::add);
        return donations;
    }

    public ArrayList<Donation> list(long id) {
        ArrayList<Donation> donations = new ArrayList<>();
        donationRepository.findAll().forEach(donation -> {
            if (donation.getPerson().getId() == id) {
                donations.add(donation);
            }
        });
        return donations;
    }
}
