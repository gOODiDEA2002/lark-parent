package lark.db.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MongoDataSourceHolder {

    private final Map<String, MongoTemplate> templates = new ConcurrentHashMap<>();

    private Map<String, MongoProperties> properties = new HashMap<>();

    public void addProperty(String name, MongoProperties properties) {
        this.properties.put(name, properties);
        MongoClientOptions.Builder options = new MongoClientOptions.Builder();
        options.connectionsPerHost(100);
        ConnectionString connectionString = new ConnectionString(properties.getUri());
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder().applyConnectionString(connectionString).build();
        MongoClient mongoClient = MongoClients.create(mongoClientSettings);
//        SimpleMongoDbFactory dbFactory = new SimpleMongoDbFactory(new MongoClientURI(properties.getUri(), options));
//        factories.put(name, dbFactory);
        //TODO: 指定converter
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, name );
        templates.put(name, mongoTemplate);
    }

    public MongoTemplate getTemplate(String name) {
        return templates.get(name);
    }

}
