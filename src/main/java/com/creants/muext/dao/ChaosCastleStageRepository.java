package com.creants.muext.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.creants.muext.entities.chaos.ChaosCastleStage;

/**
 * @author LamHM
 *
 */
@Repository
public interface ChaosCastleStageRepository extends MongoRepository<ChaosCastleStage, String> {
}
