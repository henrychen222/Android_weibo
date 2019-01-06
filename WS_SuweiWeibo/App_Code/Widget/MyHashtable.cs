using System;
using System.Data;
using System.Configuration;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Collections;
using System.Text.RegularExpressions;
using System.Text;
using System.Web.Script.Serialization;
using System.Collections.Generic;

namespace Emww.Widget
{
	/// <summary>
	/// 解析Json为Hashtable, 所有值保存为字符串
	/// </summary>
	public class MyHashtable : Hashtable
	{
		/// <summary>
		/// 获取json转换是否成功
		/// </summary>
		public bool Success { get; set; }

		/// <summary>
		/// 获取json对应的字符串
		/// </summary>
		public string Value
		{
			get
			{
				return JsonToString(this);
			}
		}

		/// <summary>
		/// 初始化json
		/// </summary>
		/// <param name="value">json 字符串</param>
		public MyHashtable(string value)
		{
			CreateJson(value);
		}
		
		public MyHashtable()
		{
			
		}

		#region CreateJson

		/// <summary>
		/// 创建Json对象
		/// </summary>
		/// <param name="value">json字符串</param>
		private void CreateJson(string value)
		{
			if (value == null)
			{
				return;
			}
			else
			{
				//value = LI.TextTools.Html.unescape(value);
			}

			string name = "";
			Regex nameR = new Regex(@"[a-zA-Z\'\""]+[0-9]*[a-zA-Z\'\""]*[\s]*[\:]");
			Match nameM = nameR.Match(value);
			while (nameM.Success)
			{
				name = nameM.Value.Replace("'", "").Replace("\"", "").Replace(":", "");

				//当前名称的下一个对应字母(', ", [, {, 0-9, 以,和}标记为值的开始则表示值为null
				Regex startR = new Regex(@"[0-9'""\[\{]");
				Match startM = startR.Match(value, nameM.Index + nameM.Length);
				//匹配成功

				if (startM.Success)
				{
					string start = startM.Value;
					int i = startM.Index;
					while (i < value.Length - 1)
					{
						i++;
						//跳过'\'字符
						if (value[i].ToString() == "\\")
						{
							i++;
							continue;
						}
						char vv = value[i];
						start = Start(start, vv);
						if (start == "")
						{
							break;
						}
					}
					string str = value.Substring(startM.Index, i - startM.Index + 1);

					nameM = nameR.Match(value, i + 1);

					char key = str[0];
					if (key == '{')
					{
						this[name] = new MyHashtable(str);
					}
					else if (key == '[')
					{
						this[name] = CreateJsonArr(str);
					}
					else if (key == '\'' || key == '"')
					{
						this[name] = str.Substring(1, str.Length - 2).Replace("\\\\", "\\").Replace("\\'", "'").Replace("\\\"", "\"");
					}
					else
					{
						this[name] = str.Replace(" ", "").Replace("\n", "").Replace("}", "").Replace(",", "").Replace("\r", ""); ;
					}
				}
				else
				{
					Clear();
					Success = false;
					return;
				}
			}
			Success = true;
		}
		#endregion

		
		#region CreateJsonArr
		/// <summary>
		/// 创建Json数组对象
		/// </summary>
		/// <param name="value">数组对象字符串包含[]</param>
		/// <returns></returns>
		public MyHashtable[] CreateJsonArr(string value)
		{
			Regex startR = new Regex(@"[\{]");
			Match startM = startR.Match(value);
			int n = 0;
			while (startM.Success)
			{
				string start = "{";
				int i = startM.Index;
				while (i < value.Length - 1)
				{
					i++;
					if (value[i].ToString() == "\\")
					{
						i++;
						continue;
					}
					start = Start(start, value[i]);
					if (start == "")
					{
						break;
					}
				}
				startM = startR.Match(value, i + 1);
				n++;
			}

			MyHashtable[] myHashtables = new MyHashtable[n];

			startM = startR.Match(value);
			n = 0;
			while (startM.Success)
			{
				string start = "{";
				int i = startM.Index;
				while (i < value.Length - 1)
				{
					i++;
					if (value[i].ToString() == "\\")
					{
						i++;
						continue;
					}
					start = Start(start, value[i]);
					if (start == "")
					{
						break;
					}
				}
				string str = value.Substring(startM.Index, i - startM.Index + 1);
				myHashtables[n] = new MyHashtable(str);
				startM = startR.Match(value, i + 1);
				n++;
			}
			return myHashtables;
		}

		#endregion

		
		#region Start判断

		/// <summary>
		/// 判断出节点包含字符串
		/// </summary>
		/// <param name="start"></param>
		/// <param name="key"></param>
		/// <returns></returns>
		private string Start(string start, char key)
		{
			//模拟伐的最后一个值
			char end = start[start.Length - 1];

			if (key == '\'' || key == '"')
			{
				if (end == key)
				{
					start = start.Substring(0, start.Length - 1);
				}
				else {
					start += key.ToString();
				}
				return start;
			}

			if (end == '{')
			{
				end = '}';
				if (key == '[' || key == '{')
				{
					start += key.ToString();
				}
			}
			else if (end == '[')
			{
				end = ']';
				if (key == '[' || key == '{')
				{
					start += key.ToString();
				}
			}

			//如果是结束字符,则删除对应开始字符
			Regex numR = new Regex(@"[0-9]");
			//如果数字作为开始字符
			if (numR.IsMatch(end.ToString()))
			{
				if (key == ',' || key == '}')
				{
					start = "";
				}
			}
			else
			{
				if (key == end)
				{
					start = start.Substring(0, start.Length - 1);
				}
			}
			return start;
		}

		#endregion


		#region Json转字符串

		public string JsonToString()
		{
			return JsonToString(this);
		}

		private string JsonToString(MyHashtable j)
		{
			string str = "{";
			int n = 0;
			foreach (DictionaryEntry de in j)
			{
				if (n > 0)
				{
					str += ",";
				}
				n++;
				str += "\"" + de.Key.ToString() + "\"" + ":";
				if(de.Value == null)
				{
					str += "null";
				}
				else if (de.Value.ToString().Split('.')[de.Value.ToString().Split('.').Length - 1] == "Json[]")
				{
					MyHashtable[] js = (MyHashtable[])this[de.Key];
					str += "[";
					for (int i = 0; i < js.Length; i++)
					{
						if (i > 0)
						{
							str += ",";
						}
						str += JsonToString(js[i]);
					}
					str += "]";
				}
				else
				{
					str += "\"" + de.Value.ToString().Replace("\\", "\\\\").Replace("'", "\\'").Replace("\"", "\\\"") + "\"";

					string abc = str;
				}
				
			}
			return (str + "}").Replace("\\", "");
		}

		#endregion


		#region 添加键值对
		
		/// <summary>
		/// 给已有的json对象添加一个键值对
		/// </summary>
		/// <param name="name">json数组名称</param>
		/// <param name="aj">需要添加的json对象</param>
		public void Add(string name, MyHashtable aj)
		{
			MyHashtable[] olds = new MyHashtable[0];
			if (this[name] != null)
			{
				olds = (MyHashtable[])this[name];
			}
			MyHashtable[] news = new MyHashtable[olds.Length + 1];
			for (int i = 0; i < olds.Length; i++)
			{
				news[i] = (MyHashtable)olds[i];
			}
			news[olds.Length] = aj;

			this[name] = news;
		}
		
		
		#endregion
		
		
		#region 复杂的jsonStr转换成 Dictionary
		
		/// <summary>
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
			catch (Exception e)
			{
				// throw new Exception(ex.Message);
				return null;
			}
		}
		
		#endregion
		
		
		public static void Main()
		{
			//        	Hashtable ht = new Hashtable(); //file创建一个Hashtable实例
			//        	ht.Add(E,e);//添加keyvalue键值对
			//        	ht.Add(A,a);
			//        	ht.Add(C,c);
			//        	ht.Add(B,b);
//
			//        	string s=(string)ht[A];
			//        	if(ht.Contains(E))// file判断哈希表是否包含特定键,其返回值为true或false
			//        		Console.WriteLine(the E keyexist);
			//        	ht.Remove(C);//移除一个keyvalue键值对
			//        	Console.WriteLine(ht[A]);//此处输出a
			//        	ht.Clear();//移除所有元素
			//        	Console.WriteLine(ht[A]); //file此处将不会有任何输出

			Console.WriteLine("-----------");
		}
		
		
	}
}
