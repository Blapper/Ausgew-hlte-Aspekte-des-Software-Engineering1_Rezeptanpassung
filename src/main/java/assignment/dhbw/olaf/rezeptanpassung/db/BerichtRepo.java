package assignment.dhbw.olaf.rezeptanpassung.db;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface BerichtRepo extends MongoRepository<BerichtDocument, ObjectId> {

    List<BerichtDocument> findByGerichtIdOrderByErstelltAmDesc( ObjectId gerichtId );
}