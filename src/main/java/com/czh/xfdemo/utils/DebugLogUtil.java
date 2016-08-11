package com.czh.xfdemo.utils;

import android.util.Log;

/**
 * 应用程序调试日志统一管理工具类 2015年9月2日09:46:48
 * 
 * @author Juxiao 描述：当程序不需要调试的时候(例如：app打包上线),应将调试信息统一关掉
 */
public class DebugLogUtil {

	// 单例访问
	private static DebugLogUtil debugUtil;
	/** 判断是否在进行Debug开发阶段, 默认false */
	private boolean isDebug;
	/** 打印日志过滤标示，默认'Application' */
	private String Filter;

	private DebugLogUtil() {
		isDebug = false;
		Filter = "Application";
	}

	/**
	 * 创建该类对象唯一的方法
	 * 
	 * @return
	 */
	public static DebugLogUtil getInstance() {
		if (debugUtil == null) {
			synchronized (DebugLogUtil.class) {
				if (debugUtil == null) {
					debugUtil = new DebugLogUtil();
				}
			}
		}
		return debugUtil;
	}

	public boolean isDebug() {
		return isDebug;
	}

	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public String getFilter() {
		return Filter;
	}

	public void setFilter(String filter) {
		Filter = filter;
	}

	public void Verbose(String logCat) {
		if (isDebug) {
			Log.v(this.Filter, "" + logCat);
		}
	}

	public void Debug(String logCat) {
		if (isDebug) {
			Log.d(this.Filter, "" + logCat);
		}
	}

	public void Info(String logCat) {
		if (isDebug) {
			Log.i(this.Filter, "" + logCat);
		}
	}

	public void Warn(String logCat) {
		if (isDebug) {
			Log.w(this.Filter, "" + logCat);
		}
	}

	public void Error(String logCat) {
		if (isDebug) {
			Log.e(this.Filter, "" + logCat);
		}
	}
}
