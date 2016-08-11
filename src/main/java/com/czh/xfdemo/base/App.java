package com.czh.xfdemo.base;

import android.app.Application;

import com.czh.xfdemo.utils.Constant;
import com.czh.xfdemo.utils.DebugLogUtil;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/*************************************************
 * @desc application
 * @auther LiJianfei
 * @time 2016/7/27 9:50
 ************************************/
public class App extends Application {
    private static App appContext;

    public static SpeechUtility getXF() {
//        根据官方api修改单例
        if (SpeechUtility.getUtility() != null) {
            return SpeechUtility.getUtility();
        } else {

            DebugLogUtil.getInstance().Info("application讯飞初始化");
//            return SpeechUtility.createUtility(appContext, Constant.XFAPPID);
            return SpeechUtility.createUtility(appContext, SpeechConstant.APPID + "=" + Constant.XFAPPID);// 到底用哪个呢
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        initDebugLog();
        getXF();// 初始化
    }


    private void initDebugLog() {
        DebugLogUtil.getInstance().setDebug(true);
        DebugLogUtil.getInstance().setFilter("lijianfei");// 防止与手机内其他进程的debug信息混淆

    }

    public static App getInstance() {
        if (appContext != null) {
            return appContext;
        } else {
            appContext = new App();
            return appContext;
        }
    }

}
