package com.creants.muext.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author LamHM
 *
 */
@JsonInclude(Include.NON_NULL)
public class Item {
	private int index;
	private int groupId;
	private String name;
	private String img;
	private Integer no;
	private Integer gridIndex;


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public Integer getNo() {
		return no;
	}


	public void setNo(Integer no) {
		this.no = no;
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

}
