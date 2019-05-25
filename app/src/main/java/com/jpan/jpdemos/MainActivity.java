package com.jpan.jpdemos;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.jpan.jpdemos.ui.ThreadControlDemo;

import butterknife.InjectView;

public class MainActivity extends BaseActivity {

    @InjectView(R.id.btn_thread_control)
    Button mThreadControl;
    @InjectView(R.id.btn_other)
    Button mOther;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mThreadControl.setOnClickListener(this);
        mOther.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_thread_control:
                intent.setClass(this, ThreadControlDemo.class);
                break;
            case R.id.btn_other:
                intent.setClass(this, ThreadControlDemo.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }
}
