package com.creants.muext.entities.item;

import org.springframework.data.annotation.Transient;

import com.creants.muext.entities.ItemBase;

/**
 * @author LamHM
 *
 */
public class HeroConsumeableItem extends HeroItem {
	private transient boolean isOverlap;
	@Transient
	private ConsumeableItemBase itemBase;


	public HeroConsumeableItem() {
	}


	public void useItem(int value) {
		if (!isOverlap)
			return;

		decr(value);
	}


	public void incr(int value) {
		this.no += value;
	}


	public void decr(int value) {
		no -= value;
		if (no < 0) {
			no = 0;
		}
	}


	public boolean isOverlap() {
		return isOverlap;
	}


	public void setOverlap(boolean isOverlap) {
		this.isOverlap = isOverlap;
	}


	public ConsumeableItemBase getItemBase() {
		return itemBase;
	}


	@Override
	public void setItemBase(ItemBase itemBase) {
		this.itemBase = (ConsumeableItemBase) itemBase;
	}


	@Override
	public boolean equals(Object obj) {
		HeroConsumeableItem heroItem = (HeroConsumeableItem) obj;
		return this.getItemGroup() == heroItem.getItemGroup() && this.index == heroItem.getIndex();
	}
}
