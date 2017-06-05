package com.creants.muext.services;

import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.muext.exception.ErrorCode;

/**
 * @author LamHM
 *
 */
public class MessageFactory {

	public static IQAntObject createErrorMsg(String errorCmd, ErrorCode errorCode) {
		IQAntObject params = QAntObject.newInstance();
		params.putShort("ec", errorCode.getId());
		params.putUtfString("msg", errorCode.getMsg());
		params.putUtfString("cmd", errorCmd);
		return params;
	}
}
