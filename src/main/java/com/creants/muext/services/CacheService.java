package com.creants.muext.services;

/**
 * @author LamHM
 *
 */
public class CacheService {
	private static CacheService instance;


	public static CacheService getInstance() {
		if (instance == null) {
			instance = new CacheService();
		}
		return instance;
	}

}
