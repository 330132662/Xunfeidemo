package com.czh.xfdemo.base;

import android.os.Bundle;

/*************************************************
 * @desc MVP-V 随时添加调用频繁的界面操作 ,集中管理这些操作 ,如果某个界面不需要复杂的操作 就只需继承baseactivity
 * @auther LiJianfei
 * @time 2016/7/27 9:52
 ************************************/
public interface ViewBase {
    /**
     * 弹出提示
     *
     * @param toast
     */
    void showToast(String toast);

    /**
     * 跳转界面
     *
     * @param cls
     * @param bundle
     */
    void loadActivity(Class cls, Bundle bundle);

    /**
     * 查找控件对象
     */
    void initView();
}
