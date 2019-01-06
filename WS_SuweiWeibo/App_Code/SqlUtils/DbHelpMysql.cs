using System;
using System.Collections.Generic;
using System.Web;
using System.Data.OleDb;
using System.Data;
using System.Collections;
using System.Web.UI.WebControls;
using Maticsoft.DBUtility;
using System.Web.Security;
using MySql.Data.MySqlClient;
using System.Configuration;


namespace Emww.DBUtil
{

	/// <summary>
	///DBClassNew 的摘要说明
	/// </summary>
	public class DbHelpMysql
	{
		
		//法一：
//		public static string connectionString = ConfigurationManager.AppSettings["ConnectionMySql"];
		//法二：
		public static string connectionString = ConfigurationManager.ConnectionStrings["MySqlStr"].ConnectionString;


		public DbHelpMysql()
		{
		}


		#region 公用方法

		//public static int GetMaxID(string FieldName, string TableName)
		//{
		//    string strsql = "select max(" + FieldName + ")+1 from " + TableName;
		//    object obj = DbHelperSQL.GetSingle(strsql);
		//    if (obj == null)
		//    {
		//        return 1;
		//    }
		//    else
		//    {
		//        return int.Parse(obj.ToString());
		//    }
		//}
		//public static bool Exists(string strSql)
		//{
		//    object obj = DbHelperSQL.GetSingle(strSql);
		//    int cmdresult;
		//    if ((Object.Equals(obj, null)) || (Object.Equals(obj, System.DBNull.Value)))
		//    {
		//        cmdresult = 0;
		//    }
		//    else
		//    {
		//        cmdresult = int.Parse(obj.ToString());
		//    }
		//    if (cmdresult == 0)
		//    {
		//        return false;
		//    }
		//    else
		//    {
		//        return true;
		//    }
		//}
		public bool Exists(string strSql, params MySqlParameter[] cmdParms)
		{
			object obj = GetSingle(strSql, cmdParms);
			int cmdresult;
			if ((Object.Equals(obj, null)) || (Object.Equals(obj, System.DBNull.Value)))
			{
				cmdresult = 0;
			}
			else
			{
				cmdresult = int.Parse(obj.ToString());
			}
			if (cmdresult == 0)
			{
				return false;
			}
			else
			{
				return true;
			}
		}

		#endregion

		#region  执行简单SQL语句

		/// <summary>
		/// 执行SQL语句，返回影响的记录数
		/// </summary>
		/// <param name="SQLString">SQL语句</param>
		/// <returns>影响的记录数</returns>
		public int ExecuteSql(string SQLString)
		{
			using (MySqlConnection connection = new MySqlConnection(connectionString))
			{
				using (MySqlCommand cmd = new MySqlCommand(SQLString, connection))
				{
					try
					{
						connection.Open();
						int rows = cmd.ExecuteNonQuery();
						return rows;
					}
					catch (MySql.Data.MySqlClient.MySqlException E)
					{
						connection.Close();
						throw new Exception(E.Message);
					}
				}
			}
		}

		/// <summary>
		/// 执行多条SQL语句，实现数据库事务。
		/// </summary>
		/// <param name="SQLStringList">多条SQL语句</param>
		public void ExecuteSqlTran(ArrayList SQLStringList)
		{
			using (MySqlConnection conn = new MySqlConnection(connectionString))
			{
				conn.Open();
				MySqlCommand cmd = new MySqlCommand();
				cmd.Connection = conn;
				MySqlTransaction tx = conn.BeginTransaction();
				cmd.Transaction = tx;
				try
				{
					for (int n = 0; n < SQLStringList.Count; n++)
					{
						string strsql = SQLStringList[n].ToString();
						if (strsql.Trim().Length > 1)
						{
							cmd.CommandText = strsql;
							cmd.ExecuteNonQuery();
						}
					}
					tx.Commit();
				}
				catch (MySql.Data.MySqlClient.MySqlException E)
				{
					tx.Rollback();
					throw new Exception(E.Message);
				}
			}
		}
		/// <summary>
		/// 执行带一个存储过程参数的的SQL语句。
		/// </summary>
		/// <param name="SQLString">SQL语句</param>
		/// <param name="content">参数内容,比如一个字段是格式复杂的文章，有特殊符号，可以通过这个方式添加</param>
		/// <returns>影响的记录数</returns>
		public int ExecuteSql(string SQLString, string content)
		{
			using (MySqlConnection connection = new MySqlConnection(connectionString))
			{
				MySqlCommand cmd = new MySqlCommand(SQLString, connection);
				MySql.Data.MySqlClient.MySqlParameter myParameter = new MySql.Data.MySqlClient.MySqlParameter("@fs", MySqlDbType.Binary);
				myParameter.Value = content;
				cmd.Parameters.Add(myParameter);
				try
				{
					connection.Open();
					int rows = cmd.ExecuteNonQuery();
					return rows;
				}
				catch (MySql.Data.MySqlClient.MySqlException E)
				{
					throw new Exception(E.Message);
				}
				finally
				{
					cmd.Dispose();
					connection.Close();
				}
			}
		}
		/// <summary>
		/// 向数据库里插入图像格式的字段(和上面情况类似的另一种实例)
		/// </summary>
		/// <param name="strSQL">SQL语句</param>
		/// <param name="fs">图像字节,数据库的字段类型为image的情况</param>
		/// <returns>影响的记录数</returns>
		public int ExecuteSqlInsertImg(string strSQL, byte[] fs)
		{
			using (MySqlConnection connection = new MySqlConnection(connectionString))
			{
				MySqlCommand cmd = new MySqlCommand(strSQL, connection);
				MySql.Data.MySqlClient.MySqlParameter myParameter = new MySql.Data.MySqlClient.MySqlParameter("@fs", MySqlDbType.Binary);
				myParameter.Value = fs;
				cmd.Parameters.Add(myParameter);
				try
				{
					connection.Open();
					int rows = cmd.ExecuteNonQuery();
					return rows;
				}
				catch (MySql.Data.MySqlClient.MySqlException E)
				{
					throw new Exception(E.Message);
				}
				finally
				{
					cmd.Dispose();
					connection.Close();
				}
			}
		}

		/// <summary>
		/// 执行一条计算查询结果语句，返回查询结果（object）。
		/// </summary>
		/// <param name="SQLString">计算查询结果语句</param>
		/// <returns>查询结果（object）</returns>
		public object GetSingle(string SQLString)
		{
			using (MySqlConnection connection = new MySqlConnection(connectionString))
			{
				using (MySqlCommand cmd = new MySqlCommand(SQLString, connection))
				{
					try
					{
						connection.Open();
						object obj = cmd.ExecuteScalar();
						if ((Object.Equals(obj, null)) || (Object.Equals(obj, System.DBNull.Value)))
						{
							return null;
						}
						else
						{
							return obj;
						}
					}
					catch (MySql.Data.MySqlClient.MySqlException e)
					{
						connection.Close();
						throw new Exception(e.Message);
					}
				}
			}
		}
		/// <summary>
		/// 执行查询语句，返回OleDbDataReader
		/// </summary>
		/// <param name="strSQL">查询语句</param>
		/// <returns>OleDbDataReader</returns>
		public MySqlDataReader ExecuteReader(string strSQL)
		{
			MySqlConnection connection = new MySqlConnection(connectionString);
			MySqlCommand cmd = new MySqlCommand(strSQL, connection);
			try
			{
				connection.Open();
				MySqlDataReader myReader = cmd.ExecuteReader();
				return myReader;
			}
			catch (MySql.Data.MySqlClient.MySqlException e)
			{
				throw new Exception(e.Message);
			}

		}
		/// <summary>
		/// 执行查询语句，返回DataSet
		/// </summary>
		/// <param name="SQLString">查询语句</param>
		/// <returns>DataSet</returns>
		public DataSet seleDB(string SQLString)
		{
			using (MySqlConnection connection = new MySqlConnection(connectionString))
			{
				DataSet ds = new DataSet();
				//try
				//{
				//    connection.Open();
				//    MySqlDataAdapter command = new MySqlDataAdapter(SQLString, connection);
				//    command.Fill(ds, "ds");
				
				ds = MySqlHelper.ExecuteDataset(connection, SQLString);
				//}
				//catch (MySql.Data.MySqlClient.MySqlException ex)
				//{
				//    throw new Exception(ex.Message);
				//}
				return ds;
			}
		}

		/// <summary>
		/// 绑定DropDownList
		/// </summary>
		/// <param name="SQLString">查询语句</param>
		/// <param name="drop">控件ID</param>
		/// <param name="text">显示字段</param>
		/// <param name="value">关键字段</param>
		public void BindDrop(string SQLString, DropDownList drop, string text, string value)
		{
			using (MySqlConnection connection = new MySqlConnection(connectionString))
			{
				DataSet ds = new DataSet();
				try
				{
					connection.Open();
					MySqlDataAdapter command = new MySqlDataAdapter(SQLString, connection);
					command.Fill(ds, "ds");
					if (ds != null && ds.Tables.Count > 0)
					{
						//drop.DataSource = ds;
						//drop.DataTextField = text;
						//drop.DataValueField = value;
						//drop.DataBind();
						for (int i = 0; i < ds.Tables[0].Rows.Count; i++)
						{
							drop.Items.Add(new ListItem(ds.Tables[0].Rows[i][text].ToString(), ds.Tables[0].Rows[i][value].ToString()));
						}
					}
				}
				catch (MySql.Data.MySqlClient.MySqlException ex)
				{
					throw new Exception(ex.Message);
				}
			}
		}


		#endregion

		#region 执行带参数的SQL语句

		/// <summary>
		/// 执行SQL语句，返回影响的记录数
		/// </summary>
		/// <param name="SQLString">SQL语句</param>
		/// <returns>影响的记录数</returns>
		public int ExecuteSql(string SQLString, params MySqlParameter[] cmdParms)
		{
			using (MySqlConnection connection = new MySqlConnection(connectionString))
			{
				using (MySqlCommand cmd = new MySqlCommand())
				{
					try
					{
						PrepareCommand(cmd, connection, null, SQLString, cmdParms);
						int rows = cmd.ExecuteNonQuery();
						cmd.Parameters.Clear();
						return rows;
					}
					catch (MySql.Data.MySqlClient.MySqlException E)
					{
						throw new Exception(E.Message);
					}
				}
			}
		}

		/// <summary>
		/// 执行Sql更新语句
		/// </summary>
		/// <param name="sqlstr">传入的Sql语句</param>
		/// <returns>布尔值</returns>
		public bool ExecuteUpdate(string sqlstr)
		{
			using (MySqlConnection connection = new MySqlConnection(connectionString))
			{
				using (MySqlCommand cmd = new MySqlCommand())
				{
					try
					{
						PrepareCommand(cmd, connection, null, sqlstr,null);
						int rows = cmd.ExecuteNonQuery();
						cmd.Parameters.Clear();
						if (rows > 0)
						{
							return true;
						}
						else
						{
							return false;
						}
					}
					catch (MySql.Data.MySqlClient.MySqlException E)
					{
						throw new Exception(E.Message);
					}
				}
			}
		}

		/// <summary>
		/// 执行Sql更新语句
		/// </summary>
		/// <param name="sqlstr">传入的Sql语句</param>
		/// <returns>刚插入的ID</returns>
		public int GetNewId(string sqlstr)
		{
			using (MySqlConnection connection = new MySqlConnection(connectionString))
			{
				using (MySqlCommand cmd = new MySqlCommand())
				{
					try
					{
						PrepareCommand(cmd, connection, null, sqlstr, null);
						int rows = cmd.ExecuteNonQuery();
						cmd.CommandText = "SELECT @@IDENTITY";
						int newId = (int)cmd.ExecuteScalar();
						cmd.Parameters.Clear();
						if (rows > 0)
						{
							return newId;
						}
						else
						{
							return 0;
						}
					}
					catch (MySql.Data.MySqlClient.MySqlException E)
					{
						throw new Exception(E.Message);
					}
				}
			}
		}

		/// <summary>
		/// 执行多条SQL语句，实现数据库事务。
		/// </summary>
		/// <param name="SQLStringList">SQL语句的哈希表（key为sql语句，value是该语句的OleDbParameter[]）</param>
		public void ExecuteSqlTran(Hashtable SQLStringList)
		{
			using (MySqlConnection conn = new MySqlConnection(connectionString))
			{
				conn.Open();
				using (MySqlTransaction trans = conn.BeginTransaction())
				{
					MySqlCommand cmd = new MySqlCommand();
					try
					{
						//循环
						foreach (DictionaryEntry myDE in SQLStringList)
						{
							string cmdText = myDE.Key.ToString();
							MySqlParameter[] cmdParms = (MySqlParameter[])myDE.Value;
							PrepareCommand(cmd, conn, trans, cmdText, cmdParms);
							int val = cmd.ExecuteNonQuery();
							cmd.Parameters.Clear();

							trans.Commit();
						}
					}
					catch
					{
						trans.Rollback();
						throw;
					}
				}
			}
		}


		/// <summary>
		/// 执行一条计算查询结果语句，返回查询结果（object）。
		/// </summary>
		/// <param name="SQLString">计算查询结果语句</param>
		/// <returns>查询结果（object）</returns>
		public object GetSingle(string SQLString, params MySqlParameter[] cmdParms)
		{
			using (MySqlConnection connection = new MySqlConnection(connectionString))
			{
				using (MySqlCommand cmd = new MySqlCommand())
				{
					try
					{
						PrepareCommand(cmd, connection, null, SQLString, cmdParms);
						object obj = cmd.ExecuteScalar();
						cmd.Parameters.Clear();
						if ((Object.Equals(obj, null)) || (Object.Equals(obj, System.DBNull.Value)))
						{
							return null;
						}
						else
						{
							return obj;
						}
					}
					catch (MySql.Data.MySqlClient.MySqlException e)
					{
						throw new Exception(e.Message);
					}
				}
			}
		}

		/// <summary>
		/// 执行查询语句，返回OleDbDataReader
		/// </summary>
		/// <param name="strSQL">查询语句</param>
		/// <returns>OleDbDataReader</returns>
		public MySqlDataReader ExecuteReader(string SQLString, params MySqlParameter[] cmdParms)
		{
			MySqlConnection connection = new MySqlConnection(connectionString);
			MySqlCommand cmd = new MySqlCommand();
			try
			{
				PrepareCommand(cmd, connection, null, SQLString, cmdParms);
				MySqlDataReader myReader = cmd.ExecuteReader();
				cmd.Parameters.Clear();
				return myReader;
			}
			catch (MySql.Data.MySqlClient.MySqlException e)
			{
				throw new Exception(e.Message);
			}

		}

		/// <summary>
		/// 执行查询语句，返回DataSet
		/// </summary>
		/// <param name="SQLString">查询语句</param>
		/// <returns>DataSet</returns>
		public DataSet Query(string SQLString, params MySqlParameter[] cmdParms)
		{
			using (MySqlConnection connection = new MySqlConnection(connectionString))
			{
				MySqlCommand cmd = new MySqlCommand();
				PrepareCommand(cmd, connection, null, SQLString, cmdParms);
				using (MySqlDataAdapter da = new MySqlDataAdapter(cmd))
				{
					DataSet ds = new DataSet();
					try
					{
						da.Fill(ds, "ds");
						cmd.Parameters.Clear();
					}
					catch (MySql.Data.MySqlClient.MySqlException ex)
					{
						throw new Exception(ex.Message);
					}
					return ds;
				}
			}
		}

		public void GridViewBD(GridView gridview, string sql, string id)
		{
			using (MySqlConnection connection = new MySqlConnection(connectionString))
			{
				MySqlCommand cmd = new MySqlCommand();
				PrepareCommand(cmd, connection, null, sql, null);
				using (MySqlDataAdapter da = new MySqlDataAdapter(cmd))
				{
					DataSet ds = new DataSet();
					try
					{
						da.Fill(ds, "ds");
						cmd.Parameters.Clear();
						if (ds != null && ds.Tables.Count > 0)
						{
							gridview.DataSource = ds;
							gridview.DataKeyNames = new string[] { id};
							gridview.DataBind();
						}
					}
					catch (MySql.Data.MySqlClient.MySqlException ex)
					{
						throw new Exception(ex.Message);
					}
				}
			}
		}

		public void DataListBD(DataList datalis, string sql)
		{
			using (MySqlConnection connection = new MySqlConnection(connectionString))
			{
				MySqlCommand cmd = new MySqlCommand();
				PrepareCommand(cmd, connection, null, sql, null);
				using (MySqlDataAdapter da = new MySqlDataAdapter(cmd))
				{
					DataSet ds = new DataSet();
					try
					{
						da.Fill(ds, "ds");
						cmd.Parameters.Clear();
						if (ds != null && ds.Tables.Count > 0)
						{
							datalis.DataSource = ds;
							//gridview.DataKeyNames = new string[] { id };
							datalis.DataBind();
						}
					}
					catch (MySql.Data.MySqlClient.MySqlException ex)
					{
						throw new Exception(ex.Message);
					}
				}
			}
		}

		public void DataListBD(DataList datalis, string sql, string id)
		{
			using (MySqlConnection connection = new MySqlConnection(connectionString))
			{
				MySqlCommand cmd = new MySqlCommand();
				PrepareCommand(cmd, connection, null, sql, null);
				using (MySqlDataAdapter da = new MySqlDataAdapter(cmd))
				{
					DataSet ds = new DataSet();
					try
					{
						da.Fill(ds, "ds");
						cmd.Parameters.Clear();
						if (ds != null && ds.Tables.Count > 0)
						{
							datalis.DataSource = ds;
							//gridview.DataKeyNames = new string[] { id };
							datalis.DataBind();
						}
					}
					catch (MySql.Data.MySqlClient.MySqlException ex)
					{
						throw new Exception(ex.Message);
					}
				}
			}
		}

		private void PrepareCommand(MySqlCommand cmd, MySqlConnection conn, MySqlTransaction trans, string cmdText, MySqlParameter[] cmdParms)
		{
			if (conn.State != ConnectionState.Open)
				conn.Open();
			cmd.Connection = conn;
			cmd.CommandText = cmdText;
			if (trans != null)
				cmd.Transaction = trans;
			cmd.CommandType = CommandType.Text;//cmdType;
			if (cmdParms != null)
			{
				foreach (MySqlParameter parm in cmdParms)
					cmd.Parameters.Add(parm);
			}
		}

		#endregion

		public string MD5(string toCryString)
		{
			return FormsAuthentication.HashPasswordForStoringInConfigFile(toCryString, "MD5");
		}
		public string MD5(string toCryString,int code)
		{
			if (code == 16) //16位MD5加密（取32位加密的9~25字符）
			{
				return FormsAuthentication.HashPasswordForStoringInConfigFile(toCryString, "MD5");
			}
			if (code == 32)
			{
				return FormsAuthentication.HashPasswordForStoringInConfigFile(toCryString, "MD5").ToLower();
			}
			return toCryString;
		}
	}


}