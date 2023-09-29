package com.app.marketplace.washingMachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WashingMachineService {
    private final WashingMachineRepository washingMachineRepository;

    @Autowired
    public WashingMachineService(WashingMachineRepository washingMachineRepository) {
        this.washingMachineRepository = washingMachineRepository;
    }

    public List<WashingMachine> getAllWashingMachines() {
        return (List<WashingMachine>) washingMachineRepository.findAll();
    }

    public Optional<WashingMachine> getWashingMachineById(Long id) {
        return washingMachineRepository.findById(id);
    }

    public WashingMachine saveWashingMachine(WashingMachine washingMachine) {
        return washingMachineRepository.save(washingMachine);
    }

    public void deleteWashingMachine(Long id) {
        washingMachineRepository.deleteById(id);
    }

    public Optional<WashingMachine> updateWashingMachine(Long id, WashingMachine updatedWashingMachine) {
        Optional<WashingMachine> washingMachineOptional = getWashingMachineById(id);
        if (washingMachineOptional.isEmpty())
            return Optional.empty();

        WashingMachine dbWashingMachine = washingMachineOptional.get();

        if (updatedWashingMachine.getManufacturer() != null && !updatedWashingMachine.getManufacturer().isBlank())
            dbWashingMachine.setManufacturer(updatedWashingMachine.getManufacturer());

        if (updatedWashingMachine.getTankVolume() > 0)
            dbWashingMachine.setTankVolume(updatedWashingMachine.getTankVolume());

        if (updatedWashingMachine.getSellerNumber() != null && !updatedWashingMachine.getSellerNumber().isBlank())
            dbWashingMachine.setSellerNumber(updatedWashingMachine.getSellerNumber());

        if (updatedWashingMachine.getPrice() > 0)
            dbWashingMachine.setPrice(updatedWashingMachine.getPrice());

        if (updatedWashingMachine.getProductType() != null && !updatedWashingMachine.getProductType().isBlank())
            dbWashingMachine.setProductType(updatedWashingMachine.getProductType());

        if (updatedWashingMachine.getTitle() != null && !updatedWashingMachine.getTitle().isBlank())
            dbWashingMachine.setTitle(updatedWashingMachine.getTitle());

        return Optional.of(saveWashingMachine(dbWashingMachine));
    }

}

