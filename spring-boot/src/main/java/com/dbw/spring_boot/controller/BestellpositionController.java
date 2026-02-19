package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Bestellposition;
import com.dbw.spring_boot.model.BestellpositionPut;
import com.dbw.spring_boot.repositories.BestellpositionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bestellpositionen")
public class BestellpositionController {

    private final BestellpositionRepository repo;

    public BestellpositionController() {
        this.repo = new BestellpositionRepository();
    }

    @GetMapping
    public ResponseEntity<List<Bestellposition>> getAllBestellpositionen() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bestellposition> getBestellpositionById(@PathVariable int id) {
        Bestellposition bp = repo.findByPositionId(id);
        return bp != null ? ResponseEntity.ok(bp) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Bestellposition> createBestellposition(@RequestBody BestellpositionPut bpPut) {
        if (bpPut.getSku() == null) {
            return ResponseEntity.badRequest().build();
        }

        Bestellposition b = new Bestellposition();
        b.setProduktSku(bpPut.getSku());
        b.setBestellungId(bpPut.getBestellung_id());
        b.setMenge(bpPut.getMenge());

        Bestellposition saved = repo.save(b);

        return saved != null ? ResponseEntity.status(HttpStatus.CREATED).body(saved)
                : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBestellposition(@PathVariable int id) {
        int anzahlEntfernt = repo.deleteByPositionId(id);
        return anzahlEntfernt == 1 ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}