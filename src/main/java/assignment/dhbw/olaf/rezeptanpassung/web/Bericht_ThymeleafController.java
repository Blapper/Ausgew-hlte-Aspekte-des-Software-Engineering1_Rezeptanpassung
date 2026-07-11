package assignment.dhbw.olaf.rezeptanpassung.web;

import java.util.List;
import java.util.Optional;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import assignment.dhbw.olaf.rezeptanpassung.db.GerichtDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    
    //Anlegen eines neuen Berichtes

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

        final List<BerichtDocument> berichte = _berichtRepo.findByGerichtIdOrderByNummerDesc( gerichtId );

        model.addAttribute( "berichte", berichte );
        model.addAttribute( "gericht", gericht );

        return "berichte-liste";
    }

    //Anlegen eines neuen Berichtes 
    @GetMapping( "/berichte/neu/{nummer}" )
    public String berichtNeuFormular( @PathVariable("nummer") int nummer, Model model ) {

        final Optional<GerichtDocument> gerichtOptional = _gerichtRepo.findByNummer( nummer );

        if ( gerichtOptional.isEmpty() ) {

            final String meldung = format( "Kein Gericht mit Nummer \"%s\" gefunden.", nummer );

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        model.addAttribute( "gericht", gerichtOptional.get() );

        return "bericht-neu";
    }

    //Speichern eines neuen Berichtes
    @PostMapping( "/berichte/neu/{nummer}" )
    public String berichtNeuSpeichern( @PathVariable("nummer") int nummer,
                                      @RequestParam("anzahlWoelflinge") int anzahlWoelflinge,
                                      @RequestParam("anzahlPfadfinder") int anzahlPfadfinder,
                                      @RequestParam("anzahlRangerRover") int anzahlRangerRover,
                                      @RequestParam("prozentualerUeberschuss") double prozentualerUeberschuss,
                                      Model model ) {

        final Optional<GerichtDocument> gerichtOptional = _gerichtRepo.findByNummer( nummer );

        if ( gerichtOptional.isEmpty() ) {

            final String meldung = format( "Kein Gericht mit Nummer \"%s\" gefunden.", nummer );

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        if ( anzahlWoelflinge <= 0 ) {

            final String meldung = "Anzahl der Woelflinge muss größer als 0 sein.";

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        if ( anzahlPfadfinder <= 0 ) {

            final String meldung = "Anzahl der Pfadfinder muss größer als 0 sein.";

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        if ( anzahlRangerRover <= 0 ) {

            final String meldung = "Anzahl der RangerRover muss größer als 0 sein.";

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        final GerichtDocument gericht = gerichtOptional.get();

        final Optional<BerichtDocument> hoechsterBericht = _berichtRepo.findTopByGerichtIdOrderByNummerDesc( gericht.getId() );

        final int naechsteBerichtNummer = hoechsterBericht.map( BerichtDocument::getNummer ).orElse( 0 ) + 1;

        final BerichtDocument bericht = new BerichtDocument( gericht.getId(), naechsteBerichtNummer, anzahlWoelflinge,
                                                               anzahlPfadfinder, anzahlRangerRover, prozentualerUeberschuss );

        _berichtRepo.save( bericht );

        return "redirect:/berichte/" + nummer;
    }

}

/** Anlegen eines neuen Gerichtes 
    @GetMapping( "/gerichte/neu" )
    public String gerichtNeuFormular( Model model ) {

    return "gericht-neu";
    } 
    
    Speichern eines neuen Gerichtes 
    @PostMapping( "/gerichte/neu" )
    public String gerichtNeuSpeichern( Model model,
                                    @RequestParam("name") String name ) {

        if ( name.isBlank() ) {

            final String meldung = "Name darf nicht leer sein.";

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        final Optional<GerichtDocument> hoechstesGericht = _gerichtRepo.findTopByOrderByNummerDesc();

        final int naechsteNummer = hoechstesGericht.map( GerichtDocument::getNummer ).orElse( 0 ) + 1;

        final GerichtDocument gericht = new GerichtDocument( naechsteNummer, name, 1, null );

        _gerichtRepo.save( gericht );

        model.addAttribute( "gericht", gericht );
        
        return "gericht-detail"; // oder eigenes Erfolgs-Template
    }
    
    */