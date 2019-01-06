/*
 * Created by SharpDevelop.
 * User: ww
 * Date: 2013/4/8
 * Time: 18:12
 * 
 * To change this template use Tools | Options | Coding | Edit Standard Headers.
 */
using System;
using System.Collections;
using System.Text;
using System.Collections.Generic;

namespace Emww.DBUtil
{
	/// <summary>
	/// Description of SqlUtil.
	/// </summary>
	public class SqlUtil
	{
		public SqlUtil()
		{
			
		}
		
		/// <summary>
		/// 把dictionary转换成insert语句
		/// </summary>
		/// <param name="table"></param>
		/// <param name="dictionary"></param>
		/// <returns></returns>
		public static String getInsertSql(String table, Dictionary<string, object> dictionary)
		{
			return getSqlByStart(table, "INSERT", dictionary);
		}
		
		/// <summary>
		/// 获取REPLACE语句
		/// </summary>
		/// <param name="table"></param>
		/// <param name="dictionary"></param>
		/// <returns></returns>
		public static String getReplaceSql(String table, Dictionary<string, object> dictionary)
		{
			return getSqlByStart(table, "REPLACE", dictionary);
		}
		
		/// <summary>
		/// 把dictionary转换成insert语句
		/// </summary>
		/// <param name="table"></param>
		/// <param name="dictionary"></param>
		/// <param name="start">INSERT 或者 REPLACE </param>
		/// <returns></returns>
		public static String getSqlByStart(String table, string start, Dictionary<string, object> dictionary)
		{
			var sql = new StringBuilder();
			sql.Append(start);
			sql.Append(" INTO ");
			sql.Append(table);
			sql.Append(" (");

			int i = 0;
			foreach(KeyValuePair<string, object>  kvalue in dictionary)
			{
				sql.Append((i++ > 0) ? ", " : "");
				sql.Append(kvalue.Key);
			}
			sql.Append(") ");
			sql.Append(" VALUES (");
			int j = 0;
			foreach (KeyValuePair<string, object>  kvalue in dictionary)
			{
				sql.Append(j++ > 0 ? ", '" + kvalue.Value + "'" : "'" + kvalue.Value + "'");
			}
			sql.Append(')');
			return sql.ToString();
		}
		
		/// <summary>
		/// 获取跟新语句 如：update t_people set name = '我问问', age = '12', sex = '男', phone = '131654564'
		/// </summary>
		/// <param name="table"></param>
		/// <param name="dictionary"></param>
		/// <returns></returns>
		public static String getUpdateSql(String table, Dictionary<string, object> dictionary)
		{
			var sql = new StringBuilder();
			sql.Append("update ");
			sql.Append(table);
			sql.Append(" set ");

			int i = 0;
			foreach(KeyValuePair<string, object>  kvalue in dictionary)
			{
				sql.Append((i > 0) ? ", " : "");
				sql.Append(kvalue.Key).Append(" = ");
				if(kvalue.Value.GetType() == typeof(int)){
					sql.Append(kvalue.Value);
				}else{
					sql.Append("'" + kvalue.Value + "'");
				}
				i++;
				
			}
			return sql.ToString();
		}

		/// <summary>
		/// 把Hashtable转换成insert语句
		/// </summary>
		/// <param name="table"></param>
		/// <param name="hashtable"></param>
		/// <returns></returns>
		public static String getInsertSql(String table, Hashtable hashtable)
		{
			var sql = new StringBuilder();
			sql.Append("INSERT");
			// sql.Append(CONFLICT_VALUES[conflictAlgorithm]);
			sql.Append(" INTO ");
			sql.Append(table);
			sql.Append(" (");

//			Object[] bindArgs = null;
//			int size = (keyMap != null && keyMap. > 0) ? keyMap.size() : 0;
//			if (size > 0) {
//				bindArgs = new Object[100];
//				int i = 0;
//				for(int i=0; i<size; i++){
//					sql.Append((i > 0) ? ", " : "");
//					sql.Append(keyMap.Keys[i]);
//					bindArgs[i++] = keyMap.get(colName);
//				}
			int i = 0;
			foreach(DictionaryEntry de in hashtable)
			{
				sql.Append((i++ > 0) ? ", " : "");
				sql.Append(de.Key);
//					bindArgs[i++] = de.Value;
			}
			sql.Append(") ");
			sql.Append(" VALUES (");
			int j = 0;
			foreach (DictionaryEntry de in hashtable)
			{
				sql.Append(j++ > 0 ? ", '" + de.Value + "'" : "'" + de.Value + "'");
			}
//			} else {
//				sql.Append(nullColumnHack + ") VALUES (NULL");
//			}
			sql.Append(')');
			return sql.ToString();
		}
		
		
		
		
		
		
		
	}
}
