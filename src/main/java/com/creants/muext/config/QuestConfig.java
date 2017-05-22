package com.creants.muext.config;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.math.NumberUtils;

import com.creants.muext.entities.quest.GroupDailyQuest;
import com.creants.muext.entities.quest.GroupMainQuest;
import com.creants.muext.entities.quest.Quest;
import com.creants.muext.entities.quest.Task;
import com.creants.muext.entities.quest.TaskType;
import com.creants.muext.services.QuestManager;

/**
 * @author LamHM
 *
 */
public class QuestConfig {
	private static final String QUEST_CONFIG = "resources/quests.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();

	private static QuestConfig instance;
	private Map<Integer, Quest> quests;
	private Map<Integer, Set<Integer>> monsterInQuest;


	public static QuestConfig getInstance() {
		if (instance == null) {
			instance = new QuestConfig();
		}
		return instance;
	}


	private QuestConfig() {
		loadMonsters();
	}


	private void loadMonsters() {
		try {
			quests = new HashMap<>();
			monsterInQuest = new HashMap<Integer, Set<Integer>>();

			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(QUEST_CONFIG));
			sr.next(); // to point to <Monsters>
			while (sr.hasNext()) {
				sr.next();

				if (sr.getEventType() == XMLStreamReader.START_ELEMENT) {
					int taskIndex = 0;
					int taskGroup = 0;
					int taskType = 0;
					int monsterIndex1 = -1;
					int monsterIndex2 = -1;
					int taskCount = 0;
					int rewardIndex = 0;
					String taskName = null;
					String taskDesc = null;

					int attributeCount = sr.getAttributeCount();
					for (int i = 0; i < attributeCount; i++) {
						String attName = sr.getAttributeLocalName(i);
						String attValue = sr.getAttributeValue(i);

						switch (attName) {
							case "TaskIndex":
								taskIndex = Integer.parseInt(attValue);
								break;
							case "TaskName":
								taskName = attValue;
								break;
							case "TaskDescription":
								taskDesc = attValue;
								break;
							case "TaskGroup":
								taskGroup = Integer.parseInt(attValue);
								break;
							case "TaskType":
								taskType = Integer.parseInt(attValue);
								break;
							case "MonsterIndex1":
								if (attValue != null && NumberUtils.isNumber(attValue)) {
									monsterIndex1 = Integer.parseInt(attValue);
								}
								break;
							case "MonsterIndex2":
								if (attValue != null && NumberUtils.isNumber(attValue)) {
									monsterIndex2 = Integer.parseInt(attValue);
								}
								break;
							case "TaskCount":
								taskCount = Integer.parseInt(attValue);
								break;
							case "TaskReward":
								rewardIndex = Integer.parseInt(attValue);
								break;

							default:
								break;
						}

					}

					Quest quest = null;
					if (QuestManager.GROUP_MAIN_QUEST == taskGroup) {
						quest = new GroupMainQuest();
					} else {
						quest = new GroupDailyQuest();
					}

					Task task = new Task();
					if (taskType == TaskType.MonsterKill.getId()) {
						HashMap<Object, Object> properties = new HashMap<>();
						properties.put(monsterIndex1, taskCount);
						properties.put(monsterIndex2, taskCount);
						task.setProperties(properties);
						if (monsterIndex1 > 0) {
							addMonsterToQuest(monsterIndex1, taskIndex);
						}

						if (monsterIndex2 > 0) {
							addMonsterToQuest(monsterIndex2, taskIndex);
						}
					}

					quest.setId(taskIndex);
					quest.setName(taskName);
					quest.setGroupId(taskGroup);
					quest.setTaskType(taskType);
					quest.setDesc(taskDesc);
					quest.setTask(task);
					quests.put(taskIndex, quest);

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


	public static void main(String[] args) {
		QuestConfig.getInstance();
	}

}
