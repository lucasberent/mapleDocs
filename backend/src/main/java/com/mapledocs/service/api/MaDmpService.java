package com.mapledocs.service.api;

import com.mapledocs.api.dto.core.MaDmpDTO;
import com.mapledocs.api.dto.core.SchemaValidationExceptionDTO;
import com.mapledocs.api.exception.rest.MaDmpServiceCreationException;
import com.mapledocs.api.exception.rest.MaDmpServiceDoiAssignmentException;
import com.mapledocs.api.exception.rest.MaDmpServiceValidationException;

import java.util.List;

public interface MaDmpService {
    String createMaDmp(final MaDmpDTO maDmpDTO) throws MaDmpServiceCreationException, MaDmpServiceDoiAssignmentException;

    List<MaDmpDTO> findAllPaged(int page, int size);

    MaDmpDTO findOne(final String docId);

    SchemaValidationExceptionDTO validateForCurrentSchema(final String json) throws MaDmpServiceValidationException;
}
