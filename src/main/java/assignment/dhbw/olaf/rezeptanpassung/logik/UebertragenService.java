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

        // Äußere Schleife: geht jede Zutat des alten Gerichts einzeln durch
        for ( int i = 0; i < altesGericht.getZutatenlisteProPerson().size(); i++ ) {

            final Zutat alteZutat = altesGericht.getZutatenlisteProPerson().get( i );

            double prozentUeberschuss = 0.0;

            /** Innere Schleife: sucht in der Überschuss-Liste den Eintrag, 
             erkennt Zutaten über Zutat-Name  */ 

            for ( int j = 0; j < ueberschuesse.size(); j++ ) {

                if ( ueberschuesse.get( j ).zutatName().equals( alteZutat.name() ) ) {

                    prozentUeberschuss = ueberschuesse.get( j ).prozentualerUeberschuss();
                }
            }
                /** Berechne neu Menge, rechnet mit 0 falls zuvor kein Treffer  */ 
            final double neueMenge = bestimmeVeraenderung( alteZutat.menge(), prozentUeberschuss );

            final Zutat neueZutat = new Zutat( alteZutat.name(), neueMenge, alteZutat.einheit() );

            neueZutaten.add( neueZutat );
        }

        final Optional<GerichtDocument> hoechstesGericht = _gerichtRepo.findTopByOrderByNummerDesc();

        final int neueNummer = hoechstesGericht.get().getNummer() + 1;

          /**
         * Version zeigt an was die vorherige Version war
        * mehrere Versionen wie V3 möglich.
         */        
        final GerichtDocument neuesGericht = new GerichtDocument(
                neueNummer,
                altesGericht.getName(),
                altesGericht.getVersion() + 1,
                neueZutaten );

        return _gerichtRepo.save( neuesGericht );
    }

    /**
         * Hilfsmethode für die Übernahme der Veränderung
        * rechnet *(-1) da 20% Überschuss = zuviel
         */
     private double bestimmeVeraenderung( double menge, double prozentUeberschuss ) {

        final double veraenderung = 1 + ( prozentUeberschuss / 100 * ( -1 ) );

        final double neueMenge = menge * veraenderung;

        /**
        * Runden auf 3 Komma-Stellen
        */
        return Math.round( neueMenge * 1000.0 ) / 1000.0;
    }

}
