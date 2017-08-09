package com.creants.muext.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.creants.muext.config.ItemConfig;
import com.creants.muext.dao.HeroItemRepository;
import com.creants.muext.entities.ItemBase;
import com.creants.muext.entities.item.ConsumeableItemBase;
import com.creants.muext.entities.item.EquipmentBase;
import com.creants.muext.entities.item.HeroItem;
import com.creants.muext.services.AutoIncrementService;

/**
 * @author LamHM
 *
 */
@Service
public class HeroItemManager implements InitializingBean {
	private ItemConfig itemConfig;
	@Autowired
	private HeroItemRepository heroItemRep;
	@Autowired
	private AutoIncrementService autoIncrService;


	@Override
	public void afterPropertiesSet() throws Exception {
		itemConfig = ItemConfig.getInstance();
	}


	public List<HeroItem> getItems(String gameHeroId) {
		return heroItemRep.findItemsByGameHeroId(gameHeroId);
	}


	public List<HeroItem> getItems(Collection<Long> itemIds) {
		List<HeroItem> heroItems = new ArrayList<>();
		Iterable<HeroItem> findAll = heroItemRep.findAll(itemIds);
		if (findAll != null) {
			for (HeroItem heroItem : findAll) {
				heroItems.add(heroItem);
			}
		}
		return heroItems;
	}


	public void removeItems(Collection<HeroItem> heroItems) {
		heroItemRep.delete(heroItems);
	}


	public void updateItem(Collection<HeroItem> heroItems) {
		heroItemRep.save(heroItems);
	}


	/**
	 * @param itemArrString
	 *            format (itemIndex/no#itemIndex/no)
	 */
	public List<HeroItem> addItems(String gameHeroId, String itemArrString) {
		try {
			List<HeroItem> items = splitItem(itemArrString);
			addItems(gameHeroId, items);
			return items;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<>();
	}


	public List<HeroItem> addItems(String gameHeroId, List<HeroItem> items) {
		if (items == null || items.size() <= 0)
			return new ArrayList<>();

		List<HeroItem> heroItems = heroItemRep.findItemsByIsOverlap(true);
		List<HeroItem> itemsUpdate = new ArrayList<>();

		Map<HeroItem, Integer> overlapItems = new HashMap<>();
		for (HeroItem item : items) {
			if (heroItems.contains(item) && item.isOverlap()) {
				overlapItems.put(item, item.getNo());
			}
		}

		// lưu item mới
		items.removeAll(overlapItems.keySet());
		for (HeroItem item : items) {
			itemsUpdate.add(new HeroItem(autoIncrService.genItemId(), gameHeroId, item));
		}

		// cập nhật số lượng item cho overlap
		if (overlapItems.size() > 0) {
			for (HeroItem heroItem : heroItems) {
				Integer no = overlapItems.get(heroItem);
				if (no != null) {
					heroItem.incr(no);
					itemsUpdate.add(heroItem);
				}
			}
		}

		return heroItemRep.save(itemsUpdate);
	}


	public List<HeroItem> splitItem(String itemArrString) {
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
				HeroItem item = new HeroItem();
				item.setNo(ir[1]);
				item.setIndex(itemBase.getIndex());
				item.setItemGroup(itemBase.getGroupId());
				item.setItemBase(itemBase);
				if (itemBase instanceof ConsumeableItemBase) {
					item.setOverlap(true);
				} else if (itemBase instanceof EquipmentBase) {
					EquipmentBase equipmentBase = (EquipmentBase) itemBase;
					item.setElement(equipmentBase.getElemental());
				}
				itemList.add(item);
			}
		}
		return itemList;
	}


	public static void main(String[] args) {
		List<HeroItem> heroItems = new ArrayList<>();
		HeroItem heroItem1 = new HeroItem();
		heroItem1.setItemGroup(10);
		heroItem1.setIndex(100);
		heroItems.add(heroItem1);

		HeroItem heroItem2 = new HeroItem();
		heroItem2.setItemGroup(11);
		heroItem2.setIndex(101);
		heroItem2.setGameHeroId("test1000");
		heroItems.add(heroItem2);

		HeroItem heroItem3 = new HeroItem();
		heroItem3.setItemGroup(11);
		heroItem3.setIndex(101);
		System.out.println(heroItems.size());

	}
}
