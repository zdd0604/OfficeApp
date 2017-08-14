package officeapp.zdd.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;

import com.wangjie.androidbucket.utils.ABTextUtil;

import java.util.ArrayList;
import java.util.List;

import officeapp.zdd.com.R;
import officeapp.zdd.com.common.Constant;
import officeapp.zdd.com.mylibrary.RapidFloatingActionButton;
import officeapp.zdd.com.mylibrary.RapidFloatingActionHelper;
import officeapp.zdd.com.mylibrary.RapidFloatingActionLayout;
import officeapp.zdd.com.mylibrary.contentimpl.labellist.RFACLabelItem;
import officeapp.zdd.com.mylibrary.contentimpl.labellist.RapidFloatingActionContentLabelList;
import officeapp.zdd.com.utils.StringUtils;
import officeapp.zdd.com.widget.SuperActivity;

public class MemoryActivity extends SuperActivity implements
        RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {
    private String mymoryAddress;
    private EditText ed_memory_name, ed_memory_time,
            ed_memory_address, ed_memory_content;
    private double myLatitude = 0.00;
    private double myLongitude = 0.00;

    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaButton;
    private RapidFloatingActionHelper rfabHelper;
    private List<RFACLabelItem> items = new ArrayList<>();

    private Thread mThread;

    public static final int START_TYPE = 0;
    private static final int SUCCEED_TYPE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar = getSupportActionBar();
        setContentView(R.layout.activity_memory);
        showActionBar(R.string.memotyActivity_name);

        getIntentDate();

        initView();

        addRapidBtn();
    }

    private void initView() {
        ed_memory_name = (EditText) findViewById(R.id.ed_memory_name);
        ed_memory_time = (EditText) findViewById(R.id.ed_memory_time);
        ed_memory_address = (EditText) findViewById(R.id.ed_memory_address);
        ed_memory_content = (EditText) findViewById(R.id.ed_memory_content);
        rfaLayout = (RapidFloatingActionLayout) findViewById(R.id.label_list_sample_rfal);
        rfaButton = (RapidFloatingActionButton) findViewById(R.id.label_list_sample_rfab);


        if (StringUtils.isStrTrue(mymoryAddress)) {
            ed_memory_address.setText(mymoryAddress);
        }
        ed_memory_time.setText(getCurrentTime(Constant.yyyy_MM_dd_HH_mm_ss));
    }

    private void addRapidBtn() {

        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(context);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);

        items.add(new RFACLabelItem<Integer>()
                .setLabel("保存")
                .setResId(R.mipmap.ico_test_d)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setWrapper(0)
        );
//        items.add(new RFACLabelItem<Integer>()
//                        .setLabel("tiantian.china.2@gmail.com")
////                        .setResId(R.mipmap.ico_test_c)
//                        .setDrawable(getResources().getDrawable(R.mipmap.ico_test_c))
//                        .setIconNormalColor(0xff4e342e)
//                        .setIconPressedColor(0xff3e2723)
//                        .setLabelColor(Color.WHITE)
//                        .setLabelSizeSp(14)
//                        .setLabelBackgroundDrawable(ABShape.generateCornerShapeDrawable(0xaa000000, ABTextUtil.dip2px(context, 4)))
//                        .setWrapper(1)
//        );
//        items.add(new RFACLabelItem<Integer>()
//                .setLabel("WangJie")
//                .setResId(R.mipmap.ico_test_b)
//                .setIconNormalColor(0xff056f00)
//                .setIconPressedColor(0xff0d5302)
//                .setLabelColor(0xff056f00)
//                .setWrapper(2)
//        );
//        items.add(new RFACLabelItem<Integer>()
//                .setLabel("Compose")
//                .setResId(R.mipmap.ico_test_a)
//                .setIconNormalColor(0xff283593)
//                .setIconPressedColor(0xff1a237e)
//                .setLabelColor(0xff283593)
//                .setWrapper(3)
//        );
        rfaContent.setItems(items)
                .setIconShadowRadius(ABTextUtil.dip2px(context, 5))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(ABTextUtil.dip2px(context, 5));

        rfabHelper = new RapidFloatingActionHelper(
                context,
                rfaLayout,
                rfaButton,
                rfaContent
        ).build();
    }


    public void getIntentDate() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(bundleKry);
        mymoryAddress = bundle.getString("address");
        myLongitude = bundle.getDouble("myLongitude");
        myLatitude = bundle.getDouble("myLatitude");
    }

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        addSqliteDate(item);
        rfabHelper.toggleContent();
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        addSqliteDate(item);
        rfabHelper.toggleContent();
    }

    private void addSqliteDate(RFACLabelItem item) {
        waitDialogRectangle.show();
        waitDialogRectangle.setText("正在保存...");
        switch ((int) item.getWrapper()) {
            case START_TYPE:
                handler.sendEmptyMessage(START_TYPE);
                break;
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_TYPE:
                    sqliteUtils.insertMapLocation(mymoryAddress,
                            getEditextString(ed_memory_name),
                            myLongitude,
                            myLatitude,
                            getEditextString(ed_memory_time),
                            getEditextString(ed_memory_content),
                            "没有");
                    waitThread();
                    break;
                case SUCCEED_TYPE:
                    setResult(22);
                    deleteView();
                    toastLONG("添加成功");
                    finish();
                    break;
            }
        }
    };

    private void deleteView(){
        waitDialogRectangle.dismiss();
    }


    private void waitThread() {
        mThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    handler.sendEmptyMessage(SUCCEED_TYPE);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
    }

}
