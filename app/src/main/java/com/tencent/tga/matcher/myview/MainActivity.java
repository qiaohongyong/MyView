package com.tencent.tga.matcher.myview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.tga.matcher.myview.ninelock.SetNineLockActivity;
import com.tencent.tga.matcher.myview.ninelock.TestNineLockActivity;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button btn_set_secret;
    private Button btn_test_secret;

    public static String mSecret = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn_set_secret = (Button) findViewById(R.id.btn1);
        btn_test_secret = (Button) findViewById(R.id.btn2);

        btn_set_secret.setOnClickListener(this);
        btn_test_secret.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                if(TextUtils.isEmpty(MainActivity.mSecret)){
                    Toast.makeText(MainActivity.this,"密码为空，请先设置密码",Toast.LENGTH_SHORT).show();
                }else{
                    startActivity(new Intent(MainActivity.this, TestNineLockActivity.class));
                }
                break;
            case R.id.btn2:
                startActivity(new Intent(MainActivity.this, SetNineLockActivity.class));
                break;

        }
    }
}
