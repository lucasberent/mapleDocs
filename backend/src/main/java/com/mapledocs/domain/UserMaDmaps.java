package com.mapledocs.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/*
    Mapping table to keep track of a users MaDmps via id references. the maDmpId references the id of the document in
    the documentstore.
 */
@Entity
public class UserMaDmaps {
    @Id
    @GeneratedValue
    private Long id;
    private Long maDmpId;
}
