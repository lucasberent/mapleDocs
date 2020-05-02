package com.mapledocs.dao;

import com.mapledocs.api.dto.MaDmpDTO;
import com.mapledocs.util.Constants;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MaDmpRepository {
    private final MongoCollection<Document> mongoCollection;

    @Autowired
    public MaDmpRepository(MongoDbFactory mongoDbFactory) {
        mongoCollection = mongoDbFactory.getDb(Constants.MONGO_DB).getCollection(Constants.COLLECTION);
    }

    public Long saveMaDMP(final MaDmpDTO maDmpDTO) {
        Document document = Document.parse(maDmpDTO.getJson());
        document.append(Constants.USER_ID_FIELD, maDmpDTO.getUserId());
        mongoCollection.insertOne(document);

        return document.getLong("_id");
    }

    public List<MaDmpDTO> findAllByUserId(final Long userId) {
        List<MaDmpDTO> result = new ArrayList<>();
        Document searchDoc = new Document(Constants.USER_ID_FIELD, userId);
        mongoCollection.find(searchDoc).cursor().forEachRemaining(document ->
                result.add(new MaDmpDTO(document.toJson())));   //TODO parse out userId
        return result;
    }
}
