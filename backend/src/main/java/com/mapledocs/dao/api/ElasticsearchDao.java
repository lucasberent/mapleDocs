package com.mapledocs.dao.api;

import com.mapledocs.api.exception.ElasticSeachDaoDeletionException;
import com.mapledocs.api.exception.ElasticsearchDaoIndexingException;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;

public interface ElasticsearchDao {
    IndexResponse indexMaDmp(final String maDmpJson) throws ElasticsearchDaoIndexingException;

    DeleteResponse deleteMaDmp(final String deleteJson) throws ElasticSeachDaoDeletionException;
}
