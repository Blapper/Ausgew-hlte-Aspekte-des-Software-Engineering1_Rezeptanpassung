package assignment.dhbw.olaf.rezeptanpassung.db;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.bson.types.ObjectId;

@Document(collection = "berichte")
public class BerichtDocument {

    @Id
    private ObjectId id;
    private ObjectId gerichtId;
    private int nummer;
    private int anzahlWoelflinge;
    private int anzahlPfadfinder;
    private int anzahlRangerRover;
    private List<ZutatUeberschuss> zutatenUeberschuesse;


    /** Default-Konstruktor. */
    public BerichtDocument() {}

    /**
     * Konstruktor, für neue Rezepte.
     */
    public BerichtDocument( ObjectId gerichtId, int nummer, int anzahlWoelflinge, int anzahlPfadfinder, int anzahlRangerRover,
                            List<ZutatUeberschuss> zutatenUeberschuesse){

        this.gerichtId                  = gerichtId;
        this.nummer                     = nummer;
        this.anzahlWoelflinge           = anzahlWoelflinge;
        this.anzahlPfadfinder           = anzahlPfadfinder;
        this.anzahlRangerRover          = anzahlRangerRover;
        this.zutatenUeberschuesse       = zutatenUeberschuesse;
    }

    public ObjectId getId() { return id; }
    public ObjectId getGerichtId() { return gerichtId; }

    public int getNummer() { return nummer; }
    public void setNummer(int nummer) { this.nummer = nummer; }

    public int getanzahlWoelflinge() { return anzahlWoelflinge; }
    public void setanzahlWoelflinge(int anzahlWoelflinge) { this.anzahlWoelflinge = anzahlWoelflinge; }

    public int getanzahlPfadfinder() { return anzahlPfadfinder; }
    public void setanzahlPfadfinder(int anzahlPfadfinder) { this.anzahlPfadfinder = anzahlPfadfinder; }

    public int getanzahlRangerRover() { return anzahlRangerRover; }
    public void setanzahlRangerRover(int anzahlRangerRover) { this.anzahlRangerRover = anzahlRangerRover; }

    public List<ZutatUeberschuss> getZutatenUeberschuesse() { return zutatenUeberschuesse; }
    public void setZutatenUeberschuesse(List<ZutatUeberschuss> zutatenUeberschuesse) { this.zutatenUeberschuesse = zutatenUeberschuesse; }


     @Override // Ohne diese Funktion wird im Fall einer falschen Notation die Speicheradresse ausgegeben
    public String toString() {
    return "Bericht Nr. " + nummer + " für Gericht " + gerichtId
         + " (Woelflinge: " + anzahlWoelflinge
         + ", Pfadfinder: " + anzahlPfadfinder
         + ", RangerRover: " + anzahlRangerRover
         + ", Zutaten-Überschüsse: " + zutatenUeberschuesse + ")";
    } 

}


