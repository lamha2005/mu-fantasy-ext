package com.creants.muext.entities.shop;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;
import com.creants.muext.config.ItemConfig;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
@JsonIgnoreProperties({ "itemString", "priceItemIndex", "priceItemNo" })
public class ShopPackage implements SerializableQAntType {
	@JacksonXmlProperty(localName = "Index", isAttribute = true)
	public int index;
	@JacksonXmlProperty(localName = "Items", isAttribute = true)
	private String itemString;
	@JacksonXmlProperty(localName = "Price", isAttribute = true)
	public String price;

	public List<String> items;
	private int priceItemIndex;
	private int priceItemNo;


	public void init() {
		items = ItemConfig.getInstance().splitItemString(itemString);
		String[] split = StringUtils.split(price, "/");
		priceItemIndex = Integer.parseInt(split[0]);
		priceItemNo = Integer.parseInt(split[1]);

	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public String getPrice() {
		return price;
	}


	public void setPrice(String price) {
		this.price = price;
	}


	public String getItemString() {
		return itemString;
	}


	public void setItemString(String itemString) {
		this.itemString = itemString;
	}


	public List<String> getItems() {
		return items;
	}


	public void setItems(List<String> items) {
		this.items = items;
	}


	public int getPriceItemIndex() {
		return priceItemIndex;
	}


	public void setPriceItemIndex(int priceItemIndex) {
		this.priceItemIndex = priceItemIndex;
	}


	public int getPriceItemNo() {
		return priceItemNo;
	}


	public void setPriceItemNo(int priceItemNo) {
		this.priceItemNo = priceItemNo;
	}

}
