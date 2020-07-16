package com.mapledocs.service.impl;

import com.google.gson.Gson;
import com.mapledocs.api.dto.core.MaDmpDTO;
import com.mapledocs.api.dto.core.MaDmpSearchDTO;
import com.mapledocs.api.dto.external.DoiServiceAuthenticateDTO;
import com.mapledocs.api.exception.*;
import com.mapledocs.dao.MaDmpRepository;
import com.mapledocs.dao.UserRepository;
import com.mapledocs.domain.AppUser;
import com.mapledocs.security.SecurityUtils;
import com.mapledocs.service.api.DoiService;
import com.mapledocs.service.api.MaDmpService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MaDmpServiceImpl implements MaDmpService {
    private final MaDmpRepository maDmpRepository;
    private final UserRepository userRepository;
    private final DoiService doiService;
    private static final Logger LOGGER = LoggerFactory.getLogger(MaDmpServiceImpl.class);

    @Data
    private static class MaDMPJson {
        private Map<String, Object> dmp;
        private List<String> fieldsToHide;
    }

    @Transactional
    public String createMaDmp(final MaDmpDTO maDmpDTO) throws MaDmpServiceCreationException {
        if (maDmpDTO == null || maDmpDTO.getJson() == null || maDmpDTO.getJson().isEmpty()) {
            throw new ValidationException("json empty");
        }

        AppUser currUser = this.getCurrentUserOrNotLoggedIn();
        maDmpDTO.setUserId(currUser.getId());

        MaDMPJson parsed = new Gson().fromJson(maDmpDTO.getJson(), MaDMPJson.class);

        if (parsed.getDmp() == null) {
            throw new MaDmpServiceCreationException("JSON invalid");
        }

        if (parsed.getDmp().get("dmp_id") == null && maDmpDTO.getAssignNewDoi()) {
            if (maDmpDTO.getDoiServicePassword() == null) {
                throw new ValidationException("Password for doi service is empty");
            }
            this.assignNewDoiToMaDmp(parsed,
                    new DoiServiceAuthenticateDTO(currUser.getExternalDoiServiceCredentials().getUsername(),
                            maDmpDTO.getDoiServicePassword(), currUser.getExternalDoiServiceCredentials().getDoiPrefix()));
        }

        parsed.setFieldsToHide(maDmpDTO.getFieldsToHide());
        maDmpDTO.setJson(new Gson().toJson(parsed));
        return this.maDmpRepository.saveMaDmp(maDmpDTO);
    }

    private void assignNewDoiToMaDmp(MaDMPJson maDmp, final DoiServiceAuthenticateDTO doiServiceAuthenticateDTO) {
        String doi = null;
        try {
            doi = this.doiService.getNewDoi(doiServiceAuthenticateDTO);
        } catch (DoiServiceException e) {
            // alternatively the creation process can be failed here with an CreationException
            LOGGER.error("Error getting new doi from doi service {}, continuing with doi set to null", e.getMessage());
        }
        Map<String, String> dmpId = new HashMap<>();
        dmpId.put("identifier", doi);
        dmpId.put("type", "doi");
        maDmp.getDmp().put("dmp_id", dmpId);
    }

    @Transactional
    public List<MaDmpDTO> findAllPaged(int page, int size) {
        return maDmpRepository.findAllPaged(page, size, this.getCurrentUserOrNotLoggedIn().getId());
    }


    @Transactional
    public void deleteMaDmp(final String docId) {
        MaDmpDTO maDmpDTO = this.maDmpRepository.findOneById(docId, this.getCurrentUserOrNotLoggedIn().getId());
        Long userId = this.getCurrentUserOrNotLoggedIn().getId();
        if (!userId.equals(maDmpDTO.getUserId())) {
            throw new ForbiddenException("User cannot delete documents of another user");
        }
        this.maDmpRepository.removeMaDmp(docId);
    }

    private AppUser getCurrentUserOrNotLoggedIn() {
        return userRepository.findByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow(() ->
                new NotLoggedInException("Current user not logged in")));
    }

    public MaDmpDTO findOne(final String docId) {
        if (docId == null) {
            throw new NotFoundException("MaDMP with not found for id " + docId);
        }
        return this.maDmpRepository.findOneById(docId, this.getCurrentUserOrNotLoggedIn().getId());
    }

    public List<MaDmpDTO> findAllPagedByDocId(final MaDmpSearchDTO maDmpSearchDto) {
        return this.maDmpRepository.findAllByDocIds(maDmpSearchDto, this.getCurrentUserOrNotLoggedIn().getId());
    }
}
