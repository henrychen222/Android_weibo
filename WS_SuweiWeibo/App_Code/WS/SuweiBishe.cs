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


public class SuweiBishe :  System.Web.Services.WebService
{
	public SuweiBishe()
	{
	}
	
	[WebMethod]
	public string login(string name, string login)
	{
		string s = string.Format("Time: {0}", DateTime.Now);
		return s;
	}
	
	[WebMethod(Description = "<span style='color:#f74'>找回密码</span>")]
	public string getPassWord(string loginName, string phone)
	{
		return "{\"result\":\"0\",\"passWord\":\"123456\"}";
	}
	
	
}
