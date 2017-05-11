package com.creants.muext.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.creants.muext.entities.world.WorldStats;

/**
 * @author LamHM
 *
 */
public interface WorldStatsRepository extends MongoRepository<WorldStats, String> {
	
}
