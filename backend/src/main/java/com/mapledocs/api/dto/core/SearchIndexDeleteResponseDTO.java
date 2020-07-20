package com.mapledocs.api.dto.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.elasticsearch.action.delete.DeleteResponse;

@Data
@AllArgsConstructor
public class SearchIndexDeleteResponseDTO {
    private DeleteResponse deleteResponse;
}
