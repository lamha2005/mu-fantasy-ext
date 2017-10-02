package com.creants.muext.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.muext.config.ItemConfig;
import com.creants.muext.dao.HeroItemRepository;
import com.creants.muext.entities.item.HeroConsumeableItem;
import com.creants.muext.entities.item.HeroEquipment;
import com.creants.muext.entities.item.HeroItem;
import com.creants.muext.services.AutoIncrementService;

/**
 * @author LamHM
 *
 */
@Service
public class HeroItemManager implements InitializingBean {
	private static final int MAX_ITEM_PER_PAGE = 20;
	private static final ItemConfig itemConfig = ItemConfig.getInstance();
	@Autowired
	private HeroItemRepository heroItemRep;
	@Autowired
	private AutoIncrementService autoIncrService;


	@Override
	public void afterPropertiesSet() throws Exception {
		// itemIndex/no#itemIndex/no
		// addItems("mus1#323", "7000/1");
		// addItems("mus1#317", "11017/3#11018/3#11019/3");
		// addItems("mus1#317", "1001/1");
		// for (int i = 0; i < 50; i++) {
		// addItems("mus1#323", "7000/1#1001/1");
		// }
	}


	public List<HeroItem> getItems(String gameHeroId) {
		List<HeroItem> items = heroItemRep.findAllByGameHeroId(gameHeroId);
		items.forEach(heroItem -> {
			heroItem.setItemBase(itemConfig.getItem(heroItem.getIndex()));
		});
		return items;
	}


	public Page<HeroItem> getItems(String gameHeroId, int page) {
		return heroItemRep.findAllByGameHeroId(gameHeroId, new PageRequest(page - 1, MAX_ITEM_PER_PAGE));
	}


	public List<HeroEquipment> getTakeOnEquipments(long heroId) {
		List<HeroEquipment> items = heroItemRep.findTakeOnEquipmentsByHeroId(heroId);
		items.forEach(heroItem -> {
			heroItem.setItemBase(itemConfig.getItem(heroItem.getIndex()));
		});
		return items;
	}


	public HeroEquipment getEquipment(long itemId, String gameHeroId) {
		HeroEquipment equipment = heroItemRep.findEquipmentByIdAndGameHeroId(itemId, gameHeroId);
		if (equipment != null) {
			equipment.setItemBase(itemConfig.getItem(equipment.getIndex()));
		}
		return equipment;
	}


	public HeroEquipment getEquipment(long itemId, String gameHeroId, long heroId) {
		HeroEquipment equipment = heroItemRep.findEquipmentByIdAndGameHeroIdAndHeroId(itemId, gameHeroId, heroId);
		if (equipment != null) {
			equipment.setItemBase(itemConfig.getItem(equipment.getIndex()));
		}
		return equipment;
	}


	public HeroEquipment getEquipmentBySlot(int slotIndex, long heroId) {
		HeroEquipment equipment = heroItemRep.findEquipmentBySlotIndexAndHeroId(slotIndex, heroId);
		if (equipment != null) {
			equipment.setItemBase(itemConfig.getItem(equipment.getIndex()));
		}
		return equipment;
	}


	public List<HeroConsumeableItem> useItems(String gameHeroId, Map<Long, Integer> items) {
		List<HeroConsumeableItem> consumeableItems = getConsumeableItems(gameHeroId, items.keySet());
		if (consumeableItems == null) {
			QAntTracer.warn(this.getClass(), "useItems not found, gameHeroId: " + gameHeroId);
			return new ArrayList<>();
		}

		if (consumeableItems != null) {
			for (HeroConsumeableItem heroItem : consumeableItems) {
				heroItem.useItem(items.get(heroItem.getId()));
				if (heroItem.getNo() <= 0) {
					// TODO add to remove
				}
			}
		}

		QAntTracer.debug(this.getClass(),
				"userItems gameHeroId: " + gameHeroId + "/itemIds:" + items.keySet() + "/no:" + items.values());
		return heroItemRep.save(consumeableItems);
	}


	public List<HeroConsumeableItem> getConsumeableItems(String gameHeroId, Collection<Long> itemIds) {
		List<HeroConsumeableItem> result = new ArrayList<>();
		Iterable<HeroItem> findAll = heroItemRep.findAll(itemIds);
		Iterator<HeroItem> iterator = findAll.iterator();
		while (iterator.hasNext()) {
			HeroConsumeableItem consItem = (HeroConsumeableItem) iterator.next();
			if (!consItem.getGameHeroId().equals(gameHeroId)) {
				// TODO throw exception
			}
			result.add(consItem);
		}
		return result;
	}


	public void updateEquipments(Collection<HeroEquipment> heroEquipments) {
		heroItemRep.save(heroEquipments);
	}


	public void updateConsumeableItem(Collection<HeroConsumeableItem> items) {
		heroItemRep.save(items);
	}


	public void takeOn(HeroEquipment equipment, long heroId, int slot) {
		equipment.takeOn(heroId, slot);
		heroItemRep.save(equipment);
	}


	public void takeOff(HeroEquipment heroEquipment) {
		heroEquipment.takeOff();
		heroItemRep.save(heroEquipment);
	}


	/**
	 * @param itemArrString
	 *            format (itemIndex/no#itemIndex/no)
	 */
	public List<HeroItem> addItems(String gameHeroId, String itemArrString) {
		try {
			return addItems(gameHeroId, itemConfig.splitItemToHeroItem(itemArrString));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<>();
	}


	public List<HeroItem> addItems(String gameHeroId, List<HeroItem> items) {
		if (items == null || items.size() <= 0)
			return new ArrayList<>();

		List<HeroConsumeableItem> heroOverlapItems = heroItemRep.findItemsByGameHeroIdAndIsOverlapIsTrue(gameHeroId);
		List<HeroItem> itemsUpdate = new ArrayList<>();

		Map<Integer, Integer> overlapItems = new HashMap<>();
		Iterator<HeroItem> iterator = items.iterator();
		while (iterator.hasNext()) {
			HeroItem item = iterator.next();
			if (heroOverlapItems.contains(item)) {
				overlapItems.put(item.getIndex(), item.getNo());
				iterator.remove();
			}
		}

		// lưu item mới
		for (HeroItem item : items) {
			item.setId(autoIncrService.genItemId());
			item.setGameHeroId(gameHeroId);
			itemsUpdate.add(item);
		}

		// cập nhật số lượng item cho overlap
		if (overlapItems.size() > 0) {
			for (HeroConsumeableItem heroItem : heroOverlapItems) {
				Integer no = overlapItems.get(heroItem.getIndex());
				if (no != null) {
					heroItem.incr(no);
					itemsUpdate.add(heroItem);
				}
			}
		}

		return heroItemRep.save(itemsUpdate);
	}

}
