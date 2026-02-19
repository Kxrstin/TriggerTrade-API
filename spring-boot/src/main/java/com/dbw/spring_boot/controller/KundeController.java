package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Kunde;
import com.dbw.spring_boot.model.KundePut;
import com.dbw.spring_boot.model.LoginKunde;
import com.dbw.spring_boot.repositories.KundenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kunden")
public class KundeController {

    private final KundenRepository repo;

    public KundeController() {
        this.repo = new KundenRepository();
    }

    @GetMapping
    public ResponseEntity<List<Kunde>> getKunden(@RequestParam(required = false) String email) {
        if (email != null) {
            Kunde k = repo.findByEmail(email);
            return k != null ? ResponseEntity.ok(List.of(k)) : ResponseEntity.notFound().build();
        }

        List<Kunde> kunden = repo.findAllOrByIdWithAdresse(null);
        return ResponseEntity.ok(kunden);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Kunde> getKundeById(@PathVariable int id) {
        return repo.findAllOrByIdWithAdresse(id).stream()
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Kunde> createKunde(@RequestBody KundePut kundePut) {
        if (isInvalid(kundePut)) {
            return ResponseEntity.badRequest().build();
        }

        Kunde k = mapToEntity(kundePut, null);
        Kunde saved = repo.save(k);

        return saved != null ? ResponseEntity.status(HttpStatus.CREATED).body(saved)
                : ResponseEntity.internalServerError().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateKunde(@PathVariable int id, @RequestBody KundePut kundePut) {
        if (isInvalid(kundePut)) {
            return ResponseEntity.badRequest().build();
        }

        Kunde k = mapToEntity(kundePut, id);
        int rows = repo.updateKunde(k);

        return rows == 1 ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Kunde> loginKunde(@RequestBody LoginKunde request) {
        if (request.getEmail() == null || request.getPasswort() == null) {
            return ResponseEntity.badRequest().build();
        }

        Kunde k = repo.findByEmail(request.getEmail());
        if (k != null && k.getPasswort().equals(request.getPasswort())) {
            k.setPasswort(null);
            return ResponseEntity.ok(k);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private boolean isInvalid(KundePut kp) {
        return kp.getEmail() == null || kp.getVorname() == null ||
                kp.getNachname() == null || kp.getPasswort() == null;
    }

    private Kunde mapToEntity(KundePut kp, Integer id) {
        Kunde k = new Kunde();
        k.setKundeId(id);
        k.setEmail(kp.getEmail());
        k.setVorname(kp.getVorname());
        k.setNachname(kp.getNachname());
        k.setPasswort(kp.getPasswort());
        return k;
    }
}