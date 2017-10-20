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
	@Query("{'gameHeroId' : ?0, 'questIndex' : {'$in' : ?1}}")
	List<HeroQuest> getQuests(String gameHeroId, Integer[] questIds);


	/**
	 * Lấy danh sách quest chưa hoàn thành theo group
	 * 
	 * @param gameHeroId
	 * @param groupId
	 *            group cần lấy
	 * @return
	 */
	@Query("{'gameHeroId' : ?0, 'groupId' : ?1, 'finish' : false}")
	List<HeroQuest> getQuests(String gameHeroId, String groupId);


	/**
	 * Lấy danh sách quest chưa hoàn thành
	 * 
	 * @param gameHeroId
	 * @return
	 */
	@Query("{'gameHeroId' : ?0, 'finish' : false}")
	List<HeroQuest> getQuests(String gameHeroId);


	/**
	 * Lấy danh sách quest mới bao gồm quest đã hoàn thành
	 * 
	 * @param gameHeroId
	 * @return
	 */
	@Query("{'gameHeroId' : ?0, $or: [{seen: {$exists: false}}, {seen: false}, {claim:true}]}")
	List<HeroQuest> getNewQuests(String gameHeroId);


	/**
	 * Lấy danh sách quest mới bao gồm quest đã hoàn thành theo group
	 * 
	 * @param gameHeroId
	 * @param groupId
	 * @return
	 */
	@Query("{'gameHeroId' : ?0, 'groupId' : ?1, $or: [{seen: {$exists: false}}, {seen: false}, {claim:true}]}")
	List<HeroQuest> getNewQuests(String gameHeroId, String groupId);


	@Query("{'gameHeroId' : ?0, 'groupId' : 'daily', 'createTime' : {'$lt' : ?1}}")
	List<HeroQuest> findDailyQuestNeedRemove(String gameHeroId, long startOfDateMili);


	@Query("{'gameHeroId' : ?0, 'groupId' : 'daily', 'createTime' : {'$gte' : ?1, '$lte' : ?2}}")
	List<HeroQuest> findDailyQuests(String gameHeroId, long startOfDateMili, long endOfDateMili);


	@Query("{'gameHeroId' : ?0, 'groupId' : 'daily', 'createTime' : {'$gte' : ?1, '$lte' : ?2}, 'seen' : ?3}")
	List<HeroQuest> findDailyQuests(String gameHeroId, long startOfDateMili, long endOfDateMili, boolean seen);
}
