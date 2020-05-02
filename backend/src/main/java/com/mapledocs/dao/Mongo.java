package com.mapledocs.dao;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.UnknownHostException;

@Configuration
public class Mongo {
    @Bean
    public MongoClient mongoClient() throws UnknownHostException {
        return new MongoClient("127.0.0.1:27017");
    }
}
