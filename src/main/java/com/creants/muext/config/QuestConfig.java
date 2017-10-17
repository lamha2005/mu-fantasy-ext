package com.creants.muext.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import com.creants.muext.entities.quest.CompleteQuest;
import com.creants.muext.entities.quest.CompleteTask;
import com.creants.muext.entities.quest.HeroQuest;
import com.creants.muext.entities.quest.KillMonsterQuest;
import com.creants.muext.entities.quest.MonsterKillTask;
import com.creants.muext.entities.quest.Quest;
import com.creants.muext.entities.quest.WinQuest;
import com.creants.muext.entities.quest.WinTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author LamHM
 *
 */
public class QuestConfig {
	public static final String QUEST_GROUP_DAILY = "daily";
	public static final String QUEST_GROUP_WORLD = "world";

	public static final int MONSTER_KILL = 1;
	public static final int WIN_TASK = 2;
	public static final int COMPLETE_TASK1 = 3;
	public static final int COMPLETE_TASK2 = 4;
	public static final int COMPLETE_TASK3 = 5;
	public static final int COMPLETE_TASK4 = 6;
	public static final int COMPLETE_TASK5 = 7;
	public static final int COMPLETE_TASK6 = 8;

	private static final String QUEST_CONFIG = "resources/quests.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();

	private static QuestConfig instance;
	private Map<Integer, Quest> questMap;
	private Map<Integer, HeroQuest> dailyQuestMap;
	private Map<Integer, HeroQuest> worldQuestMap;
	// Monster có trong các nhiệm vụ
	private Map<Integer, Set<Integer>> monsterInQuest;


	public static QuestConfig getInstance() {
		if (instance == null) {
			instance = new QuestConfig();
		}
		return instance;
	}


	private QuestConfig() {
		monsterInQuest = new HashMap<>();
		questMap = new HashMap<>();
		dailyQuestMap = new TreeMap<>();
		worldQuestMap = new TreeMap<>();
		loadQuest();
	}


	private void loadQuest() {
		try {
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(QUEST_CONFIG));
			XmlMapper mapper = new XmlMapper();
			sr.next();
			sr.next();
			Quest quest = null;
			while (sr.hasNext()) {
				if (!sr.isStartElement()) {
					sr.next();
					continue;
				}

				HeroQuest heroQuest = null;
				int taskType = Integer.parseInt(sr.getAttributeValue(null, "TaskType"));
				switch (taskType) {
					case MONSTER_KILL:
						MonsterKillTask monsterKillTask = mapper.readValue(sr, MonsterKillTask.class);
						quest = monsterKillTask;
						quest.convertBase();

						addMonsterToQuest(monsterKillTask.doGetMonsterList(), quest.getIndex());

						KillMonsterQuest killQuest = new KillMonsterQuest();
						killQuest.setMonsters(monsterKillTask.getMonsters());
						heroQuest = killQuest;
						break;
					case WIN_TASK:
						WinTask winTask = mapper.readValue(sr, WinTask.class);
						quest.convertBase();
						quest = winTask;

						WinQuest winQuest = new WinQuest();
						winQuest.setNo(winTask.getWinNo());
						heroQuest = winQuest;
						break;
					case COMPLETE_TASK1:
					case COMPLETE_TASK2:
					case COMPLETE_TASK3:
					case COMPLETE_TASK4:
					case COMPLETE_TASK5:
					case COMPLETE_TASK6:
						CompleteTask completeTask = mapper.readValue(sr, CompleteTask.class);
						quest = completeTask;
						CompleteQuest completeQuest = new CompleteQuest();
						heroQuest = completeQuest;
						break;
					default:
						quest = null;
						sr.next();
						break;
				}

				if (quest == null)
					continue;

				heroQuest.setQuestIndex(quest.getIndex());
				heroQuest.setGroupId(quest.getGroupId());
				heroQuest.setTaskType(taskType);

				questMap.put(quest.getIndex(), quest);
				switch (quest.getGroupId()) {
					case QUEST_GROUP_WORLD:
						worldQuestMap.put(quest.getIndex(), heroQuest);
						break;
					case QUEST_GROUP_DAILY:
						dailyQuestMap.put(quest.getIndex(), heroQuest);
						break;

					default:
						break;
				}
			}

			sr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void addMonsterToQuest(int monsterId, int questId) {
		Set<Integer> list = monsterInQuest.get(monsterId);
		if (list == null) {
			list = new HashSet<Integer>();
		}

		list.add(questId);
		monsterInQuest.put(monsterId, list);
	}


	private void addMonsterToQuest(Collection<Integer> monsters, int questId) {
		for (Integer monsterId : monsters) {
			addMonsterToQuest(monsterId, questId);
		}
	}


	public Set<Integer> getQuestsContainMonster(Set<Integer> monsters) {
		Set<Integer> quests = new HashSet<Integer>();
		for (int monsterId : monsters) {
			Set<Integer> set = monsterInQuest.get(monsterId);
			if (set != null && set.size() > 0) {
				quests.addAll(set);
			}
		}

		return quests;
	}


	public List<HeroQuest> getDailyQuestList() {
		return new ArrayList<>(dailyQuestMap.values());
	}


	public List<HeroQuest> getNormalQuestList() {
		return new ArrayList<>(worldQuestMap.values());
	}


	public List<Quest> getQuests() {
		return new ArrayList<>(questMap.values());
	}


	public void writeToJsonFile() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(new File("export/quests.json"), getQuests());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public Quest getQuest(int questIndex) {
		return questMap.get(questIndex);
	}


	public static void main(String[] args) {
		QuestConfig.getInstance().writeToJsonFile();

	}

}
