using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Web;

namespace Emww.DBUtil
{

	/// <summary>
	/// JsonUtil 的摘要说明
	/// </summary>
	public static class Db2JsonUtil
	{

		
		public static string DataTable2Json(DataTable dt)
		{
			return DataTable2Json(dt, null, null);
		}
		/// <summary>
		/// 把表（DataTable）转换成json
		/// </summary>
		/// <param name="dt">主表查出的json</param>
		/// <param name="arrayName">嵌入的子表key名</param>
		/// <param name="subDt">嵌入的子表值，如[{},{}]或者string值</param>
		/// <returns>[ {"id":"1","name":"ww ","age":"12","pass":"ww ", "prdt": [{"id": "1","pName": "焊锡"},{"id": "2","pName": "锡丝"}]},
		/// {"id":"2","name":"java ","age":"23","pass":"java", "prdt": [{"id": "1","pName": "焊锡"},{"id": "2","pName": "锡丝"}]} ]</returns>
		public static string DataTable2Json(DataTable dt, string arrayName, object subDt)
		{
			if (dt != null && dt.Rows.Count > 0)
			{
				var jsonBuilder = new StringBuilder();
				//jsonBuilder.Append("{");
				//jsonBuilder.Append("ds");
				jsonBuilder.Append("[");
				for (int i = 0; i < dt.Rows.Count; i++)
				{
					jsonBuilder.Append("{");
					for (int j = 0; j < dt.Columns.Count; j++)
					{
						//jsonBuilder.Append("\"");
						jsonBuilder.Append("\"" + dt.Columns[j].ColumnName + "\"");
						jsonBuilder.Append(":\"");
						jsonBuilder.Append(dt.Rows[i][j].ToString().Trim().Replace("\"", "'").Replace("\r\n", "<BR>"));
						jsonBuilder.Append("\",");
					}
					if(arrayName != null || subDt != null)
					{
						
						if(subDt.GetType() == typeof(DataTable))
						{
							string subStr = DataTable2Json(subDt as DataTable);
							jsonBuilder.Append("\"" + arrayName + "\"");
							jsonBuilder.Append(":");
							jsonBuilder.Append(subStr.Trim().Replace("\"", "'").Replace("\r\n", "<BR>"));
						}
						else if(subDt.GetType() == typeof(String))
						{
							jsonBuilder.Append("\"" + arrayName + "\"");
							jsonBuilder.Append(":\"");
							jsonBuilder.Append(subDt.ToString().Trim().Replace("\"", "'").Replace("\r\n", "<BR>"));
							jsonBuilder.Append("\"");
						}
						else
						{
							jsonBuilder.Remove(jsonBuilder.Length - 1, 1);
						}
					}
					else
					{
						jsonBuilder.Remove(jsonBuilder.Length - 1, 1);
					}
					jsonBuilder.Append("},");
				}
				jsonBuilder.Remove(jsonBuilder.Length - 1, 1);
				jsonBuilder.Append("]");
				// jsonBuilder.Append("}");
				return jsonBuilder.ToString();
			}
			else
			{
				return "";
			}
		}

		
		/// <summary>
		/// 数据库字段类型（int string dataTime）
		/// </summary>
		/// <param name="dt"></param>
		/// <returns></returns>
		public static string DataTable2JsonList(DataTable dt)
		{
			return DataTable2JsonList(dt, null);
		}
		
		/// <summary>
		/// 
		/// </summary>
		/// <param name="dt"></param>
		/// <param name="dateType"></param>
		/// <returns></returns>
		public static string DataTable2JsonList(DataTable dt, string dateType)
		{
			if(string.IsNullOrEmpty(dateType))
			{
				dateType = "yyyy-MM-dd HH:mm:ss";
			}
			
			if (dt != null && dt.Rows.Count > 0)
			{
				var jsonBuilder = new StringBuilder();
				//jsonBuilder.Append("{");
				//jsonBuilder.Append("ds");
				jsonBuilder.Append("[");
				for (int i = 0; i < dt.Rows.Count; i++)
				{
					jsonBuilder.Append("{");
					for (int j = 0; j < dt.Columns.Count; j++)
					{
						//jsonBuilder.Append("\"");
						jsonBuilder.Append("\"" + dt.Columns[j].ColumnName + "\"");
						if(dt.Columns[j].DataType == typeof(int))
						{
							jsonBuilder.Append(":");
							jsonBuilder.Append(dt.Rows[i][j].ToString());
							jsonBuilder.Append(",");
						}
						// else if(dt.Columns[j].DataType == typeof(DateTime) || dt.Columns[j].DataType.ToString() == "MySql.Data.Types.MySqlDateTime")
						else if(dt.Columns[j].DataType == typeof(DateTime) || dt.Columns[j].DataType == typeof(MySql.Data.Types.MySqlDateTime))
						{
							jsonBuilder.Append(":\"");
							jsonBuilder.Append( Convert.ToDateTime(dt.Rows[i][j]).ToString(dateType));
//							jsonBuilder.Append(dt.Columns[j].DataType.ToString());
							jsonBuilder.Append("\",");
						}
						else
						{
							jsonBuilder.Append(":\"");
							jsonBuilder.Append(dt.Rows[i][j].ToString().Trim().Replace("\"", "'").Replace("\r\n", "<BR>"));
							jsonBuilder.Append("\",");
						}
					}
					
					jsonBuilder.Remove(jsonBuilder.Length - 1, 1);
					jsonBuilder.Append("},");
				}
				jsonBuilder.Remove(jsonBuilder.Length - 1, 1);
				jsonBuilder.Append("]");
				// jsonBuilder.Append("}");
				return jsonBuilder.ToString();
			}
			else
			{
				return "";
			}
		}
		
		
		#region 根据表名生成Json格式的String(3重载)


		/// <summary>
		/// 根据表名生成Json格式的String
		/// </summary>
		/// <param name="dr">SqlDataReader对象</param>
		/// <param name="strTableName">AABB</param>
		/// <returns>{"AABB":[{"id":"1","name":"ww ","age":"12","pass":"ww "},{"id":"2","name":"java ","age":"23","pass":"java "}]}</returns>
		public static string DataReader2JSON(SqlDataReader dr, string strTableName)
		{
			StringBuilder builder = new StringBuilder();
			builder.Append("{\"");
			builder.Append(strTableName);
			builder.Append("\":");
			builder.Append(DataReader2JSON(dr));
			builder.Append("}");
			return builder.ToString();
		}
		
		public static string DataReader2JSON(SqlDataReader dr)
		{
			return DataReader2JSON(dr, null, null);
		}
		
		
		/// <summary>
		/// 把表生成Json格式的String
		/// </summary>
		/// <param name="dt">主表查出的json</param>
		/// <param name="arrayName">嵌入的子表key名</param>
		/// <param name="subDt">嵌入的子表值，如[{},{}]或者string值</param>
		/// <returns>[ {"id":"1","name":"ww ","age":"12","pass":"ww ", "prdt": [{"id": "1","pName": "焊锡"},{"id": "2","pName": "锡丝"}]},
		/// {"id":"2","name":"java ","age":"23","pass":"java", "prdt": [{"id": "1","pName": "焊锡"},{"id": "2","pName": "锡丝"}]} ]</returns>
		public static string DataReader2JSON(SqlDataReader dr, string arrayName, object subDt)
		{
			StringBuilder builder = new StringBuilder();
			builder.Append("[");
			bool flag = false;
			try
			{
				while (dr.Read())
				{
					flag = true;
					builder.Append("{");
					for (int i = 0; i < dr.FieldCount; i++)
					{
						builder.Append("\"");
						builder.Append(dr.GetName(i));
						builder.Append("\":\"");
						builder.Append(dr.GetValue(i).ToString().Replace(@"\", @"\\").Replace("\"", "\\\""));
						// 如果是最后一项加上一个引号，否则加上引号和逗号
						if (i == (dr.FieldCount - 1))
						{
							if (arrayName != null && subDt != null)
							{
								
								if(subDt.GetType() == typeof(DataTable))
								{
									builder.Append("\",");
									string subStr = DataTable2Json(subDt as DataTable);
									builder.Append("\"" + arrayName + "\"");
									builder.Append(":");
									builder.Append(subStr.Trim().Replace("\"", "'").Replace("\r\n", "<BR>"));
								}
								else if(subDt.GetType() == typeof(String))
								{
									builder.Append("\",");
									builder.Append("\"" + arrayName + "\"");
									builder.Append(":\"");
									builder.Append(subDt.ToString().Trim().Replace("\"", "'").Replace("\r\n", "<BR>"));
									builder.Append("\"");
								}
								else
								{
									builder.Append("\"");
								}
								
							}
							else
							{
								builder.Append("\"");
							}
						}
						else
						{
							builder.Append("\",");
						}
					}
					builder.Append("},");
				}
				dr.Close();
				if (flag)
				{
					builder.Remove(builder.Length - 1, 1);
				}
			}
			catch
			{
				dr.Close();
			}
			builder.Append("]");
			return builder.ToString();
		}
		

		#endregion

	
	
	/**
	 * --------------------------------------------------------------------------
	 * 
	 */
	
	
	
	}
}