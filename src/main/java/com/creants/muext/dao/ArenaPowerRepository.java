package com.creants.muext.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.creants.muext.entities.arena.ArenaPower;

/**
 * @author LamHM
 *
 */
@Repository
public interface ArenaPowerRepository extends MongoRepository<ArenaPower, String> {
	@Query("{'teamPower' : {'$gte' : ?0}, 'teamPower' :{'$lte': ?1}}")
	List<ArenaPower> getArenaPowerList(int fromPower, int toPower);


	List<ArenaPower> findByTeamPowerBetween(int from, int to);
}
