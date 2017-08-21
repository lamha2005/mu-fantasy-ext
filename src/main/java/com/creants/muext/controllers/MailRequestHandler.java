package com.creants.muext.controllers;

import java.util.List;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.config.ItemConfig;
import com.creants.muext.dao.MailRepository;
import com.creants.muext.entities.Mail;
import com.creants.muext.entities.ext.MailActionExt;
import com.creants.muext.entities.item.HeroItem;
import com.creants.muext.exception.GameErrorCode;
import com.creants.muext.managers.HeroItemManager;
import com.creants.muext.services.MessageFactory;

/**
 * @author LamHM
 *
 */
public class MailRequestHandler extends BaseClientRequestHandler {
	private static final String CMD = "cmd_mail";
	private static final int READ = 1;
	private static final int MAIL_ACTION = 2;
	private static final int CLAIM_ALL = 3;
	private static final int REMOVE_ALL = 4;
	private static final int GET_SHORT_LIST = 5;

	private MailRepository mailRepository;
	private HeroItemManager itemManager;


	public MailRequestHandler() {
		mailRepository = Creants2XApplication.getBean(MailRepository.class);
		itemManager = Creants2XApplication.getBean(HeroItemManager.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		Integer action = params.getInt("act");
		if (action == null) {
			action = GET_SHORT_LIST;
		}

		switch (action) {
			case READ:
				processReadMail(user, params);
				break;
			case MAIL_ACTION:
				processClaim(user, params);
				break;
			case REMOVE_ALL:
				removeAll(user, params);
				break;
			case CLAIM_ALL:
				claimAll(user, params);
				break;

			default:
				getShortListInfo(user, params);
				break;
		}

	}


	private void claimAll(QAntUser user, IQAntObject params) {
		String gameHeroId = user.getName();
		List<Mail> mails = mailRepository.findByGameHeroIdAndReceivedIsFalse(gameHeroId);
		if (mails.size() > 0) {
			String giftString = "";
			for (Mail mail : mails) {
				giftString += mail.getGiftString() + ItemConfig.SEPERATE_OTHER_ITEM;
			}
			giftString = giftString.substring(0, giftString.length() - 1);

			List<HeroItem> addItems = itemManager.addItems(gameHeroId, giftString);
			IQAntArray itemArr = QAntArray.newInstance();
			for (HeroItem heroItem : addItems) {
				itemArr.addQAntObject(QAntObject.newFromObject(heroItem));
			}

			params.putQAntArray("items", itemArr);
		}

		mailRepository.delete(mails);
		send(CMD, params, user);
	}


	private void removeAll(QAntUser user, IQAntObject params) {
		mailRepository.deleteAll();
		send(CMD, params, user);
	}


	private void processClaim(QAntUser user, IQAntObject params) {
		long id = params.getLong("id");
		Mail mail = mailRepository.findOne(id);
		if (mail == null || !mail.getGameHeroId().equals(user.getName())) {
			// TODO send error
			return;
		}

		String action = params.getUtfString("act");
		if (action.equals(MailActionExt.CLAIM)) {
			List<HeroItem> addItems = itemManager.addItems(user.getName(), mail.getGiftString());
			IQAntArray itemArr = QAntArray.newInstance();
			for (HeroItem heroItem : addItems) {
				itemArr.addQAntObject(QAntObject.newFromObject(heroItem));
			}

			params.putQAntArray("items", itemArr);
		}

		params.putInt("code", 1);
		mailRepository.delete(mail);
		send(CMD, params, user);
	}


	private void processReadMail(QAntUser user, IQAntObject params) {
		long id = params.getLong("id");
		Mail mail = mailRepository.findOne(id);
		if (mail == null || !mail.getGameHeroId().equals(user.getName())) {
			QAntTracer.warn(this.getClass(), "Read mail not exist: " + user.getName() + "/mailId:" + id);
			sendError(MessageFactory.createErrorMsg(CMD, GameErrorCode.MAIL_NOT_FOUND), user);
			return;
		}

		params.putQAntObject("detail", QAntObject.newFromObject(mail));
		mail.setRead(true);
		mailRepository.save(mail);

		send(CMD, params, user);
	}


	private void getShortListInfo(QAntUser user, IQAntObject params) {
		List<Mail> mails = mailRepository.findByGameHeroIdAndReadIsFalseAndReceivedIsFalse(user.getName());
		IQAntArray mailArr = QAntArray.newInstance();
		if (mails.size() > 0) {
			IQAntObject mailObj = null;
			boolean isFirst = true;
			for (Mail mail : mails) {
				mailObj = QAntObject.newInstance();
				mailObj.putUtfString("title", mail.getTitle());
				mailObj.putBool("read", mail.isRead());
				mailObj.putUtfString("expiredDate", mail.getExpiredDate());
				if (isFirst) {
					isFirst = false;
					mailObj.putQAntObject("detail", QAntObject.newFromObject(mail));
				}
				mailArr.addQAntObject(mailObj);
			}

		}

		params.putQAntArray("mails", mailArr);
		send(CMD, params, user);
	}

}
