package com.tencent.tga.matcher.myview.ninelock;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.tencent.tga.matcher.myview.MainActivity;
import com.tencent.tga.matcher.myview.R;

public class SetNineLockActivity extends Activity {

    private NineLockView mNineLockView;
    private String mFirst = "";
    private String mSecond = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_nine_lock);
        Toast.makeText(SetNineLockActivity.this,"请设置密码",Toast.LENGTH_SHORT).show();
        initView();
    }

    private void initView() {
        mNineLockView = (NineLockView) findViewById(R.id.mNineLockView);
        mNineLockView.setOnResultListener(new NineLockView.onResultListener() {
            @Override
            public void setResult(String result) {
                if(TextUtils.isEmpty(mFirst)){
                    mFirst = result;
                    mNineLockView.setFinishUI(true);
                    Toast.makeText(SetNineLockActivity.this,"请再输入一次",Toast.LENGTH_SHORT).show();
                }else{
                    mSecond = result;
                    if(mSecond.equals(mFirst)){
                        mNineLockView.setFinishUI(true);
                        MainActivity.mSecret = mSecond;
                        Toast.makeText(SetNineLockActivity.this,"设置成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        mFirst = "";
                        mSecond = "";
                        mNineLockView.setFinishUI(false);
                        Toast.makeText(SetNineLockActivity.this,"设置失败，请重新设置",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
