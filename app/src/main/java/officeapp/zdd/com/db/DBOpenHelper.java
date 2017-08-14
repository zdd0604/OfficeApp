package officeapp.zdd.com.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by Admin on 2017/3/31.
 * 创建数据库
 * 当你完成了对数据库的操作（例如你的 Activity 已经关闭），需要调用 SQLiteDatabase 的 Close() 方法来释放掉数据库连接。
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 创建数据库后，对数据库的操作
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
       List<String> tabls = SqliteStatement.getCSqlite();
        if (tabls.size() > 0) {
            for (int i = 0; i < tabls.size(); i++) {
                db.execSQL(tabls.get(i));
            }
        }
    }

    /**
     * 更改数据库版本的操作
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (SqliteStatement.listTable.size() > 0) {
            for (int i = 0; i < SqliteStatement.listTable.size(); i++) {
                db.execSQL("DROP TABLE IF EXISTS " + SqliteStatement.listTable.get(i));
            }
            onCreate(db);
        }
    }
}
