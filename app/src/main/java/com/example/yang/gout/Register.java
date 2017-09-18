package com.example.yang.gout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.CollationKey;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.gout.db.MyUser;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SignupActivity";
    private EditText nameText,passwordText,addressText,reEnterPasswordText,emailText,mobileText;
    private Button signupButton;
    private CompositeSubscription mCompositeSubscription;
    private TextView loginLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //第一：默认初始化
        Bmob.initialize(this, "773f0345c73b4d184f2cae0f97e1f530");
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");
        init();
        signupButton.setOnClickListener(this);
        loginLink.setOnClickListener(this);


    }
    @SuppressLint("UseValueOf")
    private void sigUp() {
        Log.d(TAG, "Signup");
        if (!validate()) {
            onSignupFailed();
            return;
        }
        signupButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(Register.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("注册中...");
        progressDialog.show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        uplode();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    private void onSignupSuccess() {
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    private void onSignupFailed() {
        Toast.makeText(getBaseContext(), "注册失败", Toast.LENGTH_LONG).show();

        signupButton.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String address = addressText.getText().toString();
        String email = emailText.getText().toString();
        String mobile = mobileText.getText().toString();
        String password = passwordText.getText().toString();
        String reEnterPassword = reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (address.isEmpty()) {
            addressText.setError("输入地址");
            valid = false;
        } else {
            addressText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("输入正确的邮箱");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            mobileText.setError("输入正确的手机号");
            valid = false;
        } else {
            mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            reEnterPasswordText.setError(null);
        }

        return valid;
    }


    /**
     * 解决Subscription内存泄露问题
     * @param s
     */
    private void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }

    private void init() {
        nameText= (EditText) findViewById(R.id.input_name);
        passwordText= (EditText) findViewById(R.id.input_password);
        addressText= (EditText) findViewById(R.id.input_address);
        reEnterPasswordText= (EditText) findViewById(R.id.input_reEnterPassword);
        emailText= (EditText) findViewById(R.id.input_email);
        mobileText= (EditText) findViewById(R.id.input_mobile);
        signupButton= (Button) findViewById(R.id.btn_signup);
        loginLink= (TextView) findViewById(R.id.link_login);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signup:
                sigUp();
                break;
            case R.id.link_login:
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }
    }

    private void uplode() {
        String name = nameText.getText().toString();
        String address = addressText.getText().toString();
        String email = emailText.getText().toString();
        String mobile = mobileText.getText().toString();
        String password = passwordText.getText().toString();
        final MyUser myUser = new MyUser();
        myUser.setUsername(name);
        myUser.setPassword(password);
        myUser.setAddress(address);
        myUser.setEmail(email);
        myUser.setMobile(mobile);
        addSubscription(myUser.signUp(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser s, BmobException e) {
                if (e == null) {
                    onSignupSuccess();

                } else {
                    onSignupFailed();
                }
            }
        }));
    }
}
