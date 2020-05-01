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
	}

}
