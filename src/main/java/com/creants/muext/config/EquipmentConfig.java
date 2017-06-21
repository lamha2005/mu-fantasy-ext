package com.creants.muext.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;

import com.creants.muext.entities.item.EquipmentBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author LamHM
 *
 */
public class EquipmentConfig {
	private static final String EFFECT_CONFIG = "resources/equipments.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();
	private static EquipmentConfig instance;
	private Map<Integer, EquipmentBase> equipmentMap;


	public static EquipmentConfig getInstance() {
		if (instance == null) {
			instance = new EquipmentConfig();
		}
		return instance;
	}


	private EquipmentConfig() {
		loadEquipment();
	}


	private void loadEquipment() {
		try {
			equipmentMap = new HashMap<>();
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(EFFECT_CONFIG));
			XmlMapper mapper = new XmlMapper();
			sr.next();
			sr.next();
			EquipmentBase equipment = null;
			while (sr.hasNext()) {
				try {
					equipment = mapper.readValue(sr, EquipmentBase.class);
					String availableHeroesString = equipment.getAvailableHeroesString();
					String[] items = StringUtils.split(availableHeroesString, ";");
					int[] availableHeroes = new int[items.length];
					for (int i = 0; i < items.length; i++) {
						availableHeroes[i] = Integer.parseInt(items[i]);
					}
					equipment.setAvailableHeroes(availableHeroes);

					equipmentMap.put(equipment.getIndex(), equipment);
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
			mapper.writeValue(new File("export/equipments.json"), getEquipments());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private Object getEquipments() {
		return equipmentMap.values();
	}


	public static void main(String[] args) {
		EquipmentConfig.getInstance().writeToJsonFile();
	}
}
