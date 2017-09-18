package com.example.yang.gout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.gout.db.MyUser;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.Bind;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import rx.Subscriber;

import static com.example.yang.gout.BaseActivity.log;
import static com.example.yang.gout.BaseActivity.loge;

public class Login extends Activity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    // 登陆按钮
    private Button loginButton;
    // 调试文本，注册文本
    private TextView signupLink;
    // 显示用户名和密码
    EditText userName, userPassword;
    private String name,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //第一：默认初始化
        Bmob.initialize(this, "Your Application ID");
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");
        init();

    }

    private void init() {
        // 获取控件
        userName = (EditText) findViewById(R.id.input_name);
        userPassword= (EditText) findViewById(R.id.input_password);
        loginButton = (Button) findViewById(R.id.btn_login);
        signupLink= (TextView) findViewById(R.id.link_signup);

        // 设置按钮监听器
        loginButton.setOnClickListener(this);
        signupLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                name= userName.getText().toString();
                password =userPassword.getText().toString();
                // 检测网络，无法检测wifi
                if (!checkNetwork()) {
                    Toast toast = Toast.makeText(Login.this, "网络未连接", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    break;
                }
                if (!validate()) {
                    onLoginFailed();
                    break;
                }
                loginButton.setEnabled(false);
                final ProgressDialog progressDialog = new ProgressDialog(Login.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("登录中...");
                progressDialog.show();
                // 创建子线程，分别进行Get和Post传输
                 //new Thread(new MyThread()).start();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                login();
                                progressDialog.dismiss();
                            }
                        }, 3000);
                break;
            case R.id.link_signup:
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }
    }

    private void onLoginSuccess() {
        Toast.makeText(getBaseContext(), "登录成功", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
        finish();
    }

    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), "登录失败", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        name = userName.getText().toString();
        password =userPassword.getText().toString();

        if (name.isEmpty()) {
            userName.setError("请输入用户名");
            valid = false;
        } else {
            userName.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            userPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            userPassword.setError(null);
        }

        return valid;
    }

    private void login() {
        final MyUser user = new MyUser();
        user.setUsername(name);
        user.setPassword(password);
        user.loginObservable(BmobUser.class).subscribe(new Subscriber<BmobUser>() {
            @Override
            public void onCompleted() {
                log("----onCompleted----");
            }

            @Override
            public void onError(Throwable e) {
                onLoginFailed();
            }

            @Override
            public void onNext(BmobUser bmobUser) {
                onLoginSuccess();
            }
        });
    }

    /*
    
            // 子线程接收数据，主线程修改数据
            public class MyThread implements Runnable {
                @Override
                public void run() {
    
                    info = WebService.executeHttpGet(username.getText().toString(), password.getText().toString());
                    // info = WebServicePost.executeHttpPost(username.getText().toString(), password.getText().toString());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            infotv.setText(info);
                            dialog.dismiss();
                        }
                    });
                }
    
            }
     */
    // 检测网络
    private boolean checkNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

}
