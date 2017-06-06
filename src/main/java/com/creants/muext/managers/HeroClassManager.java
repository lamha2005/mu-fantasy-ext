package com.creants.muext.managers;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.creants.muext.dao.HeroRepository;
import com.creants.muext.dao.SequenceRepository;
import com.creants.muext.entities.HeroBase;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.HeroClassType;
import com.creants.muext.entities.Monster;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author LamHM
 *
 */
@Service
public class HeroClassManager implements InitializingBean {
	public static final String HERO_ID_SEQ = "hero_id";
	private static final String HEROES_CONFIG = "resources/heroes.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();

	@Autowired
	private SequenceRepository sequenceRepository;
	@Autowired
	private HeroRepository heroRepository;

	@Value("${firstDeploy}")
	private boolean firstDeploy;

	private Map<Integer, HeroBase> heroes;
	private Map<Integer, Monster> monsters;


	@Override
	public void afterPropertiesSet() throws Exception {
		if (firstDeploy) {
			sequenceRepository.createSequenceDocument(HERO_ID_SEQ);
			sequenceRepository.setDefaultValue(HERO_ID_SEQ, 1000);
			sequenceRepository.createSequenceDocument("hero_stage_id");
		}
		loadHeroes();
	}


	public long getNextHeroId() {
		return sequenceRepository.getNextSequenceId(HERO_ID_SEQ);
	}


	private void loadHeroes() {
		try {
			heroes = new HashMap<>();
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(HEROES_CONFIG));
			XmlMapper mapper = new XmlMapper();
			sr.next(); // to point to <Heros>
			sr.next(); // to point to root-element under Heros

			HeroBase heroBase = null;
			while (sr.hasNext()) {
				try {
					heroBase = mapper.readValue(sr, HeroBase.class);
					heroes.put(heroBase.getIndex(), heroBase);
				} catch (NoSuchElementException e) {

				}
			}

			sr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public List<HeroClass> findHeroesByGameHeroId(String gameHeroId) {
		List<HeroClass> heroes = heroRepository.findHeroesByGameHeroId(gameHeroId);
		for (HeroClass heroClass : heroes) {
			heroClass.setHeroBase(getHeroBase(heroClass.getIndex()));
		}
		return heroes;
	}


	public HeroBase getHeroBase(int index) {
		return heroes.get(index);
	}


	public HeroClass createNewHero(String gameHeroId, HeroClassType type) {
		HeroClass heroClass = new HeroClass(getHeroBase(type.getId()));
		heroClass.setId(getNextHeroId());
		heroClass.setGameHeroId(gameHeroId);
		return heroClass;
	}


	public void save(List<HeroClass> heroes) {
		heroRepository.save(heroes);
	}


	/**
	 * Cho 2 nhân vật khi đăng ký tài khoản
	 * 
	 * @param gameHeroId
	 */
	public List<HeroClass> endowHeroes(String gameHeroId) {
		List<HeroClass> heroes = new ArrayList<>(2);
		heroes.add(createNewHero(gameHeroId, HeroClassType.DARK_KNIGHT));
		heroes.add(createNewHero(gameHeroId, HeroClassType.FAIRY_ELF));
		heroRepository.save(heroes);
		return heroes;
	}


	public Monster getMonster(int id) {
		return monsters.get(id);
	}

}
