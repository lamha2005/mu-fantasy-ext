package com.creants.muext.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.creants.muext.om.UserInfo;

@Repository
public interface UserRepository extends MongoRepository<UserInfo, Integer> {

}
