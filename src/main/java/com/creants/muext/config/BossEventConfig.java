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

import com.creants.muext.entities.event.BossEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author LamHM
 *
 */
public class BossEventConfig {
	private static final String EFFECT_CONFIG = "resources/events/boss_events.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();
	private static BossEventConfig instance;
	private Map<Integer, BossEvent> eventMap;


	public static BossEventConfig getInstance() {
		if (instance == null) {
			instance = new BossEventConfig();
		}
		return instance;
	}


	private BossEventConfig() {
		loadBossEvent();
	}


	private void loadBossEvent() {
		try {
			eventMap = new HashMap<>();
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(EFFECT_CONFIG));
			XmlMapper mapper = new XmlMapper();
			sr.next();
			sr.next();
			BossEvent bossEvent = null;
			while (sr.hasNext()) {
				try {
					bossEvent = mapper.readValue(sr, BossEvent.class);
					eventMap.put(bossEvent.getIndex(), bossEvent);
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
			mapper.writeValue(new File("export/boss_events.json"), getEvents());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public List<BossEvent> getEvents() {
		return new ArrayList<>(eventMap.values());
	}


	public BossEvent getEvent(int index) {
		return eventMap.get(index);
	}


	public static void main(String[] args) {
		BossEventConfig.getInstance().writeToJsonFile();
	}
}
