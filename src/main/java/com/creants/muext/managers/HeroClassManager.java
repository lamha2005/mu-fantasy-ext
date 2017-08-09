package com.creants.muext.managers;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.creants.muext.dao.HeroRepository;
import com.creants.muext.entities.HeroBase;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.HeroClassType;
import com.creants.muext.entities.Monster;
import com.creants.muext.entities.skill.Skill;
import com.creants.muext.services.AutoIncrementService;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author LamHM
 *
 */
@Service
public class HeroClassManager implements InitializingBean {
	private static final String HEROES_CONFIG = "resources/heroes.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();

	@Autowired
	private AutoIncrementService autoIncrService;
	@Autowired
	private HeroRepository heroRepository;

	private Map<Integer, HeroBase> heroes;
	private Map<Integer, Monster> monsters;


	@Override
	public void afterPropertiesSet() throws Exception {
		loadHeroes();

		// TODO remove
		// endowHero("mus1#317", HeroClassType.DARK_WIZARD);
	}


	public long genHeroId() {
		return autoIncrService.genHeroId();
	}


	private void loadHeroes() {
		try {
			heroes = new HashMap<>();
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(HEROES_CONFIG));
			XmlMapper mapper = new XmlMapper();
			sr.next(); // to point to <Heroes>
			sr.next(); // to point to root-element under Heroes

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


	/**
	 * Lấy danh sách hero class
	 * 
	 * @param gameHeroId
	 * @return
	 */
	public List<HeroClass> findHeroesByGameHeroId(String gameHeroId) {
		// List<HeroClass> heroes =
		// heroRepository.findHeroesByGameHeroId(gameHeroId, new
		// Sort(Sort.Direction.ASC));
		List<HeroClass> heroes = heroRepository.findHeroesByGameHeroId(gameHeroId);
		for (HeroClass heroClass : heroes) {
			heroClass.setHeroBase(getHeroBase(heroClass.getIndex()));
			if (heroClass.getSkillList().size() <= 0) {
				HeroBase heroBase = getHeroBase(heroClass.getIndex());
				resetSkill(heroClass, heroBase.getSkills());
				// heroRepository.save(heroClass);
			}
		}
		return heroes;
	}


	public List<HeroClass> findHeroes(Collection<Long> heroIds) {
		List<HeroClass> heroes = new ArrayList<>();
		Iterable<HeroClass> findAll = heroRepository.findAll(heroIds);
		for (HeroClass heroClass : findAll) {
			heroClass.setHeroBase(getHeroBase(heroClass.getIndex()));
			if (heroClass.getSkillList().size() <= 0) {
				HeroBase heroBase = getHeroBase(heroClass.getIndex());
				resetSkill(heroClass, heroBase.getSkills());
				// heroRepository.save(heroClass);
			}
			heroes.add(heroClass);
		}
		return heroes;
	}


	public HeroClass findHeroById(long heroId) {
		HeroClass hero = heroRepository.findOne(heroId);
		if (hero == null)
			return null;

		hero.setHeroBase(getHeroBase(hero.getIndex()));
		return hero;
	}


	public HeroBase getHeroBase(int index) {
		return heroes.get(index);
	}


	public void remove(Collection<HeroClass> heroes) {
		heroRepository.delete(heroes);
	}


	public List<HeroClass> summon(String gameHeroId) {
		HeroBase heroBase = new ArrayList<>(heroes.values()).get((new Random()).nextInt(heroes.size()));
		HeroClass createNewHero = createNewHero(gameHeroId, heroBase);
		save(createNewHero);

		List<HeroClass> heroList = new ArrayList<>();
		heroList.add(createNewHero);
		return heroList;
	}


	public HeroClass createNewHero(String gameHeroId, HeroClassType type) {
		HeroBase heroBase = getHeroBase(type.getId());
		HeroClass heroClass = new HeroClass(heroBase);
		heroClass.setId(genHeroId());
		heroClass.setGameHeroId(gameHeroId);
		resetSkill(heroClass, heroBase.getSkills());
		return heroClass;
	}


	public HeroClass createNewHero(String gameHeroId, HeroBase heroBase) {
		HeroClass heroClass = new HeroClass(heroBase);
		heroClass.setId(genHeroId());
		heroClass.setGameHeroId(gameHeroId);
		resetSkill(heroClass, heroBase.getSkills());
		return heroClass;
	}


	private void resetSkill(HeroClass heroClass, int[] skillArr) {
		for (int i = 0; i < skillArr.length; i++) {
			int skillIndex = skillArr[i];
			Skill skill = new Skill();
			skill.setIndex(skillIndex);
			skill.setLevel(1);
			heroClass.addSkill(skill);
		}
	}


	public void save(List<HeroClass> heroes) {
		heroRepository.save(heroes);
	}


	public void save(HeroClass heroClass) {
		heroRepository.save(heroClass);
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


	/**
	 * Xin cấp hero để test
	 * 
	 * @param gameHeroId
	 * @param type
	 */
	public void endowHero(String gameHeroId, HeroClassType type) {
		heroRepository.save(createNewHero(gameHeroId, type));
	}


	public Monster getMonster(int id) {
		return monsters.get(id);
	}

}
