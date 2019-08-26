package torach.java_conf.gr.jp.carmaintenance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MaintenanceDataHelper extends SQLiteOpenHelper {

    //テーブル情報定義
    public static final String TABLE_NAME = "maintenanceDB";
    public static final String ROW_ID = "_id";
    public static final String ROW_NAME = "date";
    public static final String ROW_CATEGORY = "category";
    public static final String ROW_PRICE = "price";
    public static final String ROW_NOTES = "notes";

    //データベースファイル名とバージョン情報
    public static final String DB_NAME = "maintenance.db";
    private static final int DB_VERSION = 1;

    //コンストラクタ
    public MaintenanceDataHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    //テーブル作成
    private static final String SQL_CREATE =

        "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME
                + " ("
                + ROW_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ROW_NAME
                + " TEXT,"
                + ROW_CATEGORY
                + " TEXT,"
                + ROW_PRICE
                + " TEXT,"
                + ROW_NOTES
                + " TEXT"
                + ");";


    //データベースのバージョンアップ
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    //データベースのバージョンアップ後のテーブル再作成
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS maintenanceDB");
        onCreate(db);
    }

}


