package com.creants.muext.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.creants.muext.entities.quest.QuestStats;

/**
 * @author LamHM
 *
 */
@Repository
public interface QuestStatsRepository extends MongoRepository<QuestStats, Long> {

	@Query("{'heroId' : ?0, 'questId' : {'$in' : ?1}}")
	List<QuestStats> getQuests(String heroId, int[] questIds);

	@Query("{'heroId' : ?0, 'groupId' : ?1, 'isFinish' : ?2}")
	List<QuestStats> getQuests(String heroId, int groupId, boolean isFinish);
	
	
	List<QuestStats> findByHeroIdAndGroupId(String heroId, int groupId);
}
