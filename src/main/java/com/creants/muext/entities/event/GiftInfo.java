package com.creants.muext.entities.event;

import java.util.ArrayList;
import java.util.List;

import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;

/**
 * @author LamHM
 *
 */
public class GiftInfo implements SerializableQAntType {
	public boolean isClaim;
	public int giftIndex;
	public long anchorTime;
	public long lastUpdateTime;
	public List<Integer> claimed;
	public List<Integer> claim;
	public String giftCode;


	public GiftInfo() {
		claimed = new ArrayList<>();
		claim = new ArrayList<>();
	}


	public GiftInfo(GiftEventBase eventBase) {
		giftIndex = eventBase.getIndex();
		claimed = new ArrayList<>();
		claim = new ArrayList<>();
	}


	public void reset() {
		claimed = new ArrayList<>();
		claim = new ArrayList<>();
		isClaim = false;
		anchorTime = System.currentTimeMillis();
	}


	private int getNextPackage() {
		if (claimed.size() == 0)
			return 0;

		return claimed.get(claimed.size() - 1) + 1;
	}


	public boolean claimNextPackage() {
		claim.clear();
		return claimPackage(getNextPackage());
	}


	private boolean claimPackage(int packageIndex) {
		if (claimed.contains(packageIndex)) {
			return false;
		}

		claimed.add(packageIndex);
		claim.add(packageIndex);
		return true;
	}


	public boolean isClaim() {
		return isClaim;
	}


	public void setClaim(boolean isClaim) {
		this.isClaim = isClaim;
	}


	public int getGiftIndex() {
		return giftIndex;
	}


	public void setGiftIndex(int giftIndex) {
		this.giftIndex = giftIndex;
	}


	public long getAnchorTime() {
		return anchorTime;
	}


	public void setAnchorTime(long anchorTime) {
		this.anchorTime = anchorTime;
	}


	public long getLastUpdateTime() {
		return lastUpdateTime;
	}


	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}


	public List<Integer> getClaimed() {
		return claimed;
	}


	public void setClaimed(List<Integer> claimed) {
		this.claimed = claimed;
	}


	public List<Integer> getClaim() {
		return claim;
	}


	public void setClaim(List<Integer> claim) {
		this.claim = claim;
	}


	public String getGiftCode() {
		return giftCode;
	}


	public void setGiftCode(String giftCode) {
		this.giftCode = giftCode;
	}


	public IQAntObject convertToQAntObject() {
		// TODO check thuoc category nao de them cac tham so thich hop
		IQAntObject result = QAntObject.newInstance();
		result.putBool("isClaim", isClaim);
		result.putInt("giftIndex", giftIndex);
		result.putLong("lastUpdateTime", lastUpdateTime);
		result.putIntArray("claimed", claimed);
		result.putIntArray("claim", claim);
		return result;
	}

}
