package com.creants.muext.om;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LamHM
 *
 */
public class ItemPackageInfo {
	public int index;
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
