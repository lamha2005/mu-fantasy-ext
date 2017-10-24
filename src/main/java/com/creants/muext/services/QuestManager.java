package com.creants.muext.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.muext.config.GiftEventConfig;
import com.creants.muext.config.QuestConfig;
import com.creants.muext.dao.QuestStatsRepository;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.quest.HeroQuest;

/**
 * @author LamHM
 *
 */
@Service
public class QuestManager implements InitializingBean {
	public static final String GROUP_WORLD_QUEST = "world";
	public static final String GROUP_DAILY_QUEST = "daily";
	@Autowired
	private QuestStatsRepository questStatsRespository;

	@Autowired
	private AutoIncrementService autoIncrService;

	private QuestConfig questConfig;

	// gameHeroId, questId, questType
	private Map<String, Map<Long, Integer>> heroQuestMap;


	public void afterPropertiesSet() throws Exception {
		heroQuestMap = new HashMap<>();
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


	public List<HeroQuest> getKillMonsterQuest(String gameHeroId, Integer[] questIds) {
		return questStatsRespository.getQuests(gameHeroId, questIds);
	}


	public void save(List<HeroQuest> quests) {
		questStatsRespository.save(quests);
	}


	public void saveQuests(List<HeroQuest> quests) {
		questStatsRespository.save(quests);
	}


	public void save(HeroQuest heroQuest) {
		questStatsRespository.save(heroQuest);
	}


	public void delete(HeroQuest heroQuest) {
		questStatsRespository.delete(heroQuest);
	}


	public List<Long> containQuestType(String gameHeroId, int questType) {
		List<Long> questIds = new ArrayList<>();
		Map<Long, Integer> questMap = heroQuestMap.get(gameHeroId);
		if (questMap == null || questMap.size() <= 0 || !questMap.containsValue(questType)) {
			return questIds;
		}

		for (Long questId : questMap.keySet()) {
			int type = questMap.get(questId);
			if (questType == type) {
				questIds.add(questId);
			}
		}

		return questIds;
	}


	public void finishQuest(String gameHeroId, Collection<Long> questFinishList) {
		Map<Long, Integer> questMap = heroQuestMap.get(gameHeroId);
		if (questMap == null)
			return;

		for (Long id : questFinishList) {
			questMap.remove(id);
		}
	}


	/**
	 * Nhận quest mới
	 * 
	 * @param gameHeroId
	 * @param quests
	 */
	public void registerQuests(String gameHeroId, List<HeroQuest> quests) {
		Map<Long, Integer> questMap = heroQuestMap.get(gameHeroId);
		if (questMap == null)
			questMap = new ConcurrentHashMap<>();

		for (HeroQuest heroQuest : quests) {
			heroQuest.setGameHeroId(gameHeroId);
			heroQuest.setId(autoIncrService.genQuestId());
			heroQuest.setCreateTime(System.currentTimeMillis());
			questMap.put(heroQuest.getId(), heroQuest.getTaskType());
		}

		questStatsRespository.save(quests);
	}


	public boolean registerQuests(String gameHeroId, int chapter) {
		List<HeroQuest> questList = questConfig.getQuestList(chapter);
		if (questList != null && questList.size() > 0) {
			registerQuests(gameHeroId, questList);
			return true;
		}
		return false;
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
		return questStatsRespository.getQuests(heroId, groupId);
	}


	public List<HeroQuest> getNewQuests(String gameHeroId) {
		return questStatsRespository.getNewQuests(gameHeroId);
	}


	public List<HeroQuest> getQuests(String gameHeroId) {
		return questStatsRespository.getQuests(gameHeroId);
	}


	/**
	 * Đếm danh sách quest mới và quest chưa nhận thưởng, cache quest chưa hoàn
	 * thành
	 * 
	 * @param gameHeroId
	 * @return
	 */
	public IQAntArray countNewQuest(String gameHeroId) {
		Map<Long, Integer> questMap = heroQuestMap.get(gameHeroId);
		if (questMap == null) {
			questMap = new ConcurrentHashMap<>();
			heroQuestMap.put(gameHeroId, questMap);
		}

		List<HeroQuest> newQuests = getQuests(gameHeroId);
		Map<String, Integer> countMap = new HashMap<>();
		countMap.put(QuestManager.GROUP_WORLD_QUEST, 0);
		countMap.put(QuestManager.GROUP_DAILY_QUEST, 0);

		for (HeroQuest heroQuest : newQuests) {
			if (!heroQuest.isClaim())
				questMap.put(heroQuest.getId(), heroQuest.getTaskType());

			if (!heroQuest.isSeen() || heroQuest.isClaim()) {
				String groupId = heroQuest.getGroupId();
				Integer count = countMap.get(groupId);
				count++;
				countMap.put(groupId, count);
			}
		}

		IQAntArray questArr = QAntArray.newInstance();
		for (String groupId : countMap.keySet()) {
			IQAntObject quest = QAntObject.newInstance();
			quest.putUtfString("group", groupId);
			quest.putInt("no", countMap.get(groupId));
			questArr.addQAntObject(quest);
		}

		return questArr;
	}


	public List<HeroQuest> getNewQuests(String gameHeroId, String groupId) {
		return questStatsRespository.getNewQuests(gameHeroId, groupId);
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
