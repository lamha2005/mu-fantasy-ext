package com.creants.muext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

import com.creants.creants_2x.core.QAntEventType;
import com.creants.creants_2x.core.controllers.AbstractController;
import com.creants.creants_2x.core.entities.Room;
import com.creants.creants_2x.core.extension.QAntExtension;
import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.creants_2x.socket.gate.entities.IQAntArray;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntArray;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.creants_2x.socket.gate.wood.RequestComparator;
import com.creants.creants_2x.socket.io.IRequest;
import com.creants.muext.config.MonsterConfig;
import com.creants.muext.controllers.event.HitBossEventRequestHandler;
import com.creants.muext.controllers.event.JoinRoomBossEventHandler;
import com.creants.muext.entities.Monster;
import com.creants.muext.entities.event.DAMInfo;
import com.creants.muext.util.UserHelper;

/**
 * @author LamHM
 *
 */
public class BossEventExtension extends QAntExtension implements Runnable {
	private Integer monsterHp;
	protected BlockingQueue<IRequest> hitQueue;
	private Map<String, QAntUser> userMap;
	private Map<String, DAMInfo> damMap;
	private List<QAntUser> topUsers;


	@Override
	public void init() {
		QAntTracer.debug(this.getClass(), "- BOSS EVENT");
		addEventRequestHandler();
		userMap = new HashMap<>();
		damMap = new ConcurrentHashMap<>();
		topUsers = new ArrayList<>();
		hitQueue = new PriorityBlockingQueue<IRequest>(50, new RequestComparator());

		Room parentRoom = getParentRoom();
		int monsterIndex = (int) parentRoom.getProperty("monsterIndex");
		Monster monster = MonsterConfig.getInstance().getMonster(monsterIndex);
		monsterHp = monster.getHp();
	}


	// TODO sau khi trả thưởng kết thúc sự kiện thực hiện gọi init đẻ reset lại
	public void reset() {
		init();
	}


	private void addEventRequestHandler() {
		addEventHandler(QAntEventType.USER_JOIN_ROOM, JoinRoomBossEventHandler.class);

		addRequestHandler("cmd_boss_event_hit", HitBossEventRequestHandler.class);
	}


	public void join(QAntUser user) {
		userMap.put(user.getName(), user);
		damMap.put(user.getName(), new DAMInfo());
	}


	public int hit(String gameHeroId, int dam) {
		synchronized (monsterHp) {
			monsterHp -= dam;
			DAMInfo damInfo = damMap.get(gameHeroId);
			if (damInfo != null) {
				damInfo.incDam(dam);
			}

			return monsterHp;
		}
	}


	private List<QAntUser> getTop() {
		ArrayList<DAMInfo> list = new ArrayList<>(damMap.values());
		Collections.sort(list);
		List<DAMInfo> newTop = list.subList(0, 10);
		boolean updateTop = false;
		for (int i = 0; i < newTop.size(); i++) {
			QAntUser qAntUser = topUsers.get(i);
			if (qAntUser == null || !qAntUser.getName().equals(newTop.get(i).getGameHeroId())) {
				updateTop = true;
				break;
			}
		}
		if (!updateTop)
			return null;

		List<QAntUser> users = new ArrayList<>();
		for (DAMInfo damInfo : newTop) {
			users.add(userMap.get(damInfo.getGameHeroId()));
		}

		topUsers = users;
		return topUsers;

	}


	public int getMonsterHp() {
		return monsterHp;
	}


	public void putMessage(IRequest message) {
		try {
			hitQueue.put(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public IQAntObject getData() {
		IQAntObject data = QAntObject.newInstance();
		data.putInt("monsterHp", monsterHp);
		data.putQAntArray("top", genTopData());
		return data;
	}


	private IQAntArray genTopData() {
		IQAntArray arr = QAntArray.newInstance();
		for (QAntUser user : topUsers) {
			IQAntObject obj = QAntObject.newInstance();
			obj.putUtfString("name", UserHelper.getGameHeroName(user));
			obj.putInt("dam", damMap.get(user.getName()).getPoint());
		}

		return arr;
	}


	@Override
	public void run() {
		try {
			hitQueue.take();
			List<QAntUser> top = getTop();
			if (top != null) {
				Room parentRoom = getParentRoom();
				List<QAntUser> userList = parentRoom.getUserList();

				IQAntObject response = QAntObject.newInstance();
				response.putQAntArray("top", genTopData());

				getApi().sendExtensionResponse("cmd_bot_event_top", response, userList, parentRoom);
			}
		} catch (InterruptedException e) {
			QAntTracer.warn(AbstractController.class, "BossEventExtension loop was interrupted");
		} catch (Throwable t) {
			t.printStackTrace();
			QAntTracer.error(AbstractController.class, "Runable fail!");
		}
	}

}
