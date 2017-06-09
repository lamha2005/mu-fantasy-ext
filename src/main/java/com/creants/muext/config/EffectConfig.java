package com.creants.muext.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import com.creants.muext.entities.EffectBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author LamHM
 *
 */
public class EffectConfig {
	private static final String EFFECT_CONFIG = "resources/effects.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();
	private static EffectConfig instance;
	private Map<Integer, EffectBase> effectMap;


	public static EffectConfig getInstance() {
		if (instance == null) {
			instance = new EffectConfig();
		}
		return instance;
	}


	private EffectConfig() {
		loadSkill();
	}


	private void loadSkill() {
		try {
			effectMap = new HashMap<>();
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(EFFECT_CONFIG));
			XmlMapper mapper = new XmlMapper();
			sr.next(); // to point to <Monsters>
			sr.next();
			EffectBase effect = null;
			while (sr.hasNext()) {
				try {
					effect = mapper.readValue(sr, EffectBase.class);
					effectMap.put(effect.getIndex(), effect);
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
			mapper.writeValue(new File("export/effects.json"), getEffects());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private Object getEffects() {
		return effectMap.values();
	}


	public static void main(String[] args) {
		EffectConfig.getInstance().writeToJsonFile();
	}
}
