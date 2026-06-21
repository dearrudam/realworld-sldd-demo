package dev.realworld.authuser.control;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
class MongoIndexStartup {
    @Inject
    MongoClient mongo;

    @ConfigProperty(name = "jnosql.document.database")
    String database;

    @PostConstruct
    void createIndexes() {
        mongo.getDatabase(database)
                .getCollection("users")
                .createIndex(Indexes.ascending("email"), new IndexOptions().unique(true));
    }
}
