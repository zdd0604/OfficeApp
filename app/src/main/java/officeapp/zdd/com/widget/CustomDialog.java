package officeapp.zdd.com.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;

import officeapp.zdd.com.R;
import officeapp.zdd.com.utils.ScreenInfoUtil;


public class CustomDialog extends Dialog {

    private View layoutView;
	
	public CustomDialog(Context context, View view) {
		this(context, view, R.style.CustomProgressDialog);
	}
	
	public CustomDialog(Context context, View view, int theme) {
        super(context, theme);
        this.layoutView = view;
        init(context);
       
    } 
	
	private void init(Context context) {
		setContentView(layoutView);
		// 底部弹出
		getWindow().setGravity(Gravity.BOTTOM);
				
		getWindow().getAttributes().width = ScreenInfoUtil.getInstance(context).getScreenWidth();
	}
}
