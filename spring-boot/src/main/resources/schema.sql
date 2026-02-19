create table adresse (
    adresse_id serial primary key,
	aktiv boolean not null,
	strasse varchar(60) not null check(strasse ~ '^[A-ZÄÖÜ][a-zäöüß]*$'),
	hausnummer varchar(20) not null check(hausnummer ~ '^[0-9]+[a-z]?$'),
	plz varchar(12) not null check(plz ~ '^[0-9]+$'),
	ort varchar(200) not null,
	land varchar(200) not null
);

create table kunde (
	kunde_id serial primary key,
	email varchar(256) unique not null check(email ~ '^.+@.+\..+$'),
	vorname varchar(32) not null check(vorname ~ '^[a-zA-ZÄäÖöÜüß]+$'),
	nachname varchar(32) not null check(nachname ~ '^[a-zA-ZÄäÖöÜüß]+$'),
	passwort varchar(20) not null check(
		length(passwort) >= 5 and
		passwort ~ '[a-zA-Z]' and
		passwort ~ '[0-9]' and
		passwort ~ '[^a-zA-Z0-9]')
);

create table kunde_hat_adressen (
	adresse_id int not null,
	kunde_id int not null,
	typ varchar(16) not null check(typ in ('Lieferadresse', 'Rechnungsadresse')),
	primary key(adresse_id, kunde_id),
	foreign key(adresse_id) references adresse(adresse_id),
	foreign key(kunde_id) references kunde(kunde_id)
);

create table mitarbeiter (
	personal_nr serial primary key,
	vorname varchar(32) not null,
	nachname varchar(32) not null,
	email varchar(256) not null,
	passwort varchar(20) not null check(
		length(passwort) >= 5 and
		passwort ~ '[a-zA-Z]' and
		passwort ~ '[0-9]' and
		passwort ~ '[^a-zA-Z0-9]')
);

create table bestellung (
	bestellung_id serial primary key,
	datum timestamp not null,
	status varchar(13) not null check(status in ('neu', 'bezahlt', 'versendet', 'abgeschlossen', 'storniert')),
	mitarbeiterzuweis int not null,
	kunde_id int not null,
	foreign key(mitarbeiterzuweis) references mitarbeiter(personal_nr) on delete cascade,
	foreign key(kunde_id) references kunde(kunde_id)
);

create table produkt (
	sku varchar(255) primary key,
	name varchar(32) not null,
	preis decimal(10,2) not null,
	lagerbestand int not null check(lagerbestand >= 0),
	angelegt_von int not null,
	foreign key(angelegt_von) references mitarbeiter(personal_nr) on delete cascade
);

create table bestellposition (
	position_id serial primary key,
	menge int not null check(menge > 0),
	sku varchar(255) not null,
	bestellung_id int not null,
	foreign key(sku) references produkt(sku) on delete cascade,
	foreign key(bestellung_id) references bestellung(bestellung_id) on delete cascade
);


create or replace function insert_bestellposition_function() returns trigger as $$
declare
    aktueller_status varchar;
begin
    select status into aktueller_status from bestellung where bestellung_id = NEW.bestellung_id;

    if aktueller_status <> 'storniert' then
        if (select lagerbestand from produkt where sku = NEW.sku) < NEW.menge then
            raise exception 'Das geht leider nicht, es gibt nicht genug davon auf Lager!';
        end if;

        update produkt set lagerbestand = lagerbestand - NEW.menge where sku = NEW.sku;
    end if;
    return new;
end;
$$ language plpgsql;

create trigger insert_bestellposition
before insert on bestellposition
for each row execute function insert_bestellposition_function();


create or replace function update_bestellposition_function() returns trigger as $$
declare
    aktueller_status varchar;
begin
    select status into aktueller_status from bestellung where bestellung_id = NEW.bestellung_id;

    if aktueller_status <> 'storniert' then
        if (select lagerbestand from produkt where sku = NEW.sku) < NEW.menge - OLD.menge then
            raise exception 'Die Erhöhung der Menge ist nicht erlaubt, da nicht genug auf Lager ist!';
        end if;
        update produkt set lagerbestand = lagerbestand + OLD.menge - NEW.menge where sku = NEW.sku;
    end if;

    return NEW;
end;
$$ language plpgsql;

create trigger update_bestellposition
before update of menge on bestellposition
for each row execute function update_bestellposition_function();


create or replace function delete_bestellposition_function() returns trigger as $$
declare
    aktueller_status varchar;
begin
    select status into aktueller_status from bestellung where bestellung_id = OLD.bestellung_id;
    if aktueller_status <> 'storniert' then
        update produkt set lagerbestand = lagerbestand + OLD.menge where sku = OLD.sku;
    end if;
    return OLD;
end;
$$ language plpgsql;

create trigger delete_bestellposition
before delete on bestellposition
for each row execute function delete_bestellposition_function();


create or replace function status_bestellung_function() returns trigger as $$
begin
    if NEW.status = 'storniert' and OLD.status not in ('neu', 'bezahlt') then
        raise exception 'Der Status muss vorher "neu" oder "bezahlt" lauten, bevor man ihn auf "storniert" ändert!';
    end if;

    if NEW.status = 'storniert' and OLD.status <> 'storniert' then
        update produkt p set lagerbestand = p.lagerbestand + bp.menge from bestellposition bp
        where bp.sku = p.sku and bp.bestellung_id = NEW.bestellung_id;
    end if;
    return NEW;
end;
$$ language plpgsql;

create trigger status_bestellung
before update of status on bestellung
for each row execute function status_bestellung_function();

create or replace view v_kunde_summe_anzahl_bestellungen
as select k.kunde_id, k.email, count(distinct b.bestellung_id) as "anzahlBestellungen", coalesce(sum(bp.menge * p.preis), 0) as gesamtsumme
   from kunde k left join bestellung b on k.kunde_id = b.kunde_id left join bestellposition bp on b.bestellung_id = bp.bestellung_id  left join produkt p on bp.sku = p.sku
   group by k.kunde_id, k.email order by k.kunde_id asc;

create or replace view v_produkt_verkaufszahlen as
    select p.sku as sku,
           p.name as name,
           coalesce(sum(
                        case
                            when b.status not in ('neu', 'storniert') then bp.menge
                            else 0
                        end
                    ), 0) as gesamt_verkaufte_menge,
           coalesce(sum(
                   case
                       when b.status not in ('neu', 'storniert') then (p.preis * bp.menge)
                       else 0
                    end
                    ), 0) as umsatz,
           count(distinct b.bestellung_id) as anzahl_bestellungen
    from produkt p left join bestellposition bp on p.sku = bp.sku
    left join bestellung b on bp.bestellung_id = b.bestellung_id
    group by p.sku, p.name
    order by gesamt_verkaufte_menge desc;

create or replace view v_mitarbeiter_uebersicht
as select m.personal_nr, count(distinct b.bestellung_id) as anzahl_verwalteter_bestellungen, count(distinct p.sku) as anzahl_angelegter_produkte
    from mitarbeiter m left join bestellung b on m.personal_nr = b.mitarbeiterzuweis left join produkt p on m.personal_nr = p.angelegt_von
    group by m.personal_nr
    order by m.personal_nr asc;

create or replace view v_mitarbeiter_bestellstatus_uebersicht as
    with status_array as (select unnest(array['neu', 'bezahlt', 'versendet', 'abgeschlossen', 'storniert']) as status_name)
    select m.personal_nr, st.status_name as status, count(b.bestellung_id) as anzahl_bestellungen
    from mitarbeiter m cross join status_array st left join bestellung b on m.personal_nr = b.mitarbeiterzuweis and st.status_name = b.status
    group by m.personal_nr, st.status_name
    order by m.personal_nr asc, st.status_name asc;