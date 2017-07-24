package com.creants.muext.entities.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
@JsonInclude(Include.NON_NULL)
public class BossEvent {
	@JacksonXmlProperty(localName = "Index", isAttribute = true)
	private int index;
	@JacksonXmlProperty(localName = "MonsterIndex", isAttribute = true)
	private int monsterIndex;
	@JacksonXmlProperty(localName = "EventName", isAttribute = true)
	private String eventName;
	@JacksonXmlProperty(localName = "NotificationTime", isAttribute = true)
	private transient String notificationTime;
	@JacksonXmlProperty(localName = "EventTime", isAttribute = true)
	private transient String eventTime;
	@JacksonXmlProperty(localName = "BossEventRewardIndex", isAttribute = true)
	private int bossEventRewardIndex;
	@JacksonXmlProperty(localName = "Description", isAttribute = true)
	private String desc;
	@JacksonXmlProperty(localName = "Image", isAttribute = true)
	private String image;


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public int getMonsterIndex() {
		return monsterIndex;
	}


	public void setMonsterIndex(int monsterIndex) {
		this.monsterIndex = monsterIndex;
	}


	public String getEventName() {
		return eventName;
	}


	public void setEventName(String eventName) {
		this.eventName = eventName;
	}


	public String getNotificationTime() {
		return notificationTime;
	}


	public void setNotificationTime(String notificationTime) {
		this.notificationTime = notificationTime;
	}


	public String getEventTime() {
		return eventTime;
	}


	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}


	public int getBossEventRewardIndex() {
		return bossEventRewardIndex;
	}


	public void setBossEventRewardIndex(int bossEventRewardIndex) {
		this.bossEventRewardIndex = bossEventRewardIndex;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}


	@Override
	public String toString() {
		return "{ eventName:" + eventName + ", monsterIndex:" + monsterIndex + ", eventTime: " + eventTime + "}";
	}

}
