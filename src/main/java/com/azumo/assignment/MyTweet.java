package com.azumo.assignment;

import java.util.Date;

public class MyTweet  {
	private String text;
	private Date createdAt;
	private String user;
	
	public MyTweet(String text, Date createdAt, String user) {
		this.text = text;
		this.createdAt = createdAt;
		this.user = user;
	}

	public String getText() {
		return text;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public String getUser() {
		return user;
	}
	

}
