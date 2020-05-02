package com.mapledocs.rest;

import com.mapledocs.api.dto.MaDmpDTO;
import com.mapledocs.service.MaDmpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/madmps")
@RequiredArgsConstructor
public class MaDmpController {
    private final MaDmpService maDmpService;

    @PostMapping
    @PreAuthorize("hasRole(\"USER\")")
    public ResponseEntity<MaDmpDTO> createMaDmp(@Valid @RequestBody MaDmpDTO maDmpDTO) {
        this.maDmpService.createDmap(maDmpDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
