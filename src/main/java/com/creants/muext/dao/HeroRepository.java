package com.creants.muext.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.creants.muext.entities.HeroClass;

/**
 * @author LamHM
 *
 */
@Repository
public interface HeroRepository extends MongoRepository<HeroClass, Long> {

	List<HeroClass> findHeroesByGameHeroId(String gameHeroId);
	List<HeroClass> findHeroesByGameHeroId(String gameHeroId, Sort sort);

}
