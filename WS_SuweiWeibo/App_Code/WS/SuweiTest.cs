using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Data;
using System.Text.RegularExpressions;
using Emww.DBUtil;
using Emww.Widget;
using System.Web.Script.Serialization;
using System.Text;
//using System.Collections.Generic;
using System.Collections;
using System.IO;
//using Microsoft.SqlServer.Management.Smo;
using Newtonsoft.Json.Converters;
using Newtonsoft.Json;

[WebService(Namespace = "http://demo.java.eezz.cn/")]
[WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
//若要允许使用 ASP.NET AJAX 从脚本中调用此 Web 服务，请取消对下行的注释。
// [System.Web.Script.Services.ScriptService]


public class SuweiTest :  System.Web.Services.WebService
{
	
	private string downImagePrefix = "http://192.168.15.253/WebServiceDemo/image/";
	
	
	public SuweiTest()
	{
	}
	
	[WebMethod(Description = "<span style='color:#f74'>找回密码</span>")]
	public string getPassWord(string loginName)
	{
		return "{\"result\":\"0\",\"passWord\":\"123456\"}";
	}
	
	
	
	[WebMethod(Description = "<span style='color:#f74'>登录</span>")]
	public string login(string loginName, string passWord, string loginType)
	{
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		if(loginName.Length == 0 || passWord.Length == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "用户名、密码不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		allMap.Add("result","0");
		allMap.Add("loginName",loginName);
		allMap.Add("passWord",passWord);
		return JsonHelper.ToJson(allMap);
	}
	
	
	[WebMethod(Description = "<span style='color:#f74'>获取列表</span>")]
	public string getPeopleList(string loginName)
	{
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		
		List<Dictionary<string,object>> list = new List<Dictionary<string,object>>();
		Dictionary<string,object> map;
		for (int i = 0; i < 5; i++) {
			map = new Dictionary<string, object>();
			map.Add("name","张三" + i);
			map.Add("age","2" + i);
			map.Add("sex","男");
			map.Add("phone","1351511871" + i);
			list.Add(map);
		}
		
		
		allMap.Add("result","0");
		allMap.Add("peopleList",list);
		return JsonHelper.ToJson(allMap);
	}
	
	private DbHelpMysql dbMysql = new DbHelpMysql();
	
	[WebMethod(Description = "<span style='color:#f74'>itemId：向上刷新：第一条id 向下刷新：最后一条id	必填 首次加载传0 <br> flag：向上刷新、向下刷新标示	必填 0向上刷新；1向下刷新</span>")]
	public string getWeiBoList(string userId, string count, string itemId, string flag){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		
		string sql = "select * from t_weibo limit 0, 3";
		DataSet ds = dbMysql.seleDB(sql);
		if(ds.Tables[0].Rows.Count == 0){
			return "{\"result\":\"0\", \"fileList\":[]}";
		}else{
			return "{\"result\":\"0\", \"fileList\":" + Db2JsonUtil.DataTable2JsonList(ds.Tables[0]) + "}";
		}
//		allMap.Add("result", "0");
//		allMap.Add("fileList", ds.Tables[0]);
//		return	JsonHelper.ToJson(allMap);
		
	}
	
	
	
	/// <summary>
	/// 写微博（写动态，发表说说）
	/// </summary>
	/// <param name="userId"></param>
	/// <param name="content"></param>
	/// <param name="imageFile"></param>
	/// <returns></returns>
	[WebMethod(Description = "<span style='color:#f74'>写微博（写动态，发表说说）</span>")]
	public string writeWeibo(string userId, string content, string imageFile){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		
		
		if(imageFile.Length == 0 )
		{
			allMap.Add("result", "1");
			allMap.Add("message", "图片为空");
			return JsonHelper.ToJson(allMap);
		}
		
		string imageUrl = "";
		// 20149921195.jpg
		string fileName = Utils.FileUploadImage(imageFile);
		if(fileName != null){
			imageUrl = downImagePrefix + fileName;
		}
		
		allMap.Add("result", "0");
		allMap.Add("imageUrl", "/image/" + fileName);
		
		return JsonHelper.ToJson(allMap);
	}
	
	
	
	
}
