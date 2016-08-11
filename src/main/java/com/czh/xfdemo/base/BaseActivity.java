package com.czh.xfdemo.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.iflytek.sunflower.FlowerCollector;


public class BaseActivity extends AppCompatActivity implements ViewBase {
    @Override
    protected void onResume() {
        super.onResume();
        FlowerCollector.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FlowerCollector.onPause(this);
    }

    /**
     * 弹出提示
     *
     * @param toast
     */
    @Override
    public void showToast(String toast) {
        Toast.makeText(getApplication(), toast, Toast.LENGTH_SHORT).show();
    }

    /**
     * 生成简易的代替toast的snackbar 很快就会消失 跟封装的showToast一样
     *
     * @param view
     * @param s    左侧提示语
     */
    public static void showSnack(View view, String s) {
        Snackbar.make(view, s, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 重载跳转activity 可以传入bundle进行传值
     *
     * @param cls
     * @param bundle 传值 没值必须传null
     */
    public void loadActivity(Class cls, Bundle bundle) {
        Intent intent = new Intent(BaseActivity.this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 查找控件对象
     */
    @Override
    public void initView() {

    }

    //通过id获取控件
    public final <T extends View> T fView(int id) {
        return (T) BaseActivity.this.findViewById(id);
    }

    public void backButton(View v) {
        onBackPressed();
    }


    public void finish(View v) {
//        关闭此界面
        super.finish();
    }


}
