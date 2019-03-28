package torach.java_conf.gr.jp.carmaintenance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {


    //データベースファイル名
    private static final String DATABASE_NAME = "basicData.db";

    //バージョン情報の定数フィールド
    private static final int DATABASE_VERSION = 1;

    //コンストラクタ
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //テーブル作成用SQL文字列の作成

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE basicData (");
        sb.append("_id INTEGER PRIMARY KEY,");
        sb.append("makerName TEXT,");
        sb.append("carName TEXT");
        sb.append(");");
        String sql = sb.toString();

        //SQL実行
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
