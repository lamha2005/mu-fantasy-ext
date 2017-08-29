package com.creants.muext.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.creants.muext.entities.chaos.ChaosCastleInfo;

/**
 * @author LamHM
 *
 */
@Repository
public interface ChaosCastleRepository extends MongoRepository<ChaosCastleInfo, String> {
	@Query("{'teamPower' : {'$gte' : ?0}, 'teamPower' :{'$lte': ?1}}")
	List<ChaosCastleInfo> getChaosCastleInfoList(int fromPower, int toPower);
}
