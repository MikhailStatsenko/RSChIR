package com.app.marketplace.phone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhoneService {
    private final PhoneRepository phoneRepository;

    @Autowired
    public PhoneService(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    public List<Phone> getAllPhones() {
        return (List<Phone>) phoneRepository.findAll();
    }

    public Optional<Phone> getPhoneById(Long id) {
        return phoneRepository.findById(id);
    }

    public Phone savePhone(Phone phone) {
        return phoneRepository.save(phone);
    }

    public void deletePhone(Long id) {
        phoneRepository.deleteById(id);
    }

    public Optional<Phone> updatePhone(Long id, Phone updatedPhone) {
        Optional<Phone> phoneOptional = getPhoneById(id);
        if (phoneOptional.isEmpty())
            return Optional.empty();

        Phone dbPhone = phoneOptional.get();

        if (updatedPhone.getManufacturer() != null && !updatedPhone.getManufacturer().isBlank())
            dbPhone.setManufacturer(updatedPhone.getManufacturer());

        if (updatedPhone.getBatteryCapacity() > 0)
            dbPhone.setBatteryCapacity(updatedPhone.getBatteryCapacity());

        if (updatedPhone.getTitle() != null && !updatedPhone.getTitle().isBlank())
            dbPhone.setTitle(updatedPhone.getTitle());

        if (updatedPhone.getPrice() > 0)
            dbPhone.setPrice(updatedPhone.getPrice());

        if (updatedPhone.getProductType() != null && !updatedPhone.getProductType().isBlank())
            dbPhone.setProductType(updatedPhone.getProductType());

        if (updatedPhone.getSellerNumber() != null && !updatedPhone.getSellerNumber().isBlank())
            dbPhone.setSellerNumber(updatedPhone.getSellerNumber());

        return Optional.of(savePhone(dbPhone));
    }

}

