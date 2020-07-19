package com.mapledocs.dao.impl;

import com.mapledocs.api.exception.ElasticSeachDaoDeletionException;
import com.mapledocs.dao.api.ElasticsearchDao;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ElasticsearchDaoImpl implements ElasticsearchDao {
    private final RestHighLevelClient client;
    private final static String INDEX_NAME = "madmps";
    private final static String ELASTICSEARCH_CONN_URL = "localhost";
    private final static int ELASTICSEARCH_CONN_PORT = 9200;

    public ElasticsearchDaoImpl() {
        this.client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(ELASTICSEARCH_CONN_URL, ELASTICSEARCH_CONN_PORT, "http")));
    }

    @Override
    public IndexResponse indexMaDmp(final String maDmpJson) {
        return null;
    }

    @Override
    public DeleteResponse deleteMaDmp(final String id) throws ElasticSeachDaoDeletionException {
        DeleteRequest deleteRequest = new DeleteRequest(INDEX_NAME);
        deleteRequest.id(id);
        try {
            return this.client.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ElasticSeachDaoDeletionException(e.getMessage());
        }
    }
}
