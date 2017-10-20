package com.creants.muext.services;

import java.util.Collection;
import java.util.Map;

import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.muext.controllers.ExtensionEvent;
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


	public static IQAntObject buildNotification(String group, String type) {
		IQAntObject response = QAntObject.newInstance();
		response.putUtfString("group", group);
		response.putUtfString("type", type);
		return response;
	}


	public static IQAntObject buildNotificationCountQuest(Map<String, Integer> countMap) {
		IQAntObject response = QAntObject.newInstance();
		response.putUtfString("group", ExtensionEvent.NTF_GROUP_QUEST);
		response.putUtfString("type", ExtensionEvent.NTF_TYPE_COUNT);
		IQAntArray questArr = QAntArray.newInstance();
		IQAntObject quest = QAntObject.newInstance();

		for (String groupId : countMap.keySet()) {
			quest.putUtfString("group", groupId);
			quest.putInt("no", countMap.get(groupId));
			questArr.addQAntObject(quest);
			response.putQAntArray("quests", questArr);
		}
		return response;
	}


	public static IQAntObject buildNotiFinishQuest(Collection<Long> ids) {
		IQAntObject response = QAntObject.newInstance();
		response.putUtfString("group", ExtensionEvent.NTF_GROUP_QUEST);
		response.putUtfString("type", ExtensionEvent.NTF_TYPE_FINISH);
		response.putLongArray("ids", ids);
		return response;
	}

}
