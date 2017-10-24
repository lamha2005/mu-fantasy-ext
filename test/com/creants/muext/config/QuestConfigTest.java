package com.creants.muext.config;

import org.junit.Before;
import org.junit.Test;

import com.creants.muext.entities.quest.QuestBase;

/**
 * @author LamHM
 *
 */
public class QuestConfigTest {
	private QuestConfig questConfig;


	@Before
	public void init() {
		questConfig = QuestConfig.getInstance();
	}


	@Test
	public void getQuestTest() {
		QuestBase quest = questConfig.getQuest(17);
		System.out.println(quest.getItemRewardString());
	}
}
