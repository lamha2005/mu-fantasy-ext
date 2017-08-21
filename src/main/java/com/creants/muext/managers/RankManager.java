package com.creants.muext.managers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.muext.dao.GameHeroRepository;
import com.creants.muext.entities.GameHero;

/**
 * @author LamHM
 *
 */
@Service
public class RankManager implements InitializingBean {
	@Autowired
	private GameHeroRepository gameHeroRepository;
	private List<GameHero> topLevelList;
	private static final Timer timer = new Timer();


	@Override
	public void afterPropertiesSet() throws Exception {
		topLevelList = new ArrayList<>();
		sortTopLevel();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 15);
		calendar.set(Calendar.MINUTE, 15);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				QAntTracer.error(RankManager.class, "Sort task");
				sortTopLevel();
			}
		}, calendar.getTime());

	}


	public List<GameHero> getTopLevelList() {
		return topLevelList;
	}


	private void sortTopLevel() {
		Page<GameHero> findAll = gameHeroRepository
				.findAll(new PageRequest(0, 10, new Sort(Direction.DESC, "level", "exp")));
		topLevelList = findAll.getContent();
	}
}
