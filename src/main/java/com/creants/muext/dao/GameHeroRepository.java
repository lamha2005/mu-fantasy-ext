package com.creants.muext.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.creants.muext.entities.GameHero;

/**
 * @author LamHM
 *
 */
@Repository
public interface GameHeroRepository extends MongoRepository<GameHero, String> {

	List<GameHero> findHeroesByIsNPCIsTrue();
}
