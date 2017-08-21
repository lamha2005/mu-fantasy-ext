package com.creants.muext.entities.ext;

import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;

/**
 * @author LamHM
 *
 */
public class MailActionExt implements SerializableQAntType {
	public static final String CLAIM = "claim";
	private String id;
	private String name;


	public MailActionExt() {

	}


	public MailActionExt(String id, String name) {
		this.id = id;
		this.name = name;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

}
