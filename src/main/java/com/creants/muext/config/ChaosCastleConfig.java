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

import com.creants.muext.entities.chaos.ChaosStageBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author LamHM
 *
 */
public class ChaosCastleConfig {
	private static final String CHAOS_CASTLE_CONFIG = "resources/chaos_castle.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();
	private static ChaosCastleConfig instance;
	private int revison;
	private Map<Integer, ChaosStageBase> stageMap;


	public static ChaosCastleConfig getInstance() {
		if (instance == null) {
			instance = new ChaosCastleConfig();
		}

		return instance;
	}


	private ChaosCastleConfig() {
		loadStage();
	}


	public void loadStage() {
		try {
			stageMap = new HashMap<>();
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(CHAOS_CASTLE_CONFIG));
			XmlMapper mapper = new XmlMapper();
			sr.next(); // to point to <Unicode>
			sr.next();
			ChaosStageBase stage = null;
			while (sr.hasNext()) {
				try {
					stage = mapper.readValue(sr, ChaosStageBase.class);
					stage.initBase();
					stageMap.put(stage.getIndex(), stage);
				} catch (NoSuchElementException e) {
				}
			}

			sr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public int getRevison() {
		return revison;
	}


	public ChaosStageBase getStage(int index) {
		return stageMap.get(index);
	}


	public List<ChaosStageBase> getStages() {
		return new ArrayList<>(stageMap.values());
	}


	public ChaosStageBase getFirstStage() {
		return getStage(1);
	}


	public void writeToJsonFile() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(new File("export/chaos_castle.json"), getStages());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		ChaosCastleConfig.getInstance().writeToJsonFile();
	}

}
