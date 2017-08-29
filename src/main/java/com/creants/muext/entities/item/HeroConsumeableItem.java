package com.creants.muext.entities.item;

import org.springframework.data.annotation.Transient;

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


	public void setItemBase(ConsumeableItemBase itemBase) {
		this.itemBase = itemBase;
	}

}
