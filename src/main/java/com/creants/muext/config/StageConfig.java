package com.creants.muext.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import com.creants.muext.entities.world.Chapter;
import com.creants.muext.entities.world.Stage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author LamHM
 *
 */
public class StageConfig {
	private static final String STAGE_CONFIG = "resources/stages.xml";
	private static final XMLInputFactory f = XMLInputFactory.newFactory();
	private static StageConfig instance;
	private Map<Integer, Stage> stages;
	private Map<Integer, Chapter> chapters;
	private Stage firstStage;


	public static StageConfig getInstance() {
		if (instance == null) {
			instance = new StageConfig();
		}

		return instance;
	}


	private StageConfig() {
		loadStage();
	}


	public void loadStage() {
		try {
			stages = new HashMap<>();
			chapters = new HashMap<>();
			XMLStreamReader sr = f.createXMLStreamReader(new FileInputStream(STAGE_CONFIG));
			XmlMapper mapper = new XmlMapper();
			sr.next(); // to point to <Stages>
			sr.next();
			Stage stage = null;
			int prevIndex = -1;
			while (sr.hasNext()) {
				try {
					stage = mapper.readValue(sr, Stage.class);
					stage.init();
					if (firstStage == null) {
						firstStage = stage;
					}

					int index = stage.getIndex();
					stages.put(index, stage);
					Stage prevStage = stages.get(prevIndex);
					if (prevStage != null) {
						prevStage.setNextStage(index);
					}
					prevIndex = index;

					Chapter chapter = new Chapter();
					chapter.setIndex(stage.getChapterIndex());
					chapter.setName(stage.getChapterName());
					chapters.put(stage.getChapterIndex(), chapter);
				} catch (NoSuchElementException e) {
				}
			}

			sr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public Stage getStage(int index) {
		return stages.get(index);
	}


	public Stage getNextStage(int index) {
		Stage stage = stages.get(index);
		return stages.get(stage.getNextStage());
	}


	public Stage getFirstStage() {
		return firstStage;
	}


	public List<Stage> getStages() {
		return new ArrayList<>(stages.values());
	}


	public void writeToJsonFile() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(new File("export/stages.json"), getStages());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		StageConfig instance2 = StageConfig.getInstance();
		instance2.writeToJsonFile();
	}

}
