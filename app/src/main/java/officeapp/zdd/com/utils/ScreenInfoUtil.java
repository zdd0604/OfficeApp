package officeapp.zdd.com.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 
* @ClassName: ScreenInfoUtil 
* @Description: 屏幕信息工具类
* @author LiangShouHeng
* @date 2015-9-23 上午9:54:41
 */
public class ScreenInfoUtil {

    private static ScreenInfoUtil screenInfoUtil;
//	private Activity activity;
	private DisplayMetrics dm;
	/** 屏幕密度（像素比例：0.75/1.0/1.5/2.0） **/
	private float density;
	/** 屏幕密度（每寸像素：120/160/240/320） **/
	private int densityDPI;
	/** 屏幕详细信息 **/
	private String screenInfo;
	
	private ScreenInfoUtil(Context context){
//		this.activity = activity;
		dm = new DisplayMetrics();
//		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		density = dm.density;
		densityDPI = dm.densityDpi;

	}
	
	public static ScreenInfoUtil getInstance(Context context){
		if(screenInfoUtil == null){
			screenInfoUtil = new ScreenInfoUtil(context);
		}
		
		return screenInfoUtil;
	}
	
	public static void clearOldInfo(){
		screenInfoUtil = null;
	}

//	/** 显示屏幕信息 **/
//	public void showScreenInfo(){
//
//		float xdpi = dm.xdpi;
//		float ydpi = dm.ydpi;
//		Li.i("xdpi=" + xdpi + "; ydpi=" + ydpi);  
//		Li.i("density=" + density + "; densityDPI=" + densityDPI);  
//
//		int screenWidthDip = dm.widthPixels; // 屏幕宽（dip，如：320dip）
//		int screenHeightDip = dm.heightPixels; // 屏幕宽（dip，如：533dip）
//		Li.i("screenWidthDip=" + screenWidthDip + "; screenHeightDip=" + screenHeightDip);  
//
//		int screenWidth = (int) (dm.widthPixels * density + 0.5f); // 屏幕宽（px，如：480px）
//		int screenHeight = (int) (dm.heightPixels * density + 0.5f); // 屏幕高（px，如：800px）
//		Li.i("screenWidth=" + screenWidth + "; screenHeight=" + screenHeight);
//	}
	
	/** 返回屏幕信息 **/
	public String getScreenInfo() {
		if(screenInfo == null){
			float xdpi = dm.xdpi;
			float ydpi = dm.ydpi;
			StringBuffer sb = new StringBuffer();
			sb.append("xdpi=");
			sb.append(xdpi);
			sb.append("; ydpi=");
			sb.append(ydpi);
			sb.append("\ndensity=");
			sb.append(density);
			sb.append("; densityDPI=");
			sb.append(densityDPI);
			int screenWidthPx = dm.widthPixels; // 屏幕宽（px，如：480px）
			int screenHeightPx = dm.heightPixels; // 屏幕高（px，如：800px）
			sb.append("\nscreenWidthDip=");
			sb.append(screenWidthPx);
			sb.append("px");
			sb.append("; screenHeightDip=");
			sb.append(screenHeightPx);
			sb.append("px");
			int screenWidth = (int) (screenWidthPx / density); // 屏幕宽（dip，如：320dip）
			int screenHeight = (int) (screenHeightPx / density);// 屏幕宽（dip，如：533dip）
			sb.append("\nscreenWidth=");
			sb.append(screenWidth);
			sb.append("dip");
			sb.append("; screenHeight=");
			sb.append(screenHeight);
			sb.append("dip");
			
			screenInfo = sb.toString();
		}
		return screenInfo;
	}
	
	public float getDensity(){
		return density;
	}

	public int getScreenWidth(){
		return (int) (dm.widthPixels);
	}
	
	public int getScreenHeight(){
		return (int) (dm.heightPixels);
	}
	
	public int getScreenWidthDP(){
		return (int) (dm.widthPixels / density);
	}
	
	public int getScreenHeightDP(){
		return (int) (dm.heightPixels / density);
	}
	
	/** 转换Dp尺寸为Pixels尺寸 **/
	public int transformationDpToPixels(int Dp){
		return (int) (Dp * density);
	}
	
	public static int getStatusBarHeight(){
		return Resources.getSystem().getDimensionPixelSize(Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
	}
	
}
