package com.mapledocs.rest;

import com.mapledocs.api.dto.core.MaDmpDTO;
import com.mapledocs.api.dto.core.SchemaValidationExceptionDTO;
import com.mapledocs.service.api.MaDmpService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/madmps")
@RequiredArgsConstructor
public class MaDmpController {
    private final MaDmpService maDmpService;
    private static final Logger LOGGER = LoggerFactory.getLogger(MaDmpController.class);

    @PostMapping
    @PreAuthorize("hasRole(\"ROLE_USER\") || hasRole(\"ROLE_ADMIN\")")
    public ResponseEntity<String> createMaDmp(@RequestBody MaDmpDTO maDmpDTO) {
        LOGGER.debug("Creating Madmp for DTO: {}", maDmpDTO);
        String id = this.maDmpService.createMaDmp(maDmpDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PostMapping("/validation")
    @PreAuthorize("hasRole(\"ROLE_USER\") || hasRole(\"ROLE_ADMIN\")")
    public ResponseEntity<SchemaValidationExceptionDTO> validateMaDMPAgainstSchema(@RequestBody String maDmpJson) {
        LOGGER.debug("Validating Madmp: {}", maDmpJson);
        return ResponseEntity.status(HttpStatus.OK).body(this.maDmpService.validateForCurrentSchema(maDmpJson));
    }

    @GetMapping
    @PreAuthorize("hasRole(\"ROLE_USER\") || hasRole(\"ROLE_ADMIN\")")
    public ResponseEntity<List<MaDmpDTO>> findAllMaDmps(@RequestParam("page") int page, @RequestParam("size") int size) {
        LOGGER.info("Finding all madmps");
        return new ResponseEntity<>(this.maDmpService.findAllPaged(page, size), HttpStatus.OK);
    }

    @GetMapping("/details/{docId}")
    @PreAuthorize("hasRole(\"ROLE_USER\") || hasRole(\"ROLE_ADMIN\")")
    public ResponseEntity<MaDmpDTO> findOneDmap(@PathVariable String docId) {
        LOGGER.info("Finding one madmp");
        return new ResponseEntity<>(this.maDmpService.findOne(docId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(\"ROLE_USER\") || hasRole(\"ROLE_ADMIN\")")
    public ResponseEntity<MaDmpDTO> deleteMaDmp(@PathVariable("id") String id) throws NoSuchMethodException {
        throw new NoSuchMethodException();
    }
}
