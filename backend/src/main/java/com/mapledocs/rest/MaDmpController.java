package com.mapledocs.rest;

import com.mapledocs.api.dto.MaDmpDTO;
import com.mapledocs.service.MaDmpService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping
    @PreAuthorize("hasRole(\"ROLE_USER\") || hasRole(\"ROLE_ADMIN\")")
    public ResponseEntity<MaDmpDTO> createMaDmp(@Valid @RequestBody MaDmpDTO maDmpDTO) {
        this.maDmpService.createMaDmp(maDmpDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(params = {"page", "size"})
    @PreAuthorize("hasRole(\"ROLE_USER\") || hasRole(\"ROLE_ADMIN\")")
    public ResponseEntity<List<MaDmpDTO>> findAllMaDmps(@RequestParam("page") int page, @RequestParam("size") int size) {
        return new ResponseEntity<>(this.maDmpService.findAllPaged(page, size), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(\"ROLE_USER\") || hasRole(\"ROLE_ADMIN\")")
    public ResponseEntity<MaDmpDTO> deleteMaDmp(@PathParam("id") Long id) {
        this.maDmpService.deleteMaDmp(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
