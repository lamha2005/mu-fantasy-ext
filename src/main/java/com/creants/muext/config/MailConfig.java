package com.creants.muext.config;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import com.creants.muext.entities.MailBase;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author LamHM
 *
 */
public class MailConfig {
	private static final String MAIL_CONFIG = "resources/mails.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();
	private static MailConfig instance;
	private Map<Integer, MailBase> mailBaseMap;


	public static MailConfig getInstance() {
		if (instance == null) {
			instance = new MailConfig();
		}
		return instance;
	}


	private MailConfig() {
		mailBaseMap = new HashMap<>();
		loadMailBase();
	}


	private void loadMailBase() {
		try {
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(MAIL_CONFIG));
			XmlMapper mapper = new XmlMapper();
			sr.next();
			sr.next();
			MailBase mail = null;
			while (sr.hasNext()) {
				try {
					mail = mapper.readValue(sr, MailBase.class);
					mailBaseMap.put(mail.getIndex(), mail);
				} catch (NoSuchElementException e) {
				}
			}

			sr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public MailBase getMail(int index) {
		return mailBaseMap.get(index);
	}


	public MailBase getWelcomeMail() {
		return mailBaseMap.get(100);
	}


	public MailBase getWelcomeGiftMail() {
		return mailBaseMap.get(101);
	}

}
