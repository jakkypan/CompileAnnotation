/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package baidu.myapp;

import com.example.InjectView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import baidu.myandroidprocessor.HyViewInjector;

@InjectView(R.layout.activity_main)
public class MainActivity extends Activity {
    @InjectView(R.id.id_tv_name)
    public TextView mTvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HyViewInjector.inject(this);
        mTvName.setText("Hello Annotation");
    }

}
