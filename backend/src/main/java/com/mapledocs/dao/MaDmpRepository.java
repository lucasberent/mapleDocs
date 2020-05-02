package com.mapledocs.dao;

import com.mapledocs.api.dto.MaDmpDTO;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MaDmpRepository {
    private final DBCollection mongoCollection;

    @Autowired
    public MaDmpRepository(MongoDbFactory mongoDbFactory) {
        mongoCollection = mongoDbFactory.getMongoDbInstance().getCollection("COLLECTION");
    }

    public Long saveMaDMP(final MaDmpDTO maDmpDTO) {
        DBObject dbObject = (DBObject) JSON.parse(maDmpDTO.getJson());
        mongoCollection.save(dbObject);

        return (Long) dbObject.get("_id");
    }

    public List<MaDmpDTO> findAllForUser(final Long userId) {
        List<MaDmpDTO> result = new ArrayList<>();
        DBObject searchObject = BasicDBObjectBuilder.start("_id", userId).get();
        mongoCollection.find(searchObject).forEachRemaining(dbObject -> {
            result.add(new MaDmpDTO(dbObject.toString()));
        });
        return result;
    }


}
