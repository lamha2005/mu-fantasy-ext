package com.creants.muext.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
@JsonInclude(Include.NON_NULL)
public class Item {
	@JacksonXmlProperty(localName = "ItemIndex", isAttribute = true)
	private int index;
	@JacksonXmlProperty(localName = "ItemGroup", isAttribute = true)
	private int groupId;
	@JacksonXmlProperty(localName = "ItemName", isAttribute = true)
	private String name;

	@JacksonXmlProperty(localName = "Imagefile", isAttribute = true)
	private String img;
	private Integer gridIndex;
	private Integer no;


	public int getIndex() {
		return index;
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


	public Integer getGridIndex() {
		return gridIndex;
	}


	public void setGridIndex(Integer gridIndex) {
		this.gridIndex = gridIndex;
	}


	public Integer getNo() {
		return no;
	}


	public void setNo(Integer no) {
		this.no = no;
	}

}
