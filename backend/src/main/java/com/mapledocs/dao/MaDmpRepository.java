package com.mapledocs.dao;

import com.mapledocs.domain.MaDmp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaDmpRepository extends MongoRepository<MaDmp, Long> {

}
