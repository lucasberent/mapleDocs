package com.mapledocs.service.api;

import com.mapledocs.api.dto.core.MaDmpDTO;
import com.mapledocs.api.dto.core.MaDmpSearchDTO;
import com.mapledocs.api.exception.MaDmpServiceCreationException;

import java.util.List;

public interface MaDmpService {
    String createMaDmp(final MaDmpDTO maDmpDTO) throws MaDmpServiceCreationException;

    List<MaDmpDTO> findAllPaged(int page, int size);

    List<MaDmpDTO> findAllPagedByDocId(final MaDmpSearchDTO maDmpSearchDto);
}
