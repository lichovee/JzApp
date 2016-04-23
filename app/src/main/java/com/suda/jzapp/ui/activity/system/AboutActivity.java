package com.suda.jzapp.ui.activity.system;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;

public class AboutActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyContentView(false,R.layout.activity_abount);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AboutActivity.this.onBackPressed();
                    }
                }
        );
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initWidget();
    }

    @Override
    protected void initWidget() {
        findViewById(R.id.background).setBackgroundColor(getColor(this, getMainTheme().getMainColorID()));
    }
    private Toolbar mToolbar;
}
