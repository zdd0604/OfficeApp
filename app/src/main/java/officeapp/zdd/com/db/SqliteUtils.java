package officeapp.zdd.com.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * Created by Admin on 2017/3/31.
 */

public class SqliteUtils {
    public static SQLiteDatabase db;
    private Context context;

    public SqliteUtils(Context context) {
        this.context = context;
    }

    public void open() throws SQLiteException {
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context, SqliteStatement.DBNAME, null, 1);
        try {
            db = dbOpenHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = dbOpenHelper.getReadableDatabase();
        }
    }

    /**
     * 插入个人信息表
     *
     * @param name
     * @param age
     * @param sex
     * @param address
     * @param phone
     * @return
     */
    public boolean insertUserInfo(String name, int age, String sex, String address, String phone) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteStatement.UserName, name);
        contentValues.put(SqliteStatement.UserAge, age);
        contentValues.put(SqliteStatement.UserSex, sex);
        contentValues.put(SqliteStatement.UserAddress, address);
        contentValues.put(SqliteStatement.UserPhone, phone);
        return db.insert(SqliteStatement.USERINFO, null, contentValues) != -1;
    }

    public Cursor selectUserInfo(String phone) {
        Cursor contentValues = db.query(SqliteStatement.USERINFO,
                new String[]{SqliteStatement.UserName},
                SqliteStatement.UserPhone + "=?",
                new String[]{phone}, null, null, null);
        return contentValues;
    }


    /**
     * 地图的相关信息
     * @param address
     * @param longitude
     * @param latitude
     * @param mapTime
     * @param content
     * @param photos
     * @return
     */
    public boolean insertMapLocation(String address,String title, double longitude,
                                     double latitude, String mapTime, String content,
                                     String photos)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteStatement.MapAddress, address);
        contentValues.put(SqliteStatement.MapMemoryTitle, title);
        contentValues.put(SqliteStatement.MapLongitude, longitude);
        contentValues.put(SqliteStatement.MapLatitude, latitude);
        contentValues.put(SqliteStatement.MapTime, mapTime);
        contentValues.put(SqliteStatement.MapMemoryContent, content);
        contentValues.put(SqliteStatement.MapPhotos, photos);
        return db.insert(SqliteStatement.MAPTABLE, null, contentValues) != -1;
    }

    /**
     * 查询所有添加的地址信息
     * @return
     */
    public Cursor selectMapInfo() {
//        Cursor query(String table, String[] columns, String selection,
//                String[] selectionArgs, String groupBy, String having,
//                String orderBy)
        Cursor contentValues = db.query(SqliteStatement.MAPTABLE,null,null, null, null, null, null);
        return contentValues;
    }
}
