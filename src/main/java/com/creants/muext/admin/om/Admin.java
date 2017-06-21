package com.creants.muext.admin.om;

import org.springframework.data.annotation.Id;

/**
 * @author LamHM
 *
 */
public class Admin {
	@Id
	private long id;
	private String avatar;
	private String fullName;
	private AdminRole role;
	private long createTime;


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getAvatar() {
		return avatar;
	}


	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}


	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	public AdminRole getRole() {
		return role;
	}


	public void setRole(AdminRole role) {
		this.role = role;
	}


	public long getCreateTime() {
		return createTime;
	}


	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
