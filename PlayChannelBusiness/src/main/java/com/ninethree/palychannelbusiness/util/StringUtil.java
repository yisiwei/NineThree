package com.ninethree.palychannelbusiness.util;

public class StringUtil {

	public StringUtil() {

	}

	/**
	 * 判断字符串是否为null或空
	 * 
	 * @param string
	 * @return true:为 空或null;false:不为 空或null
	 */
	public static boolean isNullOrEmpty(String string) {
		boolean flag = false;
		if (null == string || string.trim().length() == 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 手机号中间4位以*代替
	 * 
	 * @param phone
	 * @return
	 */
	public static String replacePhone(String phone) {
		String str1 = phone.substring(0, 3);
		String str2 = phone.substring(7, phone.length());
		return str1 + "****" + str2;
	}

	/**
	 * 身份证号最后8位以*代替
	 * 
	 * @param card
	 * @return
	 */
	public static String replaceCard(String card) {
		if (isNullOrEmpty(card)) {
			return "";
		}else if(card.length() == 18){
			return card.substring(0, 10) + "********";
		}else{
			return card;
		}
	}

}
