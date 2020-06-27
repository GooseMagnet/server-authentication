package com.karasik.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.karasik.model.UserDto;
import com.karasik.util.connectors.MongoConnector;
import com.karasik.util.properies.PropertyReader;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MongoDao {

    private final ObjectMapper objectMapper;
    private static final String mongodb_database = PropertyReader.getProperty("mongo.database");
    private static final String mongodb_collection = PropertyReader.getProperty("mongo.collection");

    @Inject
    public MongoDao(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String createUser(UserDto userDto) throws JsonProcessingException {
        MongoCollection<Document> mongoCollection = getUsersCollection();

        Document document = Document.parse(objectMapper.writeValueAsString(userDto));
        mongoCollection.insertOne(document);
        return document.get("_id").toString();
    }

    public UserDto getUserByEmail(String email) {
        MongoCollection<Document> mongoCollection = getUsersCollection();

        List userDocument = mongoCollection.find(new Document("email", email)).into(new ArrayList<>());
        if (!userDocument.isEmpty()) {
            UserDto userDto = new UserDto();
            userDto.setEmail(((Document) userDocument.get(0)).get("email").toString());
            userDto.setName(((Document) userDocument.get(0)).get("name").toString());
            userDto.setId(((Document) userDocument.get(0)).get("_id").toString());
            userDto.setPassword(((Document) userDocument.get(0)).get("password").toString());
            userDto.setSalt(((Document) userDocument.get(0)).get("salt").toString());
            return userDto;
        }
        return null;
    }

    private MongoCollection<Document> getUsersCollection() {
        MongoClient mongoClient = MongoConnector.getConnection();
        MongoDatabase mongoDatabase = mongoClient.getDatabase(mongodb_database);
        return mongoDatabase.getCollection(mongodb_collection);
    }
}
