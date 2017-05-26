package com.creants.muext.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.creants.muext.entities.quest.HeroQuest;

/**
 * @author LamHM
 *
 */
@Repository
public interface QuestStatsRepository extends MongoRepository<HeroQuest, Long> {

	/**
	 * Lấy tất cả các nhiệm vụ có trong danh sách quest
	 * 
	 * @param heroId
	 * @param questIds
	 * @return
	 */
	@Query("{'heroId' : ?0, 'questIndex' : {'$in' : ?1}}")
	List<HeroQuest> getQuests(String heroId, Integer[] questIds);


	@Query("{'heroId' : ?0, 'groupId' : ?1, 'finish' : ?2}")
	List<HeroQuest> getQuests(String heroId, int groupId, boolean isFinish);


	List<HeroQuest> findByHeroIdAndGroupId(String heroId, int groupId);


	@Query("{'heroId' : ?0, 'groupId' : 2, 'createTime' : {'$lt' : ?1}}")
	List<HeroQuest> findDailyQuestNeedRemove(String heroId, long startOfDateMili);


	@Query("{'heroId' : ?0, 'groupId' : 2, 'createTime' : {'$gte' : ?1, '$lte' : ?2}}")
	List<HeroQuest> findDailyQuests(String heroId, long startOfDateMili, long endOfDateMili);
}
