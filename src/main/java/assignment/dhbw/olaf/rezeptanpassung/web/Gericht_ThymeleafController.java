package assignment.dhbw.olaf.rezeptanpassung.web;

import java.util.List;
import java.util.Optional;
import static java.lang.String.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import assignment.dhbw.olaf.rezeptanpassung.db.GerichtDocument;
import assignment.dhbw.olaf.rezeptanpassung.db.GerichtRepo;


/**
 * Controller für Thymeleaf-Templates zur Gerichte-Verwaltung.
 */
@Controller
public class Gericht_ThymeleafController {

    private final static Logger LOG = LoggerFactory.getLogger( Gericht_ThymeleafController.class );

    /** Repo-Bean für Zugriff auf MongoDB-Collection mit Gerichten. */
    @Autowired
    private GerichtRepo _gerichtRepo;

    /**
     * Zeigt die Liste aller Gerichte an.
     *
     * @param model Objekt für die Platzhalter im Template.
     *
     * @return Name der Template-Datei ohne Endung {@code .html}
     */

    @GetMapping( "/gerichte" )
    public String gerichteListe( Model model ) {

        final List<GerichtDocument> gerichte =
                     _gerichtRepo.findAllByOrderByNameAsc();
          

        model.addAttribute( "gerichte", gerichte );

        return "gerichte-liste";
    }

    @GetMapping( "/gerichte/{nummer}" )
    public String gerichtDetail( @PathVariable("nummer") String nummer, Model model ) {

    final Optional<GerichtDocument> gerichtOptional = _gerichtRepo.findByNummer( nummer );

    if ( gerichtOptional.isEmpty() ) {

        final String meldung = format( "Kein Gericht mit Nummer \"%s\" gefunden.", nummer );

        LOG.warn( meldung );

        model.addAttribute( "fehlermeldung", meldung );

        return "fehler";
    }

    final GerichtDocument gericht = gerichtOptional.get();

    model.addAttribute( "gericht", gericht );

    return "gericht-detail";
}


    /**
     * Zeigt das leere Formular zum Anlegen eines neuen Gerichts.
     *
     * @param model Objekt für die Platzhalter im Template.
     *
     * @return Name der Template-Datei ohne Endung {@code .html}
     */
    @GetMapping( "/gerichte/neu" )
    public String gerichtNeuFormular( Model model ) {

    return "gericht-neu";
    }


    /**
     * Verarbeitet das Absenden des Formulars zum Anlegen eines neuen Gerichts.
     *
     * @param model Objekt für die Platzhalter im Template.
     * @param nummer Eingegebene Nummer.
     * @param name Eingegebener Name.
     * @param version Eingegebene Version.
     *
     * @return Name der Template-Datei ohne Endung {@code .html}
     */
    
    @PostMapping( "/gerichte/neu" )
    public String gerichtNeuSpeichern( Model model,
                                    @RequestParam("nummer") String nummer,
                                    @RequestParam("name") String name,
                                    @RequestParam("version") String version ) {

    final Optional<GerichtDocument> vorhanden = _gerichtRepo.findByNummer( nummer );
                                    

    if ( vorhanden.isPresent() ) {

        final String meldung = format( "Gericht mit Nummer \"%s\" existiert bereits.", nummer );

        LOG.warn( meldung );

        model.addAttribute( "fehlermeldung", meldung );
        return "fehler";
    }

    final GerichtDocument gericht = new GerichtDocument( nummer, name, version, null );

    _gerichtRepo.save( gericht );

    model.addAttribute( "gericht", gericht );
    return "gericht-detail"; // oder eigenes Erfolgs-Template
    }



}
