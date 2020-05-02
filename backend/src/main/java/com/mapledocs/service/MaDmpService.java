package com.mapledocs.service;

import com.mapledocs.api.dto.MaDmpDTO;
import com.mapledocs.api.exception.NotLoggedInException;
import com.mapledocs.dao.MaDmpRepository;
import com.mapledocs.dao.UserRepository;
import com.mapledocs.domain.AppUser;
import com.mapledocs.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaDmpService {
    private final MaDmpRepository maDmpRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createDmap(final MaDmpDTO maDmpDTO) {
        AppUser currUser = this.getCurrentUserOrNotLoggedIn();
        maDmpDTO.setUserId(currUser.getId());
        this.maDmpRepository.saveMaDMP(maDmpDTO);
    }

    @Transactional
    public List<MaDmpDTO> findAllForCurrentUser() {
        Long currUserId = this.getCurrentUserOrNotLoggedIn().getId();

        return this.maDmpRepository.findAllByUserId(currUserId);
    }

    private AppUser getCurrentUserOrNotLoggedIn() {
        return userRepository.findByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow(() ->
                new NotLoggedInException("Current user not logged in")));
    }
}
