package com.creants.muext.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.creants.muext.entities.item.HeroItem;

/**
 * @author LamHM
 */
@Repository
public interface HeroItemRepository extends MongoRepository<HeroItem, Long> {
	List<HeroItem> findItemsByGameHeroId(String gameHeroId);


	List<HeroItem> findItemsByIsOverlap(boolean isOverlap);
}
