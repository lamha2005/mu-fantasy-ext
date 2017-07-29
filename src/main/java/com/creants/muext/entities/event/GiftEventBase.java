package com.creants.muext.entities.event;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.creants_2x.socket.gate.protocol.serialization.SerializableQAntType;
import com.creants.muext.config.ItemConfig;
import com.creants.muext.entities.ItemBase;
import com.creants.muext.om.ItemInfo;
import com.creants.muext.om.ItemPackageInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author LamHM
 *
 */
@JsonInclude(Include.NON_NULL)
public class GiftEventBase implements SerializableQAntType {
	@JacksonXmlProperty(localName = "Index", isAttribute = true)
	public int index;
	@JacksonXmlProperty(localName = "EventName", isAttribute = true)
	public String name;
	@JacksonXmlProperty(localName = "Description", isAttribute = true)
	public String desc;
	@JacksonXmlProperty(localName = "GiftList", isAttribute = true)
	public String giftStr;

	private transient List<ItemPackageInfo> itemPackages;


	public void init() {
		if (giftStr == null) {
			return;
		}

		try {
			itemPackages = new ArrayList<>();
			String[] packageArr = StringUtils.split(giftStr, "#");
			for (int i = 0; i < packageArr.length; i++) {
				ItemPackageInfo newPackage = new ItemPackageInfo();
				newPackage.setIndex(i + 1);

				String pk = packageArr[i];
				String[] itemArr = StringUtils.split(pk, ",");
				for (int j = 0; j < itemArr.length; j++) {
					String itemStr = itemArr[j];
					String[] item = StringUtils.split(itemStr, "/");
					int itemIndex = Integer.parseInt(item[0]);
					ItemBase itemBase = ItemConfig.getInstance().getItem(itemIndex);
					newPackage.addItem(new ItemInfo(itemBase.getGroupId(), itemIndex, Integer.parseInt(item[1])));
				}

				itemPackages.add(newPackage);
			}

		} catch (Exception e) {
			QAntTracer.error(GiftEventBase.class, "Gift event resource data fail! " + giftStr);
			throw new RuntimeException();
		}
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public String getGiftStr() {
		return giftStr;
	}


	public void setGiftStr(String giftStr) {
		this.giftStr = giftStr;
	}


	public List<ItemPackageInfo> getItemPackages() {
		return itemPackages;
	}

}
