package com.jpan.jpdemos.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jpan.jpdemos.BaseActivity;
import com.jpan.jpdemos.MyApplication;
import com.jpan.jpdemos.R;
import com.jpan.jpdemos.helper.MemoryTestSingleton;
import com.squareup.leakcanary.RefWatcher;

import java.lang.ref.WeakReference;

import butterknife.InjectView;

public class MemoryTestDemo extends BaseActivity {

    @InjectView(R.id.tv_memory_test)
    TextView mTvMemoryTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(this);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(8000000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.demo_memory_test;
    }

    @Override
    public void initView() {
    }

    @Override
    public void onClick(View v) {
    }
}
