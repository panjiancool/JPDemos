package com.jpan.jpdemos.ui;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jpan.jpdemos.BaseActivity;
import com.jpan.jpdemos.R;
import com.jpan.jpdemos.utils.PrintHelper;

import butterknife.InjectView;

public class TouchEventDemo extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    @InjectView(R.id.btn_touch_event)
    Button mTouchEventBtn;
    @InjectView(R.id.btn_touch_event_clear)
    Button mTouchClearBtn;
    @InjectView(R.id.tv_touch_event)
    TextView mTouchEventTv;
    @InjectView(R.id.cb_touch_event)
    CheckBox mTouchEventCb;
    @InjectView(R.id.cb_touch_event_click)
    CheckBox mTouchEventClickCb;

    private boolean isCheck = true;

    @Override
    public int getLayoutResourceId() {
        return R.layout.demo_touch_event;
    }

    @Override
    public void initView() {
        mTouchEventBtn.setOnClickListener(this);
        mTouchClearBtn.setOnClickListener(this);
        mTouchEventCb.setOnCheckedChangeListener(this);
        mTouchEventClickCb.setOnCheckedChangeListener(this);

        mTouchEventBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isCheck) {
                    setContent("View onTouch return true");
                    return true;
                } else {
                    setContent("View onTouch return false");
                    return false;
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        setContent("ViewGroup onTouchEvent");
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_touch_event:
                setContent("onClick");
                break;
            case R.id.btn_touch_event_clear:
                PrintHelper.getInstance().clear();
                mTouchEventTv.setText("");
                break;
            default:
                break;
        }
    }

    private void setContent(String content) {
        mTouchEventTv.setText(PrintHelper.getInstance().print(content));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_touch_event:
                isCheck = isChecked;
                break;
            case R.id.cb_touch_event_click:
                mTouchEventBtn.setClickable(isChecked);
                break;
            default:
                break;
        }
    }
}
