package com.creants.muext.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import com.creants.muext.entities.item.ConsumeableItemBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author LamHM
 *
 */
public class ConsumableItemConfig {
	private static final String ITEM_CONFIG = "resources/consumable_items.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();
	private static ConsumableItemConfig instance;
	private Map<Integer, ConsumeableItemBase> itemMap;


	public static ConsumableItemConfig getInstance() {
		if (instance == null) {
			instance = new ConsumableItemConfig();
		}
		return instance;
	}


	private ConsumableItemConfig() {
		loadConsumableItem();
	}


	private void loadConsumableItem() {
		try {
			itemMap = new HashMap<>();
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(ITEM_CONFIG));
			XmlMapper mapper = new XmlMapper();
			sr.next();
			sr.next();
			ConsumeableItemBase item = null;
			while (sr.hasNext()) {
				try {
					item = mapper.readValue(sr, ConsumeableItemBase.class);
					itemMap.put(item.getIndex(), item);
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
			mapper.writeValue(new File("export/consumable_items.json"), getitems());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private Object getitems() {
		return itemMap.values();
	}


	public static void main(String[] args) {
		ConsumableItemConfig.getInstance().writeToJsonFile();
	}
}
