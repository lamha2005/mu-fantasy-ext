package com.creants.muext.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.creants.muext.dao.GameHeroRepository;
import com.creants.muext.services.AutoIncrementService;

/**
 * @author LamHM
 *
 */
@Service
public class NPCManager {
	@Autowired
	private AutoIncrementService autoIncrService;

	@Autowired
	private GameHeroRepository gameHeroRepository;

	@Autowired
	private HeroClassManager heroManager;

}
