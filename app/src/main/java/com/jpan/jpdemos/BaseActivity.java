package com.jpan.jpdemos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        ButterKnife.inject(this);
        initView();
    }

    /**
     * 加载布局
     * @return 布局id
     */
    public abstract int getLayoutResourceId();

    /**
     * 初始化View元素
     */
    public abstract void initView();
}
