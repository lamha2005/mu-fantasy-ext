package com.creants.muext.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.muext.config.ItemConfig;
import com.creants.muext.dao.HeroItemRepository;
import com.creants.muext.entities.ItemBase;
import com.creants.muext.entities.item.ConsumeableItemBase;
import com.creants.muext.entities.item.EquipmentBase;
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
	private static final ItemConfig itemConfig = ItemConfig.getInstance();
	@Autowired
	private HeroItemRepository heroItemRep;
	@Autowired
	private AutoIncrementService autoIncrService;


	@Override
	public void afterPropertiesSet() throws Exception {
		// itemIndex/no#itemIndex/no
		// addItems("mus1#323", "7000/1");
		// addItems("mus1#317", "7000/1");
	}


	public List<HeroItem> getItems(String gameHeroId) {
		return heroItemRep.findAllByGameHeroId(gameHeroId);
	}


	public List<HeroEquipment> getTakeOnEquipments(long heroId) {
		return heroItemRep.findTakeOnEquipmentsByHeroId(heroId);
	}


	public HeroEquipment getEquipment(long itemId, String gameHeroId) {
		return heroItemRep.findEquipmentByIdAndGameHeroId(itemId, gameHeroId);
	}


	public HeroEquipment getEquipment(long itemId, String gameHeroId, long heroId) {
		return heroItemRep.findEquipmentByIdAndGameHeroIdAndHeroId(itemId, gameHeroId, heroId);
	}


	public HeroEquipment getEquipmentBySlot(int slotIndex, long heroId) {
		return heroItemRep.findEquipmentBySlotIndexAndHeroId(slotIndex, heroId);
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
			return addItems(gameHeroId, splitItem(itemArrString));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<>();
	}


	public List<HeroItem> addItems(String gameHeroId, List<HeroItem> items) {
		if (items == null || items.size() <= 0)
			return new ArrayList<>();

		List<HeroConsumeableItem> heroOverlapItems = heroItemRep.findConsumeableItemsByGameHeroId(gameHeroId);
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


	private List<HeroItem> splitItem(String itemArrString) {
		List<int[]> itemsReward = new ArrayList<>();
		if (StringUtils.isNotBlank(itemArrString)) {
			String[] items = StringUtils.split(itemArrString, "#");
			for (int i = 0; i < items.length; i++) {
				String[] split = StringUtils.split(items[i], "/");
				itemsReward.add(new int[] { Integer.parseInt(split[0]), Integer.parseInt(split[1]) });
			}
		}
		return convertToItem(itemsReward);
	}


	private List<HeroItem> convertToItem(List<int[]> items) {
		List<HeroItem> itemList = new ArrayList<>();
		if (items.size() > 0) {
			for (int[] ir : items) {
				ItemBase itemBase = itemConfig.getItem(ir[0]);
				if (itemBase instanceof ConsumeableItemBase) {
					HeroConsumeableItem consItem = new HeroConsumeableItem();
					consItem.setIndex(itemBase.getIndex());
					consItem.setItemGroup(itemBase.getGroupId());
					consItem.setOverlap(true);
					consItem.setElement(itemBase.getElemental());
					consItem.setNo(ir[1]);
					consItem.setItemBase((ConsumeableItemBase) itemBase);
					itemList.add(consItem);
				} else if (itemBase instanceof EquipmentBase) {
					itemList.add(new HeroEquipment((EquipmentBase) itemBase));
				}
			}
		}
		return itemList;
	}

}
