package officeapp.zdd.com.widget;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sdyy.utils.XPermissionListener;
import com.sdyy.utils.XPermissions;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import officeapp.zdd.com.R;
import officeapp.zdd.com.db.SharedPerferencesUtils;
import officeapp.zdd.com.db.SqliteUtils;
import officeapp.zdd.com.utils.StringUtils;

/**
 * Created by zdd on 2017/3/31.
 */

public class SuperActivity extends ActionBarActivity {
    protected Context context;
    protected ActionBar mActionBar;
    protected WaitDialog waitDialogSupport;
    protected WaitDialogRectangle waitDialogRectangle;
    protected NotificationManager notificationManager;
    public SharedPerferencesUtils sharedUtils;
    private static String TAG = "SuperActivity";
    protected int countTime = 0;//记时
    protected TimerTask timerTask;
    //    protected RSAUtil rsaUtil;
    protected RSAPublicKey pubKey;
    protected RSAPrivateKey priKey;
    protected byte[] enRsaBytes;
    protected String bundleKry = "bundle";
    protected SqliteUtils sqliteUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        if (sharedUtils == null) {
            sharedUtils = SharedPerferencesUtils.getInstance(context);
        }

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        waitDialogSupport = new WaitDialog(context);
        waitDialogRectangle = new WaitDialogRectangle(context);

        sqliteUtils = new SqliteUtils(this);
        sqliteUtils.open();
//        //RSA加密
//        rsaUtil = new RSAUtil();
//        try {
//            pubKey  = rsaUtil.getRSAPublicKey();
//            priKey = rsaUtil.getRSAPrivateKey();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    public void setPermissions(){
        XPermissions.getPermissions(new String[]{
                        XPermissions.CAMERA,
                        XPermissions.WRITE_EXTERNAL_STORAGE,
                        XPermissions.ACCESS_COARSE_LOCATION,
                        XPermissions.ACCESS_FINE_LOCATION,
                        XPermissions.READ_PHONE_STATE},
                this, new XPermissionListener()
                {
                    @Override
                    public void onAcceptAccredit()
                    {

                    }

                    @Override
                    public void onRefuseAccredit(String[] results)
                    {
                        // 用户拒绝授权(API>=23时)
                        toastLONG("定位授权失败");
                    }
                });
    }

    /**
     * 获取输入框的内容
     * @param editText
     * @return
     */
    public String getEditextString(EditText editText) {
        if (editText != null) {
            String content = editText.getText().toString().trim();
            if (StringUtils.isStrTrue(content))
                return content;
            return "空";
        }
        return "空";
    }


    /**
     * 显示actionbar
     *
     * @param content
     */
    public void showActionBar(int content) {
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setTitle(content);
        }
    }

    /**
     * 跳转页面
     *
     * @param toClass
     */
    public void intentActivity(Class toClass) {
        Intent intent = new Intent(context, toClass);
        startActivity(intent);
    }

    /**
     * 跳转页面
     *
     * @param toClass
     */
    public void intentActivity(Class toClass, int type) {
        Intent intent = new Intent(context, toClass);
        startActivityForResult(intent, type);
    }

    /**
     * 跳转页面
     *
     * @param toClass
     */
    public void intentActivity(Class toClass, int type, Bundle bundle) {
        Intent intent = new Intent(context, toClass);
        intent.putExtra(bundleKry, bundle);
        startActivityForResult(intent, type);
    }


    /**
     * @param timeStyle
     * @return 时间格式
     */
    public static String getCurrentTime(String timeStyle) {
        // 获取当前时间
        SimpleDateFormat formatter = new SimpleDateFormat(timeStyle);
        Date curDate = new Date(System.currentTimeMillis());
        String currentTime = formatter.format(curDate);

        return currentTime;
    }


    /**
     * 倒计时
     *
     * @param view         显示的文本控件
     * @param sumTime      总倒计时
     * @param leftContent  时间前面的内容
     * @param rightContent 时间后面的内容提示
     * @param content      倒计时完成时的显示（刚开始的显示内容）
     *                     <p>
     *                     countDown(ceshi, 59, "还有", "秒", "获取验证码")   还有59秒
     */
    public void countDown(final TextView view, final int sumTime, final String leftContent,
                          final String rightContent, final String content) {
        final Timer timer = new Timer();
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        view.setText(leftContent + (sumTime - countTime) + rightContent);
                        if (countTime == sumTime) {
                            view.setText(content);
                            countTime = -1;
                            timerTask.cancel();
                        }
                        countTime++;
                        break;
                }
            }
        };

        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };
        timer.schedule(timerTask, 1000, 1000);
    }

    /**
     * 校验网络-如果没有网络就弹出设置,并返回true.
     *
     * @return
     */
    public boolean validateInternet() {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            openWirelessSet();
            return false;
        } else {
            NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        openWirelessSet();
        return false;
    }

    /**
     * 校验网络-如果没有网络就返回true.
     *
     * @return
     */
    public boolean hasInternetConnected() {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo network = manager.getActiveNetworkInfo();
            if (network != null && network.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }


    public void openWirelessSet() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder
                .setTitle(R.string.prompt)
                .setMessage(context.getString(R.string.check_connection))
                .setPositiveButton(R.string.menu_settings,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                                Intent intent = new Intent(
                                        Settings.ACTION_WIRELESS_SETTINGS);
                                context.startActivity(intent);
                            }
                        })
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        dialog.cancel();
                    }
                });
        dialogBuilder.show();
    }


    /**
     * 长toast
     *
     * @param content
     */
    public void toastLONG(String content) {
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }

    /**
     * 短toast
     *
     * @param content
     */
    public void toastSHORT(String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * log输出
     *
     * @param content
     */
    public void logOut(String content) {
        Log.v(TAG, content);
    }

    /**
     * 检查应用是否在后台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param iconId       图标
     * @param contentTitle 标题
     * @param contentText  内容
     * @param activity
     */
    public void setNotiType(int iconId, String contentTitle,
                            String contentText, Class activity, String from) {
        /*
         * 创建新的Intent，作为点击Notification留言条时， 会运行的Activity
		 */
        Intent notifyIntent = new Intent(this, activity);
        notifyIntent.putExtra("to", from);
        // notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		/* 创建PendingIntent作为设置递延运行的Activity */
        PendingIntent appIntent = PendingIntent.getActivity(this, 0,
                notifyIntent, 0);

		/* 创建Notication，并设置相关参数 */
//        Notification myNoti = new Notification();
//		// 点击自动消失
//		myNoti.flags = Notification.FLAG_AUTO_CANCEL;
//		/* 设置statusbar显示的icon */
//		myNoti.icon = iconId;
//		/* 设置statusbar显示的文字信息 */
//		myNoti.tickerText = contentTitle;
//		/* 设置notification发生时同时发出默认声音 */
//		myNoti.defaults = Notification.DEFAULT_SOUND;
        /* 设置Notification留言条的参数 */
        Notification myNoti = new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(appIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(iconId)
                .setTicker(contentTitle)
                .setOngoing(true)
                .build();


//		myNoti.setLatestEventInfo(this, contentTitle, contentText, appIntent);
        /* 送出Notification */
        notificationManager.notify(0, myNoti);
    }

    /**
     * 关闭键盘事件
     */
    public void closeInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && this.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            // 启动activity时不自动弹出软键盘
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    /**
     * 增加点击edittext区域外，收起软键盘功能
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }


    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > right
                    && event.getY() > top && event.getY() < bottom) {//如果是输入框右边的部分就保留
                return false;
            }
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return false;
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();
        destroyDate();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyDate();
    }

    private void destroyDate() {
        if (waitDialogSupport != null)
            waitDialogSupport.dismiss();

        if (waitDialogRectangle != null)
            waitDialogRectangle.dismiss();
    }
}
