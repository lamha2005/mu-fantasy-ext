package com.creants.muext.om;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;

/**
 * @author LamHM
 *
 */
public class ItemInfo implements SerializableQAntType {
	public int groupId;
	public int index;
	public int no;


	public ItemInfo(int groupId, int index, int no) {
		this.groupId = groupId;
		this.index = index;
		this.no = no;
	}


	public int getGroupId() {
		return groupId;
	}


	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}


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

}
