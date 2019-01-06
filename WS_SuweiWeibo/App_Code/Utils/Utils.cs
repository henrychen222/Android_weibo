using System;
using System.IO;
using System.Linq;
using System.Text;
using System.Web;


namespace Emww.DBUtil
{
    /// <summary>
    /// 普通工具类
    /// </summary>
    public class Utils
    {


        public static bool strIsInt(string str)
        {
            return str2IntBydefaule(str, -1) != -1;
        }


        public static int str2IntBydefaule(string str, int defaultNum)
        {
            try
            {
                return Convert.ToInt32(str);
            }
            catch (Exception)
            {
                return defaultNum;
            }
        }

        public static bool strIsDouble(string str)
        {
            return str2DoubleBydefaule(str, -1) != -1;
        }

        public static double str2DoubleBydefaule(string str, double defaultNum)
        {
            try
            {
                return Convert.ToDouble(str);
            }
            catch (Exception)
            {
                return defaultNum;
            }
        }

        // [WebMethod(Description = "<span style='color:#f74'>插入视图测试：viewName视图名字；viewSql视图sql如：SELECT * FROM AABB</span>")]
        public static string FileUploadImage(string bytestr)
        {
            string fileName = "";
            try
            {
                Random random = new Random();
                string i = random.Next(0, 10000000).ToString();
                fileName = DateTime.Now.Year.ToString() + DateTime.Now.Month + DateTime.Now.Day + DateTime.Now.Hour + DateTime.Now.Minute + DateTime.Now.Second;
                fileName = fileName + ".jpg";
                bool flag = StringToFile(bytestr, System.Web.HttpContext.Current.Server.MapPath("image\\") + "" + fileName);
                return fileName;

            }
            catch (Exception e)
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


        public static string fileToString(string fileName)
        {
            FileStream fs = new FileStream(@"E:\\19.png", FileMode.Open, FileAccess.Read);
            BinaryReader br = new BinaryReader(fs);
            byte[] buffer = new byte[1024];
            //MemoryStream ms = new MemoryStream();
            string result = "";
            int i = 0;
            while (i < fs.Length)
            {
                int read = br.Read(buffer, 0, buffer.Length);
                i += read;
                result += Encoding.Default.GetString(buffer);
                // ms.Write(buffer, 0, read);
            }
            fs.Close();
            br.Close();
            //Console.WriteLine(ms.Length);
            return result;

            //FileStream file = new FileStream(fileName, FileMode.Open, FileAccess.Read);
            //BinaryReader read = new BinaryReader(file);
            //int count = (int)file.Length;
            //byte[] buffer = new byte[count];
            //read.Read(buffer, 0, buffer.Length);
            //string msg = Encoding.Default.GetString(buffer);

           // FileStream fs = new FileStream(fileName, FileMode.Open, FileAccess.Read);
           //// System.IO.FileStream fs = new System.IO.FileStream(fileName,System.IO.FileMode.Create);
           // System.IO.BinaryReader br = new System.IO.BinaryReader(fs);
           // int count = (int)fs.Length;
           // byte[] buffer = new byte[count];
           // br.Read(buffer, 0, buffer.Length);
           // string msg = Encoding.Default.GetString(buffer);
           // return msg;

            //float aspectRatio;
            //string tempDirectory = "";
            //int autoSaveTime;
            //bool showStatusBar;

            //if (File.Exists(fileName))
            //{
            //    using (BinaryReader reader = new BinaryReader(File.Open(fileName, FileMode.Open)))
            //    {
            //        aspectRatio = reader.ReadSingle();
            //        tempDirectory = reader.ReadString();
            //        autoSaveTime = reader.ReadInt32();
            //        showStatusBar = reader.ReadBoolean();
            //    }

            //    Console.WriteLine("Aspect ratio set to: " + aspectRatio);
            //    Console.WriteLine("Temp directory is: " + tempDirectory);
            //    Console.WriteLine("Auto save time set to: " + autoSaveTime);
            //    Console.WriteLine("Show status bar: " + showStatusBar);
            //}

            //return tempDirectory;
        }


        public static string getParam(HttpContext context, string key)
        {
            string value = getParamByGet(context, key);
            if (String.IsNullOrEmpty(value))
            {
                value = getParamByPost(context, key);
            }
            return value;
        }

        public static string getParamByGet(HttpContext context, string key)
        {
            return context.Request.QueryString[key];
        }
        public static string getParamByPost(HttpContext context, string key)
        {
            return context.Request.Form[key];
        }






    }
}