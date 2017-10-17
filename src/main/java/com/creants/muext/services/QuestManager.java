package com.creants.muext.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.creants.muext.config.GiftEventConfig;
import com.creants.muext.config.QuestConfig;
import com.creants.muext.dao.GameHeroRepository;
import com.creants.muext.dao.QuestRepository;
import com.creants.muext.dao.QuestStatsRepository;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.quest.HeroQuest;
import com.creants.muext.entities.quest.KillMonsterQuest;
import com.creants.muext.entities.quest.Quest;

/**
 * @author LamHM
 *
 */
@Service
public class QuestManager implements InitializingBean {
	public static final String GROUP_WORLD_QUEST = "world";
	public static final String GROUP_DAILY_QUEST = "daily";
	@Autowired
	private GameHeroRepository heroRepository;
	@Autowired
	private QuestStatsRepository questStatsRespository;
	@Autowired
	private QuestRepository questRepository;

	@Autowired
	private AutoIncrementService autoIncrService;

	private QuestConfig questConfig;


	public void afterPropertiesSet() throws Exception {
		questConfig = QuestConfig.getInstance();
		GiftEventConfig.getInstance();
	}


	/**
	 * Lấy danh sách nhiệm vụ có con quái này
	 * 
	 * @param monsters
	 * @return
	 */
	public Set<Integer> getQuestsContainMonster(Set<Integer> monsters) {
		return questConfig.getQuestsContainMonster(monsters);
	}


	public List<HeroQuest> getQuests(String gameHeroId, String groupId) {
		List<HeroQuest> quests = questStatsRespository.findByGameHeroIdAndGroupId(gameHeroId, groupId);
		if (groupId.equals(GROUP_WORLD_QUEST)) {
			GameHero hero = heroRepository.findOne(gameHeroId);
			int level = hero.getLevel();
			List<Quest> newQuests = questRepository.getQuests(level);
			if (newQuests != null) {

			}
		}

		return quests;
	}


	public List<HeroQuest> getQuests(Collection<Long> ids) {
		List<HeroQuest> quests = new ArrayList<>();
		questStatsRespository.findAll(ids).forEach(quests::add);
		return quests;
	}


	public HeroQuest getQuest(String gameHeroId, long questId) {
		return questStatsRespository.findOne(questId);
	}


	public List<HeroQuest> getQuests(String gameHeroId, Integer[] questIds) {
		return questStatsRespository.getQuests(gameHeroId, questIds);
	}


	public List<KillMonsterQuest> getKillMonsterQuest(String gameHeroId, Integer[] questIds) {
		return questStatsRespository.getKillMonsterQuest(gameHeroId, questIds);
	}


	public void save(List<KillMonsterQuest> quests) {
		questStatsRespository.save(quests);
	}


	public void saveQuests(List<HeroQuest> quests) {
		questStatsRespository.save(quests);
	}


	public void save(HeroQuest heroQuest) {
		questStatsRespository.save(heroQuest);
	}


	public void registerQuestsFromHero(GameHero gameHero) {
		List<HeroQuest> quests = new ArrayList<HeroQuest>();
		quests.addAll(questConfig.getNormalQuestList());
		quests.addAll(questConfig.getDailyQuestList());

		for (HeroQuest heroQuest : quests) {
			heroQuest.setGameHeroId(gameHero.getId());
			heroQuest.setId(autoIncrService.genQuestId());
			heroQuest.setCreateTime(System.currentTimeMillis());
		}

		questStatsRespository.save(quests);
	}


	public List<HeroQuest> resetDailyQuest(GameHero gameHero) {
		List<HeroQuest> quests = questStatsRespository.findDailyQuestNeedRemove(gameHero.getId(),
				getStartOfDateMilis());
		long currentTimeMillis = System.currentTimeMillis();
		for (HeroQuest heroQuest : quests) {
			heroQuest.setCreateTime(currentTimeMillis);
			heroQuest.setFinish(false);
			heroQuest.setClaim(false);
		}

		questStatsRespository.save(quests);
		// TODO trường hợp sau này có thêm/bỏ nhiệm vụ sẽ cập nhật ở đây mỗi khi
		// user đăng nhập
		return quests;
	}


	public List<HeroQuest> getQuests(String heroId, String groupId, boolean isFinish) {
		return questStatsRespository.getQuests(heroId, groupId, isFinish);
	}


	public List<HeroQuest> getNewQuests(String gameHeroId) {
		return questStatsRespository.getNewQuests(gameHeroId);
	}


	public List<HeroQuest> getQuests(String heroId, String groupId, boolean isFinish, boolean seen) {
		return questStatsRespository.getQuests(heroId, groupId, isFinish, seen);
	}


	public List<HeroQuest> getNewDailyQuests(String gameHeroId) {
		return questStatsRespository.findDailyQuests(gameHeroId, getStartOfDateMilis(), getEndOfDateMilis());
	}


	public List<HeroQuest> getDailyQuests(String gameHeroId) {
		return questStatsRespository.findDailyQuests(gameHeroId, getStartOfDateMilis(), getEndOfDateMilis());
	}


	public List<HeroQuest> getDailyQuests(String gameHeroId, boolean seen) {
		return questStatsRespository.findDailyQuests(gameHeroId, getStartOfDateMilis(), getEndOfDateMilis(), seen);
	}


	private long getStartOfDateMilis() {
		return DateUtils.truncate(new Date(), Calendar.DATE).getTime();
	}


	private long getEndOfDateMilis() {
		return DateUtils.addMilliseconds(DateUtils.ceiling(new Date(), Calendar.DATE), -1).getTime();
	}

}
