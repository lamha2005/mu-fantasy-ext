package com.creants.muext.services;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.creants.muext.config.StageConfig;
import com.creants.muext.dao.SequenceRepository;

@Service
public class AutoIncrementService implements InitializingBean {
	private static final String HERO_STAGE_ID = "hero_stage_id";
	public static final String HERO_ID = "hero_id";
	public static final String QUEST_ID = "quest_id";
	public static final String ITEM_ID = "item_id";

	@Value("${firstDeploy}")
	private boolean firstDeploy;

	@Autowired
	private SequenceRepository sequenceRepository;


	@Override
	public void afterPropertiesSet() throws Exception {
		if (firstDeploy) {
			sequenceRepository.createSequenceDocument(HERO_ID);
			sequenceRepository.createSequenceDocument(HERO_STAGE_ID);
			sequenceRepository.createSequenceDocument(QUEST_ID);
			sequenceRepository.createSequenceDocument(ITEM_ID);

			sequenceRepository.setDefaultValue(HERO_ID, 1000);
		}
		
		StageConfig.getInstance();
	}


	public long genHeroStageId() {
		return sequenceRepository.getNextSequenceId(HERO_STAGE_ID);
	}


	public long genHeroId() {
		return sequenceRepository.getNextSequenceId(HERO_ID);
	}


	public long genQuestId() {
		return sequenceRepository.getNextSequenceId(QUEST_ID);
	}


	public long genItemId() {
		return sequenceRepository.getNextSequenceId(ITEM_ID);
	}

}
