package dev.realworld.authuser.control;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.jnosql.mapping.DatabaseQualifier;

@ApplicationScoped
public class MongoIndexStartup {

    @Inject
    MongoClient mongoClient;

    void onStart(@Observes StartupEvent event) {
        var database = mongoClient.getDatabase("realworld");
        var users = database.getCollection("users");
        users.createIndex(Indexes.ascending("email"), new IndexOptions().unique(true));
    }
}
