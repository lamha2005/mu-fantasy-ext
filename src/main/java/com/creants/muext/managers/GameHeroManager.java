package com.creants.muext.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.creants.muext.dao.GameHeroRepository;
import com.creants.muext.entities.GameHero;

/**
 * @author LamHM
 *
 */
@Service
public class GameHeroManager {
	@Autowired
	private GameHeroRepository repository;


	public GameHero getHero(String gameHeroId) {
		return repository.findOne(gameHeroId);
	}


	public void update(GameHero gameHero) {
		repository.save(gameHero);
	}


	public GameHero incrZen(String gameHeroId, long zen) {
		GameHero gameHero = repository.findOne(gameHeroId);
		if (gameHero == null) {
			return null;
		}

		gameHero.incrZen(zen);
		return repository.save(gameHero);
	}


	public GameHero incrBless(String gameHeroId, long bless) {
		GameHero gameHero = repository.findOne(gameHeroId);
		if (gameHero == null) {
			return null;
		}

		gameHero.incrBless(bless);
		return repository.save(gameHero);
	}


	public GameHero incrBless(GameHero gameHero, long bless) {
		gameHero.incrBless(bless);
		return repository.save(gameHero);
	}

}
