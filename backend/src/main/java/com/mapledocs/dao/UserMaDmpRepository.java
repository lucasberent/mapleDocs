package com.mapledocs.dao;

import com.mapledocs.domain.UserMaDmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMaDmpRepository extends JpaRepository<UserMaDmp, Long> {
    List<UserMaDmp> findAllByUserId(Long userId);
}
