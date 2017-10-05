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

import com.creants.muext.entities.shop.ShopPackage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author LamHM
 *
 */
public class ShopConfig {
	private static final String SHOP_CONFIG = "resources/shop.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();
	private static ShopConfig instance;
	private Map<Integer, ShopPackage> packageMap;


	public static ShopConfig getInstance() {
		if (instance == null) {
			instance = new ShopConfig();
		}

		return instance;
	}


	private ShopConfig() {
		loadShop();
	}


	public void loadShop() {
		try {
			packageMap = new HashMap<>();
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(SHOP_CONFIG));
			XmlMapper mapper = new XmlMapper();
			sr.next(); // to point to <Unicode>
			sr.next();
			ShopPackage shopPackage = null;
			while (sr.hasNext()) {
				try {
					shopPackage = mapper.readValue(sr, ShopPackage.class);
					shopPackage.init();
					packageMap.put(shopPackage.getIndex(), shopPackage);
				} catch (NoSuchElementException e) {
				}
			}

			sr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public List<ShopPackage> getPackages() {
		return new ArrayList<>(packageMap.values());
	}


	public ShopPackage getPackage(int index) {
		return packageMap.get(index);
	}


	public void writeToJsonFile() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(new File("export/shop.json"), getPackages());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		ShopConfig instance2 = ShopConfig.getInstance();
		instance2.writeToJsonFile();
	}

}
