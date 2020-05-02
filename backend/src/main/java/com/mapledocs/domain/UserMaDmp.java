package com.mapledocs.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/*
    Mapping table to keep track of a users MaDmps via id references. the maDmpId references the id of the document in
    the documentstore.
 */
@Entity
@NoArgsConstructor
public class UserMaDmp {
    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private Long maDmpId;

    public UserMaDmp(Long userid, Long maDmpId) {
        this.userId = userid;
        this.maDmpId = maDmpId;
    }
}
