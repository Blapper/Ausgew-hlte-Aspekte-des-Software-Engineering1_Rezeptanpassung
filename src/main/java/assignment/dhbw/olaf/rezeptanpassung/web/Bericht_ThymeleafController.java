package assignment.dhbw.olaf.rezeptanpassung.web;

import java.util.ArrayList;
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
import assignment.dhbw.olaf.rezeptanpassung.db.Zutat;
import assignment.dhbw.olaf.rezeptanpassung.db.ZutatUeberschuss;
import assignment.dhbw.olaf.rezeptanpassung.logik.UebertragenService;
import assignment.dhbw.olaf.rezeptanpassung.web.EingabeValidierung;

@Controller
public class Bericht_ThymeleafController {


    private final static Logger LOG = LoggerFactory.getLogger( Bericht_ThymeleafController.class );

    /** Repo-Bean für Zugriff auf MongoDB-Collection mit Berichten. */
    @Autowired
    private BerichtRepo _berichtRepo;

    @Autowired
    private GerichtRepo _gerichtRepo;

    @Autowired
    private UebertragenService _uebertragenService;

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
                                      @RequestParam("anzahlWoelflinge") String anzahlWoelflingeText,
                                      @RequestParam("anzahlPfadfinder") String anzahlPfadfinderText,
                                      @RequestParam("anzahlRangerRover") String anzahlRangerRoverText,
                                      @RequestParam(value = "ueberschuss", required = false) List<String> ueberschussTexte,
                                      Model model ) {

        final Optional<GerichtDocument> gerichtOptional = _gerichtRepo.findByNummer( nummer );

        if ( gerichtOptional.isEmpty() ) {

            final String meldung = format( "Kein Gericht mit Nummer \"%s\" gefunden.", nummer );

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        if ( anzahlWoelflingeText.isBlank() ) {

            final String meldung = "Anzahl der Woelflinge darf nicht leer sein.";

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        if ( !EingabeValidierung.istZahl( anzahlWoelflingeText ) ) {

            final String meldung = "Anzahl der Woelflinge muss eine Zahl sein.";

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        if ( anzahlPfadfinderText.isBlank() ) {

            final String meldung = "Anzahl der Pfadfinder darf nicht leer sein.";

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        if ( !EingabeValidierung.istZahl( anzahlPfadfinderText ) ) {

            final String meldung = "Anzahl der Pfadfinder muss eine Zahl sein.";

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        if ( anzahlRangerRoverText.isBlank() ) {

            final String meldung = "Anzahl der RangerRover darf nicht leer sein.";

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        if ( !EingabeValidierung.istZahl( anzahlRangerRoverText ) ) {

            final String meldung = "Anzahl der RangerRover muss eine Zahl sein.";

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        final int anzahlWoelflinge = Integer.parseInt( anzahlWoelflingeText.trim() );
        final int anzahlPfadfinder = Integer.parseInt( anzahlPfadfinderText.trim() );
        final int anzahlRangerRover = Integer.parseInt( anzahlRangerRoverText.trim() );

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

        final List<Zutat> zutaten = gericht.getZutatenlisteProPerson();

        final List<String> ueberschuesseTexte = ueberschussTexte == null ? List.of() : ueberschussTexte;

        final int anzahlZutaten = zutaten == null ? 0 : zutaten.size();

        if ( ueberschuesseTexte.size() != anzahlZutaten ) {

            final String meldung = "Es muss für jede Zutat genau ein prozentualer Überschuss angegeben werden.";

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        /** Erste Schleife: nur Validierung der Überschuss-Texte. */
        for ( int i = 0; i < anzahlZutaten; i++ ) {

            final String ueberschussText = ueberschuesseTexte.get( i );
            final String zutatName = zutaten.get( i ).name();

            if ( ueberschussText == null || ueberschussText.isBlank() ) {

                final String meldung = format( 
                    "Überschuss für Zutat \"%s\" darf nicht leer sein."+
                     " 0 Eintragen wenn keine Änderung vorgenommen werden soll", zutatName );

                LOG.warn( meldung );

                model.addAttribute( "fehlermeldung", meldung );

                return "fehler";
            }

            if ( !EingabeValidierung.istZahl( ueberschussText ) ) {

                final String meldung = format( "Überschuss für Zutat \"%s\" muss eine Zahl sein.", zutatName );

                LOG.warn( meldung );

                model.addAttribute( "fehlermeldung", meldung );

                return "fehler";
            }
        }

        /** Nach erfolgreicher Validierung parsen. */
        final List<Double> ueberschuesse = new ArrayList<>();
        
        for ( int i = 0; i < anzahlZutaten; i++ ) {

        final String ueberschussText = ueberschuesseTexte.get( i );

        ueberschuesse.add( Double.parseDouble( ueberschussText.trim().replace( ",", "." ) ) );
        
        }

        final List<ZutatUeberschuss> zutatenUeberschuesse = new ArrayList<>();

        for ( int i = 0; i < anzahlZutaten; i++ ) {

            zutatenUeberschuesse.add( new ZutatUeberschuss( zutaten.get( i ).name(), ueberschuesse.get( i ) ) );
        }

        final Optional<BerichtDocument> hoechsterBericht = _berichtRepo.findTopByGerichtIdOrderByNummerDesc( gericht.getId() );

        final int naechsteBerichtNummer = hoechsterBericht.map( BerichtDocument::getNummer ).orElse( 0 ) + 1;

        final BerichtDocument bericht = new BerichtDocument( gericht.getId(), naechsteBerichtNummer, anzahlWoelflinge,
                                                               anzahlPfadfinder, anzahlRangerRover, zutatenUeberschuesse );

        _berichtRepo.save( bericht );

        return "redirect:/berichte/" + nummer;
    }

    //Rückfrage vor dem Löschen eines Berichtes
    @GetMapping( "/berichte/{nummer}/{berichtNummer}/loeschen" )
    public String berichtLoeschenBestaetigen( @PathVariable("nummer") int nummer,
                                               @PathVariable("berichtNummer") int berichtNummer,
                                               Model model ) {

        final Optional<GerichtDocument> gerichtOptional = _gerichtRepo.findByNummer( nummer );

        if ( gerichtOptional.isEmpty() ) {

            final String meldung = format( "Kein Gericht mit Nummer \"%s\" gefunden.", nummer );

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        final GerichtDocument gericht = gerichtOptional.get();

        final Optional<BerichtDocument> berichtOptional = _berichtRepo.findByGerichtIdAndNummer( gericht.getId(), berichtNummer );

        if ( berichtOptional.isEmpty() ) {

            final String meldung = format( "Kein Bericht mit Nummer \"%d\" für Gericht \"%s\" gefunden.", berichtNummer, nummer );

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        model.addAttribute( "gericht", gericht );
        model.addAttribute( "bericht", berichtOptional.get() );

        return "bericht-loeschen";
    }

    //Löschen eines Berichtes
    @PostMapping( "/berichte/{nummer}/{berichtNummer}/loeschen" )
    public String berichtLoeschen( @PathVariable("nummer") int nummer,
                                    @PathVariable("berichtNummer") int berichtNummer,
                                    Model model ) {

        final Optional<GerichtDocument> gerichtOptional = _gerichtRepo.findByNummer( nummer );

        if ( gerichtOptional.isEmpty() ) {

            final String meldung = format( "Kein Gericht mit Nummer \"%s\" gefunden.", nummer );

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        final GerichtDocument gericht = gerichtOptional.get();

        final Optional<BerichtDocument> berichtOptional = _berichtRepo.findByGerichtIdAndNummer( gericht.getId(), berichtNummer );

        if ( berichtOptional.isEmpty() ) {

            final String meldung = format( "Kein Bericht mit Nummer \"%d\" für Gericht \"%s\" gefunden.", berichtNummer, nummer );

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        _berichtRepo.delete( berichtOptional.get() );

        return "redirect:/berichte/" + nummer;
    }

    //Übertragen eines Berichtes auf ein neues Gericht
    @PostMapping( "/berichte/{nummer}/{berichtNummer}/uebertragen" )
    public String berichtUebertragen( @PathVariable("nummer") int nummer,
                                       @PathVariable("berichtNummer") int berichtNummer,
                                       Model model ) {

        final Optional<GerichtDocument> gerichtOptional = _gerichtRepo.findByNummer( nummer );

        if ( gerichtOptional.isEmpty() ) {

            final String meldung = format( "Kein Gericht mit Nummer \"%s\" gefunden.", nummer );

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        final GerichtDocument gericht = gerichtOptional.get();

        final Optional<BerichtDocument> berichtOptional = _berichtRepo.findByGerichtIdAndNummer( gericht.getId(), berichtNummer );

        if ( berichtOptional.isEmpty() ) {

            final String meldung = format( "Kein Bericht mit Nummer \"%d\" für Gericht \"%s\" gefunden.", berichtNummer, nummer );

            LOG.warn( meldung );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        final BerichtDocument bericht = berichtOptional.get();

        final GerichtDocument neuesGericht;

        try {

            neuesGericht = _uebertragenService.uebertrageBericht( gericht, bericht );

        } catch ( RuntimeException e ) {

            final String meldung = format( "Der Bericht \"%d\" konnte nicht übertragen werden: %s",
                                            berichtNummer, e.getMessage() );

            LOG.warn( meldung, e );

            model.addAttribute( "fehlermeldung", meldung );

            return "fehler";
        }

        return "redirect:/gerichte/" + neuesGericht.getNummer();
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