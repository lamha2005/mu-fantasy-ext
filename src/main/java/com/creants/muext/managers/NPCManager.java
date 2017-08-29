package com.creants.muext.managers;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.npc.NPCGameHero;
import com.creants.muext.entities.npc.NPCHeroClass;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author LamHM
 *
 */
@Service
public class NPCManager implements InitializingBean {
	private static final String NPC_CONFIG = "resources/npc.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();
	private int revison;

	@Autowired
	private HeroClassManager heroManager;

	private Map<String, GameHero> npcMap;


	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO chỉ load khi first deploy, hoặc chủ động load
		loadNPC();
		// TODO cho phép tạo từ web
	}


	private void loadNPC() {
		try {
			npcMap = new HashMap<>();
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(NPC_CONFIG));
			XmlMapper mapper = new XmlMapper();
			sr.next(); // to point to <Unicode>
			revison = Integer.parseInt(sr.getAttributeValue(0));
			sr.next();

			Collection<GameHero> gameHeroList = new ArrayList<>();
			while (sr.hasNext()) {
				try {
					NPCGameHero npcGameHero = mapper.readValue(sr, NPCGameHero.class);
					GameHero gameHero = new GameHero();
					gameHero.setId(npcGameHero.getId());
					gameHero.setName(npcGameHero.getName());
					gameHero.setLevel(npcGameHero.getLevel());
					List<NPCHeroClass> heroList = npcGameHero.getHeroList();
					List<HeroClass> heroes = new ArrayList<>(heroList.size());
					int count = 1;
					for (NPCHeroClass npcHeroClass : heroList) {
						HeroClass heroClass = new HeroClass(heroManager.getHeroBase(npcHeroClass.getClassIndex()),
								npcHeroClass.getLevel());
						heroClass.setId(count++);
						heroClass.setRank(npcHeroClass.getCharRank());
						heroes.add(heroClass);
					}

					gameHero.setHeroes(heroes);
					gameHeroList.add(gameHero);
					npcMap.put(gameHero.getId(), gameHero);
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


	public List<GameHero> findHeroesByIsNPCIsTrue() {
		return new ArrayList<>(npcMap.values());
	}


	public List<HeroClass> findHeroesByGameHeroId(String id) {
		return npcMap.get(id).getHeroes();
	}


	public GameHero getGameHero(String id) {
		return npcMap.get(id);
	}
}
