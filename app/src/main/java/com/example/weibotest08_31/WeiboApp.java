package com.example.weibotest08_31;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.example.weibotest08_31.api.DotNetManager;
import com.example.weibotest08_31.item.User;

public class WeiboApp extends Application {
	private static Context context;
	private List<Activity> actyList=new ArrayList<Activity>();
	private DotNetManager dotNetManager;
	private User user;
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public DotNetManager getDotNetManager() {
		if (dotNetManager==null) {
			dotNetManager=new DotNetManager();
		}
		return dotNetManager;
	}

	public void addActy(Activity acty){
		actyList.add(acty);
	}
	
	public void exit(){
		for (int i = 0; i < actyList.size(); i++) {
			Activity acty=actyList.get(i);
			acty.finish();
		}
	}
	
	public static Context getInstance(){
		return context;
	}
	@Override
	public void onCreate() {
		context=this;
		super.onCreate();
	}

}
