package com.creants.muext.config;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

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
	private static final String MONSTERS_CONFIG = "resources/quests.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();

	private static QuestConfig instance;
	private Map<Integer, Quest> quests;

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
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(MONSTERS_CONFIG));
			sr.next(); // to point to <Monsters>
			while (sr.hasNext()) {
				sr.next();
				if (sr.getEventType() == XMLStreamReader.START_ELEMENT) {
					int attributeCount = sr.getAttributeCount();
					for (int i = 0; i < attributeCount; i++) {
						String attName = sr.getAttributeLocalName(i);
						String attValue = sr.getAttributeValue(i);

						int taskIndex = 0;
						int taskGroup = 0;
						int taskType = 0;
						int monsterIndex1 = 0;
						int monsterIndex2 = 0;
						int taskCount = 0;
						int rewardIndex = 0;
						String taskName = null;
						String taskDesc = null;
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
							monsterIndex1 = Integer.parseInt(attValue);
							break;
						case "MonsterIndex2":
							monsterIndex2 = Integer.parseInt(attValue);
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

						Quest quest = null;
						if (QuestManager.GROUP_MAIN_QUEST == taskGroup) {
							quest = new GroupMainQuest();
						}

						Task task = new Task();
						if (taskType == TaskType.MonsterKill.getId()) {
							HashMap<Object, Object> properties = new HashMap<>();
							properties.put(monsterIndex1, taskCount);
							properties.put(monsterIndex2, taskCount);
							task.setProperties(properties);
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
			}

			sr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		QuestConfig.getInstance();
	}

}
