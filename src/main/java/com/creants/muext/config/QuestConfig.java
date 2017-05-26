package com.creants.muext.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.creants.muext.entities.Item;
import com.creants.muext.entities.Reward;
import com.creants.muext.entities.quest.GroupDailyQuest;
import com.creants.muext.entities.quest.GroupMainQuest;
import com.creants.muext.entities.quest.Quest;
import com.creants.muext.entities.quest.Task;
import com.creants.muext.entities.quest.TaskType;
import com.creants.muext.services.QuestManager;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author LamHM
 *
 */
public class QuestConfig {
	private static final String QUEST_CONFIG = "resources/quests.xml";
	private static final String REWARD_CONFIG = "resources/quest_reward.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();

	private static QuestConfig instance;
	private Map<Integer, Quest> quests;
	private Map<Integer, Reward> rewardMap;
	private Map<Integer, Set<Integer>> monsterInQuest;


	public static QuestConfig getInstance() {
		if (instance == null) {
			instance = new QuestConfig();
		}
		return instance;
	}


	private QuestConfig() {
		loadItems();
		loadMonsters();
	}


	private void loadItems() {
		rewardMap = new HashMap<>();

		XMLStreamReader sr;
		try {
			sr = f.createXMLStreamReader(new FileInputStream(REWARD_CONFIG));
			sr.next(); // to point to <Monsters>
			while (sr.hasNext()) {
				sr.next();

				if (sr.getEventType() != XMLStreamReader.START_ELEMENT) {
					continue;
				}

				Reward reward = new Reward();
				int attributeCount = sr.getAttributeCount();
				for (int i = 0; i < attributeCount; i++) {
					String attName = sr.getAttributeLocalName(i);
					String attValue = sr.getAttributeValue(i);

					switch (attName) {
						case "Index":
							reward.setIndex(Integer.parseInt(attValue));
							break;
						case "Exp":
							if (NumberUtils.isNumber(attValue)) {
								reward.setExp(Integer.parseInt(attValue));
							}
							break;
						case "Zen":
							if (NumberUtils.isNumber(attValue)) {
								reward.setZen(Integer.parseInt(attValue));
							}
							break;
						case "Soul":
							if (NumberUtils.isNumber(attValue)) {
								reward.setSoul(Integer.parseInt(attValue));
							}
							break;
						case "Items":
							List<Item> items = reward.getItems();
							String[] itemArr = StringUtils.split(attValue, ";");
							if (itemArr.length > 0) {
								for (int j = 0; j < itemArr.length; j++) {
									String[] infos = StringUtils.split(itemArr[j], ",");
									if (infos.length <= 0)
										continue;

									Item item = new Item();
									for (int k = 0; k < infos.length; k++) {
										String[] stringItemArr = StringUtils.split(infos[k], "=");
										if (stringItemArr[0].trim().startsWith("index")) {
											item.setIndex(Integer.parseInt(stringItemArr[1].trim()));
										} else if (stringItemArr[0].trim().startsWith("no")) {
											item.setNo(Integer.parseInt(stringItemArr[1].trim()));
										}
									}
									items.add(item);
								}
							}
							reward.setItems(items);
							break;

						default:
							break;
					}

				}

				rewardMap.put(reward.getIndex(), reward);
			}
			sr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public Reward getReward(int rewardIndex) {
		return rewardMap.get(rewardIndex);
	}


	private void loadMonsters() {
		try {
			quests = new HashMap<>();
			monsterInQuest = new HashMap<Integer, Set<Integer>>();

			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(QUEST_CONFIG));
			sr.next(); // to point to <Monsters>
			while (sr.hasNext()) {
				sr.next();

				if (sr.getEventType() != XMLStreamReader.START_ELEMENT) {
					continue;
				}

				int taskIndex = 0;
				int taskGroup = 0;
				int taskType = 0;
				int monsterIndex1 = -1;
				int monsterIndex2 = -1;
				int taskCount = 0;
				int rewardIndex = 0;
				String taskName = null;
				String taskDesc = null;

				// TODO tùy theo loại nhiệm vụ là gì thì đọc thông tin đó
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
							if (attValue != null && NumberUtils.isNumber(attValue)) {
								taskCount = Integer.parseInt(attValue);
							}
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

					quest.setDesc(taskDesc);
				} else if (taskType == TaskType.WinCampain.getId() || taskType == TaskType.WinChaos.getId()) {
					HashMap<Object, Object> properties = new HashMap<>();
					properties.put("count", taskCount);
					quest.setDesc(taskDesc + " " + taskCount + " lần.");
					task.setProperties(properties);
				}

				quest.setReward(getReward(rewardIndex));
				quest.setIndex(taskIndex);
				quest.setName(taskName);
				quest.setGroupId(taskGroup);
				quest.setTaskType(taskType);
				quest.setTask(task);
				quests.put(taskIndex, quest);
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


	public List<Quest> getQuests() {
		return new ArrayList<>(quests.values());
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
		return quests.get(questIndex);
	}


	public static void main(String[] args) {
		QuestConfig.getInstance().writeToJsonFile();
		;
	}

}
