package com.creants.muext.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.creants.muext.config.QuestConfig;
import com.creants.muext.dao.GameHeroRepository;
import com.creants.muext.dao.QuestRepository;
import com.creants.muext.dao.QuestStatsRepository;
import com.creants.muext.dao.SequenceRepository;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.quest.HeroQuest;
import com.creants.muext.entities.quest.Quest;
import com.creants.muext.entities.quest.Task;
import com.creants.muext.entities.quest.TaskType;

/**
 * @author LamHM
 *
 */
@Service
public class QuestManager implements InitializingBean {
	public static final String QUEST_ID_SEQ = "quest_id";
	public static final int GROUP_MAIN_QUEST = 1;
	public static final int GROUP_DAILY_QUEST = 2;
	@Autowired
	private GameHeroRepository heroRepository;
	@Autowired
	private QuestStatsRepository questStatsRespository;
	@Autowired
	private QuestRepository questRepository;

	@Autowired
	private SequenceRepository sequenceRepository;

	@Value("${firstDeploy}")
	private boolean firstDeploy;

	private QuestConfig questConfig;


	public void afterPropertiesSet() throws Exception {
		questConfig = QuestConfig.getInstance();
		// Chỉ tạo lần đầu khi deploy hệ thống
		if (firstDeploy) {
			sequenceRepository.createSequenceDocument(QUEST_ID_SEQ);
		}
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


	public List<HeroQuest> getQuests(String heroId, int groupId) {
		List<HeroQuest> quests = questStatsRespository.findByHeroIdAndGroupId(heroId, groupId);
		if (groupId == GROUP_MAIN_QUEST) {
			GameHero hero = heroRepository.findOne(heroId);
			int level = hero.getLevel();
			List<Quest> newQuests = questRepository.getQuests(level);
			if (newQuests != null) {

			}
		}

		return quests;
	}


	public void registerQuestsFromHero(GameHero gameHero) {
		List<HeroQuest> quests = new ArrayList<HeroQuest>();
		for (int i = 0; i < 5; i++) {
			HeroQuest quest = new HeroQuest();
			quest.setHeroId(gameHero.getId());
			quest.setQuestIndex(1);
			quest.setId(sequenceRepository.getNextSequenceId(QUEST_ID_SEQ));
			quest.setGroupId(GROUP_MAIN_QUEST);
			quest.setCreateTime(System.currentTimeMillis());
			quest.setTaskType(TaskType.MonsterKill.getId());
			quest.setName("Bull Hunter");
			quest.setDesc("Hunt Bull Fighter in Loren");
			Task task = new Task();
			HashMap<Object, Object> properties = new HashMap<>();
			properties.put("1", 10);
			properties.put("3", 10);
			task.setProperties(properties);
			quest.setTask(task);

			quests.add(quest);
		}
		questStatsRespository.save(quests);

		registerDailyQuestFromHero(gameHero);
	}


	private void registerDailyQuestFromHero(GameHero gameHero) {
		long currentTimeMillis = System.currentTimeMillis();
		List<HeroQuest> quests = new ArrayList<HeroQuest>();
		HeroQuest quest = new HeroQuest();
		quest.setHeroId(gameHero.getId());
		quest.setQuestIndex(200);
		quest.setId(sequenceRepository.getNextSequenceId(QUEST_ID_SEQ));
		quest.setGroupId(GROUP_DAILY_QUEST);
		quest.setCreateTime(currentTimeMillis);
		quest.setTaskType(TaskType.WinCampain.getId());
		quest.setName("Win Campain");
		quest.setDesc("Người chơi chiến thắng Campain");
		Task task = new Task();
		HashMap<Object, Object> properties = new HashMap<>();
		properties.put("count", 5);
		task.setProperties(properties);
		quest.setTask(task);
		quests.add(quest);

		HeroQuest quest1 = new HeroQuest();
		quest1.setHeroId(gameHero.getId());
		quest1.setQuestIndex(201);
		quest1.setId(sequenceRepository.getNextSequenceId(QUEST_ID_SEQ));
		quest1.setGroupId(GROUP_DAILY_QUEST);
		quest1.setCreateTime(currentTimeMillis);
		quest1.setTaskType(TaskType.WinChaos.getId());
		quest1.setName("Win Chaos");
		quest1.setDesc("Người chơi chiến thắng Chaos");
		Task task1 = new Task();
		HashMap<Object, Object> properties1 = new HashMap<>();
		properties1.put("count", 5);
		task1.setProperties(properties);
		quest1.setTask(task1);

		quests.add(quest1);
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


	private long getStartOfDateMilis() {
		return DateUtils.truncate(new Date(), Calendar.DATE).getTime();
	}

}
