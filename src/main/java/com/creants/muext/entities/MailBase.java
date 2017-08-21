package com.creants.muext.entities;

import java.util.ArrayList;
import java.util.List;

import com.creants.muext.entities.ext.MailActionExt;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
@JsonInclude(Include.NON_NULL)
public class MailBase {
	@JacksonXmlProperty(localName = "Index", isAttribute = true)
	private int index;
	@JacksonXmlProperty(localName = "Sender", isAttribute = true)
	private String sender;
	@JacksonXmlProperty(localName = "Type", isAttribute = true)
	private String type;
	@JacksonXmlProperty(localName = "Title", isAttribute = true)
	private String title;
	@JacksonXmlProperty(localName = "Content", isAttribute = true)
	private String content;
	@JacksonXmlProperty(localName = "ExpireAfterSeconds", isAttribute = true)
	private long expireAfterSeconds;
	@JacksonXmlProperty(localName = "Gift", isAttribute = true)
	private String giftString;
	@JacksonXmlProperty(localName = "Zen", isAttribute = true)
	private long zen;


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
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


	public long getZen() {
		return zen;
	}


	public void setZen(long zen) {
		this.zen = zen;
	}


	public List<MailActionExt> getActions() {
		List<MailActionExt> actions = new ArrayList<>();
		if (type.equals(Mail.TYPE_GIFT)) {
			// TODO move to enum
			actions.add(new MailActionExt(MailActionExt.CLAIM, "Claim"));
		}

		return actions;
	}

}
