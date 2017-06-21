package com.creants.muext.admin.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.creants.creants_2x.QAntServer;
import com.creants.creants_2x.core.api.IQAntApi;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.QAntUser;
import com.creants.muext.admin.dao.AdminRepository;
import com.creants.muext.admin.om.Admin;

@Service
public class AdminManager implements InitializingBean {
	private static final AtomicInteger CCU = new AtomicInteger(0);
	private Map<Long, QAntUser> admins;
	private IQAntApi qantApi;

	@Autowired
	private AdminRepository adminRepository;


	@Override
	public void afterPropertiesSet() throws Exception {
		admins = new HashMap<>();

	}


	public void initQAntApi() {
		qantApi = QAntServer.getInstance().getAPIManager().getQAntApi();
	}


	public void registerAdmin(Admin admin) {
		adminRepository.save(admin);
	}


	public boolean containUser(long creantsUserId) {
		return adminRepository.findOne(creantsUserId) != null;
	}


	public void login(QAntUser admin) {
		admins.put(admin.getCreantsUserId(), admin);
		notifyCCU(admin);
	}


	public void incrAndNotifyCCU() {
		qantApi.sendExtensionResponse("cmd_ccu", getAntIncrementCCU(), getAdminList(), null);
	}


	public void decrAndNotifyCCU() {
		qantApi.sendExtensionResponse("cmd_ccu", getAntDecrementCCU(), getAdminList(), null);
	}


	public void notifyCCU(QAntUser user) {
		qantApi.sendExtensionResponse("cmd_ccu", getCCU(), user, null);
	}


	public void logout(long creantsUserId) {
		admins.remove(creantsUserId);
	}


	public List<QAntUser> getAdminList() {
		return new ArrayList<>(admins.values());
	}


	private void incrCCU() {
		synchronized (CCU) {
			CCU.incrementAndGet();
		}
	}


	private void decrCCU() {
		synchronized (CCU) {
			CCU.decrementAndGet();
		}
	}


	private IQAntObject getAntIncrementCCU() {
		incrCCU();
		IQAntObject params = QAntObject.newInstance();
		params.putInt("ccu", CCU.get());
		return params;
	}


	private IQAntObject getCCU() {
		IQAntObject params = QAntObject.newInstance();
		params.putInt("ccu", CCU.get());
		return params;
	}


	private IQAntObject getAntDecrementCCU() {
		decrCCU();
		IQAntObject params = QAntObject.newInstance();
		params.putInt("ccu", CCU.get());
		return params;
	}

}
