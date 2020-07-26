package com.mapledocs.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapledocs.api.dto.core.MaDMPMap;
import com.mapledocs.api.dto.core.MaDmpDTO;
import com.mapledocs.api.dto.core.SchemaValidationExceptionDTO;
import com.mapledocs.api.dto.external.DoiServiceAuthenticateDTO;
import com.mapledocs.api.exception.DoiServiceException;
import com.mapledocs.api.exception.MaDmpRepositoryException;
import com.mapledocs.api.exception.rest.*;
import com.mapledocs.dao.api.UserRepository;
import com.mapledocs.dao.impl.MongoMaDmpRepository;
import com.mapledocs.domain.AppUser;
import com.mapledocs.security.SecurityUtils;
import com.mapledocs.service.api.DoiService;
import com.mapledocs.service.api.MaDmpService;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MaDmpServiceImpl implements MaDmpService {
    private final MongoMaDmpRepository mongoMaDmpRepository;
    private final UserRepository userRepository;
    private final DoiService doiService;
    private static final Logger LOGGER = LoggerFactory.getLogger(MaDmpServiceImpl.class);
    private final static String MADMP_SCHEMA = "maDMP-schema-1.0.json";
    private final JSONObject jsonSchema;
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public MaDmpServiceImpl(final MongoMaDmpRepository mongoMaDmpRepository,
                            final UserRepository userRepository,
                            final DoiService doiService) {
        this.mongoMaDmpRepository = mongoMaDmpRepository;
        this.userRepository = userRepository;
        this.doiService = doiService;
        try {
            this.jsonSchema = new JSONObject(
                    new JSONTokener(new ClassPathResource(MADMP_SCHEMA).getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public String createMaDmp(final MaDmpDTO maDmpDTO) throws MaDmpServiceCreationException,
            MaDmpServiceDoiAssignmentException {
        this.requireMaDmpDTONotNull(maDmpDTO);

        AppUser currUser = this.getCurrentUserOrNotLoggedIn();
        maDmpDTO.setUserId(currUser.getId());
        MaDMPMap parsed = MaDMPMap.fromJsonString(maDmpDTO.getJson());

        if (parsed.getDmp() == null) {
            LOGGER.error("Parsed maDMP is null");
            throw new MaDmpServiceCreationException("parsed JSON invalid, is null");
        }

        if (parsed.getDmp().get("dmp_id") == null && maDmpDTO.getAssignNewDoi()) {
            this.requireExternalDoiCredentialsNotNull(currUser, maDmpDTO);
            this.assignNewDoiToMaDmp(parsed, getDoiServiceAuthenticationDTO(currUser, maDmpDTO));
        }

        parsed.setFieldsToHide(maDmpDTO.getFieldsToHide());
        maDmpDTO.setJson(MaDMPMap.toJsonString(parsed));
        try {
            return this.mongoMaDmpRepository.saveMaDmp(maDmpDTO);
        } catch (MaDmpRepositoryException e) {
            throw new MaDmpServiceCreationException("Error saving maDmp: " + e.getMessage());
        }
    }

    public SchemaValidationExceptionDTO validateForCurrentSchema(final String json) {
        Schema schema = SchemaLoader.load(this.jsonSchema);
        try {
            schema.validate(new JSONObject(new JSONTokener(json)));
        } catch (org.everit.json.schema.ValidationException e) {
            LOGGER.debug("Schema validation failed: {}", e.getMessage());
            return new SchemaValidationExceptionDTO(e.getCausingExceptions());
        }
        return null;
    }

    private void requireMaDmpDTONotNull(final MaDmpDTO maDmpDTO) {
        if (maDmpDTO == null || maDmpDTO.getJson() == null || maDmpDTO.getJson().isEmpty()) {
            LOGGER.error("Madmp json or dto null");
            throw new ValidationException("json empty");
        }
    }

    private DoiServiceAuthenticateDTO getDoiServiceAuthenticationDTO(final AppUser user, final MaDmpDTO maDmpDTO) {
        return new DoiServiceAuthenticateDTO(user.getExternalDoiServiceCredentials().getUsername(),
                maDmpDTO.getDoiServicePassword(),
                user.getExternalDoiServiceCredentials().getDoiPrefix());
    }

    private void requireExternalDoiCredentialsNotNull(final AppUser currUser, final MaDmpDTO maDmpDTO) {
        if (maDmpDTO.getDoiServicePassword() == null ||
                currUser.getExternalDoiServiceCredentials() == null ||
                currUser.getExternalDoiServiceCredentials().getUsername() == null ||
                currUser.getExternalDoiServiceCredentials().getDoiPrefix() == null) {
            LOGGER.error("Missing fields for doi service authentication");
            throw new MaDmpServiceValidationException("Missing fields for doi service authentication");
        }
    }

    private void assignNewDoiToMaDmp(MaDMPMap maDmp, final DoiServiceAuthenticateDTO doiServiceAuthenticateDTO)
            throws MaDmpServiceDoiAssignmentException {
        LOGGER.info("Trying to assign new doi to maDMP");
        String doi;
        try {
            doi = this.doiService.getNewDoi(doiServiceAuthenticateDTO);
        } catch (DoiServiceException e) {
            // alternatively the creation process can be failed here with an CreationException
            LOGGER.error("Error getting new doi from doi service {}, continuing with doi set to null", e.getMessage());
            throw new MaDmpServiceDoiAssignmentException("Error creating DOI: " + e.getMessage() + "retry or do not assign a new doi");
        }
        Map<String, String> dmpId = new HashMap<>();
        dmpId.put("identifier", doi);
        dmpId.put("type", "doi");
        maDmp.getDmp().put("dmp_id", dmpId);
    }

    @Transactional
    public List<MaDmpDTO> findAllPaged(int page, int size) {
        return mongoMaDmpRepository.findAllPaged(page, size, this.getCurrentUserOrNotLoggedIn().getId());
    }

    private AppUser getCurrentUserOrNotLoggedIn() {
        return userRepository.findByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow(() ->
                new NotLoggedInException("Current user not logged in")));
    }

    public MaDmpDTO findOne(final String docId) {
        if (docId == null) {
            throw new NotFoundException("MaDMP with not found for id " + docId);
        }
        return this.mongoMaDmpRepository.findOneById(docId, this.getCurrentUserOrNotLoggedIn().getId());
    }
}
