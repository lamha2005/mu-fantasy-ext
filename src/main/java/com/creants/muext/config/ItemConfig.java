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

import com.creants.muext.entities.ItemBase;
import com.creants.muext.entities.item.ConsumeableItemBase;
import com.creants.muext.entities.item.EquipmentBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author LamHM
 *
 */
public class ItemConfig {
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
					String[] items = StringUtils.split(availableClassString, "#");
					int[] availableClassGroups = new int[items.length];
					for (int i = 0; i < items.length; i++) {
						availableClassGroups[i] = Integer.parseInt(items[i]);
					}
					equipment.setAvailableClassGroups(availableClassGroups);
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
