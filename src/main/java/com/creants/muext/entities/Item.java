package com.creants.muext.entities;

/**
 * @author LamHM
 *
 */
public class Item {
	private int id;
	private String name;
	private int groupId;
	private String img;
	private int no;


	public Item() {
		groupId = -1;
	}


	public int getId() {
		return id;
	}


	public int getNo() {
		return no;
	}


	public void setNo(int no) {
		this.no = no;
	}


	public void setId(int id) {
		this.id = id;
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

}
