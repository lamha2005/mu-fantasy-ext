package com.creants.muext.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.creants.muext.dao.GameHeroRepository;
import com.creants.muext.dao.QuestRepository;
import com.creants.muext.dao.QuestStatsRepository;
import com.creants.muext.dao.SequenceRepository;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.quest.Quest;
import com.creants.muext.entities.quest.QuestStats;
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

	private Map<Integer, Set<Integer>> monsterInQuest;


	public void afterPropertiesSet() throws Exception {
		monsterInQuest = new HashMap<Integer, Set<Integer>>();
		// Chỉ tạo lần đầu khi deploy hệ thống
		if (firstDeploy) {
			sequenceRepository.createSequenceDocument(QUEST_ID_SEQ);
		}
	}


	public void addMonsterToQuest(int monsterId, int questId) {
		Set<Integer> list = monsterInQuest.get(monsterId);
		if (list == null) {
			list = new HashSet<Integer>();
		}

		list.add(questId);
	}


	/**
	 * Lấy danh sách nhiệm vụ có con quái này
	 * 
	 * @param monsters
	 * @return
	 */
	public Set<Integer> getQuestsContainMonster(int... monsters) {
		Set<Integer> quests = new HashSet<Integer>();
		for (int monsterId : monsters) {
			Set<Integer> set = monsterInQuest.get(monsterId);
			if (set != null && set.size() > 0) {
				quests.addAll(set);
			}
		}

		return quests;
	}


	public List<QuestStats> getQuests(String heroId, int groupId) {
		List<QuestStats> quests = questStatsRespository.findByHeroIdAndGroupId(heroId, groupId);
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
		List<QuestStats> quests = new ArrayList<QuestStats>();
		for (int i = 0; i < 5; i++) {
			QuestStats quest = new QuestStats();
			quest.setHeroId(gameHero.getId());
			quest.setId(sequenceRepository.getNextSequenceId(QUEST_ID_SEQ));
			quest.setGroupId(GROUP_MAIN_QUEST);
			quest.setCreateTime(System.currentTimeMillis());
			quest.setTaskType(TaskType.MonsterKill.getId());
			quest.setName("Bull Hunter");
			quest.setDesc("Hunt Bull Fighter in Loren");
			Task task = new Task();
			HashMap<Object, Object> properties = new HashMap<>();
			properties.put("0", 10);
			properties.put("3", 10);
			task.setProperties(properties);
			quest.setTask(task);

			quests.add(quest);
		}
		questStatsRespository.save(quests);
	}

}
