package com.example.weibotest08_31.item;

import java.io.Serializable;

public class Weibo implements Serializable {

	private String id;
	private String userId;
	private String creatTime;
	private long createTimelong;
	private String hasPraise;
	private String content;
	private String imageUrl;
	private int praiseNum;
	private int commentNum;
	private String headUrl;
	private String loginName;
	private String name;
	private String sex;

	public String getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	public long getCreateTimelong() {
		return createTimelong;
	}

	public void setCreateTimelong(long createTimelong) {
		this.createTimelong = createTimelong;
	}


	public String getHasPraise() {
		return hasPraise;
	}

	public void setHasPraise(String hasPraise) {
		this.hasPraise = hasPraise;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(int praiseNum) {
		this.praiseNum = praiseNum;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}
