package com.mapledocs.dao.api;

import com.mapledocs.api.dto.core.SearchIndexDeleteResponseDTO;
import com.mapledocs.api.dto.core.SearchIndexIndexingResponseDTO;
import com.mapledocs.api.exception.ElasticSeachDaoDeletionException;
import com.mapledocs.api.exception.ElasticsearchDaoIndexingException;

public interface ElasticsearchDao {
    SearchIndexIndexingResponseDTO indexMaDmp(final String maDmpJson, String mongoId) throws ElasticsearchDaoIndexingException;

    SearchIndexDeleteResponseDTO deleteMaDmp(final String deleteJson) throws ElasticSeachDaoDeletionException;
}
