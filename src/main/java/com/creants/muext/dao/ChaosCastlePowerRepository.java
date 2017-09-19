package com.creants.muext.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.creants.muext.entities.chaos.ChaosCastlePower;

/**
 * @author LamHM
 *
 */
@Repository
public interface ChaosCastlePowerRepository extends MongoRepository<ChaosCastlePower, String> {
	@Query("{'teamPower' : {'$gte' : ?0}, 'teamPower' :{'$lte': ?1}}")
	List<ChaosCastlePower> getChaosCastleInfoList(int fromPower, int toPower);


	List<ChaosCastlePower> findByTeamPowerBetween(int from, int to);
}
