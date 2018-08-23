package com.example.weibotest08_31.api;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

public class WSdotnet {
	// public static final String NameSpace = "http://zl.emnet.cn/";
	// public static final String NameSpace = "http://test.java.eezz.cn/";
	public static final String NameSpace = "http://demo.java.eezz.cn/";
	public static String get(String methodName, Map<String, Object> params, String url) {
		return get(methodName, params, url, 15000);
	}
	public static String get(String methodName, Map<String, Object> params, String url, int timeOut) {
		try {
			SoapObject so = new SoapObject(NameSpace, methodName);
			Set keySet = params.keySet();
			Iterator it = keySet.iterator();
			while (it.hasNext()) {
				Object key = it.next();
				so.addProperty(key.toString(), params.get(key));
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = so;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(so);
			envelope.encodingStyle = "UTF-8";
			// HttpTransportSE ht = new HttpTransportSE(URL);
			HttpTransportSE transport = new HttpTransportSE(url, timeOut);
			transport.debug = true;
			transport.call(NameSpace + methodName, envelope);
			return envelope.getResponse().toString();
			// return envelope.bodyIn.toString();
		} catch (IOException e) {
			Log.e("WSdotnet","IO================" + e);
			return null;
		} catch (XmlPullParserException e) {
			Log.e("WSdotnet","XmlPullParser===============" + e);
			return null;
		} catch (Exception e) {
			Log.e("WSdotnet","WSClient中e为：" + e);
			return null;
		}

	}
	public static String post(String methodName, Map<String, Object> params, String url) {
		return post(methodName, params, url, 30000);
	}

	public static String post(String methodName, Map<String, Object> params, String url, int timeOut) {
		return get(methodName, params, url, timeOut);
	}
	
	
	
	
	

}
