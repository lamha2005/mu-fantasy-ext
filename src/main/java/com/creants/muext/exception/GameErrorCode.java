package com.creants.muext.exception;

import com.creants.creants_2x.core.exception.IErrorCode;

/**
 * @author LamHM
 *
 */
public enum GameErrorCode implements IErrorCode {
	NOT_ENOUGH_STAMINA("NOT_ENOUGH_STAMINA", 1),
	NOT_ENOUGH_ZEN("NOT_ENOUGH_ZEN", 2),
	NOT_ENOUGH_BLESS("NOT_ENOUGH_BLESS", 3),
	STAGE_NOT_FOUND("STAGE_NOT_FOUND", 20),
	EVENT_NOT_OPEN_YET("BOSS_EVENT_NOT_OPEN_YET", 30),
	CAN_NOT_FIND_HERO("CAN_NOT_FIND_HERO", 40),
	FEE_NULL("FEE_NULL", 50),
	MATERIAL_CAN_NOT_USE("MATERIAL_CAN_NOT_USE", 60),
	LACK_OF_INFOMATION("LACK_OF_INFOMATION", 70),
	ITEM_IS_USING("ITEM_IS_USING", 80),
	NOT_EXIST_ITEM("NOT_EXIST_ITEM", 90),
	CAN_NOT_UPGRADE_ITEM("CAN_NOT_UPGRADE_ITEM", 100),
	CAN_NOT_UPGRADE_HERO("CAN_NOT_UPGRADE_HERO", 101),
	MAIL_NOT_FOUND("MAIL_NOT_FOUND", 120);
	private short id;
	private String msg;


	private GameErrorCode(String msg, int id) {
		this.id = (short) id;
		this.msg = msg;
	}


	@Override
	public short getId() {
		return id;
	}


	public String getMsg() {
		return msg;
	}

}
