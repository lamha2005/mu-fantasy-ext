package com.creants.muext.services;

import com.creants.muext.entities.ErrorCode;
import com.creants.muext.entities.Message;

/**
 * @author LamHa
 *
 */
public class MessageFactory {

	public static Message createErrorMessage(int errorCode, String message) {
		Message msg = new Message();
		msg.setCode(errorCode);
		msg.setMsg(message);
		return msg;
	}


	public static Message createErrorMessage(ErrorCode error) {
		Message msg = new Message();
		msg.setCode(error.getId());
		msg.setMsg(error.getMessage());
		return msg;
	}


	public static Message createMessage(Object data) {
		Message msg = new Message();
		msg.setCode(1);
		msg.setMsg("success");
		if (data != null) {
			msg.setData(data);
		}
		return msg;
	}
}
