package com.mapledocs;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.UnknownHostException;

@SpringBootApplication
public class MapledocsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MapledocsApplication.class, args);
		try {
			MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

}
