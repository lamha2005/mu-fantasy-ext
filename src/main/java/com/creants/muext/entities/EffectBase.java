package com.creants.muext.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
@JsonInclude(Include.NON_NULL)
public class EffectBase {
	@JacksonXmlProperty(localName = "Index", isAttribute = true)
	private int index;
	@JacksonXmlProperty(localName = "Name", isAttribute = true)
	private String name;
	@JacksonXmlProperty(localName = "Description", isAttribute = true)
	private String desc;
	@JacksonXmlProperty(localName = "ValueType", isAttribute = true)
	private String valueType;
	@JacksonXmlProperty(localName = "EffectType", isAttribute = true)
	private String effectType;


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


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public String getValueType() {
		return valueType;
	}


	public void setValueType(String valueType) {
		this.valueType = valueType;
	}


	public String getEffectType() {
		return effectType;
	}


	public void setEffectType(String effectType) {
		this.effectType = effectType;
	}

}
