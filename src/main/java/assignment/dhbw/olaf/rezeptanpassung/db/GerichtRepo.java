package main.java.assignment.dhbw.olaf.rezeptanpassung.db; // ohne Main und Java wurde das Package nicht erkannt
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface GerichtRepo extends MongoRepository<GerichtDocument, ObjectId> {

    Optional<GerichtDocument> findByNummer( String nummer );

    List<GerichtDocument> findAllByOrderByNameAsc();
}
