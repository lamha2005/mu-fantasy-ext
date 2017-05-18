package com.creants.muext.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.creants.muext.entities.world.WorldStats;

/**
 * @author LamHM
 *
 */
@Repository
public interface WorldStatsRepository extends MongoRepository<WorldStats, String> {
	
}
