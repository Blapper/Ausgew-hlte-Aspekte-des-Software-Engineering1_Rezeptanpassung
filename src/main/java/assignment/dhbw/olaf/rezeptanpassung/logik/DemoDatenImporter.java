package assignment.dhbw.olaf.rezeptanpassung.logik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.bson.types.ObjectId;

import assignment.dhbw.olaf.rezeptanpassung.db.BerichtDocument;
import assignment.dhbw.olaf.rezeptanpassung.db.BerichtRepo;
import assignment.dhbw.olaf.rezeptanpassung.db.GerichtDocument;
import assignment.dhbw.olaf.rezeptanpassung.db.Zutat;
import assignment.dhbw.olaf.rezeptanpassung.db.ZutatUeberschuss;
import assignment.dhbw.olaf.rezeptanpassung.db.GerichtRepo;

import java.util.List;
import java.util.Optional;

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

    @Autowired
    private BerichtRepo _berichtRepo;
    
    
    /**
     * Diese Methode wird kurz nach Hochfahren der Anwendung ausgeführt.
     */
    @Override
    public void run( ApplicationArguments args ) throws Exception {


    if ( _gerichtRepo.count() == 0 ) {

        LOG.info( "DB enthält keine Gerichte, lade jetzt Demo-Daten ..." );

        final Zutat mehl = new Zutat( "Mehl", 250.0, "g" );
        createGericht( 1, "Pfannkuchen", 1, List.of( mehl ) );


        final Optional<GerichtDocument> gerichtOptional = 
                _gerichtRepo.findByNummer( 1 ); // Nummer des Demo-Gerichtes ist bekannt

        ObjectId gerichtId = gerichtOptional.get().getId();

        List<ZutatUeberschuss> ueberschuesse = List.of(new ZutatUeberschuss("Mehl", 5.0));

        BerichtDocument bericht = new BerichtDocument(gerichtId, 1, 10, 5, 6, ueberschuesse);

        _berichtRepo.save(bericht);

        LOG.info( "Es sind jetzt {} Gericht und {} Bericht in der DB gespeichert.", _gerichtRepo.count(), _berichtRepo.count() );

        } else {

        LOG.info( "Es sind schon {} Gerichte und {} Berichte in der DB gespeichert, lade keine Demo-Daten.", _gerichtRepo.count(), _berichtRepo.count() );
        }
    }
  
    /**
     * Hilfsmethode zum Anlegen eines Gerichtes.
     */
    private void createGericht( int nummer, String name, int version, List<Zutat> zutatenlisteProPerson ) {

        final GerichtDocument gericht = new GerichtDocument( nummer, name, version, zutatenlisteProPerson );
        
        _gerichtRepo.save( gericht );
    }

}
