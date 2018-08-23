package com.example.weibotest08_31.config;

import java.util.HashMap;
import java.util.Map;

import com.example.weibotest08_31.WeiboApp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class ConfigSPF {
	private static ConfigSPF configSPF;
	
	public final static String NAME_BASEDATA="basedata";
	public final static String NAME_SETTING="setting";
	public final static String NAME_AUTOCOMPLETE="autocomplete";
	public final static String NAME_CACHE="cache";
	private Map<String, SharedPreferences> spfMap=new HashMap<String, SharedPreferences>();
	public static ConfigSPF getInstance(){
		if(configSPF==null)
		 configSPF=new ConfigSPF();
		return configSPF;
		
	}
	private ConfigSPF() {
	}
	public SharedPreferences getConfigSPF(String spfName){
		if(spfMap.containsKey(spfName)){
		return spfMap.get(spfName);}
		else{
			Context ctx=WeiboApp.getInstance();
			//getSharedPreferences
			SharedPreferences spf=ctx.getSharedPreferences(spfName,Activity.MODE_PRIVATE);
			spfMap.put(spfName,spf);
			return spf;
		}
	}
	
}
