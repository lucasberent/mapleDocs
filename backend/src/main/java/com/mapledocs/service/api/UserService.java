package com.mapledocs.service.api;

import com.mapledocs.api.dto.core.RegisterDTO;
import com.mapledocs.domain.AppUser;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface UserService {
    @NotNull
    Optional<AppUser> getUserByLogin(@Nullable final String login);

    @NotNull
    void register(final RegisterDTO registerDTO);
}
