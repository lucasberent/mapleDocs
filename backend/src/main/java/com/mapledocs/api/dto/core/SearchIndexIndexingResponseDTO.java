package com.mapledocs.api.dto.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.elasticsearch.action.index.IndexResponse;

@Data
@AllArgsConstructor
public class SearchIndexIndexingResponseDTO {
    private IndexResponse indexResponse;
}
