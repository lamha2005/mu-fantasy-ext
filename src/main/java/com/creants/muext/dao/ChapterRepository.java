package com.creants.muext.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.creants.muext.entities.GameHero;

/**
 * id: gameHeroId#chapterId(server_id#user_id#hero_no#chapterId)
 * 
 * @author LamHM
 */
@Repository
public interface ChapterRepository extends MongoRepository<GameHero, String> {

}
