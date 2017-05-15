package com.creants.muext.config;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import com.creants.muext.entities.world.Mission;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author LamHM
 *
 */
public class WorldConfig {
	private static final String MISSIONS_CONFIG = "resources/missions.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();
	private static WorldConfig instance;
	private Map<Integer, Mission> missions;


	public static WorldConfig getInstance() {
		if (instance == null) {
			instance = new WorldConfig();
		}

		return instance;
	}


	private WorldConfig() {
		loadMission();
	}


	public void loadMission() {
		try {
			missions = new HashMap<>();
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(MISSIONS_CONFIG));
			XmlMapper mapper = new XmlMapper();
			sr.next(); // to point to <Monsters>
			sr.next();
			Mission mission = null;
			while (sr.hasNext()) {
				try {
					mission = mapper.readValue(sr, Mission.class);
					missions.put(mission.getIndex(), mission);
				} catch (NoSuchElementException e) {

				}
			}

			sr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public Mission getMission(int index) {
		return missions.get(index);
	}

}
