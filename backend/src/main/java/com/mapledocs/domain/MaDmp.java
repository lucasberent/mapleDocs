package com.mapledocs.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document
public class MaDmp {
    @Id
    private Long id;
}
