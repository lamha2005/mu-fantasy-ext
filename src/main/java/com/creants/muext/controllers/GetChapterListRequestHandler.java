package com.creants.muext.controllers;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;

/**
 * @author LamHa Tham khảo trả về response thế nào SFSResponseApi
 */
public class GetChapterListRequestHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		// TODO lấy danh sách chapter. Gửi revision lên nếu cùng revision thì ko
		// trả về danh sách

	}

}
