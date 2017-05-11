package com.creants.muext.dao;

/**
 * @author LamHM
 *
 */
public interface SequenceRepository {
	long getNextSequenceId(String key);


	void createSequenceDocument(String documentName);
}
