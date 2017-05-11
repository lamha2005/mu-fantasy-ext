package com.creants.muext.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.creants.muext.entities.Monster;

/**
 * @author LamHM
 *
 */
@Service
public class MonsterManager implements InitializingBean {
	private Map<Integer, List<Monster>> missionMonsters;


	@Override
	public void afterPropertiesSet() throws Exception {
		missionMonsters = new HashMap<>();
	}

}
