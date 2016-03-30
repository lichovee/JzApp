package com.suda.jzapp.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.github.mrengineer13.snackbar.SnackBar;
import com.gxz.PagerSlidingTabStrip;
import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.ui.activity.account.AccountLinkActivity;
import com.suda.jzapp.ui.activity.account.MonthReportActivity;
import com.suda.jzapp.ui.activity.system.AboutActivity;
import com.suda.jzapp.ui.activity.system.EditThemeActivity;
import com.suda.jzapp.ui.activity.system.SettingsActivity;
import com.suda.jzapp.ui.adapter.MyFragmentPagerAdapter;
import com.suda.jzapp.dao.bean.OptDO;
import com.suda.jzapp.ui.adapter.OptMenuAdapter;
import com.suda.jzapp.util.ThemeUtil;
import com.umeng.update.UmengUpdateAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UmengUpdateAgent.update(this);
        initWidget();
    }

    @Override
    protected void initWidget() {
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mLvOptItems = (ListView) findViewById(R.id.opt_items);
        mLayoutBackGround = (RelativeLayout) findViewById(R.id.account_background);


        mLayoutBackGround.setBackgroundResource(ThemeUtil.getTheme(this).getMainColorID());

        // 标题的文字需在setSupportActionBar之前，不然会无效
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        //设置抽屉
        setDrawerLayout();

        initViewPager();

    }

    private void initViewPager() {
        mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), this));
        mViewPager.setOffscreenPageLimit(3);


        mPagerSlidingTabStrip.setViewPager(mViewPager);
        mPagerSlidingTabStrip
                .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int arg0) {
                        // colorChange(arg0);
                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int arg0) {
                    }
                });

        // 底部游标颜色
        mPagerSlidingTabStrip.setIndicatorColor(Color.WHITE);

        // tab的分割线颜色
        mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
        // tab背景
        mPagerSlidingTabStrip.setBackgroundColor(getColor(this, ThemeUtil.getTheme(this).getMainColorID()));

        mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources()
                        .getDisplayMetrics()));


        // 游标高度
        mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources()
                        .getDisplayMetrics()));
        // 选中的文字颜色
         mPagerSlidingTabStrip.setSelectedTextColor(Color.WHITE);
        // 正常文字颜色
        mPagerSlidingTabStrip.setTextColor(Color.BLACK);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLayoutBackGround.setBackgroundResource(ThemeUtil.getTheme(this).getMainColorID());
        mPagerSlidingTabStrip.setBackgroundColor(getColor(this, ThemeUtil.getTheme(this).getMainColorID()));
    }

    private void setDrawerLayout() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                mToolbar, R.string.open, R.string.close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                openOrClose = false;
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                openOrClose = true;
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //初始化菜单
        List<OptDO> optDOs = new ArrayList<>();

        optDOs.add(new OptDO(AccountLinkActivity.class, 0, R.drawable.ic_drawer_friends, "关联账户"));
        optDOs.add(new OptDO(MonthReportActivity.class, 1, R.drawable.ic_drawer_guide, "月报"));
        optDOs.add(new OptDO(SettingsActivity.class, 2, R.drawable.ic_drawer_settings, "设置"));
        optDOs.add(new OptDO(EditThemeActivity.class, 0, R.drawable.ic_drawer_friends, "主题切换"));
        optDOs.add(new OptDO(AboutActivity.class, 3, R.drawable.ic_drawer_about, "关于"));
        optDOs.add(new OptDO(null, 4, R.drawable.ic_drawer_exit, "退出"));
        OptMenuAdapter optMenuAdapter = new OptMenuAdapter(optDOs, this);
        mLvOptItems.setAdapter(optMenuAdapter);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (openOrClose) {
                mDrawerLayout.closeDrawers();
                return true;
            }

            if (canQuit) {
                this.finish();
            } else {
                new SnackBar.Builder(this)
                        .withMessage("再按一次退出")
                        .withDuration(SnackBar.SHORT_SNACK)
                        .show();

            }
            canQuit = true;

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    canQuit = false;
                }
            }, 1500);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mLvOptItems;
    private RelativeLayout mLayoutBackGround;

    private boolean openOrClose = false;
    private boolean canQuit = false;

}