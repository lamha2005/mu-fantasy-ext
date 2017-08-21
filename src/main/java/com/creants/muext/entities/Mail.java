package com.creants.muext.entities;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;
import com.creants.muext.config.ItemConfig;
import com.creants.muext.entities.ext.MailActionExt;
import com.creants.muext.entities.ext.SortItemExt;

/**
 * @author LamHM
 *
 */

public class Mail implements SerializableQAntType {
	public static final String TYPE_NOTIFICATION = "noti";
	public static final String TYPE_GIFT = "gift";
	@Id
	private long id;
	@Indexed
	private String gameHeroId;
	private long createTime;
	private String type;
	private boolean read;
	private Boolean received;
	private String title;
	private String sender;
	private String content;
	private long expireAfterSeconds;
	private Map<String, Object> properties;
	private String giftString;
	private Long zen;
	private List<SortItemExt> items;
	private List<MailActionExt> actions;


	public Mail() {
	}


	public Mail(String gameHeroId, MailBase mailBase) {
		this.gameHeroId = gameHeroId;
		this.createTime = System.currentTimeMillis();
		this.type = mailBase.getType();
		this.title = mailBase.getTitle();
		this.sender = mailBase.getSender();
		this.content = mailBase.getContent();
		this.expireAfterSeconds = mailBase.getExpireAfterSeconds();
		this.giftString = mailBase.getGiftString();
		this.zen = mailBase.getZen();

		items = ItemConfig.getInstance().splitItem(this.giftString);
		actions = mailBase.getActions();
	}


	public boolean isRead() {
		return read;
	}


	public void setRead(boolean read) {
		this.read = read;
	}


	public Boolean getReceived() {
		return received;
	}


	public void setReceived(Boolean received) {
		this.received = received;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getGameHeroId() {
		return gameHeroId;
	}


	public void setGameHeroId(String gameHeroId) {
		this.gameHeroId = gameHeroId;
	}


	public long getCreateTime() {
		return createTime;
	}


	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}


	public Map<String, Object> getProperties() {
		return properties;
	}


	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getSender() {
		return sender;
	}


	public void setSender(String sender) {
		this.sender = sender;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public long getExpireAfterSeconds() {
		return expireAfterSeconds;
	}


	public void setExpireAfterSeconds(long expireAfterSeconds) {
		this.expireAfterSeconds = expireAfterSeconds;
	}


	public String getGiftString() {
		return giftString;
	}


	public void setGiftString(String giftString) {
		this.giftString = giftString;
	}


	public Long getZen() {
		return zen;
	}


	public void setZen(Long zen) {
		this.zen = zen;
	}


	public List<SortItemExt> getItems() {
		return items;
	}


	public void setItems(List<SortItemExt> items) {
		this.items = items;
	}


	public List<MailActionExt> getActions() {
		return actions;
	}


	public String getExpiredDate() {
		return DateFormatUtils.format(createTime + expireAfterSeconds * 1000, "dd/MM/yyyy HH:mm");
	}

}
