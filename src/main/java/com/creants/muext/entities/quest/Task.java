package com.creants.muext.entities.quest;

import java.util.Map;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author LamHM
 *
 */
@JsonInclude(Include.NON_NULL)
public class Task implements SerializableQAntType{
	private String name;
	private String desc;
	public Map<Object, Object> properties;


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


	public Map<Object, Object> getProperties() {
		return properties;
	}


	public void setProperties(Map<Object, Object> properties) {
		this.properties = properties;
	}

}
