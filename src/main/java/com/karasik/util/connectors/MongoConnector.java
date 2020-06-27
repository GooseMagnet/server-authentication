package com.karasik.util.connectors;

import com.karasik.util.properies.PropertyReader;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoConnector {

    private static String mongodb_username = PropertyReader.getProperty("mongo.username");
    private static String mongodb_password = "password";
    private static String mongodb_host = "localhost";
    private static String mongodb_port = "27017";

    private static String connectionString = "mongodb://" + mongodb_username + ":" + mongodb_password + "@" + mongodb_host + ":" + mongodb_port;

    public static MongoClient getConnection() {
        return MongoClients.create(connectionString);
    }
}
