package com.creants.muext.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;

import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.muext.entities.ItemBase;
import com.creants.muext.entities.ext.ShortItemExt;
import com.creants.muext.entities.item.ConsumeableItemBase;
import com.creants.muext.entities.item.EquipmentBase;
import com.creants.muext.entities.item.HeroItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author LamHM
 *
 */
public class ItemConfig {
	public static final String SEPERATE_OTHER_ITEM = "#";
	public static final String SEPERATE_ITEM_NO = "/";

	private static final int CONSUMABLE_ITEM = 1;
	private static final int EQUIPMENT_ITEM = 2;
	private static final String COMSUMEABLE_ITEM_CONFIG = "resources/consumable_items.xml";
	private static final String EQUIPMENT_CONFIG = "resources/equipments.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();
	private static ItemConfig instance;
	private Map<Integer, ItemBase> itemMap;


	public static ItemConfig getInstance() {
		if (instance == null) {
			instance = new ItemConfig();
		}
		return instance;
	}


	private ItemConfig() {
		itemMap = new HashMap<>();
		loadConsumableItem();
		loadEquipment();
	}


	private void loadConsumableItem() {
		try {
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(COMSUMEABLE_ITEM_CONFIG));
			XmlMapper mapper = new XmlMapper();
			sr.next();
			sr.next();
			ConsumeableItemBase item = null;
			while (sr.hasNext()) {
				try {
					item = mapper.readValue(sr, ConsumeableItemBase.class);
					item.setType(CONSUMABLE_ITEM);
					itemMap.put(item.getIndex(), item);
				} catch (NoSuchElementException e) {
				}
			}

			sr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public boolean isEquipment(int itemIndex) {
		ItemBase item = getItem(itemIndex);
		if (item == null)
			return false;

		return item instanceof EquipmentBase;
	}


	private void loadEquipment() {
		try {
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(EQUIPMENT_CONFIG));
			XmlMapper mapper = new XmlMapper();
			sr.next();
			sr.next();
			EquipmentBase equipment = null;
			while (sr.hasNext()) {
				try {
					equipment = mapper.readValue(sr, EquipmentBase.class);
					String availableClassString = equipment.getAvailableHeroesString();
					String[] items = StringUtils.split(availableClassString, SEPERATE_OTHER_ITEM);
					int[] availableClassGroups = new int[items.length];
					for (int i = 0; i < items.length; i++) {
						availableClassGroups[i] = Integer.parseInt(items[i]);
					}
					equipment.setAvailableClassGroups(availableClassGroups);

					String equipSlotString = equipment.getEquipSlotString();
					String[] slotArr = StringUtils.split(equipSlotString, "#");
					int[] availableSlots = new int[slotArr.length];
					for (int i = 0; i < slotArr.length; i++) {
						availableSlots[i] = Integer.parseInt(slotArr[i]);
					}
					equipment.setEquipSlot(availableSlots);

					equipment.setType(EQUIPMENT_ITEM);

					itemMap.put(equipment.getIndex(), equipment);
				} catch (NoSuchElementException e) {
				}
			}

			sr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public List<ShortItemExt> splitItem(String itemArrString) {
		if (StringUtils.isBlank(itemArrString))
			return null;

		List<int[]> itemsReward = new ArrayList<>();
		if (StringUtils.isNotBlank(itemArrString)) {
			String[] items = StringUtils.split(itemArrString, SEPERATE_OTHER_ITEM);
			for (int i = 0; i < items.length; i++) {
				String[] split = StringUtils.split(items[i], SEPERATE_ITEM_NO);
				itemsReward.add(new int[] { Integer.parseInt(split[0]), Integer.parseInt(split[1]) });
			}
		}

		return convertToItem(itemsReward);
	}


	public QAntArray buildShortItemInfo(List<HeroItem> bonusItems) {
		QAntArray items = QAntArray.newInstance();
		QAntObject obj = null;
		for (HeroItem heroItem : bonusItems) {
			obj = QAntObject.newInstance();
			obj.putInt("index", heroItem.getIndex());
			obj.putInt("no", heroItem.getNo());
			items.addQAntObject(obj);
		}
		return items;
	}


	private List<ShortItemExt> convertToItem(List<int[]> items) {
		List<ShortItemExt> itemList = new ArrayList<>();
		if (items.size() > 0) {
			for (int[] ir : items) {
				ItemBase itemBase = ItemConfig.getInstance().getItem(ir[0]);
				ShortItemExt item = new ShortItemExt();
				item.setNo(ir[1]);
				item.setIndex(itemBase.getIndex());
				itemList.add(item);
			}
		}
		return itemList;
	}


	public void writeToJsonFile() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			List<ConsumeableItemBase> items = new ArrayList<>();
			List<EquipmentBase> equipments = new ArrayList<>();
			for (ItemBase itemBase : itemMap.values()) {
				if (itemBase instanceof ConsumeableItemBase) {
					items.add((ConsumeableItemBase) itemBase);
				} else if (itemBase instanceof EquipmentBase) {
					equipments.add((EquipmentBase) itemBase);
				}

			}

			mapper.writeValue(new File("export/consumable_items.json"), items);
			mapper.writeValue(new File("export/equipments.json"), equipments);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public ItemBase getItem(int index) {
		return itemMap.get(index);
	}


	public static void main(String[] args) {
		ItemConfig.getInstance().writeToJsonFile();
	}
}
