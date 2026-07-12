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

        if ( altesGericht == null ) {

            throw new IllegalArgumentException( "Gericht darf nicht null sein." );
        }

        if ( bericht == null ) {

            throw new IllegalArgumentException( "Bericht darf nicht null sein." );
        }

        if ( altesGericht.getZutatenlisteProPerson() == null ) {

            throw new IllegalArgumentException( "Die Zutatenliste des Gerichts darf nicht null sein." );
        }

        if ( bericht.getZutatenUeberschuesse() == null ) {

            throw new IllegalArgumentException( "Die Zutaten-Überschüsse des Berichts dürfen nicht null sein." );
        }

        final List<ZutatUeberschuss> ueberschuesse = new ArrayList<>( bericht.getZutatenUeberschuesse() );

        final List<Zutat> neueZutaten = new ArrayList<>();

        for ( int i = 0; i < altesGericht.getZutatenlisteProPerson().size(); i++ ) {

            final Zutat alteZutat = altesGericht.getZutatenlisteProPerson().get( i );

            double prozentUeberschuss = 0.0;

            for ( int j = 0; j < ueberschuesse.size(); j++ ) {

                if ( ueberschuesse.get( j ).zutatName().equals( alteZutat.name() ) ) {

                    prozentUeberschuss = ueberschuesse.get( j ).prozentualerUeberschuss();
                }
            }

            final double neueMenge = bestimmeVeraenderung( alteZutat.menge(), prozentUeberschuss );

            final Zutat neueZutat = new Zutat( alteZutat.name(), neueMenge, alteZutat.einheit() );

            neueZutaten.add( neueZutat );
        }

        final Optional<GerichtDocument> hoechstesGericht = _gerichtRepo.findTopByOrderByNummerDesc();

        final int neueNummer = hoechstesGericht.getNummer() + 1;

        final GerichtDocument neuesGericht = new GerichtDocument(
                neueNummer,
                altesGericht.getName(),
                altesGericht.getVersion() + 1,
                neueZutaten );

        return _gerichtRepo.save( neuesGericht );
    }

     private double bestimmeVeraenderung( double menge, double prozentUeberschuss ) {

        final double veraenderung = 1 + ( prozentUeberschuss / 100 * ( -1 ) );

        return menge * veraenderung;
    }

}
