package com.creants.muext.config.xml;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import com.creants.muext.entities.xml.Asset;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author LamHM
 *
 */
public class AssetManagerConfig {
	private static final String ASSET_MANAGER_CONFIG = "resources/asset_manager.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();
	private static AssetManagerConfig instance;
	private Map<Integer, Asset> assets;


	public static AssetManagerConfig getInstance() {
		if (instance == null) {
			instance = new AssetManagerConfig();
		}

		return instance;
	}


	private AssetManagerConfig() {
		loadStage();
	}


	public void loadStage() {
		try {
			assets = new HashMap<>();
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(ASSET_MANAGER_CONFIG));
			XmlMapper mapper = new XmlMapper();
			sr.next(); // to point to <Stages>
			sr.next();
			Asset asset = null;
			while (sr.hasNext()) {
				try {
					asset = mapper.readValue(sr, Asset.class);
					asset.convertBase();
					assets.put(asset.getIndex(), asset);
				} catch (NoSuchElementException e) {
				}
			}

			sr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public List<Asset> getStages() {
		return new ArrayList<>(assets.values());
	}


	public void writeToJsonFile() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(new File("export/asset_manager.json"), getStages());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		AssetManagerConfig.getInstance().writeToJsonFile();
	}

}
