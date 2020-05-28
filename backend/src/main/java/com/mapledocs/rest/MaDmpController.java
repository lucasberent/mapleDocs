package com.mapledocs.rest;

import com.mapledocs.api.dto.MaDmpDTO;
import com.mapledocs.api.dto.MaDmpSearchDTO;
import com.mapledocs.service.MaDmpService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/api/v1/madmps")
@RequiredArgsConstructor
public class MaDmpController {
    private final MaDmpService maDmpService;
    private static final Logger LOGGER = LoggerFactory.getLogger(MaDmpController.class);

    @PostMapping
    @PreAuthorize("hasRole(\"ROLE_USER\") || hasRole(\"ROLE_ADMIN\")")
    public ResponseEntity<MaDmpDTO> createMaDmp(@RequestBody MaDmpDTO maDmpDTO) {
        LOGGER.debug("Creating Madmp for DTO: {}", maDmpDTO);
        this.maDmpService.createMaDmp(maDmpDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole(\"ROLE_USER\") || hasRole(\"ROLE_ADMIN\")")
    public ResponseEntity<List<MaDmpDTO>> findAllMaDmps(@RequestParam("page") int page, @RequestParam("size") int size) {
        LOGGER.info("Finding all madmps");
        return new ResponseEntity<>(this.maDmpService.findAllPaged(page, size), HttpStatus.OK);
    }

    @GetMapping("/searchList")
    @PreAuthorize("hasRole(\"ROLE_USER\") || hasRole(\"ROLE_ADMIN\")")
    public ResponseEntity<List<MaDmpDTO>> findAllMaDmpsByDocId(@RequestParam("ids") MaDmpSearchDTO docIds) {
        LOGGER.info("Finding all madmps");
        return new ResponseEntity<>(this.maDmpService.findAllPagedByDocId(docIds), HttpStatus.OK);
    }

    @GetMapping("/details/{docId}")
    @PreAuthorize("hasRole(\"ROLE_USER\") || hasRole(\"ROLE_ADMIN\")")
    public ResponseEntity<MaDmpDTO> findOneDmap(@PathVariable String docId) {
        LOGGER.info("Finding one madmp");
        return new ResponseEntity<>(this.maDmpService.findOne(docId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(\"ROLE_USER\") || hasRole(\"ROLE_ADMIN\")")
    public ResponseEntity<MaDmpDTO> deleteMaDmp(@PathParam("id") String id) {
        this.maDmpService.deleteMaDmp(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
