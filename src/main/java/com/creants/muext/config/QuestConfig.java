package com.creants.muext.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import com.creants.muext.entities.quest.HeroQuest;
import com.creants.muext.entities.quest.QuestBase;
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

	private static final String QUEST_CONFIG = "resources/quests.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();

	private static QuestConfig instance;
	private Map<Integer, QuestBase> questMap;
	private Map<Integer, HeroQuest> dailyQuestMap;
	private Map<Integer, HeroQuest> worldQuestMap;
	private Map<Integer, List<HeroQuest>> chapterQuestMap;
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
		chapterQuestMap = new TreeMap<>();
		loadQuest();
	}


	private void loadQuest() {
		try {
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(QUEST_CONFIG));
			XmlMapper mapper = new XmlMapper();
			sr.next();
			sr.next();
			QuestBase quest = null;
			HeroQuest heroQuest = null;
			while (sr.hasNext()) {
				if (!sr.isStartElement()) {
					sr.next();
					continue;
				}

				quest = mapper.readValue(sr, QuestBase.class);
				quest.convertBase();
				int taskType = quest.getTaskType();
				if (taskType == MONSTER_KILL) {
					addMonsterToQuest(Integer.parseInt(quest.getTask()), quest.getIndex());
				}

				heroQuest = new HeroQuest(quest);

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

				Integer chapter = quest.getChapter();
				if (chapter != null) {
					List<HeroQuest> stageList = chapterQuestMap.get(chapter);
					if (stageList == null) {
						stageList = new ArrayList<>();
						chapterQuestMap.put(chapter, stageList);
					}
					stageList.add(heroQuest);
				}
			}

			sr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public List<HeroQuest> getQuestList(int chapter) {
		return new ArrayList<>(chapterQuestMap.get(chapter));
	}


	private void addMonsterToQuest(int monsterId, int questId) {
		Set<Integer> list = monsterInQuest.get(monsterId);
		if (list == null) {
			list = new HashSet<Integer>();
		}

		list.add(questId);
		monsterInQuest.put(monsterId, list);
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


	public List<HeroQuest> getWorldQuestList() {
		return new ArrayList<>(worldQuestMap.values());
	}


	public List<QuestBase> getQuests() {
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


	public QuestBase getQuest(int questIndex) {
		return questMap.get(questIndex);
	}


	public static void main(String[] args) {
		QuestConfig.getInstance().writeToJsonFile();

	}

}
