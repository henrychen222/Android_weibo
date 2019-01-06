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

//using WebServiceDemo.App_Code;

[WebService(Namespace = "http://demo.java.eezz.cn/")]
[WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
//若要允许使用 ASP.NET AJAX 从脚本中调用此 Web 服务，请取消对下行的注释。
// [System.Web.Script.Services.ScriptService]
public class SuweiWeibo  : System.Web.Services.WebService
{
	public SuweiWeibo()
	{
	}
	
	private DbHelpMysql dbMysql = new DbHelpMysql();
	//private string downImagePrefix = "http://192.168.0.19:8081/WS_SuweiWeibo_new/image/";
	
	
	/**
	 * ------------------------------------------------------------------------------------
	 */

	/// <summary>
	/// 登陆
	/// </summary>
	/// <param name="loginName"></param>
	/// <param name="passWord"></param>
	/// <param name="loginType"></param>
	/// <returns></returns>
	[WebMethod(Description = "<span style='color:#f74'>登陆</span>")]
	public string login(string loginName, string passWord, string loginType){
		
		DataSet ds = dbMysql.seleDB("select * from t_people where loginName = '" + loginName + "' and passWord = '" + passWord + "'");
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		if(ds.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "用户名密码错误！");
			return JsonHelper.ToJson(allMap);
		}else{
			Dictionary<string,object> voMap = JsonHelper.DataRowToDictionary(ds.Tables[0].Rows[0]);
			allMap.Add("result", "0");
			allMap.Add("people", voMap);
			return JsonHelper.ToJson(allMap);
		}
	}
	
	
	/// <summary>
	/// 找回密码
	/// </summary>
	/// <param name="loginName"></param>
	/// <param name="phone"></param>
	/// <returns></returns>
	[WebMethod(Description = "<span style='color:#f74'>找回密码</span>")]
	public string getPassWord(string loginName, string phone){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		if(loginName.Length == 0 || phone.Length == 0){
			allMap.Add("result", "1");
			allMap.Add("message", "非法参数！");
			return JsonHelper.ToJson(allMap);
		}
		
		DataSet ds = dbMysql.seleDB("select * from t_people where loginName = '" + loginName + "' and phone = '" + phone + "'");
		if(ds.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "没找到该用户！");
			return JsonHelper.ToJson(allMap);
		}else{
			Dictionary<string,object> voMap = JsonHelper.DataRowToDictionary(ds.Tables[0].Rows[0]);
			allMap.Add("result", "0");
			allMap.Add("passWord", voMap["passWord"]);
			return JsonHelper.ToJson(allMap);
		}
	}
	
	/// <summary>
	/// 更新密码
	/// </summary>
	/// <param name="loginName"></param>
	/// <param name="newPassWord"></param>
	/// <returns></returns>
	[WebMethod(Description = "<span style='color:#f74'>更新密码</span>")]
	public string updatePassWord(string loginName,string oldPassWord, string newPassWord){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		if(loginName.Length == 0 || newPassWord.Length == 0 || oldPassWord.Length == 0){
			allMap.Add("result", "1");
			allMap.Add("message", "非法参数！");
			return JsonHelper.ToJson(allMap);
		}
		
		DataSet ds = dbMysql.seleDB("select * from t_people where loginName = '" + loginName + "'");
		if(ds.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "该用户不存在！");
			return JsonHelper.ToJson(allMap);
			
		}
		
		DataSet ds2 = dbMysql.seleDB("select * from t_people where loginName = '" + loginName + "' and passWord = '" + oldPassWord + "'");
		if(ds2.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "原始密码不正确！");
			return JsonHelper.ToJson(allMap);
		}
		
		// int loginName = ds.Tables[0].Rows[0]["loginName"];
		string sql = "UPDATE t_people SET PASSWORD = '"+newPassWord+"' WHERE loginName = '" + loginName + "'";
		int result = dbMysql.ExecuteSql(sql);
		
		allMap.Add("result", "0");
		return JsonHelper.ToJson(allMap);
	}
	
	/// <summary>
	/// 判断用户名是否存在接口
	/// </summary>
	/// <param name="loginName"></param>
	/// <param name="newPassWord"></param>
	/// <returns> 0用户不存在/1存在 </returns>
	[WebMethod(Description = "<span style='color:#f74'>判断用户名是否存在 <br> 0用户不存在/1存在</span>")]
	public string isUserExist(string loginName){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		
		if(loginName.Length == 0){
			allMap.Add("result", "1");
			allMap.Add("message", "非法参数！");
			return JsonHelper.ToJson(allMap);
		}
		
		DataSet ds = dbMysql.seleDB("select * from t_people where loginName = '" + loginName+ "'");
		if(ds.Tables[0].Rows.Count > 0)
		{
			allMap.Add("result", "0");
			allMap.Add("isExist", "1");
			return JsonHelper.ToJson(allMap);
		}else{
			allMap.Add("result", "0");
			allMap.Add("isExist", "0");
			return JsonHelper.ToJson(allMap);
		
		}
		
	}
	
	
	/// <summary>
	/// 注册
	/// </summary>
	/// <param name="loginName"></param>
	/// <param name="passWord"></param>
	/// <param name="name"></param>
	/// <param name="age"></param>
	/// <param name="sex"></param>
	/// <param name="phone"></param>
	/// <param name="headUrl"></param>
	/// <returns></returns>
	[WebMethod(Description = "<span style='color:#f74'>注册</span>")]
	public string register(string loginName, string passWord, string name, string age, string sex, string phone, string headUrl){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		if(loginName.Length == 0 || passWord.Length == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "用户名、密码不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		
		DataSet ds0 = dbMysql.seleDB("select * from t_people where loginName = '" + loginName + "'");
		if(ds0.Tables[0].Rows.Count > 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "该用户已经存在！");
			return JsonHelper.ToJson(allMap);
		}
		

		string fileName = FileUploadImage(headUrl);
		if(fileName != null){
			string imageUrl = "/image/" + fileName;
		}else{
			headUrl = "/image/user_head.png";
		}
		
				
		Dictionary<string,object> inertMap = new Dictionary<string, object>();
		inertMap.Add("loginName", loginName);
		inertMap.Add("passWord", passWord);
		inertMap.Add("name", name);
		inertMap.Add("age", age);
		inertMap.Add("sex", sex);
		inertMap.Add("phone", phone);
		inertMap.Add("headUrl", headUrl);
		string inertSql = SqlUtil.getInsertSql("t_people", inertMap);
//		return inertSql;
		
		try
		{
			// 插入新用户
			dbMysql.ExecuteSql(inertSql);
			// 查出新用户
			DataSet ds = dbMysql.seleDB("SELECT * FROM t_people  ORDER BY id DESC LIMIT 0, 1");
			Dictionary<string,object> voMap = JsonHelper.DataRowToDictionary(ds.Tables[0].Rows[0]);
			allMap.Add("result", "0");
			allMap.Add("people", voMap);
			return JsonHelper.ToJson(allMap);
		}
		catch (Exception ex)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "插入报错");
			return JsonHelper.ToJson(allMap);
		}
		
	}
	
	
	
	[WebMethod(Description = "<span style='color:#f74'>itemId：向上刷新：第一条id 向下刷新：最后一条id	必填 首次加载传0 <br> flag：向上刷新、向下刷新标示	必填 0向上刷新；1向下刷新</span>")]
	public string getWeiBoList(string userId, string count, string itemId, string flag){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		
		if(userId.Length == 0 )
		{
			allMap.Add("result", "1");
			allMap.Add("message", "用户id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		DataSet ds0 = dbMysql.seleDB("select * from t_people where id = '" + userId + "'");
		if(ds0.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "该用户不存在！");
			return JsonHelper.ToJson(allMap);
		}
		
		if(count.Length == 0){
			count = "10";
		}
		
		string sql;
		// 向下刷新
		if(flag.Equals("1")){
			if(itemId.Length == 0 || itemId.Equals("0")){
				sql = "SELECT IFNULL(t_praise_nums.praiseCount, 0) AS praiseNum, IFNULL(t_comment_nums.commentCount, 0) AS commentNum, CASE WHEN t_praise.id IS NULL THEN 0 ELSE 1 END AS hasPraise, t_weibo.*, t_people.headUrl, t_people.loginName, t_people.name, t_people.sex FROM t_weibo  LEFT JOIN t_people ON t_weibo.userId = t_people.id  LEFT JOIN t_praise ON t_weibo.id = t_praise.weiboId  AND t_praise.userId = "+userId+" LEFT JOIN t_praise_nums ON t_weibo.id = t_praise_nums.weiboId LEFT JOIN t_comment_nums ON t_weibo.id = t_comment_nums.weiboId  ORDER BY t_weibo.id DESC LIMIT 0, " + count;
			}else{
				sql = "SELECT IFNULL(t_praise_nums.praiseCount, 0) AS praiseNum, IFNULL(t_comment_nums.commentCount, 0) AS commentNum, CASE WHEN t_praise.id IS NULL THEN 0 ELSE 1 END AS hasPraise, t_weibo.*, t_people.headUrl, t_people.loginName, t_people.name, t_people.sex FROM t_weibo  LEFT JOIN t_people ON t_weibo.userId = t_people.id  LEFT JOIN t_praise ON t_weibo.id = t_praise.weiboId  AND t_praise.userId = "+userId+" LEFT JOIN t_praise_nums ON t_weibo.id = t_praise_nums.weiboId LEFT JOIN t_comment_nums ON t_weibo.id = t_comment_nums.weiboId  WHERE t_weibo.id < "+itemId+" ORDER BY t_weibo.id DESC LIMIT 0, " + count;
			}
		}
		// 向上刷新
		else{
			if(itemId.Length == 0 || itemId.Equals("0")){
				sql = "SELECT IFNULL(t_praise_nums.praiseCount, 0) AS praiseNum, IFNULL(t_comment_nums.commentCount, 0) AS commentNum, CASE WHEN t_praise.id IS NULL THEN 0 ELSE 1 END AS hasPraise, t_weibo.*, t_people.headUrl, t_people.loginName, t_people.name, t_people.sex FROM t_weibo  LEFT JOIN t_people ON t_weibo.userId = t_people.id  LEFT JOIN t_praise ON t_weibo.id = t_praise.weiboId  AND t_praise.userId = "+userId+" LEFT JOIN t_praise_nums ON t_weibo.id = t_praise_nums.weiboId LEFT JOIN t_comment_nums ON t_weibo.id = t_comment_nums.weiboId ORDER BY t_weibo.id DESC LIMIT 0, " + count;
			}else{
				// SELECT * FROM (SELECT * FROM t_weibo  WHERE id > 0 ORDER BY id ASC LIMIT 0, 5) AS t_temp ORDER BY id DESC
				sql = "SELECT * FROM (SELECT IFNULL(t_praise_nums.praiseCount, 0) AS praiseNum, IFNULL(t_comment_nums.commentCount, 0) AS commentNum, CASE WHEN t_praise.id IS NULL THEN 0 ELSE 1 END AS hasPraise, t_weibo.*, t_people.headUrl, t_people.loginName, t_people.name, t_people.sex FROM t_weibo  LEFT JOIN t_people ON t_weibo.userId = t_people.id  LEFT JOIN t_praise ON t_weibo.id = t_praise.weiboId  AND t_praise.userId = "+userId+" LEFT JOIN t_praise_nums ON t_weibo.id = t_praise_nums.weiboId LEFT JOIN t_comment_nums ON t_weibo.id = t_comment_nums.weiboId WHERE t_weibo.id > "+itemId+" ORDER BY t_weibo.id ASC LIMIT 0, " + count + " ) AS t_temp ORDER BY t_temp.id DESC ";
			}
			
		}
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
		if(userId.Length == 0 )
		{
			allMap.Add("result", "1");
			allMap.Add("message", "用户id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		DataSet ds0 = dbMysql.seleDB("select * from t_people where id = '" + userId + "'");
		if(ds0.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "该用户不存在！");
			return JsonHelper.ToJson(allMap);
		}
		
		if(content.Length == 0 )
		{
			allMap.Add("result", "1");
			allMap.Add("message", "动态内容不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		string imageUrl = "";
		if(imageFile.Length > 0){
			// 20149921195.jpg
			string fileName = FileUploadImage(imageFile);
			if(fileName != null){
				imageUrl = "/image/" + fileName;
			}
		}
		
		
		// userId content imageUrl creatTime creatTimelong praiseNum commentNum
		string inertSql = "INSERT INTO t_weibo (userId, creatTime, creatTimelong, content, imageUrl) " +
			"VALUES('"+userId+"',CURRENT_TIMESTAMP(), UNIX_TIMESTAMP(), '"+content+"', '"+imageUrl+"' )";
		int result = dbMysql.ExecuteSql(inertSql);
		
		if(result > 0){
			DataSet ds3 = dbMysql.seleDB("SELECT 0 AS praiseNum, 0 AS commentCount, 0 AS hasPraise, t_weibo.*, t_people.headUrl, t_people.loginName, t_people.name, t_people.sex FROM t_weibo  LEFT JOIN t_people ON t_weibo.userId = t_people.id ORDER BY t_weibo.id DESC LIMIT 0, 1 ");
			Dictionary<string,object> voMap = JsonHelper.DataRowToDictionary(ds3.Tables[0].Rows[0]);
			allMap.Add("result", "0");
			allMap.Add("weibo", voMap);
		}else{
			allMap.Add("result", "1");
			allMap.Add("message", "处理异常");
		}
		return JsonHelper.ToJson(allMap);
		
	}
	
	
	[WebMethod(Description = "<span style='color:#f74'>查看微博详情</span>")]
	public string getWeiboDetail(string userId, string weiboId){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		if(userId.Length == 0 )
		{
			allMap.Add("result", "1");
			allMap.Add("message", "用户id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		if(weiboId.Length == 0 )
		{
			allMap.Add("result", "1");
			allMap.Add("message", "微博id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		DataSet ds0 = dbMysql.seleDB("select * from t_people where id = '" + userId + "'");
		if(ds0.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "该用户不存在！");
			return JsonHelper.ToJson(allMap);
		}
		
		DataSet ds1 = dbMysql.seleDB("select * from t_weibo where id = '" + weiboId + "'");
		if(ds1.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "该微博不存在，可能已经删除！");
			return JsonHelper.ToJson(allMap);
		}
		
		
		DataSet ds3 = dbMysql.seleDB("SELECT CASE WHEN t_praise.id IS NULL THEN 0 ELSE 1 END AS hasPraise, t_weibo.*, t_people.headUrl, t_people.loginName, t_people.name, t_people.sex FROM t_weibo LEFT JOIN t_people ON  t_weibo.userId = t_people.id LEFT JOIN t_praise ON t_weibo.id = t_praise.weiboId  AND t_praise.userId = "+userId+" WHERE t_weibo.id = "+weiboId);
		Dictionary<string, object> voMap = JsonHelper.DataRowToDictionary(ds3.Tables[0].Rows[0]);
		allMap.Add("result", "0");
		allMap.Add("weibo", voMap);
		return JsonHelper.ToJson(allMap);
		
		
	}
	
	[WebMethod(Description = "<span style='color:#f74'>赞/取消赞动态 <br>  0赞；1取消赞</span>")]
	public string praiseWeibo(string userId, string weiboId, string flag){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		if(userId.Length == 0 )
		{
			allMap.Add("result", "1");
			allMap.Add("message", "用户id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		if(weiboId.Length == 0 )
		{
			allMap.Add("result", "1");
			allMap.Add("message", "微博id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		if(flag.Length == 0 )
		{
			allMap.Add("result", "1");
			allMap.Add("message", "flag不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		DataSet ds0 = dbMysql.seleDB("select * from t_people where id = " + userId + "");
		if(ds0.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "该用户不存在！");
			return JsonHelper.ToJson(allMap);
		}
		
		DataSet ds1 = dbMysql.seleDB("select * from t_weibo where id = " + weiboId + "");
		if(ds1.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "不存在该微博！");
			return JsonHelper.ToJson(allMap);
		}
		
		DataSet ds2 = dbMysql.seleDB("select * from t_praise where userId = " + userId + " and weiboId = " + weiboId);
		if(ds2.Tables[0].Rows.Count > 0)
		{
			// 赞
			if(flag.Equals("0")){
				allMap.Add("result", "0");
				allMap.Add("message", "该微博已经赞过");
				return JsonHelper.ToJson(allMap);
			}else{
				dbMysql.ExecuteSql("delete from t_praise where  userId = " + userId + " and weiboId = " + weiboId);
				allMap.Add("result", "0");
				allMap.Add("message", "取消赞成功！");
				return JsonHelper.ToJson(allMap);
			}
			
		} else {
			// 赞
			if(flag.Equals("0")){
				dbMysql.ExecuteSql("insert into t_praise (userId, weiboId) VALUES("+userId+", "+weiboId+")");
				allMap.Add("result", "0");
				allMap.Add("message", "赞成功");
				return JsonHelper.ToJson(allMap);
			}else{
				allMap.Add("result", "0");
				allMap.Add("message", "本来就没赞过！");
				return JsonHelper.ToJson(allMap);
			}
		}
		
		
	}
	
	
	[WebMethod(Description = "<span style='color:#f74'>赞/取消赞动态 <br>  0赞；1取消赞</span>")]
	public string deleteWeibo(string userId, string weiboId){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		
		if(userId.Length == 0 )
		{
			allMap.Add("result", "1");
			allMap.Add("message", "用户id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		if(weiboId.Length == 0 )
		{
			allMap.Add("result", "1");
			allMap.Add("message", "微博id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		
		DataSet ds0 = dbMysql.seleDB("select * from t_people where id = " + userId + "");
		if(ds0.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "该用户不存在！");
			return JsonHelper.ToJson(allMap);
		}
		
		DataSet ds1 = dbMysql.seleDB("select * from t_weibo where userId = " + userId + " and id = " + weiboId);
		if(ds1.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "不存在该微博！");
			return JsonHelper.ToJson(allMap);
		}else{
			dbMysql.ExecuteSql("delete from t_weibo where userId = " + userId + " and id = " + weiboId);
			allMap.Add("result", "0");
			allMap.Add("message", "删除成功");
			return JsonHelper.ToJson(allMap);
		}
		
	}
	[WebMethod(Description = "<span style='color:#f74'>动态评论</span>")]
	public string getWeiboCommentList(string userId, string weiboId, string count){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		if(userId.Length == 0 )
		{
			allMap.Add("result", "1");
			allMap.Add("message", "用户id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		if(count.Length == 0 )
		{
			count = "10";
		}
		
		if(weiboId.Length == 0 )
		{
			allMap.Add("result", "1");
			allMap.Add("message", "微博id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		DataSet ds0 = dbMysql.seleDB("select * from t_people where id = " + userId + "");
		if(ds0.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "该用户不存在！");
			return JsonHelper.ToJson(allMap);
		}
		
		DataSet ds1 = dbMysql.seleDB("select * from t_weibo where id = " + weiboId + "");
		if(ds1.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "不存在该微博！");
			return JsonHelper.ToJson(allMap);
		}
		string sql = "SELECT t_comment.*, t_people.headUrl, t_people.loginName, t_people.name, t_people.sex FROM t_comment LEFT JOIN t_people ON  t_comment.userId = t_people.id  WHERE t_comment.weiboId = "+weiboId+" ORDER BY t_comment.id DESC LIMIT 0, " + count;
		
		
		DataSet ds = dbMysql.seleDB(sql);
		if(ds.Tables[0].Rows.Count == 0){
			return "{\"result\":\"0\", \"commentList\":[]}";
		}else{
			return "{\"result\":\"0\", \"commentList\":" +Db2JsonUtil.DataTable2JsonList(ds.Tables[0]) + "}";
		}
		
	}
	
	[WebMethod(Description = "<span style='color:#f74'>动态评论</span>")]
	public string commentWeibo(string userId, string weiboId, string content){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		if(userId.Length == 0 )
		{
			allMap.Add("result", "1");
			allMap.Add("message", "用户id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		if(weiboId.Length == 0 )
		{
			allMap.Add("result", "1");
			allMap.Add("message", "微博id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		if(content.Length == 0 )
		{
			allMap.Add("result", "1");
			allMap.Add("message", "评论内容不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		DataSet ds0 = dbMysql.seleDB("select * from t_people where id = " + userId + "");
		if(ds0.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "该用户不存在！");
			return JsonHelper.ToJson(allMap);
		}
		
		DataSet ds1 = dbMysql.seleDB("select * from t_weibo where id = " + weiboId + "");
		if(ds1.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "不存在该微博！");
			return JsonHelper.ToJson(allMap);
		}
		
		
		dbMysql.ExecuteSql("insert into t_comment (userId, weiboId, content, createTime) VALUES("+userId+", "+weiboId+",'"+content+"' , CURRENT_TIMESTAMP())");
		
		allMap.Add("result", "0");
		allMap.Add("message", "评论成功！");
		return JsonHelper.ToJson(allMap);
		
		
	}
	
	
	
	[WebMethod(Description = "<span style='color:#f74'>查看他人个人中心详细资料</span>")]
	public string getThePeopleDetail(string userId, string otherId){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		if(userId.Length == 0 )
		{
			allMap.Add("result", "1");
			allMap.Add("message", "用户id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		if(otherId.Length == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "对方id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		DataSet ds0 = dbMysql.seleDB("select * from t_people where id = " + otherId + "");
		if(ds0.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "该用户不存在！");
			return JsonHelper.ToJson(allMap);
		}else{
			// 查出对方用户实体和动态信息
			allMap.Add("result", "0");
			DataSet ds2 = dbMysql.seleDB("SELECT * FROM t_people where id = " + otherId);
			Dictionary<string,object> voMap = JsonHelper.DataRowToDictionary(ds2.Tables[0].Rows[0]);
			allMap.Add("people", voMap);
			
			DataSet ds3 = dbMysql.seleDB("SELECT * FROM t_weibo WHERE userId = "+otherId+" ORDER BY id DESC LIMIT 0, 1");
			Dictionary<string,object> weiboMap = JsonHelper.DataRowToDictionary(ds3.Tables[0].Rows[0]);
			allMap.Add("weibo", weiboMap);
			
			return JsonHelper.ToJson(allMap);
		
		}
	}
	
	[WebMethod(Description = "<span style='color:#f74'>给他人留言</span>")]
	public string leaveMsgToPeople(string userId, string otherId, string leaveMsg){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		
		if(userId.Length == 0 )
		{
			allMap.Add("result", "1");
			allMap.Add("message", "用户id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		if(otherId.Length == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "对方id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		if(leaveMsg.Length == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "留言内容不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		
		DataSet ds0 = dbMysql.seleDB("select * from t_people where id = " + otherId + "");
		if(ds0.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "该用户不存在！");
			return JsonHelper.ToJson(allMap);
		}
		
		
		dbMysql.ExecuteSql("insert into t_leave_msg (fromUserId, toUserId, content, createTime) VALUES("+userId+", "+otherId+",'"+leaveMsg+"' , CURRENT_TIMESTAMP())");
		
		allMap.Add("result", "0");
		allMap.Add("message", "留言成功！");
		return JsonHelper.ToJson(allMap);

		
	}
	
	[WebMethod(Description = "<span style='color:#f74'>获取个人留言列表</span>")]
	public string getLeaveMsgList(string userId, string lastId, string count){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		
		if(userId.Length == 0 )
		{
			allMap.Add("result", "1");
			allMap.Add("message", "用户id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		if(userId.Length == 0 )
		{
			count = "10";
		}
		
		string sql;
		if(lastId.Length == 0 || lastId.Equals("0"))
		{
			sql = "SELECT t_people.loginName, t_people.name, t_people.headUrl, t_people.sex, t_people.age, t_people.phone, t_leave_msg.* FROM t_leave_msg LEFT JOIN t_people ON t_leave_msg.fromUserId = t_people.id WHERE toUserId = "+userId+"  ORDER BY id DESC LIMIT 0, " + count;
		}
		else
		{
			sql = "SELECT t_people.loginName, t_people.name, t_people.headUrl, t_people.sex, t_people.age, t_people.phone, t_leave_msg.* FROM t_leave_msg LEFT JOIN t_people ON t_leave_msg.fromUserId = t_people.id WHERE toUserId = "+userId+"  AND t_leave_msg.id < "+lastId+" ORDER BY id DESC LIMIT 0, " + count;
		
		}
		
		DataSet ds = dbMysql.seleDB(sql);
		if(ds.Tables[0].Rows.Count == 0){
			return "{\"result\":\"0\", \"leaveMsgList\":[]}";
		}else{
			return "{\"result\":\"0\", \"leaveMsgList\":" +Db2JsonUtil.DataTable2JsonList(ds.Tables[0]) + "}";
		}
		
		
		
	}
	
	[WebMethod(Description = "<span style='color:#f74'>更新留言为已读</span>")]
	public string updateLeaveMsgHasRead(string userId, string leaveMsgId){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		
		if(userId.Length == 0 )
		{
			allMap.Add("result", "1");
			allMap.Add("message", "用户id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		if(leaveMsgId.Length == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "留言id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		
		DataSet ds0 = dbMysql.seleDB("select * from t_leave_msg where id = " + leaveMsgId + " and toUserId = " + userId);
		if(ds0.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "该留言不存在！");
			return JsonHelper.ToJson(allMap);
		}
		
		dbMysql.ExecuteSql("update t_leave_msg set isread = '1' where id = " + leaveMsgId + " and toUserId = " + userId);
		allMap.Add("result", "0");
		allMap.Add("message", "修改已读成功！");
		return JsonHelper.ToJson(allMap);
	
	}
	
	
	
	[WebMethod(Description = "<span style='color:#f74'>注册</span>")]
	public string updatePeopleDetail(string userId,  string name, string age, string sex, string phone){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		if(userId.Length == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "用户id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		
		DataSet ds0 = dbMysql.seleDB("select * from t_people where id = '" + userId + "'");
		if(ds0.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "该用户不存在！");
			return JsonHelper.ToJson(allMap);
		}
		
		
		Dictionary<string,object> inertMap = new Dictionary<string, object>();
		if(name.Length > 0)
			inertMap.Add("name", name);
		if(age.Length > 0)
			inertMap.Add("age", age);
		if(sex.Length > 0)
			inertMap.Add("sex", sex);
		if(phone.Length > 0)
			inertMap.Add("phone", phone);
		string updateSql = SqlUtil.getUpdateSql("t_people", inertMap);
//		return updateSql;
		
		try
		{
			// 更新用户信息
			dbMysql.ExecuteSql(updateSql + " where id = " + userId);
			// 查出新用户
			DataSet ds = dbMysql.seleDB("SELECT * FROM t_people  where id = " + userId);
			Dictionary<string,object> voMap = JsonHelper.DataRowToDictionary(ds.Tables[0].Rows[0]);
			allMap.Add("result", "0");
			allMap.Add("people", voMap);
			return JsonHelper.ToJson(allMap);
		}
		catch (Exception ex)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "更新异常");
			return JsonHelper.ToJson(allMap);
		}
		
	}
	
	
	

	
	[WebMethod(Description = "<span style='color:#f74'>更新头像</span>")]
	public string updatePeopleHeadImage(string userId, string imageFile){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		
		if(userId.Length == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "用户id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		if(imageFile.Length == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "图片文件不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		DataSet ds0 = dbMysql.seleDB("select * from t_people where id = '" + userId + "'");
		if(ds0.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "该用户不存在！");
			return JsonHelper.ToJson(allMap);
		}
		
		// 20149921195.jpg
		string fileName = FileUploadImage(imageFile);
		if(fileName != null){
			string imageUrl = "/image/" + fileName;
			dbMysql.ExecuteSql("update t_people set headUrl = '" + imageUrl +"' where id = " + userId);
			// 查出新用户
			allMap.Add("result", "0");
			allMap.Add("imageUrl", imageUrl);
			allMap.Add("message", "更新成功！");
			return JsonHelper.ToJson(allMap);
		}else{
			allMap.Add("result", "1");
			allMap.Add("message", "更新失败！");
			return JsonHelper.ToJson(allMap);
		}
		
		
	}
	
	[WebMethod(Description = "<span style='color:#f74'>关于我们</span>")]
	public string getAboutUs(string userId){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		allMap.Add("result", "0");
		allMap.Add("title", "关于我的微博");
		allMap.Add("content", "关于我们关于我们关于我们关于我们关于我们关于我们关于我们关于我们关于我们");
		return JsonHelper.ToJson(allMap);
	}
	
	
	
	[WebMethod(Description = "<span style='color:#f74'>意见反馈</span>")]
	public string writeFeedBack(string userId, string content){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		if(userId.Length == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "用户id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		if(content.Length == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "反馈内容不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		
		dbMysql.ExecuteSql("insert into t_feedback (userId, content, createTime) values("+userId+", '"+content+"', CURRENT_TIMESTAMP() )");
		allMap.Add("result", "0");
		allMap.Add("message", "反馈成功，感谢您的反馈");
		return JsonHelper.ToJson(allMap);
		
		
	}
	
	
	[WebMethod(Description = "<span style='color:#f74'>获取版本 <br> curVersion如：1.0，2.0 <br> loginType 1表示andriod,2表示iphone 3 表示iPad </span>")]
	public string checkVersionUpdate(string userId, string curVersion, string loginType){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		if(userId.Length == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "用户id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		if(curVersion.Length == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "当前版本不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		if(loginType.Length == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "loginType不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		allMap.Add("result", "0");
		allMap.Add("message","获取成功");
		allMap.Add("versionId","1");
		allMap.Add("version","v1.0");
		allMap.Add("isStrong","0");
		allMap.Add("appUrl","http://d2.eoemarket.com/app0/92/92938/apk/707595.apk");
		allMap.Add("newDescribe"," 1、修改了xxx \n 2、优化了xxx，\n 3、新增了xxx功能");
		return JsonHelper.ToJson(allMap);
	}
	
	
	
	
	[WebMethod(Description = "<span style='color:#f74'>获取我的粉丝列表</span>")]
	public string getFansList(string userId){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		if(userId.Length == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "用户id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		DataSet ds0 = dbMysql.seleDB("select * from t_people where id = " + userId + "");
		if(ds0.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "该用户不存在！");
			return JsonHelper.ToJson(allMap);
		}
		
		string sql = "SELECT p.* FROM t_fans AS f LEFT JOIN t_people AS p ON f.selfId = p.id WHERE f.careId = " + userId;
		
		DataSet ds = dbMysql.seleDB(sql);
		if(ds.Tables[0].Rows.Count == 0){
			return "{\"result\":\"0\", \"fansList\":[]}";
		}else{
			return "{\"result\":\"0\", \"fansList\":" +Db2JsonUtil.DataTable2JsonList(ds.Tables[0]) + "}";
		}
	}
	
	
	
	[WebMethod(Description = "<span style='color:#f74'>获取我的关注列表</span>")]
	public string getCaresList(string userId){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		if(userId.Length == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "用户id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		DataSet ds0 = dbMysql.seleDB("select * from t_people where id = " + userId + "");
		if(ds0.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "该用户不存在！");
			return JsonHelper.ToJson(allMap);
		}
		
		string sql = "SELECT p.* FROM t_fans AS f LEFT JOIN t_people AS p ON f.careId = p.id WHERE f.selfId = " + userId;
		
		DataSet ds = dbMysql.seleDB(sql);
		if(ds.Tables[0].Rows.Count == 0){
			return "{\"result\":\"0\", \"caresList\":[]}";
		}else{
			return "{\"result\":\"0\", \"caresList\":" +Db2JsonUtil.DataTable2JsonList(ds.Tables[0]) + "}";
		}
	}
	
	[WebMethod(Description = "<span style='color:#f74'>关注某人</span>")]
	public string addCareSomeOne(string userId, string careUserId){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		if(userId.Length == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "用户id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		if(userId == careUserId)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "不能关注自己！");
			return JsonHelper.ToJson(allMap);
		}
		
		DataSet ds0 = dbMysql.seleDB("select * from t_people where id = '" + userId + "'");
		if(ds0.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", userId + "用户不存在！");
			return JsonHelper.ToJson(allMap);
		}
		
		ds0 = dbMysql.seleDB("select * from t_fans where selfId = " + userId + " and careId = " + careUserId);
		if(ds0.Tables[0].Rows.Count > 0)
		{
			allMap.Add("result", "0");
			allMap.Add("message", "已经关注过此人了！");
			return JsonHelper.ToJson(allMap);
		}
		
		string sql = "insert into t_fans (selfId, careId, createTime) values("+userId+", '"+careUserId+"', CURRENT_TIMESTAMP() )";
		dbMysql.ExecuteSql(sql);
		allMap.Add("result", "0");
		allMap.Add("message", "关注成功！");
		return JsonHelper.ToJson(allMap);
	}
	
	
	
	[WebMethod(Description = "<span style='color:#f74'>取消关注某人</span>")]
	public string cancelCareSomeOne(string userId, string careUserId){
		Dictionary<string,object> allMap = new Dictionary<string, object>();
		if(userId.Length == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "用户id不能为空！");
			return JsonHelper.ToJson(allMap);
		}
		
		if(userId == careUserId)
		{
			allMap.Add("result", "1");
			allMap.Add("message", "不能取消关注自己！");
			return JsonHelper.ToJson(allMap);
		}
		
		DataSet ds0 = dbMysql.seleDB("select * from t_people where id = '" + userId + "'");
		if(ds0.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "1");
			allMap.Add("message", userId + "用户不存在！");
			return JsonHelper.ToJson(allMap);
		}
		
		ds0 = dbMysql.seleDB("select * from t_fans where selfId = " + userId + " and careId = " + careUserId);
		if(ds0.Tables[0].Rows.Count == 0)
		{
			allMap.Add("result", "0");
			allMap.Add("message", "本来就没有关注TA，无需取消关注！");
			return JsonHelper.ToJson(allMap);
		}
		
		string sql = "delete from t_fans where selfId = " + userId + " and careId = " + careUserId;
		dbMysql.ExecuteSql(sql);
		allMap.Add("result", "0");
		allMap.Add("message", "取消关注成功！");
		return JsonHelper.ToJson(allMap);
	}
	
	
	
	
	/**
	 * -------------------------------------以上是suwei的微博接口-----------------------------------------------
	 */
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// [WebMethod(Description = "<span style='color:#f74'>连接MySql数据库测试; select * from zl_user </span>")]
	public string AA_DB_MySqlDB(string tabName){
		DbHelpMysql dbMysql = new DbHelpMysql();
		DataSet ds = dbMysql.seleDB("select * from " + tabName);
		
		
//		Dictionary<string,object> allMap = new Dictionary<string, object>();
//		Dictionary<string,object> voMap = JsonHelper.DataRowToDictionary(ds.Tables[0].Rows[0]);
//		allMap.Add("result", "0");
//		allMap.Add("people", voMap);
//		return JsonHelper.ToJson(allMap);
		
//		return DataTableJson(ds.Tables[0]);
		return Db2JsonUtil.DataTable2JsonList(ds.Tables[0]);
//		return DataTableJson(ds.Tables[0]);
	}
	
	
	//*******************************************************************************
	
	// [WebMethod(Description = "<span style='color:#f74'>GetStudent</span>")]
//	public string GetStudent()
//	{
//		List<Student> list = new List<Student>();
////		list.Add(new Student(){Id = 1, Name="张三", BirthDay= Convert.ToDateTime("2000-01-15")});
////		list.Add(new Student(){Id = 1, Name="李四", BirthDay= Convert.ToDateTime("2000-09-05")});
////		list.Add(new Student(){Id = 1, Name="王五", BirthDay= Convert.ToDateTime("2000-10-20")});
//		DbHelpMysql dbMysql = new DbHelpMysql();
//		DataSet ds = dbMysql.seleDB("select id,userId,creatTime from t_weibo");
//		foreach(DataRow row in ds.Tables[0].Rows)
//		{
//			Student s = new Student();
//			s.Id = Convert.ToInt32( row["id"]);
//			if(ds.Tables[0].Columns["userId"].DataType == typeof(string))
//			{
//				s.UserId = row["userId"].ToString();
//			}
//			
//			
//			s.CreatTime = Convert.ToDateTime(row["creatTime"]);
//			
//			
//			list.Add(s);
//		}
//		
//		
//		
//		
//		string result = JsonHelper.ToJson(list);
//		return result;
//	}
	
	
	/**
	 * ------------------------------------------------------------------------------------
	 */
	
	/**
	 * 王泉第三方Newtonsoft.Json.dll
	 * 功能：datatable 转json的
	 * using Newtonsoft.Json.Converters;
	 * using Newtonsoft.Json;
	 */
	public static string DataTableJson(DataTable dt)
    {
		
//		JsonSerializerSettings microsoftDateFormatSettings = new JsonSerializerSettings {
//    		DateFormatHandling = DateFormatHandling.MicrosoftDateFormat
//  		};
//  		string microsoftJson = JsonConvert.SerializeObject(entry, microsoftDateFormatSettings);
//  		return microsoftJson;
		
        IsoDateTimeConverter timeFormat = new IsoDateTimeConverter();
        timeFormat.DateTimeFormat = "yyyy-MM-dd HH:mm:ss";
        
         return JsonConvert.SerializeObject(dt, timeFormat);
//        return JsonConvert.SerializeObject(dt, Newtonsoft.Json.Formatting.Indented, timeFormat);//new DataTableConverter()
		// return JsonConvert.SerializeObject(dt, Newtonsoft.Json.Formatting.Indented, new DataTableConverter());//new DataTableConverter()
	}


 
	/*
	 *  JSON -> OBJECT 这个是json转成object类型,Newtonsoft.Json.dll
	 */
	public static T JsonToObject<T>(string jsonData)
	{
		return Newtonsoft.Json.JsonConvert.DeserializeObject<T>(jsonData);
	}

	
	
	
	
	
	/**
	 *根据json集合生成插入语句
	 */
	// [WebMethod(Description = "<span style='color:#f74'>根据json集合生成插入语句 ;</span>")]
	public string AA_createInsertsql_ByHashTable(string key)
	{
		String jsonStr = "[{\"name\":\"王五\",\"age\":\"29\",\"start_time\":\"2013-02-03\",\"sex\":\"男\",\"itm\":\"2\"}]";
		MyHashtable table = new MyHashtable();
		return SqlUtil.getInsertSql("AABB", table.CreateJsonArr(jsonStr)[0]);
	}
	
	/**
	 *根据json对象生成插入语句
	 */
	// [WebMethod(Description = "<span style='color:#f74'>根据json对象生成插入语句 ;</span>")]
	public string AA_createInsertsql_ByDictionary(string key)
	{
		string jsonStr = "{\"name\":\"王五\",\"age\":\"29\",\"time\":\"2013-02-03\",\"sex\":\"男\"}";
		Dictionary<string, object> dictionary = MyHashtable.JsonToDictionary(jsonStr);
		if(dictionary == null)
		{
			return "{\"result\":\"1\",\"message:\"传入参数错误\"}";
		}
		
		return SqlUtil.getInsertSql("AABB", dictionary);

	}
	
	
	/**
	 * ------------------------------------------------------------------------------------
	 */
	
	
	

	
//	public bool AA_upload_img(byte[] fileData, string fileName)
//	{
//		try
//		{
//			FileStream fs = File.Create(@"C:\" + fileName);
//			fs.Write(fileData, 0, fileData.Length);
//			fs.Close();
//			return true;
//		}
//		catch
//		{
//			return false;
//		}
//	}
	
	
	
	
	
	// [WebMethod(Description = "<span style='color:#f74'>插入视图测试：viewName视图名字；viewSql视图sql如：SELECT * FROM AABB</span>")]
	public string FileUploadImage(string bytestr)
	{
		string fileName = "";
		try
		{
			Random random = new Random();
			string i = random.Next(0, 10000000).ToString();
			fileName = DateTime.Now.Year.ToString() + DateTime.Now.Month + DateTime.Now.Day + DateTime.Now.Hour + DateTime.Now.Minute + DateTime.Now.Second;
			fileName = fileName + ".jpg";
			bool flag = StringToFile(bytestr, Server.MapPath("image\\") + "" + fileName);
			return fileName;
			
		}
		catch (Exception ex)
		{
			return null;
		}
	}
	
	/// <summary>
	/// 把经过base64编码的字符串保存为文件
	/// </summary>
	/// <param name="base64String">经base64加码后的字符串 </param>
	/// <param name="fileName">保存文件的路径和文件名 </param>
	/// <returns>保存文件是否成功 </returns>
	public static bool StringToFile(string base64String, string fileName)
	{
		//string path = Path.GetDirectoryName(Assembly.GetExecutingAssembly().GetName().CodeBase) + @"/beapp/" + fileName;

		System.IO.FileStream fs = new System.IO.FileStream(fileName, System.IO.FileMode.Create);
		System.IO.BinaryWriter bw = new System.IO.BinaryWriter(fs);
		if (!string.IsNullOrEmpty(base64String) && File.Exists(fileName))
		{
			bw.Write(Convert.FromBase64String(base64String));
		}
		bw.Close();
		fs.Close();
		return true;
	}
	
	
	
	
	
	
	
}

