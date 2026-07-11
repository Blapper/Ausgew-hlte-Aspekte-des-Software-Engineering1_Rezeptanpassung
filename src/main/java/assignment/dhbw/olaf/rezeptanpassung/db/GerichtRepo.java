package assignment.dhbw.olaf.rezeptanpassung.db;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface GerichtRepo extends MongoRepository<GerichtDocument, ObjectId> {

    Optional<GerichtDocument> findByNummer( int nummer );

    Optional<GerichtDocument> findTopByOrderByNummerDesc();

    List<GerichtDocument> findAllByOrderByNameAsc();
}
