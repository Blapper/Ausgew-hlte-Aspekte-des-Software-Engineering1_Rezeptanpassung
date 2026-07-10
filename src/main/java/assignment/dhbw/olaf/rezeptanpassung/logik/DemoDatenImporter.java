package assignment.dhbw.olaf.rezeptanpassung.logik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import assignment.dhbw.olaf.rezeptanpassung.db.GerichtDocument;
import assignment.dhbw.olaf.rezeptanpassung.db.Zutat;
import assignment.dhbw.olaf.rezeptanpassung.db.GerichtRepo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Diese Bean importiert Demo-Daten unmittelbar nach dem Hochfahren der Anwendung,
 * wenn noch keine Rezepte in der Datenbank sind. 
 */
@Component
public class DemoDatenImporter implements ApplicationRunner {
    
    private final static Logger LOG = LoggerFactory.getLogger( DemoDatenImporter.class );

    /** Repo-Objekt für Zugriff auf Mongodb-Collection. */
    @Autowired
    private GerichtRepo _gerichtRepo; 
    
    
    /**
     * Diese Methode wird kurz nach Hochfahren der Anwendung ausgeführt.
     */
    @Override
    public void run( ApplicationArguments args ) throws Exception {

    if ( _gerichtRepo.count() == 0 ) {

        LOG.info( "DB enthält keine Gerichte, lade jetzt Demo-Daten ..." );

        final Zutat mehl = new Zutat( "Mehl", 250.0, "g" );
        createGericht( "G-001", "Pfannkuchen", "v1", List.of( mehl ) );

        final long anzahlGerichte = _gerichtRepo.count();
        LOG.info( "Es sind jetzt {} Gerichte in der DB gespeichert.", anzahlGerichte );

        } else {

        LOG.info( "Es sind schon {} Gerichte in der DB gespeichert, lade keine Demo-Daten.", _gerichtRepo.count() );
        }
    }
  
    /**
     * Hilfsmethode zum Anlegen eines Gerichtes.
     */
    private void createGericht( String nummer, String name, String version, List<Zutat> zutatenlisteProPerson ) {

        final GerichtDocument gericht = new GerichtDocument( nummer, name, version, zutatenlisteProPerson );
        
        _gerichtRepo.save( gericht );
    }

}
