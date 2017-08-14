package officeapp.zdd.com.utils;

import android.text.TextUtils;

import java.util.UUID;

/**
 * Created by Admin on 2016/12/5.
 */

public class StringUtils {

    // 获取可变UUID
    public static String getMyUUID() {
        UUID uuid = UUID.randomUUID();
        String uniqueId = uuid.toString();
        return uniqueId;
    }

    /***
     * 判断字符串是否为空
     *
     * @param strContent
     *            你要判断的字符串
     * @return 非空返回true
     */
    public static boolean isStrTrue(String strContent) {
        if (strContent != null && !"".equals(strContent)
                && !TextUtils.isEmpty(strContent)
                && !"".equals(strContent.trim())) {
            return true;
        }
        return false;
    }
}
