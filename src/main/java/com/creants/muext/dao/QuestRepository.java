package com.creants.muext.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.creants.muext.entities.quest.QuestBase;

/**
 * @author LamHM
 *
 */
@Repository
public interface QuestRepository extends MongoRepository<QuestBase, String> {
	@Query("{'minLevel' : {'$lte' : ?0}, 'maxLevel' :{'$gt': ?0}}")
	List<QuestBase> getQuests(int level);
}
