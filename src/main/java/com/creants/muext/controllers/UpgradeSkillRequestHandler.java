package com.creants.muext.controllers;

import java.util.List;

import com.creants.creants_2x.core.extension.BaseClientRequestHandler;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.Creants2XApplication;
import com.creants.muext.config.SkillConfig;
import com.creants.muext.dao.GameHeroRepository;
import com.creants.muext.entities.GameHero;
import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.skill.Skill;
import com.creants.muext.managers.HeroClassManager;

/**
 * @author LamHa Tham khảo trả về response thế nào SFSResponseApi
 */
public class UpgradeSkillRequestHandler extends BaseClientRequestHandler {

	private GameHeroRepository repository;
	private HeroClassManager heroManager;


	public UpgradeSkillRequestHandler() {
		heroManager = Creants2XApplication.getBean(HeroClassManager.class);
		repository = Creants2XApplication.getBean(GameHeroRepository.class);
	}


	@Override
	public void handleClientRequest(QAntUser user, IQAntObject params) {
		Long heroId = params.getLong("id");
		Integer skillIndex = params.getInt("sid");
		if (heroId == null || skillIndex == null || !SkillConfig.getInstance().containSkill(skillIndex)) {
			// TODO response error
			return;
		}

		HeroClass heroClass = heroManager.findHeroById(heroId);
		if (heroClass == null || heroClass.getSkillPoint() <= 0) {
			// TODO error
			return;
		}

		Skill result = null;
		List<Skill> skillList = heroClass.getSkillList();
		for (Skill skill : skillList) {
			if (skill.getIndex() == skillIndex) {
				result = skill;
				break;
			}
		}

		if (result == null) {
			// TODO error
			return;
		}

		result.upgradeLevel(1);
		GameHero gameHero = repository.findOne(user.getName());
		long cost = SkillConfig.getInstance().getCost(result.getIndex(), result.getLevel());
		if (gameHero.getZen() < cost) {
			// TODO error
			return;
		}

		gameHero.incrZen(-cost);
		heroManager.save(heroClass);
		repository.save(gameHero);

		params = QAntObject.newInstance();
		params.putBool("rs", true);
		send("cmd_upgrade_skill", params, user);

	}

}
