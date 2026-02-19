package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.ViewKundeBestellungBestellposition;
import com.dbw.spring_boot.model.ViewMitarbeiterBestellstatus;
import com.dbw.spring_boot.model.ViewMitarbeiterUebersicht;
import com.dbw.spring_boot.model.ViewProduktVerkaufszahlen;
import com.dbw.spring_boot.repositories.KundenRepository;
import com.dbw.spring_boot.repositories.MitarbeiterRepository;
import com.dbw.spring_boot.repositories.ProduktRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ViewController {
    private final KundenRepository kundenRepo = new KundenRepository();
    private final ProduktRepository produktRepo = new ProduktRepository();
    private final MitarbeiterRepository mitarbeiterRepo = new MitarbeiterRepository();

    @GetMapping("/report/kunde/summe-anzahl-bestellungen")
    public ResponseEntity<?> summeAnzahlBestellungen() {
        List<ViewKundeBestellungBestellposition> k = kundenRepo.getViewsummeAnzahlBestellungen();
        if(k == null) {
            return ResponseEntity.status(400).build();
        }
        return ResponseEntity.ok(k);
    }

    @GetMapping("/report/produkt/verkaufszahlen")
    public ResponseEntity<?> produktVerkaufszahlen() {
        List<ViewProduktVerkaufszahlen> k = produktRepo.getViewProduktVerkaufszahlen();
        if(k == null) {
            return ResponseEntity.status(400).build();
        }
        return ResponseEntity.ok(k);
    }

    @GetMapping("/report/mitarbeiter/uebersicht")
    public ResponseEntity<?> mitarbeiterUebersicht() {
        List<ViewMitarbeiterUebersicht> k = mitarbeiterRepo.getViewMitarbeiterUebersicht();
        if(k == null) {
            return ResponseEntity.status(400).build();
        }
        return ResponseEntity.ok(k);
    }

    @GetMapping("/report/mitarbeiter/bestellstatus-uebersicht")
    public ResponseEntity<?> mitarbeiterBestellstatusUebersicht() {
        List<ViewMitarbeiterBestellstatus> k = mitarbeiterRepo.getViewMitarbeiterBestellstatus();
        if(k == null) {
            return ResponseEntity.status(400).build();
        }
        return ResponseEntity.ok(k);
    }
}
