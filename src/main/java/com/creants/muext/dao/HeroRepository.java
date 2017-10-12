package com.creants.muext.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


	Page<HeroClass> findHeroesByGameHeroId(String gameHeroId, Pageable page);


	List<HeroClass> findHeroesByGameHeroId(String gameHeroId, Pageable page, boolean reqNew);
}
