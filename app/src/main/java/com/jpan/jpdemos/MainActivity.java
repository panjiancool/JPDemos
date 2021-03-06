package com.jpan.jpdemos;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.jpan.jpdemos.ui.ActivityLifecycleDemo;
import com.jpan.jpdemos.ui.ExceptionTestDemo;
import com.jpan.jpdemos.ui.GPUOverrideDemo;
import com.jpan.jpdemos.ui.MemoryTestDemo;
import com.jpan.jpdemos.ui.NinePathPictureDemo;
import com.jpan.jpdemos.ui.ThreadControlDemo;
import com.jpan.jpdemos.ui.TouchEventDemo;
import com.jpan.jpdemos.utils.PrintHelper;

import butterknife.InjectView;

public class MainActivity extends BaseActivity {

    @InjectView(R.id.btn_thread_control)
    Button mThreadControlBtn;
    @InjectView(R.id.btn_touch_event)
    Button mToucEventBtn;
    @InjectView(R.id.btn_gpu_override)
    Button mGPUOverrideBtn;
    @InjectView(R.id.btn_nine_path)
    Button mNinePathBtn;
    @InjectView(R.id.btn_memory)
    Button mMemoryBtn;
    @InjectView(R.id.btn_exception)
    Button mExceptionBtn;
    @InjectView(R.id.btn_activity_lifecycle)
    Button mActivityLifeBtn;
    @InjectView(R.id.btn_other)
    Button mOther;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mThreadControlBtn.setOnClickListener(this);
        mToucEventBtn.setOnClickListener(this);
        mGPUOverrideBtn.setOnClickListener(this);
        mNinePathBtn.setOnClickListener(this);
        mMemoryBtn.setOnClickListener(this);
        mExceptionBtn.setOnClickListener(this);
        mActivityLifeBtn.setOnClickListener(this);
        mOther.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_thread_control:
                intent.setClass(this, ThreadControlDemo.class);
                break;
            case R.id.btn_touch_event:
                intent.setClass(this, TouchEventDemo.class);
                break;
            case R.id.btn_gpu_override:
                intent.setClass(this, GPUOverrideDemo.class);
                break;
            case R.id.btn_nine_path:
                intent.setClass(this, NinePathPictureDemo.class);
                break;
            case R.id.btn_memory:
                intent.setClass(this, MemoryTestDemo.class);
                break;
            case R.id.btn_exception:
                intent.setClass(this, ExceptionTestDemo.class);
                break;
            case R.id.btn_activity_lifecycle:
                intent.setClass(this, ActivityLifecycleDemo.class);
                break;
            case R.id.btn_other:
                intent.setClass(this, ThreadControlDemo.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PrintHelper.getInstance().clear();
    }
}
