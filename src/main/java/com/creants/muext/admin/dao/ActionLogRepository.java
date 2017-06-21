package com.creants.muext.admin.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.creants.muext.admin.om.ActionLog;

/**
 * @author LamHM
 *
 */
@Repository
public interface ActionLogRepository extends MongoRepository<ActionLog, Long> {

}
