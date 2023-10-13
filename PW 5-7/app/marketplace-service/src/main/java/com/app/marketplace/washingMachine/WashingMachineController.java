package com.app.marketplace.washingMachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/marketplace/washing-machines")
public class WashingMachineController {
    private final WashingMachineService washingMachineService;

    @Autowired
    public WashingMachineController(WashingMachineService washingMachineService) {
        this.washingMachineService = washingMachineService;
    }

    @GetMapping
    public ResponseEntity<List<WashingMachine>> getAllWashingMachines() {
        List<WashingMachine> washingMachines = washingMachineService.getAllWashingMachines();
        if (washingMachines.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(washingMachines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WashingMachine> getWashingMachineById(@PathVariable Long id) {
        return ResponseEntity.ofNullable(washingMachineService.getWashingMachineById(id).orElse(null));
    }

    @PostMapping
    public ResponseEntity<WashingMachine> addWashingMachine(@RequestBody WashingMachine washingMachine) {
        return ResponseEntity.ok(washingMachineService.saveWashingMachine(washingMachine));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WashingMachine> updatePhone(@PathVariable Long id, @RequestBody WashingMachine washingMachine) {
        return ResponseEntity.ofNullable(washingMachineService.updateWashingMachine(id, washingMachine).orElse(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWashingMachine(@PathVariable Long id) {
        if (washingMachineService.getWashingMachineById(id).isEmpty())
            return ResponseEntity.internalServerError().body("There is no such washing machine");

        washingMachineService.deleteWashingMachine(id);
        return ResponseEntity.ok().build();
    }
}

