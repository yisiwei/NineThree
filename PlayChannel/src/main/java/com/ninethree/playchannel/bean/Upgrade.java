package com.ninethree.playchannel.bean;

import java.io.Serializable;

public class Upgrade implements Serializable {

	private static final long serialVersionUID = -4446312040642553997L;

	private int code;
	private boolean isForce;
	private String content;
	private String url;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isForce() {
		return isForce;
	}

	public void setForce(boolean isForce) {
		this.isForce = isForce;
	}

	@Override
	public String toString() {
		return "Upgrade [code=" + code + ", isForce=" + isForce + ", content="
				+ content + ", url=" + url + "]";
	}

}
