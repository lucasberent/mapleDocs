package com.mapledocs.service;

import com.google.gson.Gson;
import com.mapledocs.api.dto.MaDmpDTO;
import com.mapledocs.api.exception.ForbiddenException;
import com.mapledocs.api.exception.NotLoggedInException;
import com.mapledocs.dao.MaDmpRepository;
import com.mapledocs.dao.UserRepository;
import com.mapledocs.domain.AppUser;
import com.mapledocs.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MaDmpService {
    private final MaDmpRepository maDmpRepository;
    private final UserRepository userRepository;
    private final DoiService doiService;

    @Transactional
    public void createMaDmp(final MaDmpDTO maDmpDTO) {
        AppUser currUser = this.getCurrentUserOrNotLoggedIn();
        maDmpDTO.setUserId(currUser.getId());
        Map<String, Object> parsed = new GsonJsonParser().parseMap(maDmpDTO.getJson());
        if (parsed.get("dmp_id") == null) {
            String doi = this.doiService.getNewDoi();
            Map<String, String> dmpId = new HashMap<>();
            dmpId.put("identifier", doi);
            dmpId.put("type", "doi");
            parsed.put("dmp_id", dmpId);
        }
        parsed.put("fieldsToHide", maDmpDTO.getFieldsToHide());
        maDmpDTO.setJson(new Gson().toJson(parsed));
        this.maDmpRepository.saveMaDMP(maDmpDTO);
    }

    @Transactional
    public List<MaDmpDTO> findAllForCurrentUser() {
        Long currUserId = this.getCurrentUserOrNotLoggedIn().getId();

        return this.maDmpRepository.findAllByUserId(currUserId);
    }

    @Transactional
    public List<MaDmpDTO> findAllPaged(int page, int size) {
        return maDmpRepository.findAllPaged(page, size);
    }


    @Transactional
    public void deleteMaDmp(final Long id) {
        MaDmpDTO maDmpDTO = this.maDmpRepository.findOneById(id);
        Long userId = this.getCurrentUserOrNotLoggedIn().getId();
        if (!userId.equals(maDmpDTO.getUserId())) {
            throw new ForbiddenException("User cannot delete documents of another user");
        }
        this.maDmpRepository.removeMaDMP(id);
    }

    private AppUser getCurrentUserOrNotLoggedIn() {
        return userRepository.findByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow(() ->
                new NotLoggedInException("Current user not logged in")));
    }
}
