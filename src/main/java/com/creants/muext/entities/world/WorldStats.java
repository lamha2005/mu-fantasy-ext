package com.creants.muext.entities.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LamHM
 *
 */
@Document(collection = "world_stats")
public class WorldStats {
	@Id
	private String heroId;
	/**
	 * StageId
	 */
	private Map<Integer, List<Mission>> stagesMap;
	/**
	 * ChapterId
	 */
	private Map<Integer, List<Stage>> chapterMap;


	public WorldStats() {
		stagesMap = new HashMap<>();
		chapterMap = new HashMap<>();
	}


	public String getHeroId() {
		return heroId;
	}


	public void setHeroId(String heroId) {
		this.heroId = heroId;
	}


	public Map<Integer, List<Mission>> getStagesMap() {
		return stagesMap;
	}


	public void setStagesMap(Map<Integer, List<Mission>> stagesMap) {
		this.stagesMap = stagesMap;
	}


	public Map<Integer, List<Stage>> getChapterMap() {
		return chapterMap;
	}


	public void setChapterMap(Map<Integer, List<Stage>> chapterMap) {
		this.chapterMap = chapterMap;
	}


	public List<Stage> getStates(int chapterId) {
		return chapterMap.get(chapterId);
	}


	public List<Mission> getMissions(int stageId) {
		return stagesMap.get(stageId);
	}


	public void clearMission(int stageId, Mission mission) {
		List<Mission> list = stagesMap.get(stageId);
		if (list == null) {
			list = new ArrayList<>();
		}

		list.add(mission);
	}


	public boolean isClearMission(int missionId) {
		boolean result = false;
		int stageId = getStageId(missionId);
		List<Mission> missions = stagesMap.get(stageId);
		for (Mission mission : missions) {
			if (mission.getIndex() == missionId)
				return true;
		}

		return result;
	}


	public int getStageId(int missionId) {
		return 1;
	}
}
