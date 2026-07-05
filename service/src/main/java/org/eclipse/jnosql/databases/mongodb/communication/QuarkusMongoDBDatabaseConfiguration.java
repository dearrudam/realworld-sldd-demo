package org.eclipse.jnosql.databases.mongodb.communication;

import com.mongodb.client.MongoClient;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.jnosql.communication.Settings;
import org.eclipse.jnosql.communication.semistructured.DatabaseConfiguration;

@Singleton
public class QuarkusMongoDBDatabaseConfiguration implements DatabaseConfiguration {

    @Inject
    MongoClient client;

    @Override
    public MongoDBDocumentManagerFactory apply(Settings settings) {
        return new MongoDBDocumentManagerFactory(this.client);
    }

}