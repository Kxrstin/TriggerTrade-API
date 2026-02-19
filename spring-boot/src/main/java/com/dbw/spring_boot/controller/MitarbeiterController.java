package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.LoginMitarbeiter;
import com.dbw.spring_boot.model.Mitarbeiter;
import com.dbw.spring_boot.model.MitarbeiterPut;
import com.dbw.spring_boot.repositories.MitarbeiterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mitarbeiter")
public class MitarbeiterController {

    private final MitarbeiterRepository repo;

    public MitarbeiterController() {
        this.repo = new MitarbeiterRepository();
    }

    @GetMapping
    public ResponseEntity<List<Mitarbeiter>> getAllMitarbeiter() {
        List<Mitarbeiter> liste = repo.findAll();
        return ResponseEntity.ok(liste);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mitarbeiter> getMitarbeiterById(@PathVariable int id) {
        Mitarbeiter m = repo.findByPersonalNr(id);
        if (m == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(m);
    }

    @PostMapping
    public ResponseEntity<Mitarbeiter> createMitarbeiter(@RequestBody MitarbeiterPut mitarbeiterPut) {
        Mitarbeiter mitarbeiter = new Mitarbeiter();
        mitarbeiter.setVorname(mitarbeiterPut.getVorname());
        mitarbeiter.setNachname(mitarbeiterPut.getNachname());
        mitarbeiter.setEmail(mitarbeiterPut.getEmail());
        mitarbeiter.setPasswort(mitarbeiterPut.getPasswort());

        Mitarbeiter saved = repo.save(mitarbeiter);
        if (saved.getPersonalNr() != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMitarbeiter(@PathVariable int id) {
        int entfernt = repo.deleteByPersonalNr(id);
        if (entfernt == 1) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Mitarbeiter> login(@RequestBody LoginMitarbeiter request) {
        if (request.getPersonalNr() == null || request.getPasswort() == null) {
            return ResponseEntity.badRequest().build();
        }

        Mitarbeiter m = repo.findByPersonalNr(request.getPersonalNr());

        if (m != null && m.getPasswort().equals(request.getPasswort())) {
            m.setPasswort(null);
            return ResponseEntity.ok(m);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
