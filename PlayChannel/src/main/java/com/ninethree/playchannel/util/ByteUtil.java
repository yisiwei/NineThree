package com.ninethree.playchannel.util;

import com.ninethree.playchannel.bean.AscPackIntResult;
import com.ninethree.playchannel.bean.AscPackResult;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class ByteUtil {

	/**
	 * 
	 * @Title: getBytes
	 * @Description:截取原byte[]
	 * @param bytes
	 * @param startIndex
	 *            开始位置
	 * @return byte[]
	 * @throws
	 */
	public static byte[] getBytes(byte[] bytes, int startIndex) {
		int length = bytes.length - startIndex;
		return getBytes(bytes, startIndex, length);
	}

	/**
	 * <pre>
	 * arraycopy(src, srcPos, dest, destPos, length)
	 * 从指定源数组中复制一个数组，复制从指定的位置开始，到目标数组的指定位置结束。 
	 * srcPos:  原数组中要开始复制的第一个元素的位置 
	 * destPos: 目标数组中要开始替换的第一个元素的位置
	 * length:  要复制的元素的个数
	 * </pre>
	 * 
	 * @param bytes
	 * @param startIndex
	 * @param length
	 * @return
	 */
	public static byte[] getBytes(byte[] bytes, int startIndex, int length) {
		byte[] temp = new byte[length];
		System.arraycopy(bytes, startIndex, temp, 0, length);
		return temp;
	}

	/**
	 * 
	 * @Title: getBytes
	 * @Description: 顺序打包基础方法
	 * @param bytes
	 * @param bytesNew
	 * @return byte[]
	 * @throws
	 */
	public static byte[] getBytes(byte[] bytes, byte[] bytesNew) {
		if (bytesNew.length == 0) {
			return bytes;
		}

		byte[] temp = new byte[bytes.length + bytesNew.length];
		int count = bytes.length;
		int StartIndex = 0;
		System.arraycopy(bytes, 0, temp, StartIndex, count);

		count = bytesNew.length;
		StartIndex = bytes.length;
		System.arraycopy(bytesNew, 0, temp, StartIndex, count);

		return temp;
	}

	// /////////////////////////////// 打包 ///////////////////////////////

	/**
	 * 
	 * @Title: getBytes
	 * @Description: Map打包
	 * @param map
	 * @return byte[]
	 * @throws
	 * @author yisiwei
	 */
	public static byte[] getBytes(Map<String, String> map) {
		byte[] bytes = new byte[0];
		for (String key : map.keySet()) {
			Log.i("key=" + key + ",value=" + map.get(key));
			bytes = ascPackUp(bytes, key);
			bytes = ascPackUp(bytes, map.get(key));
		}
		return bytes;
	}

	/**
	 * 
	 * @Title: ascPackUp
	 * @Description: 将short打包到byte[]
	 * @param bytes
	 * @param myShort
	 * @return byte[]
	 * @throws
	 * @author yisiwei
	 */
	public static byte[] ascPackUp(byte[] bytes, short myShort) {
		byte[] temp = shortToByte(myShort);
		bytes = getBytes(bytes, temp);
		return bytes;
	}

	/**
	 * 
	 * @Title: ascPackUp
	 * @Description: 将int打包到byte[]
	 * @param bytes
	 * @param myInt
	 * @return byte[]
	 * @throws
	 * @author yisiwei
	 */
	public static byte[] ascPackUp(byte[] bytes, int myInt) {
		byte[] temp = intToByte(myInt);
		return bytes = getBytes(bytes, temp);
	}

	/**
	 * 
	 * @Title: ascPackUp
	 * @Description: 将long打包到byte[]
	 * @param bytes
	 * @param myLong
	 * @return byte[]
	 * @throws
	 * @author yisiwei
	 */
	public static byte[] ascPackUp(byte[] bytes, long myLong) {
		byte[] temp = longToByte(myLong);
		return bytes = getBytes(bytes, temp);
	}

	/**
	 * 
	 * @Title: ascPackUp
	 * @Description: 将boolean打包到byte[]
	 * @param bytes
	 * @param myBool
	 * @return byte[]
	 * @throws
	 * @author yisiwei
	 */
	public static byte[] ascPackUp(byte[] bytes, boolean myBool) {
		byte[] temp = booleanToByte(myBool);
		return bytes = getBytes(bytes, temp);
	}

	/**
	 * 
	 * @Title: ascPackUp
	 * @Description:将String打包到byte[]
	 * @param bytes
	 * @param myString
	 * @return byte[]
	 * @throws
	 * @author yisiwei
	 */
	public static byte[] ascPackUp(byte[] bytes, String myString) {
		byte[] temp = null;
		try {
			temp = myString.getBytes("GB2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		bytes = getBytes(bytes, intToByte(temp.length));
		return bytes = getBytes(bytes, temp);
	}

	/**
	 * 
	 * @Title: ascPackUp
	 * @Description: 将byte[]打包到byte[]
	 * @param bytes
	 * @param bytesNew
	 * @return byte[]
	 * @throws
	 * @author yisiwei
	 */
	public static byte[] ascPackUp(byte[] bytes, byte[] bytesNew) {
		return getBytes(bytes, bytesNew);
	}

	// ///////////////////////// 拆包 ////////////////////////////

	/**
	 * @Title: ascPackDownBytes
	 * @Description: 拆包
	 * @param bytes
	 * @param length
	 * @return byte[]
	 * @throws
	 * @author yisiwei
	 */
	public static byte[] ascPackDownBytes(byte[] bytes, int length) {
		byte[] temp = getBytes(bytes, 0, length);
		return temp;
	}

	/**
	 * 
	 * @Title: ascPackDownBytes
	 * @Description: 拆包
	 * @param bytes
	 * @return byte[]
	 * @throws
	 */
	public static AscPackIntResult ascPackDownBytes(byte[] bytes) {
		int length = byteToInt(getBytes(bytes, 0, 4));
		byte[] temp = getBytes(bytes, 4, bytes.length - 4);
		AscPackIntResult result = new AscPackIntResult();
		result.resultValue = length;
		result.resultBytes = temp;
		return result;
	}

	/**
	 * 
	 * @Title: ascPackDownShort
	 * @Description: 拆包 short
	 * @param bytes
	 * @return short
	 * @throws
	 */
	public static AscPackResult ascPackDownShort(byte[] bytes) {
		byte[] temp = ascPackDownBytes(bytes, 2);
		short value = byteToShort(temp);
		AscPackResult result = new AscPackResult();
		result.setResultValue(value);
		result.setResultBytes(getBytes(bytes, 2, bytes.length - 2));
		return result;
	}

	/**
	 * 
	 * @Title: ascPackDownInt
	 * @Description: 拆包 int
	 * @param bytes
	 * @return AscPackResult
	 * @throws
	 * @author yisiwei
	 */
	public static AscPackResult ascPackDownInt(byte[] bytes) {
		byte[] temp = ascPackDownBytes(bytes, 4);
		int value = byteToInt(temp);
		AscPackResult result = new AscPackResult();
		result.setResultValue(value);
		result.setResultBytes(getBytes(bytes, 4, bytes.length - 4));
		return result;
	}

	/**
	 * 
	 * @Title: ascPackDownLong
	 * @Description:拆包long
	 * @param bytes
	 * @return long
	 * @throws
	 * @author yisiwei
	 */
	public static AscPackResult ascPackDownLong(byte[] bytes) {
		byte[] temp = ascPackDownBytes(bytes, 8);
		long value = byteToLong(temp);
		AscPackResult result = new AscPackResult();
		result.setResultValue(value);
		result.setResultBytes(getBytes(bytes, 8, bytes.length - 8));
		return result;
	}

	/**
	 * 
	 * @Title: ascPackDownBool
	 * @Description: 拆包boolean
	 * @param bytes
	 * @return boolean
	 * @throws
	 * @author yisiwei
	 */
	public static AscPackResult ascPackDownBoolean(byte[] bytes) {
		byte[] temp = ascPackDownBytes(bytes, 1);
		boolean value = byteToBoolean(temp);
		AscPackResult result = new AscPackResult();
		result.setResultValue(value);
		result.setResultBytes(getBytes(bytes, 1, bytes.length - 1));
		return result;
	}

	/**
	 * 
	 * @Title: ascPackDownString
	 * @Description: 拆包String
	 * @param bytes
	 * @return String
	 * @throws
	 * @author yisiwei
	 */
	public static AscPackResult ascPackDownString(byte[] bytes) {
		AscPackIntResult temp = ascPackDownBytes(bytes);

		String value = null;
		try {
			value = new String(temp.resultBytes, "GB2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		AscPackResult result = new AscPackResult();
		result.setResultValue(value);
		result.setResultBytes(getBytes(temp.resultBytes, temp.resultValue,
				temp.resultBytes.length - temp.resultValue));
		return result;
	}

	/**
	 * 
	 * @Title: ascPackDownGuid
	 * @Description: 拆包GUID
	 * @param bytes
	 * @return AscPackResult
	 * @throws
	 */
	public static AscPackResult ascPackDownGuid(byte[] bytes) {
		AscPackIntResult temp = ascPackDownBytes(bytes);
		Log.i("GUID Length:"+temp.resultValue);
		byte[] guidByte = ascPackDownBytes(temp.resultBytes, temp.resultValue);

		AscPackResult result = new AscPackResult();
		result.setResultValue(guidByte);
		result.setResultBytes(getBytes(temp.resultBytes, temp.resultValue,
				temp.resultBytes.length - temp.resultValue));
		return result;

	}

	// ///////////////////////////// byte[] 转化////////////////////////////

	/**
	 * 
	 * @Title: stringToByte
	 * @Description: String转byte[]
	 * @param: str
	 * @return: byte[]
	 * @throws
	 * @author yisiwei
	 */
	public static byte[] stringToByte(String str) {
		byte[] bytes = null;

		try {
			bytes = str.getBytes("GB2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return bytes;
	}

	/**
	 * 
	 * @Title: byteToString
	 * @Description: byte[]转 String
	 * @param bytes
	 * @return String
	 * @throws
	 * @author yisiwei
	 */
	public static String byteToString(byte[] bytes) {
		String str = null;
		try {
			str = new String(bytes, "GB2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 
	 * @Title: shortToByte
	 * @Description: 基于位移的short转byte[]
	 * @param s
	 * @return byte[]
	 * @throws
	 * @author yisiwei
	 */
	public static byte[] shortToByte(short s) {
		// byte[] bytes = new byte[2];
		// bytes[0] = (byte) (s & 0xff);
		// bytes[1] = (byte) ((s >> 8) & 0xff);
		// return bytes;

		byte[] bytes = new byte[2];
		bytes[1] = (byte) ((s >> 8) & 0xFF);
		bytes[0] = (byte) (s & 0xFF);
		return bytes;
	}

	/**
	 * 
	 * @Title: byteToShort
	 * @Description: byte[]转short
	 * @param bytes
	 * @return short
	 * @throws
	 * @author yisiwei
	 */
	public static short byteToShort(byte[] bytes) {
		return byteToShort(bytes, 0);
	}

	/**
	 * 
	 * @Title: byteToShort
	 * @Description: byte[]转short
	 * @param bytes
	 * @param offset
	 * @return short
	 * @throws
	 * @author yisiwei
	 */
	public static short byteToShort(byte[] bytes, int offset) {
		return (short) ((bytes[offset] & 0xFF) | ((bytes[offset + 1] & 0xFF) << 8));
	}

	/**
	 * 
	 * @Title: intToByte
	 * @Description: 基于位移的int转byte[] 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。
	 * @param num
	 * @return byte[]
	 * @throws
	 * @author yisiwei
	 */
	public static byte[] intToByte(int num) {
		byte[] bytes = new byte[4];
		bytes[3] = (byte) ((num >> 24) & 0xFF);
		bytes[2] = (byte) ((num >> 16) & 0xFF);
		bytes[1] = (byte) ((num >> 8) & 0xFF);
		bytes[0] = (byte) (num & 0xFF);
		return bytes;
	}

	/**
	 * 
	 * @Title: byteToInt
	 * @Description: byte[]转int
	 * @param bytes
	 * @return int
	 * @throws
	 * @author yisiwei
	 */
	public static int byteToInt(byte[] bytes) {
		return byteToInt(bytes, 0);
	}

	/**
	 * 
	 * @Title: byteToInt
	 * @Description: byte[]转int
	 * @param bytes
	 * @param offset
	 * @return int
	 * @throws
	 */
	public static int byteToInt(byte[] bytes, int offset) {
		int value = (int) ((bytes[offset] & 0xFF)
				| ((bytes[offset + 1] & 0xFF) << 8)
				| ((bytes[offset + 2] & 0xFF) << 16) | ((bytes[offset + 3] & 0xFF) << 24));
		return value;
	}

	/**
	 * 
	 * @Title: longToByte
	 * @Description: 基于位移的long转byte[]
	 * @param l
	 * @return byte[]
	 * @throws
	 * @author yisiwei
	 */
	public static byte[] longToByte(long l) {

		byte[] bytes = new byte[8];
		for (int i = 0; i < 8; i++) {
			int offset = 64 - (bytes.length - i) * 8;
			bytes[i] = (byte) ((l >>> offset) & 0xff);
		}
		return bytes;
	}

	/**
	 * 
	 * @Title: byteToLong
	 * @Description: byte[]转long
	 * @param bytes
	 * @return long
	 * @throws
	 * @author yisiwei
	 */
	public static long byteToLong(byte[] bytes) {
		return byteToLong(bytes, 0);
	}

	/**
	 * 
	 * @Title: byteToLong
	 * @Description:byte[]转long
	 * @param bytes
	 * @param offset
	 * @return long
	 * @throws
	 * @author yisiwei
	 */
	public static long byteToLong(byte[] bytes, int offset) {

		long value = (long) ((bytes[offset] & 0xFF)
				| ((bytes[offset + 1] & 0xFF) << 8)
				| ((bytes[offset + 2] & 0xFF) << 16)
				| ((bytes[offset + 3] & 0xFF) << 24)
				| ((bytes[offset + 4] & 0xFF) << 32)
				| ((bytes[offset + 5] & 0xFF) << 40)
				| ((bytes[offset + 6] & 0xFF) << 48) | ((bytes[offset + 7] & 0xFF) << 56));

		return value;

		// long num = 0;
		// for (int i = 0; i < 8; i++) {
		// num <<= 8;
		// num |= (bytes[index + i] & 0xff);
		// }
		// return num;
	}

	/**
	 * 
	 * @Title: booleanToByte
	 * @Description: boolean转byte[]
	 * @param bool
	 * @return byte[]
	 * @throws
	 * @author yisiwei
	 */
	public static byte[] booleanToByte(boolean bool) {
		byte[] b = new byte[0];
		b[0] = (byte) (bool ? 1 : 0);
		return b;
	}

	/**
	 * 
	 * @Title: byteToBoolean
	 * @Description: byte[]转boolean
	 * @param bytes
	 * @return boolean
	 * @throws
	 * @author yisiwei
	 */
	public static boolean byteToBoolean(byte[] bytes) {
		return bytes[0] != 0;
	}

}
