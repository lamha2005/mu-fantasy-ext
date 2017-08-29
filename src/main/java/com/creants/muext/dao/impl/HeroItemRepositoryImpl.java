package com.creants.muext.dao.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.stereotype.Repository;

import com.creants.muext.dao.HeroItemRepository;
import com.creants.muext.entities.item.HeroConsumeableItem;
import com.creants.muext.entities.item.HeroEquipment;
import com.creants.muext.entities.item.HeroItem;

/**
 * @author LamHM
 *
 */
public class HeroItemRepositoryImpl {

	private MongoOperations mongoOperations;


	public List<HeroConsumeableItem> getConsumeableItems(String gameHeroId, Collection<Long> itemIds) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<HeroItem> getAllHeroItem(String gameHeroId) {
		return mongoOperations.findAll(HeroItem.class);
	}


	public List<HeroConsumeableItem> getConsumeableItems(String gameHeroId) {
		return null;
	}


	public List<HeroEquipment> getEquipments(String gameHeroId) {
		// TODO Auto-generated method stub
		return null;
	}


	public HeroEquipment getEquipment(int slotIndex, long heroId) {
		// TODO Auto-generated method stub
		return null;
	}


	public HeroEquipment getEquipment(long itemId, String gameHeroId, long heroId) {
		// TODO Auto-generated method stub
		return null;
	}


	public HeroEquipment getEquipment(long itemId, String gameHeroId) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<HeroEquipment> getTakeOnEquipments(long heroId) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<HeroEquipment> updateEquipment(Collection<HeroEquipment> heroItems) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<HeroConsumeableItem> updateConsumeableItems(Collection<HeroConsumeableItem> heroItems) {
		// TODO Auto-generated method stub
		return null;
	}


	public HeroEquipment updateEquipment(HeroEquipment heroEquipment) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<HeroItem> updateHeroItem(Collection<HeroItem> heroItems) {
		return null;
	}

}
