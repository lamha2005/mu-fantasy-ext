package com.creants.muext.config;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "server-config")
public class ServerConfig {
	private boolean isFirstDeploy;
	private int npcXmlRevision;


	public boolean isFirstDeploy() {
		return isFirstDeploy;
	}


	public void setFirstDeploy(boolean isFirstDeploy) {
		this.isFirstDeploy = isFirstDeploy;
	}


	public int getNpcXmlRevision() {
		return npcXmlRevision;
	}


	public void setNpcXmlRevision(int npcXmlRevision) {
		this.npcXmlRevision = npcXmlRevision;
	}

}
