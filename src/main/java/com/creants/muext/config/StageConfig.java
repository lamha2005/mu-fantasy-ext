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
			while (sr.hasNext()) {
				try {
					stage = mapper.readValue(sr, Stage.class);
					stage.init();
					stages.put(stage.getIndex(), stage);

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
		StageConfig.getInstance().writeToJsonFile();
	}

}
