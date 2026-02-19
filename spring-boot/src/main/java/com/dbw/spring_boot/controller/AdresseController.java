package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Adresse;
import com.dbw.spring_boot.model.AdressePut;
import com.dbw.spring_boot.repositories.AdressenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adressen")
public class AdresseController {

    private final AdressenRepository repo;

    public AdresseController() {
        this.repo = new AdressenRepository();
    }

    @GetMapping
    public ResponseEntity<List<Adresse>> getAllAdressen() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Adresse> getAdresseById(@PathVariable int id) {
        Adresse adresse = repo.findById(id);
        return adresse != null ? ResponseEntity.ok(adresse)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Adresse> createAdresse(@RequestBody AdressePut adressePut) {
        Adresse a = mapToEntity(adressePut, null);
        Adresse saved = repo.save(a);

        if (saved != null && saved.getAdresseId() != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAdresse(@PathVariable int id, @RequestBody AdressePut adressePut) {
        Adresse adresse = mapToEntity(adressePut, id);

        int rows = repo.updateAdresse(adresse);
        if (rows == 1) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    private Adresse mapToEntity(AdressePut dto, Integer id) {
        Adresse a = new Adresse();
        a.setAdresseId(id);
        a.setLand(dto.getLand());
        a.setOrt(dto.getOrt());
        a.setStrasse(dto.getStrasse());
        a.setHausnummer(dto.getHausnummer());
        a.setPlz(dto.getPlz());
        a.setAktiv(dto.isAktiv());
        return a;
    }
}