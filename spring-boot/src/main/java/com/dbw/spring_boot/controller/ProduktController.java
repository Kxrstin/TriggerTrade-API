package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Lagerbestand;
import com.dbw.spring_boot.model.Produkt;
import com.dbw.spring_boot.repositories.ProduktRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produkte")
public class ProduktController {
    private final ProduktRepository repo;

    public ProduktController() {
        this.repo = new ProduktRepository();
    }

    @GetMapping
    public ResponseEntity<List<Produkt>> getAllProdukte() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/{sku}")
    public ResponseEntity<Produkt> getProdukt(@PathVariable String sku) {
        Produkt produkt = repo.findBySku(sku);
        return produkt != null ? ResponseEntity.ok(produkt) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Produkt> createProdukt(@RequestBody Produkt produkt) {
        if (produkt.getPreis() == null || produkt.getName() == null || produkt.getSku() == null) {
            return ResponseEntity.badRequest().build();
        }
        Produkt saved = repo.save(produkt);
        return ResponseEntity.status(201).body(saved);
    }

    @DeleteMapping("/{sku}")
    public ResponseEntity<Void> deleteProdukt(@PathVariable String sku) {
        int anzahlEntfernt = repo.deleteBySku(sku);
        return anzahlEntfernt == 1 ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{sku}/lagerbestand")
    public ResponseEntity<Void> updateLagerbestand(@PathVariable String sku, @RequestBody Lagerbestand lagerbestand) {
        int anzahlChanged = repo.changeLagerbestandBySku(sku, lagerbestand.getLagerbestand());
        return anzahlChanged == 1 ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}

