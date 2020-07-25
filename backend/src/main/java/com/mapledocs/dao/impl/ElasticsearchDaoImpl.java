package com.mapledocs.dao.impl;

import com.mapledocs.api.dto.core.MaDMPMap;
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
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.mapledocs.util.Constants.ELASTICSEARCH_CONN_PORT;
import static com.mapledocs.util.Constants.ELASTICSEARCH_CONN_URL;

@Service
public class ElasticsearchDaoImpl implements ElasticsearchDao {
    private final RestHighLevelClient client;
    private final static String INDEX_NAME = "madmps_nested";
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchDao.class);

    public ElasticsearchDaoImpl() {
        this.client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(ELASTICSEARCH_CONN_URL, ELASTICSEARCH_CONN_PORT, "http")));
    }

    private void setupSearchIndex() throws ElasticsearchDaoIndexingException {
        LOGGER.info("Setting up index {}", INDEX_NAME);
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(INDEX_NAME);

        try {
            XContentBuilder mappingBuilder = XContentFactory.jsonBuilder();
            mappingBuilder.startObject();
            {
                mappingBuilder.startObject("properties");
                {
                    mappingBuilder.startObject("dmp");
                    {
                        mappingBuilder.startObject("properties");
                        {
                            mappingBuilder.startObject("dataset");
                            {
                                mappingBuilder.field("type", "nested");
                                // Implicitly, adding fields dynamically to the nested array is possible

                                mappingBuilder.startObject("properties");
                                {
                                    mappingBuilder.startObject("distribution");
                                    {
                                        mappingBuilder.field("type", "nested");
                                        mappingBuilder.startObject("properties");
                                        {
                                            mappingBuilder.startObject("license");
                                            {
                                                mappingBuilder.field("type", "nested");
                                            }
                                            mappingBuilder.endObject();
                                        }
                                        mappingBuilder.endObject();
                                    }
                                    mappingBuilder.endObject();
                                }
                                mappingBuilder.endObject();
                            }
                            mappingBuilder.endObject();
                            mappingBuilder.startObject("contributor");
                            {
                                mappingBuilder.field("type", "nested");
                            }
                            mappingBuilder.endObject();
                            mappingBuilder.startObject("project");
                            {
                                mappingBuilder.field("type", "nested");

                                mappingBuilder.startObject("properties");
                                {
                                    mappingBuilder.startObject("funding");
                                    {
                                        mappingBuilder.field("type", "nested");
                                    }
                                    mappingBuilder.endObject();
                                }
                                mappingBuilder.endObject();
                            }
                            mappingBuilder.endObject();
                        }
                        mappingBuilder.endObject();
                    }
                    mappingBuilder.endObject();
                }
                mappingBuilder.endObject();
            }
            mappingBuilder.endObject();

            createIndexRequest.mapping(mappingBuilder);

            XContentBuilder settingsBuilder = XContentFactory.jsonBuilder();
            settingsBuilder.startObject();
            {
                settingsBuilder.field("index.mapping.ignore_malformed", true);
            }
            settingsBuilder.endObject();

            createIndexRequest.settings(settingsBuilder);

            client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ElasticsearchDaoIndexingException("Failed to create search index", e);
        }
    }

    @Override
    public SearchIndexIndexingResponseDTO indexMaDmp(final String maDmpJson, String mongoId) throws ElasticsearchDaoIndexingException {
        LOGGER.info("Indexing maDMP");
        try {
            boolean exists = client.indices().exists(new GetIndexRequest(INDEX_NAME), RequestOptions.DEFAULT);
            if (!exists) {
                setupSearchIndex();
            }
        } catch (IOException e) {
            throw new ElasticsearchDaoIndexingException("Error checking if index exists", e);
        }

        MaDMPMap maDMPMap = MaDMPMap.fromJsonString(maDmpJson);
        maDMPMap.setMongoId(mongoId);

        if (maDMPMap.getFieldsToHide() != null) {
            for (String field : maDMPMap.getFieldsToHide()) {
                maDMPMap.getDmp().remove(field);
            }
        }

        IndexRequest indexRequest = new IndexRequest(INDEX_NAME);
        System.out.println("indexing : " + MaDMPMap.toJsonString(maDMPMap));
        indexRequest.source(MaDMPMap.toJsonString(maDMPMap), XContentType.JSON);

        try {
            return new SearchIndexIndexingResponseDTO(this.client.index(indexRequest, RequestOptions.DEFAULT));
        } catch (IOException e) {
            throw new ElasticsearchDaoIndexingException("Error indexing document", e);
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
