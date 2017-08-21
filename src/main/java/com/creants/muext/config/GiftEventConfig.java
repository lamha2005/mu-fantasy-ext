package com.creants.muext.config;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import com.creants.muext.entities.event.GiftEventBase;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author LamHM
 *
 */
public class GiftEventConfig {
	private static final String GIFT_EVENTS_CONFIG = "resources/events/gift_events.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();
	private static GiftEventConfig instance;
	private int revison;
	private Map<Integer, GiftEventBase> gifts;


	public static GiftEventConfig getInstance() {
		if (instance == null) {
			instance = new GiftEventConfig();
		}

		return instance;
	}


	private GiftEventConfig() {
		loadStage();
	}


	public void loadStage() {
		try {
			gifts = new HashMap<>();
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(GIFT_EVENTS_CONFIG));
			XmlMapper mapper = new XmlMapper();
			sr.next(); // to point to <Unicode>

			revison = Integer.parseInt(sr.getAttributeValue(0));
			sr.next();
			GiftEventBase giftEvent = null;
			while (sr.hasNext()) {
				try {
					giftEvent = mapper.readValue(sr, GiftEventBase.class);
					giftEvent.init();
					gifts.put(giftEvent.getIndex(), giftEvent);
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


	public GiftEventBase getEvent(int index) {
		return gifts.get(index);
	}


	public List<GiftEventBase> getEvents() {
		return new ArrayList<>(gifts.values());
	}

	public GiftEventBase getDailyGiftEvent(){
		return getEvent(0);
	}
}
