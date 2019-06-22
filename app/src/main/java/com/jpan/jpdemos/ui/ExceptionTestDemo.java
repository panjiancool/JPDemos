package com.jpan.jpdemos.ui;

import android.view.View;
import android.widget.Button;

import com.jpan.jpdemos.BaseActivity;
import com.jpan.jpdemos.R;

import butterknife.InjectView;

public class ExceptionTestDemo extends BaseActivity {

    @InjectView(R.id.btn_exception_test)
    Button mBtnBundleTest;

    private StringBuffer buffer;
    private int[] a = new int[3];

    @Override
    public int getLayoutResourceId() {
        return R.layout.demo_exception_test;
    }

    @Override
    public void initView() {
        mBtnBundleTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exception_test:
                test();
//                foo();
                break;
            default:
                break;
        }
    }

    private void test() {
        try {
            buffer.append("test1");
        } catch (NullPointerException e) {
            e.printStackTrace();
            buffer.append("test2");
//            a[4] = 1;
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int foo() {
        int i = 10;
        try {
            i = --i / 0;
            return i--;
        } catch (Exception e) {
            i = i-- / 0;
            return --i;
        } finally {
            --i;
            return i--;
        }
    }
}
