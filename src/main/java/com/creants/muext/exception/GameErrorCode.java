package com.creants.muext.exception;

import com.creants.creants_2x.core.exception.IErrorCode;

/**
 * @author LamHM
 *
 */
public enum GameErrorCode implements IErrorCode {
	NOT_ENOUGH_STAMINA("NOT_ENOUGH_STAMINA", 1),
	STAGE_NOT_FOUND("STAGE_NOT_FOUND", 2),
	EVENT_NOT_OPEN_YET("BOSS_EVENT_NOT_OPEN_YET", 3),
	CAN_NOT_FIND_HERO("CAN_NOT_FIND_HERO", 4),
	FEE_NULL("FEE_NULL", 5),
	MATERIAL_CAN_NOT_USE("MATERIAL_CAN_NOT_USE", 6),
	LACK_OF_INFOMATION("LACK_OF_INFOMATION", 7),
	ITEM_IS_USING("ITEM_IS_USING", 8),
	NOT_EXIST_ITEM("NOT_EXIST_ITEM", 9),
	CAN_NOT_UPGRADE_ITEM("CAN_NOT_UPGRADE_ITEM", 10),
	CAN_NOT_UPGRADE_HERO("CAN_NOT_UPGRADE_HERO", 11),
	MAIL_NOT_FOUND("MAIL_NOT_FOUND", 11);
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
