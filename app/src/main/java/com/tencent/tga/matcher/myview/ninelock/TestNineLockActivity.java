package com.tencent.tga.matcher.myview.ninelock;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tencent.tga.matcher.myview.MainActivity;
import com.tencent.tga.matcher.myview.R;

public class TestNineLockActivity extends Activity {

    private String TAG = "TestNineLockActivity";
    private NineLockView mNineLockView;
    //九个圆环的对应Index
    //    {0,1,2,
    //     3,4,5,
    //     6,7,8}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nine_lock);

        initView();
    }

    private void initView() {
        mNineLockView = (NineLockView) findViewById(R.id.mNineLockView);
        mNineLockView.setOnResultListener(new NineLockView.onResultListener() {
            @Override
            public void setResult(String result) {
                if(result.equals(MainActivity.mSecret)){
                    Log.e(TAG,"密码正确");
                    mNineLockView.setFinishUI(true);
                    Toast.makeText(TestNineLockActivity.this,"密码正确",Toast.LENGTH_SHORT).show();
                }else {
                    Log.e(TAG,"密码错误");
                    mNineLockView.setFinishUI(false);
                    Toast.makeText(TestNineLockActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                }
                Log.e(TAG,"输入为密码 ："+result);
            }
        });
    }
}
