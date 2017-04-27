package com.creants.muext.controllers;

import com.creants.creants_2x.core.IQAntEvent;
import com.creants.creants_2x.core.exception.QAntException;
import com.creants.creants_2x.core.extension.BaseServerEventHandler;

/**
 * @author LamHM
 *
 */
public class LoginEventHandler extends BaseServerEventHandler {

	@Override
	public void handleServerEvent(IQAntEvent event) throws QAntException {
		System.out.println("Fire event login");
	}

}
