package com.creants.muext.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
@JsonInclude(Include.NON_NULL)
public class ItemBase {
	@JacksonXmlProperty(localName = "ItemIndex", isAttribute = true)
	private int index;
	@JacksonXmlProperty(localName = "ItemGroup", isAttribute = true)
	private int groupId;
	@JacksonXmlProperty(localName = "ItemName", isAttribute = true)
	private String name;

	@JacksonXmlProperty(localName = "Imagefile", isAttribute = true)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String img;

	@JacksonXmlProperty(localName = "Icon", isAttribute = true)
	private String icon;

	private int type;


	public int getIndex() {
		return index;
	}


	public String getIcon() {
		return icon;
	}


	public void setIcon(String icon) {
		this.icon = icon;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getGroupId() {
		return groupId;
	}


	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}


	public String getImg() {
		return img;
	}


	public void setImg(String img) {
		this.img = img;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}

}
