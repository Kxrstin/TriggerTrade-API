package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Bestellung;
import com.dbw.spring_boot.model.BestellungPut;
import com.dbw.spring_boot.repositories.BestellungRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bestellungen")
public class BestellungController {
    private final BestellungRepository repo;

    public BestellungController() {
        this.repo = new BestellungRepository();
    }

    @GetMapping
    public ResponseEntity<List<Bestellung>> getAllBestellungen() {
        List<Bestellung> bestellungen = repo.findAllOrById(null);
        return ResponseEntity.ok(bestellungen);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bestellung> getBestellungById(@PathVariable int id) {
        return repo.findAllOrById(id).stream()
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Bestellung> createBestellung(@RequestBody BestellungPut bestellungPut) {
        if (bestellungPut.getDatum() == null || bestellungPut.getStatus() == null) {
            return ResponseEntity.badRequest().build();
        }

        Bestellung b = new Bestellung();
        b.setKundeId(bestellungPut.getKunde_id());
        b.setPersonalnr(bestellungPut.getPersonal_nr());
        b.setDatum(bestellungPut.getDatum());
        b.setStatus(bestellungPut.getStatus());

        Bestellung saved = repo.save(b);

        if (saved != null && saved.getBestellungId() != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBestellung(@PathVariable int id) {
        repo.setStorniert(id);
        int anzahlEntfernt = repo.deleteBestellung(id);
        if (anzahlEntfernt == 1) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
