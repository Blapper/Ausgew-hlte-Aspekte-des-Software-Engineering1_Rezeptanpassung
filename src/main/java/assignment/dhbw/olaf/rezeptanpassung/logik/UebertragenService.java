package assignment.dhbw.olaf.rezeptanpassung.logik;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import assignment.dhbw.olaf.rezeptanpassung.db.BerichtDocument;
import assignment.dhbw.olaf.rezeptanpassung.db.GerichtDocument;
import assignment.dhbw.olaf.rezeptanpassung.db.GerichtRepo;
import assignment.dhbw.olaf.rezeptanpassung.db.Zutat;
import assignment.dhbw.olaf.rezeptanpassung.db.ZutatUeberschuss;

@Component
public class UebertragenService {

    private final static Logger LOG = LoggerFactory.getLogger( UebertragenService.class );

    /** Repo-Objekt für Zugriff auf Mongodb-Collection. */
    @Autowired
    private GerichtRepo _gerichtRepo;

    /**
     * Überträgt den prozentualen Über-/Unterschuss aus einem Bericht auf die Zutatenmengen
     * und legt daraus ein komplett neues Gericht an (neue Nummer, Version + 1).
     */
    public GerichtDocument uebertrageBericht( GerichtDocument altesGericht, BerichtDocument bericht ) {


        final List<ZutatUeberschuss> ueberschuesse = new ArrayList<>( bericht.getZutatenUeberschuesse() );

        final List<Zutat> neueZutaten = new ArrayList<>();

        for ( int i = 0; i < altesGericht.getZutatenlisteProPerson().size(); i++ ) {

            final Zutat alteZutat = altesGericht.getZutatenlisteProPerson().get( i );

            final Zutat neueZutat = new Zutat( alteZutat.name(), alteZutat.menge() * 2, alteZutat.einheit() );

            neueZutaten.add( neueZutat );
        }

        final Optional<GerichtDocument> hoechstesGericht = _gerichtRepo.findTopByOrderByNummerDesc();
        final int neueNummer = hoechstesGericht.get().getNummer() + 1;

        final GerichtDocument neuesGericht = new GerichtDocument(
                neueNummer,
                altesGericht.getName(),
                altesGericht.getVersion() + 1,
                neueZutaten );

        return _gerichtRepo.save( neuesGericht );
    }

}
