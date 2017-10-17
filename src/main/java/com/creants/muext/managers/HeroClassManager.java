package com.creants.muext.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.muext.config.HeroClassConfig;
import com.creants.muext.dao.HeroRepository;
import com.creants.muext.entities.HeroBase;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.HeroClassType;
import com.creants.muext.entities.Monster;
import com.creants.muext.entities.skill.Skill;
import com.creants.muext.services.AutoIncrementService;

/**
 * @author LamHM
 *
 */
@Service
public class HeroClassManager implements InitializingBean {
	private static final int MAX_HERO_PER_PAGE = 20;

	private static final HeroClassConfig heroConfig = HeroClassConfig.getInstance();
	@Autowired
	private AutoIncrementService autoIncrService;
	@Autowired
	private HeroRepository heroRepository;
	@Autowired
	private HeroItemManager itemManager;

	private Map<Integer, Monster> monsters;


	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO remove
		// for (int i = 0; i < 3; i++) {
		// endowHero("mus1#31", HeroClassType.GREAT_DARK_KNIGHT);
		// }

		// List<HeroClass> topDamHero = getTopDamHero("mus1#323");
		// System.out.println("test");
	}


	public long genHeroId() {
		return autoIncrService.genHeroId();
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
		}
		return heroes;
	}


	public List<HeroClass> getTopDamHero(String gameHeroId) {
		List<HeroClass> heroes = heroRepository.findHeroesByGameHeroId(gameHeroId,
				new PageRequest(0, 3, new Sort(Sort.Direction.DESC, "level")), true);
		for (HeroClass heroClass : heroes) {
			heroClass.setHeroBase(getHeroBase(heroClass.getIndex()));
			heroClass.setEquipments(itemManager.getTakeOnEquipments(heroClass.getId()));
		}
		return heroes;
	}


	public Page<HeroClass> findHeroesByGameHeroId(String gameHeroId, int page) {
		return heroRepository.findHeroesByGameHeroId(gameHeroId, new PageRequest(page - 1, MAX_HERO_PER_PAGE));
	}


	public List<HeroClass> findHeroes(Collection<Long> heroIds) {
		List<HeroClass> heroes = new ArrayList<>();
		Iterable<HeroClass> findAll = heroRepository.findAll(heroIds);
		for (HeroClass heroClass : findAll) {
			heroClass.setHeroBase(getHeroBase(heroClass.getIndex()));
			heroes.add(heroClass);
		}
		return heroes;
	}


	/**
	 * lấy danh sách hero bao gồm cả vũ khí
	 * 
	 * @param heroIds
	 * @return
	 */
	public List<HeroClass> findHeroesFullInfo(Collection<Long> heroIds) {
		List<HeroClass> heroes = new ArrayList<>();
		Iterable<HeroClass> heroIter = heroRepository.findAll(heroIds);
		for (HeroClass heroClass : heroIter) {
			heroClass.setHeroBase(getHeroBase(heroClass.getIndex()));
			heroClass.setEquipments(itemManager.getTakeOnEquipments(heroClass.getId()));
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
		return heroConfig.getHeroBase(index);
	}


	public void remove(Collection<HeroClass> heroes) {
		heroRepository.delete(heroes);
	}


	public List<HeroClass> summonX1(String gameHeroId) {
		HeroClass createNewHero = createNewHero(gameHeroId, heroConfig.getRandomHero(100));
		save(createNewHero);

		List<HeroClass> heroList = new ArrayList<>();
		heroList.add(createNewHero);
		return heroList;
	}


	public List<HeroClass> summonX10(String gameHeroId) {
		List<HeroClass> heroList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			HeroClass createNewHero = createNewHero(gameHeroId, heroConfig.getRandomHero(100));
			heroList.add(createNewHero);
		}

		save(heroList);
		return heroList;
	}


	public void addHero(String gameHeroId) {
		List<HeroClass> heroes = new ArrayList<>(2);
		heroes.add(createNewHero(gameHeroId, HeroClassType.Storm_Fighter));
		heroes.add(createNewHero(gameHeroId, HeroClassType.Sacred_Fighter));
		heroRepository.save(heroes);
	}


	public HeroClass createNewHero(String gameHeroId, HeroClassType type) {
		HeroBase heroBase = getHeroBase(type.getId());
		HeroClass heroClass = new HeroClass(heroBase);
		heroClass.setId(genHeroId());
		heroClass.setGameHeroId(gameHeroId);
		resetSkill(heroClass, heroBase.getSkills());
		return heroClass;
	}


	public void upgradeHero(HeroClass heroClass) {
		HeroBase heroBase = heroClass.getHeroBase();

		Integer evolveTo = heroBase.getEvolveTo();
		HeroBase newHeroBase = getHeroBase(evolveTo);
		heroClass.setIndex(newHeroBase.getIndex());
		heroClass.setRank(heroClass.getRank() + 1);
		heroClass.setSkillList(heroBase.getSkillList());
		heroClass.setHeroBase(newHeroBase);
		save(heroClass);
		QAntTracer.info(this.getClass(),
				String.format("upgradeHero success [gameHeroId: %s, heroId:%d, HeroIndex:%d, toHeroIndex:%d]",
						heroClass.getGameHeroId(), heroClass.getId(), heroBase.getIndex(), heroClass.getIndex()));
	}


	public HeroClass createNewHero(String gameHeroId, HeroClassType type, int level) {
		HeroBase heroBase = getHeroBase(type.getId());
		HeroClass heroClass = new HeroClass(heroBase, level);
		heroClass.setId(genHeroId());
		heroClass.setGameHeroId(gameHeroId);
		resetSkill(heroClass, heroBase.getSkills());
		return heroClass;
	}


	private HeroClass createNewHero(String gameHeroId, HeroBase heroBase) {
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
		// heroes.add(createNewHero(gameHeroId, HeroClassType.FAIRY_ELF));
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
