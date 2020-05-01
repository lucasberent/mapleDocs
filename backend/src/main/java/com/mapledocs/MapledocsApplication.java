package com.mapledocs;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.net.UnknownHostException;

@SpringBootApplication
@EnableMongoRepositories
@EnableJpaRepositories
public class MapledocsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MapledocsApplication.class, args);
	}

}
