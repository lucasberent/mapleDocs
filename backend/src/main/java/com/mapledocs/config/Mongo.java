package com.mapledocs.config;

import com.mapledocs.util.Constants;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Mongo {
    @Bean
    public MongoClient mongoClient() {
        return new MongoClient(Constants.MONGO_DB_URL);
    }
}
