package com.example.weibotest08_31.api;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import com.example.weibotest08_31.config.Constant;
import com.example.weibotest08_31.item.Weibo;
import com.google.gson.Gson;

import android.content.Context;
import android.util.Log;


public class DotNetManager {
	private String TAG = "DotNetManager";

	// private static final String url = WSdotnet.NameSpace +
	// "WebServiceTest.asmx";
	// http://localhost/WS_SuweiWeibo/SuweiWeibo.asmx?op=login
	private static final String url = "http://192.168.15.253/WS_SuweiWeibo/SuweiWeibo.asmx";
	private Context ctx;

	/**
	 *
	 * 
	 * @return String
	 */
	public String peopleLogin(String loginName, String passWord) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginName", loginName);
		params.put("passWord", passWord);
		// 1andriod,2iphone 3  ipad
		params.put("loginType", "1");

		// String url =
		// "http://192.168.0.19:8081/WebServiceDemo/SuweiTest.asmx";
		String result = WSdotnet.get("login", params, url);
		if (result == null) {
			return null;
		}
		Log.e("WSdotnet", "TestManager中result：" + result);
		return result;
	}

	public String aboutUs(String loginName){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginName", loginName);

		String result=WSdotnet.get("getAboutUs", params, url);
		if (result == null) {
			return null;
		}
		return result;
	}
	public String addCareSomeOne(String userId,String careUserId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("careUserId",  careUserId);

		String result=WSdotnet.get("addCareSomeOne", params, url);
		if (result == null) {
			return null;
		}
		return result;
	}
	public String cancelCareSomeOne(String userId,String careUserId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("careUserId",  careUserId);

		String result=WSdotnet.get("cancelCareSomeOne", params, url);
		if (result == null) {
			return null;
		}
		return result;
	}

	public String editPwd(String loginName,String oldPsaaWord,String newPassWord){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginName", loginName);
		params.put("oldPassWord", oldPsaaWord);
		params.put("newPassWord", newPassWord);
		String result=WSdotnet.get("updatePassWord", params, url);
		if (result == null) {
			return null;
		}
		return result;
	}
	public String writeFeedBack(String userId,String content){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("content", content);
		String result=WSdotnet.get("writeFeedBack", params, url);
		if (result == null) {
			return null;
		}
		return result;
	}
	public String updatePeopleDetail(String userId,String name,String sex,String age,String phone){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("name", name);
		params.put("sex", sex);
		params.put("age", age);
		params.put("phone", phone);
		String result=WSdotnet.get("updatePeopleDetail", params, url);
		if (result == null) {
			return null;
		}
		return result;
	}
	public String checkVersionUpdate (String userId,String curVersion,String loginType){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("curVersion", curVersion);
		params.put("loginType", loginType);
		String result=WSdotnet.get("checkVersionUpdate", params, url);
		if (result == null) {
			return null;
		}
		return result;
	}
	public String getFansList(String userId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		String result=WSdotnet.get("getFansList", params, url);
		if (result == null) {
			return null;
		}
		return result;
	}
	public String backPwd(String loginName,String phone){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginName", loginName);
		params.put("phone", phone);
		String result=WSdotnet.get("getPassWord", params, url);
		if (result == null) {
			return null;
		}
		return result;
	}
	public String registerUser(String loginName, String passWord,String name,
			String age,String sex,String phone,String headUrl){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginName", loginName);
		params.put("passWord", passWord);
		params.put("name", name);
		params.put("age", age);
		params.put("sex", sex);
		params.put("phone", phone);
		params.put("headUrl", headUrl);
		String result = WSdotnet.get("register", params, url);
		if (result == null) {
			return null;
		}
		return result;
	}
	
	public String getWeiboList(String userId, String count, String itemId,
			String flag) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("count", count);
		params.put("itemId", itemId);
		params.put("flag", flag);

		String backStr = WSdotnet.get("getWeiBoList", params, url);
		if (backStr == null) {
			return null;
		}

		return backStr;
	}
	public String getLeaveMsgList(String userId, String count, String lastId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("count", count);
		params.put("lastId", lastId);

		String backStr = WSdotnet.get("getLeaveMsgList", params, url);
		if (backStr == null) {
			return null;
		}

		return backStr;
	}

	public String getThePeopleDetail(String userId, String otherId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("otherId", otherId);
		String backStr = WSdotnet.get("getThePeopleDetail", params, url);
		if (backStr == null) {
			return null;
		}

		return backStr;
	}
	public String getWeiboCommentList(String userId, String weiboId,String count) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("weiboId", weiboId);
		params.put("count", count);
		String backStr = WSdotnet.get("getWeiboCommentList", params, url);
		if (backStr == null) {
			return null;
		}

		return backStr;
	}
	public String commentWeibo(String userId, String weiboId,String content) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("weiboId", weiboId);
		params.put("content", content);
		String backStr = WSdotnet.get("commentWeibo", params, url);
		if (backStr == null) {
			return null;
		}

		return backStr;
	}
	public String leaveMsgToPeople(String userId, String otherId,String leaveMsg) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("otherId", otherId);
		params.put("leaveMsg", leaveMsg);
		String backStr = WSdotnet.get("leaveMsgToPeople", params, url);
		if (backStr == null) {
			return null;
		}

		return backStr;
	}
	/**0赞  1取消赞*/
	public String praiseWeibo(String userId, String weiboId, String flag) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("weiboId", weiboId);
		params.put("flag", flag);
		String backStr = WSdotnet.get("praiseWeibo", params, url);
		if (backStr == null) {
			return null;
		}

		return backStr;
	}
	public String isUserExist(String loginName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginName", loginName);
		String result = WSdotnet.get("isUserExist", params, url);
		if (result == null) {
			return null;
		}
		return result;
	}
	public String updateLeaveMsgHasRead(String userId,String leaveMsgId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("leaveMsgId", leaveMsgId);
		String result = WSdotnet.get("updateLeaveMsgHasRead", params, url);
		if (result == null) {
			return null;
		}
		return result;
	}

	public String getCaresList(String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		String result = WSdotnet.get("getCaresList", params, url);
		if (result == null) {
			return null;
		}
		return result;
	}


	public String getPeopleList(String size) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("size", size);

		// params.put("versionCode", WSApplication.VersionCode);
		// params.put("machineCode", WSApplication.MachineCode);
		String result = WSdotnet.get("AA_getPeopleList", params, url);
		if (result == null) {
			return null;
		}
		Log.e("WSdotnet", "TestManager getPeopleList" + result);
		return result;

	}

	/**
	 * 图片上传
	 */
	public String testUploadImage(String filePath, String fileName) {

		String uploadBuffer = file2Base64String(filePath, fileName);
		if (uploadBuffer == null) {
			return null;
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", "001");
		params.put("content", "xxxx内容");
		params.put("imageFile", uploadBuffer);

		String url = "http://192.168.15.253/WS_SuweiWeibo/SuweiTest.asmx";

		String result = WSdotnet.post("writeWeibo", params, url);

		return result;
	}

	/**
	 * 图片转Base64位字符串
	 * 
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	private String file2Base64String(String filePath, String fileName) {
		try {
			FileInputStream fis = new FileInputStream(filePath + fileName);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = fis.read(buffer)) >= 0) {
				baos.write(buffer, 0, count);
			}
			fis.close();
			String uploadBuffer = new String(Base64.encode(baos.toByteArray()));
			return uploadBuffer;
		} catch (Exception e) {
			Log.e(TAG, "文件转换错误--------->" + e);
			return null;
		}

	}

	public String sendWeibo(String userId, String content, String filePath,
			String fileName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("content", content);
		params.put("creatTime", System.currentTimeMillis());
		params.put("imageFile", file2Base64String(filePath, fileName));
		String result = WSdotnet.get("writeWeibo", params, url);
		if (result == null) {
			return null;
		}
		Log.e("", "" + result);
		return result;
	}
}
