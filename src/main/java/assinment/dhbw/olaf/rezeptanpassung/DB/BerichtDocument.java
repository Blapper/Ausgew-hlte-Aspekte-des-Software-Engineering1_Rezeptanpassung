package abgabe.dhbw.olaf.rezeptanpassung.db;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "berichte")
public class BerichtDocument {

    @Id
    private ObjectId id;
    private ObjectId gerichtId;
    private int anzahlWoelflinge;
    private int anzahlPfadfinder;
    private int anzahlRangerRover;
    private double prozentualerUeberschuss;
    private LocalDateTime erstelltAm;

    // Konstruktoren, Getter/Setter, toString -> selbst schreiben

    /** Default-Konstruktor. */
    public BerichtDocument() {}

    /**
     * Konstruktor, für neue Rezepte.
     */
    public GerichtDocument( ObjectId gerichtId, int anzahlWoelflinge, int anzahlPfadfinder, int anzahlRangerRover,
                            double prozentualerUeberschuss, LocalDateTime erstelltAm){

        this.gerichtId                  = gerichtId;
        this.anzahlWoelflinge           = anzahlWoelflinge;
        this.anzahlPfadfinder           = anzahlPfadfinder;
        this.anzahlRangerRover          = anzahlRangerRover;
        this.prozentualerUeberschuss    = prozentualerUeberschuss;
        this.erstelltAm                 = erstelltAm;
    }

    public ObjectId getId() { return id; }
    public ObjectId getGerichtId() { return gerichtId; }

    public int getanzahlWoelflinge() { return anzahlWoelflinge; }
    public void setanzahlWoelflinge(int anzahlWoelflinge) { this.anzahlWoelflinge = anzahlWoelflinge; }

    public int getanzahlPfadfinder() { return anzahlPfadfinder; }
    public void setanzahlPfandfindere(int anzahlPfadfinder) { this.anzahlPfadfinder = anzahlPfadfinder; }

    public int getanzahlRangerRover() { return anzahlRangerRover; }
    public void setanzahlRangerRover(int anzahlRangerRover) { this.anzahlRangerRover = anzahlRangerRover; }

    public int getprozentualerUeberschuss() { return prozentualerUeberschuss; }
    public void setprozentualerUeberschuss(int prozentualerUeberschuss) { this.prozentualerUeberschuss = prozentualerUeberschuss; }

    public int geterstelltAm() { return erstelltAm; } // kein Seter notwendig

     @Override // Ohne diese Funktion wird im Fall einer falschen Notation die Speicheradresse ausgegeben
    public String toString() { return "Gericht \"" + name + "\" (Nr. " + nummer + ")"; } 

}


