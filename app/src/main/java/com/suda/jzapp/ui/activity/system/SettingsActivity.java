package com.suda.jzapp.ui.activity.system;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.TextUtils;

import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.misc.IntentConstant;
import com.suda.jzapp.util.AlarmUtil;
import com.suda.jzapp.util.SPUtils;
import com.suda.jzapp.util.SnackBarUtil;
import com.suda.jzapp.util.StatusBarCompat;
import com.suda.jzapp.util.ThemeUtil;
import com.suda.jzapp.view.MyPreferenceCategory;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void initWidget() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTheme(ThemeUtil.getAppStyleId(this));


        if (savedInstanceState == null) {
            mSettingsFragment = new SettingsFragment(this);
            replaceFragment(R.id.settings_container, mSettingsFragment);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void replaceFragment(int viewId, android.app.Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSettingsFragment.changeGestureCheckStatus();

    }

    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        public SettingsFragment(Context context) {
            this.context = context;
        }

        public SettingsFragment() {

        }

        private Context context;
        private CheckBoxPreference mGestureLockCheck;
        private CheckBoxPreference mRemindCheck;
        private CheckBoxPreference mImmersiveCheck;
        private MyPreferenceCategory mCommonCateGory;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            mGestureLockCheck = (CheckBoxPreference) findPreference(GESTURE_LOCK);
            mRemindCheck = (CheckBoxPreference) findPreference(REMIND_SETTING);
            mImmersiveCheck = (CheckBoxPreference) findPreference(IMMERSIVE_STATUS_BAR);
            mCommonCateGory = (MyPreferenceCategory) findPreference("common_settings");

            mRemindCheck.setOnPreferenceChangeListener(this);
            mGestureLockCheck.setOnPreferenceChangeListener(this);

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                mCommonCateGory.removePreference(mImmersiveCheck);
            } else {
                mImmersiveCheck.setOnPreferenceChangeListener(this);
            }


            long alarmTime = SPUtils.gets(context, Constant.SP_ALARM_TIME, 0l);
            if (alarmTime > 0) {
                Date date = new Date(alarmTime);
                mRemindCheck.setSummaryOn("每天" + format.format(date) + "提醒记账");
            }
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference == mGestureLockCheck) {
                if (mGestureLockCheck.isChecked()) {
                    SPUtils.put(context, Constant.SP_GESTURE, "");
                    mGestureLockCheck.setChecked(false);
                } else {
                    if (MyAVUser.getCurrentUser() != null) {
                        Intent intent = new Intent(context, GestureLockActivity.class);
                        intent.putExtra(IntentConstant.SETTING_MODE, true);
                        startActivity(intent);
                    } else {
                        SnackBarUtil.showSnackInfo(getView(), context, "请先登录账户");
                    }
                }
            } else if (preference == mRemindCheck) {
                if (mRemindCheck.isChecked()) {
                    SPUtils.put(context, Constant.SP_ALARM_TIME, 0l);
                    mRemindCheck.setChecked(false);
                    AlarmUtil.createAlarmOrCancel(context, false);
                } else {
                    final Calendar calendar = Calendar.getInstance();
                    TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendar.set(Calendar.MINUTE, minute);
                            calendar.set(Calendar.SECOND, 0);
                            calendar.set(Calendar.MILLISECOND, 0);
                            SPUtils.put(context, Constant.SP_ALARM_TIME, calendar.getTimeInMillis());
                            mRemindCheck.setChecked(true);
                            mRemindCheck.setSummaryOn("每天" + format.format(calendar.getTime()) + "提醒记账");

                            AlarmUtil.createAlarm(context);
                        }
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

                    timePickerDialog.setAccentColor(getResources().getColor(ThemeUtil.getTheme(context).getMainColorID()));
                    timePickerDialog.show(getFragmentManager(), "Timepickerdialog");
                }
            } else if (preference == mImmersiveCheck) {
                if (mImmersiveCheck.isChecked()) {
                    mImmersiveCheck.setChecked(false);
                    StatusBarCompat.compat((Activity) context, context.getResources().getColor(ThemeUtil.getTheme(context).getMainDarkColorID()));
                } else {
                    mImmersiveCheck.setChecked(true);
                    StatusBarCompat.compat((Activity) context, context.getResources().getColor(ThemeUtil.getTheme(context).getMainColorID()));
                }
            }
            return false;
        }

        public void changeGestureCheckStatus() {
            if (mGestureLockCheck != null) {
                if (!TextUtils.isEmpty((String) SPUtils.get(context, Constant.SP_GESTURE, ""))) {
                    mGestureLockCheck.setChecked(true);
                }
            }
        }
    }


    public static final String GESTURE_LOCK = "gesture_lock";
    public static final String REMIND_SETTING = "remind_setting";
    public static final String IMMERSIVE_STATUS_BAR = "immersive_status_bar";
    public static final DateFormat format = new SimpleDateFormat("HH:mm");
    private SettingsFragment mSettingsFragment;
}
