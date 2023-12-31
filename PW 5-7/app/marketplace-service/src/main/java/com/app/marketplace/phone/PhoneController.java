package com.app.marketplace.phone;

import com.app.marketplace.auth.RequiresRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/marketplace/phones")
public class PhoneController {
    private final PhoneService phoneService;

    @Autowired
    public PhoneController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @GetMapping
    @RequiresRole({"USER", "SELLER", "ADMINISTRATOR"})
    public ResponseEntity<List<Phone>> getAllPhones() {
        List<Phone> phones = phoneService.getAllPhones();
        if (phones.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(phones);
    }

    @GetMapping("/{id}")
    @RequiresRole({"USER", "SELLER", "ADMINISTRATOR"})
    public ResponseEntity<Phone> getPhoneById(@PathVariable Long id) {
        return ResponseEntity.ofNullable(phoneService.getPhoneById(id).orElse(null));
    }

    @PostMapping
    @RequiresRole({"SELLER", "ADMINISTRATOR"})
    public ResponseEntity<Phone> addPhone(@RequestBody Phone phone) {
        return ResponseEntity.ok(phoneService.savePhone(phone));
    }

    @PutMapping("/{id}")
    @RequiresRole({"SELLER", "ADMINISTRATOR"})
    public ResponseEntity<?> updatePhone(@PathVariable Long id, @RequestBody Phone phone) {
        return ResponseEntity.ofNullable(phoneService.updatePhone(id, phone).orElse(null));
    }

    @DeleteMapping("/{id}")
    @RequiresRole({"SELLER", "ADMINISTRATOR"})
    public ResponseEntity<String> deletePhone(@PathVariable Long id) {
        if (phoneService.getPhoneById(id).isEmpty())
            return ResponseEntity.internalServerError().body("There is no such phone");

        phoneService.deletePhone(id);
        return ResponseEntity.ok().build();
    }
}

