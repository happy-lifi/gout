package com.example.yang.gout;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mob.MobSDK;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static java.sql.Types.TIME;

public class Sms extends AppCompatActivity implements View.OnClickListener {
    private EditText phoneNum;
    private Button getButton;
    private EditText checkNum;
    private Button checkButton;
    private TimerTask tt;
    private Timer tm;
    private static final int CODE_REPEAT = 1; //重新发送
    private int TIME = 6;
    private String phone;
    public String country="86";
    //重新发送
    Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CODE_REPEAT) {
                getButton.setEnabled(true);
                checkButton.setEnabled(true);
                tm.cancel();//取消任务
                tt.cancel();//取消任务
                TIME = 6;//时间重置
                getButton.setText("重新发送验证码");
            }else {
                getButton.setText(TIME + "重新发送验证码");
            }
        }
    };
    //回调
    EventHandler eh=new EventHandler(){
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    toast("验证成功");
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){       //获取验证码成功
                    toast("获取验证码成功");
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//如果你调用了获取国家区号类表会在这里回调
                    //返回支持发送验证码的国家列表
                }
            }else{//错误等在这里（包括验证失败）
                //错误码请参照http://wiki.mob.com/android-api-错误码参考/
                ((Throwable)data).printStackTrace();
                String str = data.toString();
                toast(str);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_content);
        MobSDK.init(this, "1fd64dbfdedf8", "b411572f03959242d14a9d2e78e27eee");
        SMSSDK.registerEventHandler(eh); //注册短信回调（记得销毁，避免泄露内存）
        initView();

    }



    private void initView() {
        phoneNum= (EditText) findViewById(R.id.phone_nub);
        getButton = (Button) findViewById(R.id.get);
        checkNum= (EditText) findViewById(R.id.check);
        checkButton= (Button) findViewById(R.id.check_button);
        getButton.setOnClickListener(this);
        checkButton.setOnClickListener(this);
    }
    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Sms.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get:
                phone = phoneNum.getText().toString().trim().replaceAll("/s","");
                if (!TextUtils.isEmpty(phone)) {
                    //定义需要匹配的正则表达式的规则
                    String REGEX_MOBILE_SIMPLE =  "[1][358]\\d{9}";
                    //把正则表达式的规则编译成模板
                    Pattern pattern = Pattern.compile(REGEX_MOBILE_SIMPLE);
                    //把需要匹配的字符给模板匹配，获得匹配器
                    Matcher matcher = pattern.matcher(phone);
                    // 通过匹配器查找是否有该字符，不可重复调用重复调用matcher.find()
                    if (matcher.find()) {//匹配手机号是否存在
                        alterWarning();
                    } else {
                        toast("手机号格式错误");
                    }
                } else {
                    toast("请先输入手机号");
                }
                break;
            case R.id.check_button:
                //获得用户输入的验证码
                String code = checkNum.getText().toString().replaceAll("/s","");
                if (!TextUtils.isEmpty(code)) {//判断验证码是否为空
                    //验证
                    SMSSDK.submitVerificationCode( country,  phone,  code);
                }else{//如果用户输入的内容为空，提醒用户
                    toast("请输入验证码后再提交");
                }
                break;
        }
    }

    private void alterWarning() {
        // 2. 通过sdk发送短信验证
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("我们将要发送到" + phone + "验证"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                // 2. 通过sdk发送短信验证（请求获取短信验证码，在监听（eh）中返回）
                SMSSDK.getVerificationCode(country, phone);
                //做倒计时操作
                Toast.makeText(Sms.this, "已发送" + which, Toast.LENGTH_SHORT).show();
                getButton.setEnabled(false);
                checkButton.setEnabled(true);
                tm = new Timer();
                tt = new TimerTask() {
                    @Override
                    public void run() {
                        hd.sendEmptyMessage(TIME--);
                    }
                };
                tm.schedule(tt,0,1000);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(Sms.this, "已取消" + which, Toast.LENGTH_SHORT).show();
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }
    protected void onDestroy() {
        super.onDestroy();
        // 注销回调接口registerEventHandler必须和unregisterEventHandler配套使用，否则可能造成内存泄漏。
        SMSSDK.unregisterEventHandler(eh);

    }
}
