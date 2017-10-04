package com.creants.muext.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.creants.muext.entities.item.HeroConsumeableItem;
import com.creants.muext.entities.item.HeroEquipment;
import com.creants.muext.entities.item.HeroItem;

/**
 * @author LamHM
 */
@Repository
public interface HeroItemRepository extends MongoRepository<HeroItem, Long> {

	Page<HeroItem> findAllByGameHeroId(String gameHeroId, Pageable page);


	@Query("{'gameHeroId' : ?0, 'itemType' : { $ne:9}}")
	Page<HeroItem> getItemListExceptMoneyType(String gameHeroId, Pageable page);


	List<HeroConsumeableItem> findItemsByGameHeroIdAndIsOverlapIsTrue(String gameHeroId);


	@Query("{'gameHeroId' : ?0, 'isOverlap' : true}")
	List<HeroConsumeableItem> getOverlapConsumeableItemList(String gameHeroId);


	HeroEquipment findEquipmentBySlotIndexAndHeroId(int slotIndex, long heroId);


	@Query("{'slotIndex' : ?0, 'heroId' : ?1}")
	HeroEquipment getEquipment(int slotIndex, long heroId);


	HeroEquipment findEquipmentByIdAndGameHeroIdAndHeroId(long itemId, String gameHeroId, long heroId);


	@Query("{'gameHeroId' : ?0, 'heroId' : ?1, 'itemId' : ?2}")
	HeroEquipment getEquipment(String gameHeroId, long heroId, long itemId);


	HeroEquipment findEquipmentByIdAndGameHeroId(long itemId, String gameHeroId);


	@Query("{'gameHeroId' : ?0, 'itemId' : ?2}")
	HeroEquipment getEquipment(String gameHeroId, long itemId);


	@Query("{'gameHeroId' : ?0, 'itemType' : 9}")
	List<HeroItem> getMoneyTypeList(String gameHeroId);


	List<HeroEquipment> findTakeOnEquipmentsByHeroId(long heroId);

	// List<HeroEquipment> updateEquipment(Collection<HeroEquipment>
	// heroEquipments);

	// List<HeroItem> updateHeroItem(Collection<HeroItem> heroItems);

	// HeroEquipment updateEquipment(HeroEquipment heroEquipment);

	// List<HeroConsumeableItem>
	// updateConsumeableItems(Collection<HeroConsumeableItem> heroItems);
}
