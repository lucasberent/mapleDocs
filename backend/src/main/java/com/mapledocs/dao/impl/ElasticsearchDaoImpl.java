package com.mapledocs.dao.impl;

import com.google.gson.Gson;
import com.mapledocs.api.dto.core.MaDMPJson;
import com.mapledocs.api.dto.core.SearchIndexDeleteResponseDTO;
import com.mapledocs.api.dto.core.SearchIndexIndexingResponseDTO;
import com.mapledocs.api.exception.ElasticSeachDaoDeletionException;
import com.mapledocs.api.exception.ElasticsearchDaoIndexingException;
import com.mapledocs.dao.api.ElasticsearchDao;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import static com.mapledocs.util.Constants.ELASTICSEARCH_CONN_PORT;
import static com.mapledocs.util.Constants.ELASTICSEARCH_CONN_URL;

@Service
public class ElasticsearchDaoImpl implements ElasticsearchDao {
    private final RestHighLevelClient client;
    private final static String INDEX_NAME = "madmps";
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchDao.class);

    public ElasticsearchDaoImpl() {
        this.client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(ELASTICSEARCH_CONN_URL, ELASTICSEARCH_CONN_PORT, "http")));
    }

    @Override
    public SearchIndexIndexingResponseDTO indexMaDmp(final String maDmpJson) throws ElasticsearchDaoIndexingException {
        // TODO
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.source(maDmpJson, XContentType.JSON);
        MaDMPJson maDMPJson = new Gson().fromJson(maDmpJson, MaDMPJson.class);
        try {
            XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
            for (String s : maDMPJson.getDmp().keySet()) {
                if (maDMPJson.getFieldsToHide().contains(s)) {
                    xContentBuilder
                            .startObject()
                            .startObject("properties")
                            .startObject(s)
                            .field("index", false);
                } else {
                    if (maDMPJson.getDmp().get(s) instanceof String) {
                        xContentBuilder.field(s, maDMPJson.getDmp().get(s));
                    } else {
                        xContentBuilder.startObject().map((Map<String, ?>) maDMPJson.getDmp().get(s));
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error building index request object");
            throw new ElasticsearchDaoIndexingException("Error indexing madmp while building context object");
        }

        try {
            return new SearchIndexIndexingResponseDTO(this.client.index(indexRequest, RequestOptions.DEFAULT));
        } catch (IOException e) {
            throw new ElasticsearchDaoIndexingException("Error indexing document " + e.getMessage());
        }
    }

    @Override
    public SearchIndexDeleteResponseDTO deleteMaDmp(final String id) throws ElasticSeachDaoDeletionException {
        DeleteRequest deleteRequest = new DeleteRequest(INDEX_NAME);
        deleteRequest.id(id);
        try {
            return new SearchIndexDeleteResponseDTO(this.client.delete(deleteRequest, RequestOptions.DEFAULT));
        } catch (IOException e) {
            throw new ElasticSeachDaoDeletionException(e.getMessage());
        }
    }
}
