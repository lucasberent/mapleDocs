package com.mapledocs.dao.api;

import com.mapledocs.api.dto.core.MaDmpDTO;
import com.mapledocs.api.exception.MaDmpRepositoryException;
import org.bson.Document;

public interface MaDmpRepository {
    String saveMaDmp(final MaDmpDTO maDmpDTO) throws MaDmpRepositoryException;
    MaDmpDTO parseEntityToMaDmpDTO(final Object entity, final Long currUserId);
    MaDmpDTO findOneById(final String docId, long currUserId);
}
