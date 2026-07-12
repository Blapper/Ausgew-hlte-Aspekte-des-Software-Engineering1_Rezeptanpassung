package assignment.dhbw.olaf.rezeptanpassung.db;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface BerichtRepo extends MongoRepository<BerichtDocument, ObjectId> {

    List<BerichtDocument> findByGerichtIdOrderByNummerDesc( ObjectId gerichtId );

    Optional<BerichtDocument> findTopByGerichtIdOrderByNummerDesc( ObjectId gerichtId );

    Optional<BerichtDocument> findByGerichtIdAndNummer( ObjectId gerichtId, int nummer );

    void deleteByGerichtId( ObjectId gerichtId );
}