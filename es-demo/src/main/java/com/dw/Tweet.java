package com.dw;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by F.C. on 28/03/2017.
 */
public class Tweet implements Serializable{
	private static final long serialVersionUID = 7279397633970274676L;


	private String id;

	private String user;
	private Date postDate;
	private String message;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getPostDate() {
		return postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("Tweet{");
		sb.append("id='").append(id).append('\'');
		sb.append(", user='").append(user).append('\'');
		sb.append(", postDate=").append(postDate);
		sb.append(", message='").append(message).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
