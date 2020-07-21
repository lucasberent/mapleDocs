package com.mapledocs.dao.impl;

import com.mapledocs.api.dto.core.MaDmpDTO;
import com.mapledocs.api.exception.ElasticsearchDaoIndexingException;
import com.mapledocs.api.exception.MaDmpRepositoryException;
import com.mapledocs.api.exception.rest.NotFoundException;
import com.mapledocs.dao.api.ElasticsearchDao;
import com.mapledocs.dao.api.MaDmpRepository;
import com.mapledocs.util.Constants;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MongoMaDmpRepository implements MaDmpRepository {
    private final MongoCollection<Document> mongoCollection;
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoMaDmpRepository.class);
    private final ElasticsearchDao elasticsearchDao;

    @Autowired
    public MongoMaDmpRepository(MongoDbFactory mongoDbFactory, ElasticsearchDao elasticsearchDao) {
        this.mongoCollection = mongoDbFactory.getDb(Constants.MONGO_DB).getCollection(Constants.COLLECTION);
        this.elasticsearchDao = elasticsearchDao;
    }

    public String saveMaDmp(final MaDmpDTO maDmpDTO) throws MaDmpRepositoryException {
        LOGGER.info("Saving maDmp {}", maDmpDTO);
        Document document = Document.parse(maDmpDTO.getJson());
        document.append(Constants.USER_ID_FIELD, maDmpDTO.getUserId());
        this.saveAndIndexOrUndo(document);
        return document.getObjectId("_id").toString();
    }

    private void saveAndIndexOrUndo(final Document document) throws MaDmpRepositoryException {
        String mongoId;
        try {
            mongoCollection.insertOne(document);

            mongoId = document.getObjectId("_id").toString();
        } catch (MongoException e) {
            throw new MaDmpRepositoryException("Error saving maDmp: " + e.getMessage());
        }
        try {
            elasticsearchDao.indexMaDmp(document.toJson(), mongoId);
        } catch (ElasticsearchDaoIndexingException e) {
            // "rollback"
            mongoCollection.deleteOne(document);
            throw new MaDmpRepositoryException("Error saving maDmp: " + e.getMessage());
        }
    }

    // Currently not used in frontend
    public List<MaDmpDTO> findAllByUserId(final Long userId) {
        LOGGER.info("Retreiving maDmps for user {}", userId);
        List<MaDmpDTO> result = new ArrayList<>();
        Document searchDoc = new Document(Constants.USER_ID_FIELD, userId);
        mongoCollection.find(searchDoc).cursor().forEachRemaining(document ->
                result.add(parseEntityToMaDmpDTO(document, userId)));
        return result;
    }

    public MaDmpDTO findOneById(final String docId, long currUserId) {
        return parseEntityToMaDmpDTO(this.mongoCollection
                .find(new Document("_id", new ObjectId(docId))).first(), currUserId);
    }

    public MaDmpDTO parseEntityToMaDmpDTO(final Object entity, final Long currUserId) {
        Document document = (Document) entity;
        if (document == null) {
            throw new NotFoundException("Document not found");
        }
        LOGGER.debug("parsing document {}", document);
        Long documentUserId = document.getLong(Constants.USER_ID_FIELD);
        String docId = document.getObjectId("_id").toString();
        List<String> fieldsToHide = document.getList("fieldsToHide", String.class);
        document.remove(Constants.USER_ID_FIELD);
        document.remove("_id");
        if (fieldsToHide != null) {
            if (!documentUserId.equals(currUserId)) {
                for (String s : fieldsToHide) {
                    document.get("dmp", Document.class).remove(s);
                }
            }
            document.remove("fieldsToHide");
        }
        MaDmpDTO result = new MaDmpDTO(document.toJson(), documentUserId);
        result.setDocId(docId);
        LOGGER.debug("parsing result: {}", result);
        return result;
    }


    public List<MaDmpDTO> findAllPaged(final int page, final int size, final long currUserId) {
        List<MaDmpDTO> result = new ArrayList<>();
        int skipElems = size * (page - 1);
        mongoCollection.find().skip(skipElems).limit(size).cursor().forEachRemaining(document ->
                result.add(parseEntityToMaDmpDTO(document, currUserId)));
        return result;
    }
}
