package dev.realworld.authuser.control;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.IndexOptions;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.bson.Document;

public class MongoIndexStartup {

    @Inject
    MongoClient mongoClient;

    void onStart(@Observes StartupEvent event) {
        var database = mongoClient.getDatabase("realworld");
        database.getCollection("User").createIndex(
            new Document("email", 1),
            new IndexOptions().unique(true)
        );
    }
}