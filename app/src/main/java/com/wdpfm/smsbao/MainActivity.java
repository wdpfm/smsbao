package com.wdpfm.smsbao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.song.http.QSHttp;
import org.song.http.framework.HttpCallback;
import org.song.http.framework.HttpException;
import org.song.http.framework.ResponseParams;

public class MainActivity extends AppCompatActivity {
    int captcha=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editPhone=findViewById(R.id.phone);
        final EditText editCaptcha=findViewById(R.id.editCaptcha);
        Button btnCaptcha=findViewById(R.id.btnCaptcha);
        Button btnLogin=findViewById(R.id.btnLogin);
        btnCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captcha=(int)((Math.random()*9+1)*100000);
                String url = "https://api.smsbao.com/sms?";
                QSHttp.get(url)
                        .param("u",Key.u)
                        .param("p",Key.p)
                        .param("m", editPhone.getText().toString())
                        .param("c", "【护花使者】您的验证码是:"+captcha)
                        .buildAndExecute(new HttpCallback() {
                            @Override
                            public void onSuccess(ResponseParams response) {
                                Log.d("目标", "手机号: "+editPhone.getText().toString());
                                Log.d("发送", "验证码是: "+captcha);
                                Log.d("结果", "返回信息: "+response.string());
                                switch (response.string()){
                                    case "0":
                                        Toast.makeText(MainActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "43":
                                        Toast.makeText(MainActivity.this, "IP地址限制", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "50":
                                        Toast.makeText(MainActivity.this, "内容含有敏感词", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "51":
                                        Toast.makeText(MainActivity.this, "手机号码不正确", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(MainActivity.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }

                            @Override
                            public void onFailure(HttpException e) {
                                e.show();
                            }
                        });
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editPhone.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editCaptcha.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editCaptcha.getText().toString().equals(String.valueOf(captcha))){
                    Toast.makeText(MainActivity.this, "验证码正确", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
