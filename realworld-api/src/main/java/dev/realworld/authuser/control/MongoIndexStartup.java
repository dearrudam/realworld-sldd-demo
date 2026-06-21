package dev.realworld.authuser.control;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.model.IndexOptions;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.bson.Document;

@ApplicationScoped
public class MongoIndexStartup {
    @Inject MongoClient mongo;
    void createEmailIndex(@Observes StartupEvent event) {
        try {
            mongo.getDatabase("realworld").getCollection("users").createIndex(new Document("email", 1), new IndexOptions().unique(true));
        } catch (MongoException _) {
            // Dev Services may be unavailable in constrained environments; repository operations still fail fast when used.
        }
    }
}
