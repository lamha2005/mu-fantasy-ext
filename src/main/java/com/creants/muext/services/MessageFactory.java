package com.creants.muext.services;

import java.util.Map;

import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.muext.exception.GameErrorCode;

/**
 * @author LamHM
 *
 */
public class MessageFactory {

	public static IQAntObject createErrorMsg(String errorCmd, GameErrorCode errorCode) {
		IQAntObject params = QAntObject.newInstance();
		params.putShort("ec", errorCode.getId());
		params.putUtfString("msg", errorCode.getMsg());
		params.putUtfString("cmd", errorCmd);
		return params;
	}


	public static IQAntObject buildAssetsChange(Map<String, Object> assetMap) {
		IQAntObject assets = QAntObject.newInstance();

		for (String key : assetMap.keySet()) {
			Object value = assetMap.get(key);
			if (value instanceof Integer) {
				assets.putInt(key, (Integer) value);
			} else if (value instanceof Long) {
				assets.putLong(key, (Long) value);
			} else if (value instanceof String) {
				assets.putUtfString(key, (String) value);
			}
		}
		return assets;
	}
}
