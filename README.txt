Hallo, falls Sie Fragen zu meiner Implementierung / Umsetzung der einzelnen Aufgaben haben, lesen Sie sich
bitte die passenden Unterpunkte durch. Die Aufgabenstellungen sind teils nicht wirklich eindeutig / verständlich,
weshalb ich hier meine Umsetzung rechtfertigen möchte. Vielen Dank fürs Lesen :)

_____________________________________________________________________________________________________________________________________________________

CRUD-REST:
 - GET: Im Ilias Kurs wurde explizit gesagt, dass die Id über den Request Param (...?id=...) angegeben werden soll.
        So steht es auch auf den Screenshots der Lehrkräfte im Ilias Kurs und der Aufgabenstellung.

 - PUT: Wenn beim Verändern (PUT) einer Entität (z.B. Kunde, …) eine ID nicht gefunden wird, steht in einigen Dokumentationen, dass man
        dann einen neuen Kunden anlegen muss. Doch in der Aufgabenstellung ist nur die Rede vom Ändern, weshalb ich hier keinen neuen Kunden
        anlege. Außerdem wurde im Ilias Frageforum explizit gesagt, dass nur sinnvolle Anfragen gestellt werden. Dementsprechend sollte keine
        Änderung veranlasst werden, wenn es keine passende ID dazu in der Datenbank gibt. Bei der Implementierung habe ich mich stark an den
        Screenshots der Lehrkräfte aus dem Frageforum orientiert. Dort werden im Request Body ebenfalls die jeweiligen gefragten Attribute
        entgegengenommen und über Request Param (...?id=...) wird die Id entgegengenommen.

 - PATCH: Im Ilias Kurs wurde ausdrücklich gesagt, dass beim Produkt nur der Lagerbestand verändert werden darf und nichts anderes.
         Auch die Id wird wie bei DELETE, GET und PUT über http://localhost:8080/...?id=... mitgeliefert, da gesagt wurde, dass wir
         entgegen der CRUD-REST-Prinzipien statt Path Variablen auch Request Param nutzen dürfen.

 - POST: Hier orientiere ich mich ebenfalls an den Screenshots, die von den Lehrkräften im Ilias-Frageforum gepostet wurden. Der Request Body
         besteht dort aus allen Attributen (bis auf die Ids, die von der Datenbank erstellt werden).

 - DELETE: Hier wurde ausdrücklich gesagt (im Ilias Kurs), dass die Id beim Löschen in der Url via …?id=… erwartet wird, und nicht wie üblich
          über .../{id}. Beziehungsweise später wurde gesagt, dass beide Varianten zulässig sind. Deshalb setze ich es hier über Request Param
          um, wozu ausdrücklich gesagt wurde, dass das OK ist.

 - Status-Codes: Bei den Status Codes (404, 200, 201, ...) habe ich mich sehr schlank gehalten, da dazu keine genaue Angabe gemacht wurde. Im Forum
          wurde immer gesagt "200 oder 201" (bei POST) ist genügend, usw. Das wirkt so, als wären die allgemeinen Codes ausreichend. Daran habe ich
          mich gehalten. Außerdem wurde gesagt, dass man nur mit vernünftigen Eingaben / Anfragen rechnen soll, weshalb einige Statuscodes sowieso
          nicht infrage kommen würden. Es wurde auch gesagt, dass man keine SQL Error Messages weiter an den Controller geben muss (also als Ausgabe
          für den Nutzer), hier habe ich meist einfach einen 400- oder 404-Fehler ausgegeben.

_____________________________________________________________________________________________________________________________________________________

Aufgabe 4:
 - Email, Preis, ...: In der Aufgabe steht, dass für nicht erneut genannte Attribute keine Zusatzbedingungen gelten. Wir sollen nur den Datentyp aussuchen.
          Deshalb habe ich für Preis und Email (bei Mitarbeiter) keine Zusatzbedingungen eingefügt. So steht es ja ausdrücklich in der Aufgabenstellung.
          Da in der Aufgabenstellung auch ausdrücklich zwischen Datentyp und zusätzlichen Bedingungen unterschieden wird, sehe ich Integer, Double, ...
          als Datentyp und z.B. check-Klauseln als Zusatzbedingungen. Also ist meine Umsetzung eigentlich richtig. Dies habe ich natürlich nicht nur bei
          Email und Preis so wahrgenommen, sondern bei ALLEN nicht erneut erwähnten Attributen.

 - g) Ich zitiere aus dem Ilias Frageforum von Nina Amelie Liebrand: "Hallo, ja Sie können davon ausgehen, dass pro Bestellung jedes Produkt eine
      eindeutige Bestellposition hat". Das brauche ich für die Mengenoperation meiner Stornierung.

_____________________________________________________________________________________________________________________________________________________

Aufgabe 5:
 - Datum: Ich gebe das Datum der Bestellungen bei GET mit T und Z aus. Doch als Eingabe (POST) erwarte ich ein Datum ohne T und Z. Das liegt daran,
          dass in den Eingabedaten data.sql nur derartige Daten vorhanden sind. Eine Implementierung, wo beide Versionen erlaubt sind, habe ich leider
          nicht finden können. Deshalb habe ich mich dafür entschieden, wie es in der data.sql Datei stand. Außerdem wurde gesagt: "datum wird als
          Datum mit Uhrzeit angegeben", was ich erfülle.

 - JacksonConfig: Im Ilias-Frageforum wurde mir geantwortet, dass ich prüfen soll, ob noch weitere, nicht geforderte, Attribute über den Request Body
          mitgeliefert werden. Falls ja, soll ich eine Fehlermeldung ausgeben. Dies setze ich mit JacksonConfig um.

 - a):  Wenn ein Mitarbeiter gelöscht wird, wird alles kaskadierend mitgelöscht. Allerdings werden die betroffenen Lagerstände nicht angepasst. Die Trigger
        der betroffenen Bestellpositionen können dann nicht mehr auf die zugehörige Bestellung zugreifen, da diese schon gelöscht wurde. Dementsprechend änden
        sie die betroffenen Lagerstände nicht. Das habe ich aber nicht weiter verändert, denn nirgendswo wurde definiert, wie wir darauf zu reagieren haben.
        Außerdem weiß ich auch nicht, was das korrekte Verhalten darauf ist. Ich habe mich darauf verlassen, dass gesagt wurde, dass nur vernünftige Anfragen
        gestellt werden. Doch eine vernünftige Anfrage sollte keinen Mitarbeiter löschen, der noch offene Bestellungen hat! Wenn ich hier anders hätte handeln
        sollen, dann hätte man das auch ankündigen müssen.

 - d):  Hier habe ich manchmal als Rückgabe nicht z.B. 500.00, sondern 500, da bei JSON automatisch die Nullen wegmacht. Dazu stand in einigen Quellen, dass
        man dies nicht anders umsetzen kann, ohne einen String zurückzugeben. Dementsprechend habe ich das so gelassen. Die Werte sind ansonsten richtig und
        eine 123.45 wird bei mir auch als 123.45 dargestellt. Deshalb habe ich darin kein Problem gesehen, da auch gesagt wurde, dass die AttributsNAMEN identisch
        sein müssen und eben das war meine oberste Priorität. In der Datenbank wird der Wert auch korrekt als decimal(10,2) gespeichert. Damit sollte das kein Problem sein.

 - f):  Hier storniere ich die Bestellung, bevor ich sie lösche. Damit sollen die Lagerbestände wieder korrigiert werden. Dort kann es zu einem Fehler kommen
        (falls der Status vorher nicht neu oder bezahlt war). Dieser wird allerdings abgefangen und ist gewollt. Ich habe es so verstanden:
        Wenn die Bestellung noch neu oder bezahlt ist und gelöscht wird, dann ist das eine Stornierung der Bestellung. Dementsprechend müssen die Lagerbestände
        wieder angepasst werden. Dies wurde nirgends explizit erklärt, schien für mich aber am vernünftigsten. Zudem wurde gesagt, dass man die Sachen möglichst
        realitätsnah umsetzen soll und das schien für mich am realitätsnächsten, da wir ja keine aktuelle Bestellung einfach so Löschen dürften. Genauere Erläuterungen
        befinden sich beim Controller BestellungController (Methode: deleteBestellung) im Ordner controller.

 - Kunde und Adresse: Im Ilias Forum wurde gesagt, dass die gesamte Aufgabe 5 nicht erwartet, dass man beim Einfügen neuer Kunden oder neuer Adressen einen End-Point
         für die Zuweisung von Kunde <-> Adresse braucht. Dementsprechend habe ich das auch nicht gemacht. Siehe "Die Zuweisung der Adressen zu den Kunden ist ein
         guter Hinweis. Da dies in Aufgabe 5 allerdings nicht verlangt wird, ist die Umsetzung eines entsprechenden Endpoints nicht notwendig" von Sergej Korlakov.

