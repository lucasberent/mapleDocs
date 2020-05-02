package com.mapledocs.dao;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongoDbFactory {

    private MongoClient mongoClient;

    public DB getMongoDbInstance() {
        return mongoClient.getDB("DB");
    }
}
