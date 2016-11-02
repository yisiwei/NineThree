package com.ninethree.playchannel.webservice;

import android.content.Context;

import com.ninethree.playchannel.util.ByteUtil;
import com.ninethree.playchannel.util.Constants;
import com.ninethree.playchannel.util.WebServiceUtil;

import org.kobjects.base64.Base64;
import org.ksoap2.serialization.SoapObject;

public class LoginService {

	private static final String URL = "http://m.93966.net:1210/AnWcfLogin/workService.svc";
	private static final String SOAP_ACTION = "http://m.93966.net:1210/workService/AndroidWork";
	private static final String METHOD_NAME = "AndroidWork";

	/**
	 * @Title: login
	 * @Description: 登录
	 * @param param
	 * @return SoapObject
	 * @throws
	 */
	public static SoapObject login(String param) {

		SoapObject result = WebServiceUtil.loadResult(URL, SOAP_ACTION,
				METHOD_NAME, param);

		return result;
	}

	/**
	 * @Title: login
	 * @Description: 登录参数打包
	 * @param context
	 * @param username
	 * @param password
	 * @return String
	 * @throws
	 */
	public static String loginParamPack(Context context, String username,
			String password) {
		byte[] bytes = new byte[0];
		// 打包GUID
		bytes = ByteUtil.ascPackUp(bytes, Constants.LOGIN_GUID);

		// 打包用户名密码
		bytes = ByteUtil.ascPackUp(bytes, username);
		bytes = ByteUtil.ascPackUp(bytes, password);

		// 转为Base64
		String param = Base64.encode(bytes);
		// Log.i("param:" + param);

		return param;
	}
}
