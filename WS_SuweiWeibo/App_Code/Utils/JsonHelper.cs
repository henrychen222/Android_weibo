using System;

using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using System.Web.Script.Serialization;

namespace Emww.DBUtil
{
	public static class JsonHelper
	{
		private static JsonSerializerSettings _jsonSettings;

		static JsonHelper()
		{
			IsoDateTimeConverter datetimeConverter = new IsoDateTimeConverter();
			datetimeConverter.DateTimeFormat = "yyyy-MM-dd HH:mm:ss";

			_jsonSettings = new JsonSerializerSettings();
			_jsonSettings.MissingMemberHandling = Newtonsoft.Json.MissingMemberHandling.Ignore;
			_jsonSettings.NullValueHandling = Newtonsoft.Json.NullValueHandling.Ignore;
			_jsonSettings.ReferenceLoopHandling = Newtonsoft.Json.ReferenceLoopHandling.Ignore;
			_jsonSettings.Converters.Add(datetimeConverter);
		}

		/// <summary>
		/// 将指定的对象序列化成 JSON 数据。
		/// </summary>
		/// <param name="obj">要序列化的对象。</param>
		/// <returns></returns>
		public static string ToJson(this object obj)
		{
			try
			{
				if (null == obj)
					return null;

				return JsonConvert.SerializeObject(obj, Formatting.None, _jsonSettings);
			}
			catch (Exception ex)
			{
				Console.WriteLine(ex + "-----------");
//				Logging.LogManager.Error(new Logging.ExceptionLogInfo()
//				{
//					ExceptionClassName = "YY.SZYD.Shop.Common.Utils.JsonHelper",
//					ExceptionMethod = "ToJson",
//					ExceptionNote = "Json序列化出错",
//					RequestInfo = obj.GetType().FullName,
//				},
//				ex);

				return null;
			}
		}
		
		public static string DataTableJson(DataTable dt)
		{
			// return JsonConvert.SerializeObject(dt, timeFormat);
			return JsonConvert.SerializeObject(dt, Newtonsoft.Json.Formatting.Indented, _jsonSettings);//new DataTableConverter()
			// return JsonConvert.SerializeObject(dt, Newtonsoft.Json.Formatting.Indented, new DataTableConverter());//new DataTableConverter()
		}

		/// <summary>
		/// 轻量级的，简单的json转换成对象<br>
		/// 对于json转Dictionary情况，复杂的json嵌套也可以转成Dictionary，
		/// 但是每个子节点是json数组（list/map）时，都不能通过强转生成Dictionary，都必须通过此方法生成Dictionary对象
		/// 建议使用JsonToDictionary(string) 方法，子节点可以强转成Dictionary
		/// 将指定的 JSON 数据反序列化成指定对象。
		/// </summary>
		/// <typeparam name="T">对象类型。</typeparam>
		/// <param name="json">JSON 数据。</param>
		/// <returns></returns>
		public static T FromJson<T>(this string json)
		{
			try
			{
				return JsonConvert.DeserializeObject<T>(json, _jsonSettings);
			}
			catch (Exception ex)
			{
				Console.WriteLine(ex + "-----------");
//				Logging.LogManager.Error(new Logging.ExceptionLogInfo()
//				{
//					ExceptionClassName = "YY.SZYD.Shop.Common.Utils.JsonHelper",
//					ExceptionMethod = "ToJson",
//					ExceptionNote = "Json序列化出错",
//					RequestInfo = json,
//				},
//				ex);

				return default(T);
			}
		}
		
		
		
		/// <summary>
		/// 重量级的，可以转换负责的jsonStr
		/// jsonStr转换成 Dictionary<string, object>
		/// </summary>
		/// <param name="jsonData"></param>
		/// <returns> Dictionary<string, object> </returns>
		public static Dictionary<string, object> JsonToDictionary(string jsonStr)
		{

			// using System.Web.Script.Serialization;
			// 实例化JavaScriptSerializer类的新实例
			JavaScriptSerializer jss = new JavaScriptSerializer();
			try
			{
				//将指定的 JSON 字符串转换为 Dictionary<string, object> 类型的对象
				return jss.Deserialize<Dictionary<string, object>>(jsonStr);
			}
			catch (Exception ex)
			{
				throw new Exception(ex.Message);
			}
		}
		
		
		
		/// <summary>
		/// 数据表转键值对集合 www.2cto.com
		/// 把DataTable转成 List集合, 存每一行
		/// 集合中放的是键值对字典,存每一列
		/// </summary>
		/// <param name="dt">数据表</param>
		/// <returns>哈希表数组</returns>
		public static List<Dictionary<string,object>> DataTableToList(DataTable dataTable)
		{
			
			return DataTableToList(dataTable, null);
		}
		
		/// <summary>
		/// 数据表转键值对集合 www.2cto.com
		/// 把DataTable转成 List集合, 存每一行
		/// 集合中放的是键值对字典,存每一列
		/// </summary>
		/// <param name="dataTable"></param>
		/// <param name="dateType">默认yyyy-MM-dd HH:mm:ss</param>
		/// <returns></returns>
		public static List<Dictionary<string,object>> DataTableToList(DataTable dataTable, string dateType)
		{
			if(string.IsNullOrEmpty(dateType))
			{
				dateType = "yyyy-MM-dd HH:mm:ss";
			}
			
			List<Dictionary<string,object>> list = new List<Dictionary<string,object>>();

			foreach(DataRow dr in dataTable.Rows)
			{
				Dictionary<string,object> dic = DataRowToDictionary(dr);
				
				list.Add(dic);
			}
			return list;
		}
		
		
		public static Dictionary<string,object> DataRowToDictionary(DataRow dataRow){
			return DataRowToDictionary(dataRow, null);
		}
		
		
		public static Dictionary<string,object> DataRowToDictionary(DataRow dataRow, string dateType){
			if(string.IsNullOrEmpty(dateType))
			{
				dateType = "yyyy-MM-dd HH:mm:ss";
			}
			
			
			Dictionary<string,object> dic = new Dictionary<string,object>();
			foreach(DataColumn dc in dataRow.Table.Columns)
			{
				//dic.Add(dc.ColumnName, dc.DataType.ToString());
				if(dc.DataType == typeof(int))
				{
					// jsonBuilder.Append(dt.Rows[i][j].ToString());
					dic.Add(dc.ColumnName, dataRow[dc.ColumnName]);
				}
				// else if(dc.DataType == typeof(DateTime) || dc.DataType.ToString() == "MySql.Data.Types.MySqlDateTime")
				else if(dc.DataType == typeof(DateTime) || dc.DataType == typeof(MySql.Data.Types.MySqlDateTime))
				{
					// jsonBuilder.Append( Convert.ToDateTime(dt.Rows[i][j]).ToString(dateType));
					// Convert.ToDateTime(dataRow[dc.ColumnName]).ToString(dateType)
					dic.Add(dc.ColumnName, Convert.ToDateTime(dataRow[dc.ColumnName]).ToString(dateType));
				}
				else
				{
					dic.Add(dc.ColumnName, dataRow[dc.ColumnName]);
				}
			}
			
			return dic;
		}
		
		
		
		
	}
}

