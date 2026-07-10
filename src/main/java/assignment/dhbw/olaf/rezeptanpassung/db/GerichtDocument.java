package assignment.dhbw.olaf.rezeptanpassung.db;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;

@Document(collection = "gerichte")
public class GerichtDocument {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    @Field("nummer")
    private String nummer;
    private String name;
    private String version;
    private List<Zutat> zutatenlisteProPerson;

/** Default-Konstruktor. */
    public GerichtDocument() {}


    /**
     * Konstruktor, für neue Rezepte.
     */
    public GerichtDocument( String nummer, String name, String version, List<Zutat> zutatenlisteProPerson ) {

        this.nummer                 = nummer;
        this.name                   = name;
        this.version                = version;
        this.zutatenlisteProPerson  = zutatenlisteProPerson;
    }

    public ObjectId getId() { return id; }

    public String getNummer() { return nummer; }
    public void setNummer(String nummer) { this.nummer = nummer; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public List<Zutat> getZutatenlisteProPerson() { return zutatenlisteProPerson; }

    public void setZutatenlisteProPerson(List<Zutat> z) { this.zutatenlisteProPerson = z; }

     @Override // Ohne diese Funktion wird im Fall einer falschen Notation die Speicheradresse ausgegeben
    public String toString() { return "Gericht \"" + name + "\" (Nr. " + nummer + ")"; } 

}