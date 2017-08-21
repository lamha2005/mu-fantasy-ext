package com.creants.muext.om;

import java.util.ArrayList;
import java.util.List;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;

/**
 * @author LamHM
 *
 */
public class ItemPackageInfo implements SerializableQAntType {
	public int index;
	private transient String itemListString;
	public List<ItemInfo> items;


	public ItemPackageInfo() {
		items = new ArrayList<>();
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public String getItemListString() {
		return itemListString;
	}


	public void setItemListString(String itemListString) {
		this.itemListString = itemListString;
	}


	public List<ItemInfo> getItems() {
		return items;
	}


	public void setItems(List<ItemInfo> items) {
		this.items = items;
	}


	public void addItem(ItemInfo item) {
		items.add(item);
	}

}
