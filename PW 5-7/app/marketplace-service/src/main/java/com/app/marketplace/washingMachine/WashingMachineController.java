package com.app.marketplace.washingMachine;

import com.app.marketplace.auth.RequiresRole;
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
    @RequiresRole({"USER", "SELLER", "ADMINISTRATOR"})
    public ResponseEntity<List<WashingMachine>> getAllWashingMachines() {
        List<WashingMachine> washingMachines = washingMachineService.getAllWashingMachines();
        if (washingMachines.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(washingMachines);
    }

    @GetMapping("/{id}")
    @RequiresRole({"USER", "SELLER", "ADMINISTRATOR"})
    public ResponseEntity<WashingMachine> getWashingMachineById(@PathVariable Long id) {
        return ResponseEntity.ofNullable(washingMachineService.getWashingMachineById(id).orElse(null));
    }

    @PostMapping
    @RequiresRole({"SELLER", "ADMINISTRATOR"})
    public ResponseEntity<WashingMachine> addWashingMachine(@RequestBody WashingMachine washingMachine) {
        return ResponseEntity.ok(washingMachineService.saveWashingMachine(washingMachine));
    }

    @PutMapping("/{id}")
    @RequiresRole({"SELLER", "ADMINISTRATOR"})
    public ResponseEntity<WashingMachine> updatePhone(@PathVariable Long id, @RequestBody WashingMachine washingMachine) {
        return ResponseEntity.ofNullable(washingMachineService.updateWashingMachine(id, washingMachine).orElse(null));
    }

    @DeleteMapping("/{id}")
    @RequiresRole({"SELLER", "ADMINISTRATOR"})
    public ResponseEntity<String> deleteWashingMachine(@PathVariable Long id) {
        if (washingMachineService.getWashingMachineById(id).isEmpty())
            return ResponseEntity.internalServerError().body("There is no such washing machine");

        washingMachineService.deleteWashingMachine(id);
        return ResponseEntity.ok().build();
    }
}

