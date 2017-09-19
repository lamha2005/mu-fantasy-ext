package com.creants.muext.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.creants.muext.entities.chaos.ChaosCastleInfo;

/**
 * @author LamHM
 *
 */
@Repository
public interface ChaosCastleRepository extends MongoRepository<ChaosCastleInfo, String> {
}
