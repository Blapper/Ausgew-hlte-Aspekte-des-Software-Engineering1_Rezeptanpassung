package assignment.dhbw.olaf.rezeptanpassung.web;

import java.util.ArrayList;
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
import assignment.dhbw.olaf.rezeptanpassung.db.Zutat;


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

    /** Zeigt  alle Gerichte an */
    @GetMapping( "/gerichte" )
    public String gerichteListe( Model model ) {

        final List<GerichtDocument> gerichte =
                     _gerichtRepo.findAllByOrderByNameAsc();
          

        model.addAttribute( "gerichte", gerichte );

        return "gericht-liste";
    }

    /** Auswahl eines spezifischen Gerichtes */
    @GetMapping( "/gerichte/{nummer}" )
    public String gerichtDetail( @PathVariable("nummer") int nummer, Model model ) {

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

    /** Anlegen eines neuen Gerichtes */
    @GetMapping( "/gerichte/neu" )
    public String gerichtNeuFormular( Model model ) {

    return "gericht-neu";
    }
    
    /** Speichern eines neuen Gerichtes */
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

    /** Löschen eines Gerichtes */
    @GetMapping( "/gerichte/{nummer}/loeschen" )
    public String gerichtLoeschenBestaetigen( @PathVariable("nummer") int nummer, Model model ) {

        final Optional<GerichtDocument> gerichtOptional = _gerichtRepo.findByNummer( nummer );

        if ( gerichtOptional.isEmpty() ) {

            final String meldung = format( "Kein Gericht mit Nummer \"%s\" gefunden.", nummer );

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        model.addAttribute( "gericht", gerichtOptional.get() );

        return "gericht-loeschen";
    }


    @PostMapping( "/gerichte/{nummer}/loeschen" )
    public String gerichtLoeschen( @PathVariable("nummer") int nummer, Model model ) {

        final Optional<GerichtDocument> gerichtOptional = _gerichtRepo.findByNummer( nummer );

        if ( gerichtOptional.isEmpty() ) {

            final String meldung = format( "Kein Gericht mit Nummer \"%s\" gefunden.", nummer );

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        _gerichtRepo.delete( gerichtOptional.get() );

        return "redirect:/gerichte";
    }

    /**Hinzufügen einer Zutat */
    
    @GetMapping( "/gerichte/{nummer}/zutaten/neu" )
    public String zutatNeuFormular( @PathVariable("nummer") int nummer, Model model ) {

        final Optional<GerichtDocument> gerichtOptional = _gerichtRepo.findByNummer( nummer );

        if ( gerichtOptional.isEmpty() ) {

            final String meldung = format( "Kein Gericht mit Nummer \"%s\" gefunden.", nummer );

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        model.addAttribute( "gericht", gerichtOptional.get() );

        return "zutat-neu";
    }

    /**Posten der Zutat */
    @PostMapping( "/gerichte/{nummer}/zutaten/neu" )
    public String zutatNeuSpeichern( @PathVariable("nummer") int nummer,
                                      @RequestParam("name") String name,
                                      @RequestParam("menge") double menge,
                                      @RequestParam("einheit") String einheit,
                                      Model model ) {

        final Optional<GerichtDocument> gerichtOptional = _gerichtRepo.findByNummer( nummer );

        if ( gerichtOptional.isEmpty() ) {

            final String meldung = format( "Kein Gericht mit Nummer \"%s\" gefunden.", nummer );

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        if ( name.isBlank() ) {

            final String meldung = "Name der Zutat darf nicht leer sein.";

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        if ( menge <= 0 ) {

            final String meldung = "Menge muss größer als 0 sein.";

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        if ( einheit.isBlank() ) {

            final String meldung = "Einheit darf nicht leer sein.";

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        final GerichtDocument gericht = gerichtOptional.get();

        final List<Zutat> vorhandeneZutaten = gericht.getZutatenlisteProPerson();

        final List<Zutat> neueZutatenliste =
                     new ArrayList<>( vorhandeneZutaten == null ? List.of() : vorhandeneZutaten );

        neueZutatenliste.add( new Zutat( name, menge, einheit ) );

        gericht.setZutatenlisteProPerson( neueZutatenliste );

        _gerichtRepo.save( gericht );

        return "redirect:/gerichte/" + nummer + "/zutaten/neu";
    }

   /**Löschen der Zutat */
    @GetMapping( "/gerichte/{nummer}/zutaten/{index}/loeschen" )
    public String zutatLoeschenBestaetigen( @PathVariable("nummer") int nummer,
                                             @PathVariable("index") int index,
                                             Model model ) {

        final Optional<GerichtDocument> gerichtOptional = _gerichtRepo.findByNummer( nummer );

        if ( gerichtOptional.isEmpty() ) {

            final String meldung = format( "Kein Gericht mit Nummer \"%s\" gefunden.", nummer );

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        final GerichtDocument gericht = gerichtOptional.get();

        final Zutat zutat = gericht.getZutatenlisteProPerson().get( index );

        model.addAttribute( "gericht", gericht );
        model.addAttribute( "zutat", zutat );
        model.addAttribute( "index", index );

        return "zutat-loeschen";
    }

    /**Bestätigung der Löschung der Zutat */
    @PostMapping( "/gerichte/{nummer}/zutaten/{index}/loeschen" )
    public String zutatLoeschen( @PathVariable("nummer") int nummer,
                                  @PathVariable("index") int index,
                                  Model model ) {

        final Optional<GerichtDocument> gerichtOptional = _gerichtRepo.findByNummer( nummer );

        if ( gerichtOptional.isEmpty() ) {

            final String meldung = format( "Kein Gericht mit Nummer \"%s\" gefunden.", nummer );

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        final GerichtDocument gericht = gerichtOptional.get();

        final List<Zutat> neueZutatenliste = new ArrayList<>( gericht.getZutatenlisteProPerson() );

        neueZutatenliste.remove( index );

        gericht.setZutatenlisteProPerson( neueZutatenliste );

        _gerichtRepo.save( gericht );

        return "redirect:/gerichte/" + nummer;
    }
}
