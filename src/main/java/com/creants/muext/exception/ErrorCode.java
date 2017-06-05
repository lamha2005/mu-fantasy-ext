package com.creants.muext.exception;

import com.creants.creants_2x.core.exception.IErrorCode;

/**
 * @author LamHM
 *
 */
public enum ErrorCode implements IErrorCode {
	NOT_ENOUGH_STAMINA("NOT_ENOUGH_STAMINA", 1), STAGE_NOT_FOUND("STAGE_NOT_FOUND", 2);
	private short id;
	private String msg;


	private ErrorCode(String msg, int id) {
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
