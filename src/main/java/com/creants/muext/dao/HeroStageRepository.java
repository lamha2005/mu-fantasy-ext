package com.creants.muext.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.creants.muext.entities.world.HeroStage;

/**
 * @author LamHM
 *
 */
@Repository
public interface HeroStageRepository extends MongoRepository<HeroStage, Long> {

	@Query("{'heroId' : ?0, 'chapterIndex' : ?1}")
	List<HeroStage> findStages(String heroId, int chapterId);


	@Query("{'heroId' : ?0, 'chapterIndex' : ?1, 'lastestSweepTime' : {'$lt' : ?2} }")
	List<HeroStage> findStages(String heroId, int chapterId, long startOfDateMilis);


	HeroStage findStageByHeroIdAndIndex(String heroId, int index);


	HeroStage findStageByHeroIdAndClearIsFalse(String heroId);
}
