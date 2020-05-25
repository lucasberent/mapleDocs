package com.mapledocs.dao;

import com.mapledocs.api.dto.MaDmpDTO;
import com.mapledocs.util.Constants;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MaDmpRepository {
    private final MongoCollection<Document> mongoCollection;
    private static final Logger LOGGER = LoggerFactory.getLogger(MaDmpRepository.class);

    @Autowired
    public MaDmpRepository(MongoDbFactory mongoDbFactory) {
        mongoCollection = mongoDbFactory.getDb(Constants.MONGO_DB).getCollection(Constants.COLLECTION);
    }

    public void saveMaDmp(final MaDmpDTO maDmpDTO) {
        LOGGER.info("Saving maDmp {}", maDmpDTO);
        Document document = Document.parse(maDmpDTO.getJson());
        document.append(Constants.USER_ID_FIELD, maDmpDTO.getUserId());
        mongoCollection.insertOne(document);
    }

    public void removeMaDmp(final Long docId) {
        LOGGER.info("Deleting maDmp with id{}", docId);
        mongoCollection.deleteOne(new Document("_id", docId));
    }

    public List<MaDmpDTO> findAllByUserId(final Long userId) {
        LOGGER.info("Retreiving maDmps for user {}", userId);
        List<MaDmpDTO> result = new ArrayList<>();
        Document searchDoc = new Document(Constants.USER_ID_FIELD, userId);
        mongoCollection.find(searchDoc).cursor().forEachRemaining(document ->
                result.add(parseDocumentToMaDmpDTO(document, userId)));
        return result;
    }

    public MaDmpDTO findOneById(final Long id, long currUserId) {
        return parseDocumentToMaDmpDTO(this.mongoCollection.find(new Document("_id", id)).first(), currUserId);
    }

    public MaDmpDTO parseDocumentToMaDmpDTO(final Document document, final Long currUserId) {
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
        result.setFieldsToHide(fieldsToHide);
        result.setDocId(docId);
        LOGGER.debug("parsing result: {}", result);
        return result;
    }

    public List<MaDmpDTO> findAllPaged(final int page, final int size, final long currUserId) {
        List<MaDmpDTO> result = new ArrayList<>();
        int skipElems = size * (page - 1);
        mongoCollection.find().skip(skipElems).limit(size).cursor().forEachRemaining(document ->
                result.add(parseDocumentToMaDmpDTO(document, currUserId)));
        return result;
    }
}
