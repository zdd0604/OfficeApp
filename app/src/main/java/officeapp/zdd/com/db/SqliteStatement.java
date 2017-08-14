package officeapp.zdd.com.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/3/31.
 * 数据库以及表的操作
 */

public class SqliteStatement {

    /**
     * 库名
     */
    public static String DBNAME = "ZDD";

    /**
     * 程序所有的表的创建语句
     */
    public static List<String> listTable = new ArrayList<>();

    //---------------用户表-----------------------
    //表名
    public static String USERINFO = "UserTable";
    //字段
    public static String UserID = "UserID";
    public static String UserName = "UserName";
    public static String UserAge = "UserAge";
    public static String UserSex = "UserSex";
    public static String UserAddress = "UserAddress";
    public static String UserPhone = "UserPhone";

//    public static String


    //---------------创建表的语句------------------
    public static String CUserTable = "create table "
            + USERINFO + "( "
            + UserID + " integer primary key, "
            + UserName + " text, "
            + UserAge + " int,"
            + UserSex + " text, "
            + UserAddress + " text , "
            + UserPhone + " text "
            + ")";


    //---------------用户定位信息表-----------------------
    //表名
    public static String MAPTABLE = "MapTable";
    //字段
    public static String MapID = "MapID";
    public static String MapMemoryTitle = "MapMemoryTitle";
    public static String MapAddress = "MapAddress";
    public static String MapLongitude = "MapLongitude";//经度
    public static String MapLatitude= "MapLatitude";//纬度
    public static String MapTime = "MapTime";
    public static String MapMemoryContent = "MapMemoryContent";
    public static String MapPhotos = "MapPhotos";

    //---------------创建表的语句------------------
    public static String CMapTable = "create table "
            + MAPTABLE + "( "
            + MapID + " integer primary key, "
            + MapMemoryTitle + " text,"
            + MapAddress + " text,"
            + MapLongitude + " text, "
            + MapLatitude + " text, "
            + MapTime + " datetime, "
            + MapMemoryContent + " text, "
            + MapPhotos + " text "
            + ")";


    public static List<String> getCSqlite() {
        listTable.add(CUserTable);
        listTable.add(CMapTable);
        return listTable;
    }
}
