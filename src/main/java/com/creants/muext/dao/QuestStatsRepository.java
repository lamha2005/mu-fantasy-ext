package com.creants.muext.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.creants.muext.entities.quest.HeroQuest;
import com.creants.muext.entities.quest.KillMonsterQuest;

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
	@Query("{'gameHeroId' : ?0, 'questIndex' : {'$in' : ?1}}")
	List<HeroQuest> getQuests(String gameHeroId, Integer[] questIds);


	@Query("{'gameHeroId' : ?0, 'questIndex' : {'$in' : ?1}}")
	List<KillMonsterQuest> getKillMonsterQuest(String gameHeroId, Integer[] questIds);


	@Query("{'gameHeroId' : ?0, 'groupId' : ?1, 'finish' : ?2}")
	List<HeroQuest> getQuests(String gameHeroId, int groupId, boolean isFinish);


	List<HeroQuest> findByGameHeroIdAndGroupId(String gameHeroId, int groupId);


	@Query("{'gameHeroId' : ?0, 'groupId' : 2, 'createTime' : {'$lt' : ?1}}")
	List<HeroQuest> findDailyQuestNeedRemove(String gameHeroId, long startOfDateMili);


	@Query("{'gameHeroId' : ?0, 'groupId' : 2, 'createTime' : {'$gte' : ?1, '$lte' : ?2}}")
	List<HeroQuest> findDailyQuests(String gameHeroId, long startOfDateMili, long endOfDateMili);
}
