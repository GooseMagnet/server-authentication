package com.karasik;

import com.karasik.dao.RedisDao;
import com.karasik.model.SessionDto;
import com.karasik.model.UserDto;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import redis.clients.jedis.Jedis;
import com.karasik.util.connectors.*;

import java.time.LocalDateTime;

public class Main {

    static  String mongodb_user = "root";
    static String mongodb_pwd = "password";
    static String mongodb_host = "localhost";
    static String mongodb_database = "authentication";

    static String connectionString = "mongodb://" + mongodb_user + ":" + mongodb_pwd + "@" + mongodb_host;

    public static void main(String[] args) {

    }

    //    public static void main(String[] args) {
//        try(MongoClient mongoClient = MongoClients.create(connectionString)) {
//            MongoDatabase mongoDatabase = mongoClient.getDatabase(mongodb_database);
//            MongoCollection mongoCollection = mongoDatabase.getCollection("users");
//
//            UserDto userDto = new UserDto();
//            userDto.setEmail("test@test.test");
//            userDto.setName("testName");
//            userDto.setPassword("testPassword");
//
//            Document document = Document.parse(new Gson().toJson(userDto));
//
//            mongoCollection.insertOne(document);
//            ObjectId id = (ObjectId)document.get( "_id" );
//            System.out.println(id);
//        }
//    }
}
