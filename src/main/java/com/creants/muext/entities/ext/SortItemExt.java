package com.creants.muext.entities.ext;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;

/**
 * @author LamHM
 *
 */
public class SortItemExt implements SerializableQAntType {
	private int index;
	private int no;
	private String name;
	private String icon;


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public int getNo() {
		return no;
	}


	public void setNo(int no) {
		this.no = no;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getIcon() {
		return icon;
	}


	public void setIcon(String icon) {
		this.icon = icon;
	}

}
