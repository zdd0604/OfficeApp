package officeapp.zdd.com.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import officeapp.zdd.com.R;


public class WaitDialog extends Dialog {
	private TextView tv;

	public WaitDialog(Context context) {
		super(context, R.style.noticeDialogStyle);
	}

	private WaitDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_certical);
//		Log.e("WaitDialog","oncreat");
		tv = (TextView)this.findViewById(R.id.tv);
		LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.LinearLayout);
		linearLayout.getBackground().setAlpha(100);
		tv.setText("正在加载...");

		setCanceledOnTouchOutside(false);
		//setCancelable(false);   //backbtn of phoneself
	}

	public void setTextView(String msg){
		this.tv.setText(msg);
	}

	public void setOnKeyListener(OnKeyListener onKeyListener) {
		// TODO Auto-generated method stub

	}

}