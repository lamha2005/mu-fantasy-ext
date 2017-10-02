package com.creants.muext.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.creants.muext.entities.item.HeroConsumeableItem;
import com.creants.muext.entities.item.HeroEquipment;
import com.creants.muext.entities.item.HeroItem;

/**
 * @author LamHM
 */
@Repository
public interface HeroItemRepository extends MongoRepository<HeroItem, Long> {
	List<HeroItem> findAllByGameHeroId(String gameHeroId);


	Page<HeroItem> findAllByGameHeroId(String gameHeroId, Pageable page);


	List<HeroConsumeableItem> findItemsByGameHeroIdAndIsOverlapIsTrue(String gameHeroId);


	List<HeroEquipment> findEquipmentsByGameHeroId(String gameHeroId);


	HeroEquipment findEquipmentBySlotIndexAndHeroId(int slotIndex, long heroId);


	HeroEquipment findEquipmentByIdAndGameHeroIdAndHeroId(long itemId, String gameHeroId, long heroId);


	HeroEquipment findEquipmentByIdAndGameHeroId(long itemId, String gameHeroId);


	List<HeroEquipment> findTakeOnEquipmentsByHeroId(long heroId);

	// List<HeroEquipment> updateEquipment(Collection<HeroEquipment>
	// heroEquipments);

	// List<HeroItem> updateHeroItem(Collection<HeroItem> heroItems);

	// HeroEquipment updateEquipment(HeroEquipment heroEquipment);

	// List<HeroConsumeableItem>
	// updateConsumeableItems(Collection<HeroConsumeableItem> heroItems);
}
