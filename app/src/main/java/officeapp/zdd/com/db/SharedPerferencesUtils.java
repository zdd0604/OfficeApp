package officeapp.zdd.com.db;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zdd in on 2017/3/31.
 */

public class SharedPerferencesUtils {
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    private static SharedPerferencesUtils instance;

    public synchronized static SharedPerferencesUtils getInstance(Context context) {
        if (null == instance) {
            instance = new SharedPerferencesUtils(context);
        }
        return instance;
    }

    public SharedPerferencesUtils(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("ZWIDGETS", context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    /**
     * 保存登录信息
     *
     * @param name
     * @param password
     */
    public void setLogin(String name, String password) {
        editor.putString("name", name);
        editor.putString("password", password);
        editor.commit();
    }

    /**
     * 获取字符类型的value
     *
     * @param key
     * @return
     */
    public String getSharedStringValue(String key) {
        return sharedPreferences.getString(key, "defaultname");
    }

    /**
     * 获取int类型的value
     *
     * @param key
     * @return
     */
    public int getSharedInegerValue(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    /**
     * 清理缓存文件
     */
    public void clearSharePreference() {
        editor.clear();
        editor.commit();
    }

}
