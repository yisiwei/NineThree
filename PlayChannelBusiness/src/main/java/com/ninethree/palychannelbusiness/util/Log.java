package com.ninethree.palychannelbusiness.util;

import com.ninethree.palychannelbusiness.BuildConfig;

/**
 * 
 * @ClassName:  Log   
 * @Description:日志
 *
 * 1.Project -> Build Automatically（取消）
 * 2.Project -> Clean
 * 3.Project -> Build
 * 4.Project Export Android application
 * 
 */
public class Log {

	public static final boolean DEBUG = BuildConfig.DEBUG;
//	public static final boolean DEBUG = true;

	private static final String TAG = "EWork";

	public static void v(String tag, String msg) {
		if (DEBUG) {
			android.util.Log.v(tag, msg);
		}
	}

	public static void v(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			android.util.Log.v(tag, msg, tr);
		}
	}

	public static void d(String tag, String msg) {
		if (DEBUG) {
			android.util.Log.d(tag, msg);
		}
	}

	public static void d(String msg) {
		if (DEBUG) {
			android.util.Log.d(TAG, msg);
		}
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			android.util.Log.d(tag, msg, tr);
		}
	}

	public static void i(String tag, String msg) {
		if (DEBUG) {
			android.util.Log.i(tag, msg);
		}
	}

	public static void i(String msg) {
		if (DEBUG) {
			android.util.Log.i(TAG, msg);
		}
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			android.util.Log.i(tag, msg, tr);
		}
	}

	public static void w(String tag, String msg) {
		if (DEBUG) {
			android.util.Log.w(tag, msg);
		}
	}

	public static void w(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			android.util.Log.w(tag, msg, tr);
		}
	}

	public static void w(String tag, Throwable tr) {
		if (DEBUG) {
			android.util.Log.w(tag, tr);
		}
	}

	public static void e(String tag, String msg) {
		if (DEBUG) {
			android.util.Log.e(tag, msg);
		}
	}

	public static void e(String msg) {
		if (DEBUG) {
			android.util.Log.e(TAG, msg);
		}
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			android.util.Log.e(tag, msg, tr);
		}
	}
	public static void e(String msg, Throwable tr) {
		if (DEBUG) {
			android.util.Log.e(TAG, msg, tr);
		}
	}

}
