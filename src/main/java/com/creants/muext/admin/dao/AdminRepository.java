package com.creants.muext.admin.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.creants.muext.admin.om.Admin;

/**
 * @author LamHM
 *
 */
@Repository
public interface AdminRepository extends MongoRepository<Admin, Long> {

}
