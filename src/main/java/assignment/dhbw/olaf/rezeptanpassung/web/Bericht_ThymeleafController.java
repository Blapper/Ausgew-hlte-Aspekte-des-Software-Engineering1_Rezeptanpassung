package assignment.dhbw.olaf.rezeptanpassung.web;

import java.util.List;
import java.util.Optional;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import assignment.dhbw.olaf.rezeptanpassung.db.GerichtDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import static java.lang.String.format;
import org.bson.types.ObjectId;

import assignment.dhbw.olaf.rezeptanpassung.db.BerichtDocument;
import assignment.dhbw.olaf.rezeptanpassung.db.BerichtRepo;
import assignment.dhbw.olaf.rezeptanpassung.db.GerichtRepo;

@Controller
public class Bericht_ThymeleafController {


    private final static Logger LOG = LoggerFactory.getLogger( Bericht_ThymeleafController.class );

    /** Repo-Bean für Zugriff auf MongoDB-Collection mit Berichten. */
    @Autowired
    private BerichtRepo _berichtRepo;

    @Autowired
    private GerichtRepo _gerichtRepo;
    

    @GetMapping( "/berichte/{nummer}" )
    public String berichteListe( @PathVariable("nummer") int nummer, Model model ) {

        final Optional<GerichtDocument> gerichtOptional = 
                _gerichtRepo.findByNummer( nummer ); // erstmal wird über die Nummer das Gericht zugeordnet

        if ( gerichtOptional.isEmpty() ) {

        final String meldung = format( "Kein Gericht mit Nummer \"%s\" gefunden.", nummer );

        LOG.warn( meldung );

        model.addAttribute( "fehlermeldung", meldung );

        return "fehler";
    }
          
        final GerichtDocument gericht = gerichtOptional.get();
        final ObjectId gerichtId = gericht.getId();   // der  DB-Schlüssel

        final List<BerichtDocument> berichte = _berichtRepo.findByGerichtIdOrderByErstelltAmDesc( gerichtId );

        model.addAttribute( "berichte", berichte );

        return "berichte-liste";
    }
}
/** Zeigt  alle Gerichte an 
    @GetMapping( "/gerichte" )
    public String gerichteListe( Model model ) {

        final List<GerichtDocument> gerichte =
                     _gerichtRepo.findAllByOrderByNameAsc();
          

        model.addAttribute( "gerichte", gerichte );

        return "gericht-liste";
    } */