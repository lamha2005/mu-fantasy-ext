package com.creants.muext.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.creants.muext.entities.HeroClass;

/**
 * @author LamHM
 *
 */
@Repository
public interface HeroRepository extends MongoRepository<HeroClass, String> {
}