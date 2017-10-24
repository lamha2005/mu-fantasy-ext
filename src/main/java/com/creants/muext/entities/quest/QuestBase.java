package com.creants.muext.entities.quest;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.creants.muext.config.ItemConfig;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({ "itemRewardString", "stageIndexString" })
public class QuestBase {
	@JacksonXmlProperty(localName = "Index", isAttribute = true)
	private int index;
	@JacksonXmlProperty(localName = "Group", isAttribute = true)
	private String groupId;
	@JacksonXmlProperty(localName = "Chapter", isAttribute = true)
	private Integer chapter;
	@JacksonXmlProperty(localName = "StageIndex", isAttribute = true)
	private String stageIndexString;
	@JacksonXmlProperty(localName = "TaskType", isAttribute = true)
	private int taskType;
	@JacksonXmlProperty(localName = "Name", isAttribute = true)
	private String name;
	@JacksonXmlProperty(localName = "Description", isAttribute = true)
	private String desc;
	@JacksonXmlProperty(localName = "Task", isAttribute = true)
	private String task;
	@JacksonXmlProperty(localName = "Count", isAttribute = true)
	private int count;

	@JacksonXmlProperty(localName = "ZenReward", isAttribute = true)
	private int zenReward;
	@JacksonXmlProperty(localName = "ExpReward", isAttribute = true)
	private int expReward;
	@JacksonXmlProperty(localName = "ItemReward", isAttribute = true)
	private String itemRewardString;
	@JacksonXmlProperty(localName = "Goto", isAttribute = true)
	private String goTo;

	private List<String> itemReward;
	private int[] stageIndexes;


	public void convertBase() {
		splitReward();
		splitStageIndex();
	}


	public String getFullReward() {
		return ItemConfig.getInstance().combineToItemString(itemRewardString, zenReward, null);
	}


	private void splitReward() {
		itemReward = ItemConfig.getInstance().splitRewardString(itemRewardString);
	}


	private void splitStageIndex() {
		if (stageIndexString != null) {
			String[] items = StringUtils.split(stageIndexString, "#");
			stageIndexes = new int[items.length];
			for (int i = 0; i < items.length; i++) {
				stageIndexes[i] = Integer.parseInt(items[i]);
			}
		}
	}


	public int[] getStageIndexes() {
		return stageIndexes;
	}


	public void setStageIndexes(int[] stageIndexes) {
		this.stageIndexes = stageIndexes;
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public int getZenReward() {
		return zenReward;
	}


	public void setZenReward(int zenReward) {
		this.zenReward = zenReward;
	}


	public int getExpReward() {
		return expReward;
	}


	public void setExpReward(int expReward) {
		this.expReward = expReward;
	}


	public String getItemRewardString() {
		return itemRewardString;
	}


	public void setItemRewardString(String itemRewardString) {
		this.itemRewardString = itemRewardString;
	}


	public List<String> getItemReward() {
		return itemReward;
	}


	public void setItemReward(List<String> itemReward) {
		this.itemReward = itemReward;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public String getTask() {
		return task;
	}


	public void setTask(String task) {
		this.task = task;
	}


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}


	public String getGroupId() {
		return groupId;
	}


	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}


	public int getTaskType() {
		return taskType;
	}


	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}


	public Integer getChapter() {
		return chapter;
	}


	public void setChapter(Integer chapter) {
		this.chapter = chapter;
	}


	public String getStageIndexString() {
		return stageIndexString;
	}


	public void setStageIndexString(String stageIndexString) {
		this.stageIndexString = stageIndexString;
	}


	public String getGoTo() {
		return goTo;
	}


	public void setGoTo(String goTo) {
		this.goTo = goTo;
	}

}
