package com.creants.muext.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.creants.muext.dao.SequenceRepository;

@Service
public class AutoIncrementService {
	private static final String HERO_STAGE_ID = "hero_stage_id";
	public static final String HERO_ID = "hero_id";
	public static final String QUEST_ID = "quest_id";

	@Autowired
	private SequenceRepository sequenceRepository;


	public long genHeroStageId() {
		return sequenceRepository.getNextSequenceId(HERO_STAGE_ID);
	}


	public long genHeroId() {
		return sequenceRepository.getNextSequenceId(HERO_ID);
	}


	public long genQuestId() {
		return sequenceRepository.getNextSequenceId(QUEST_ID);
	}
}
