package torach.java_conf.gr.jp.carmaintenance;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Date;

import java.text.SimpleDateFormat;

public class InitialSetting extends AppCompatActivity  {

    //変数宣言
    //ボタン変数
    Button bt_SetRegist;


    //文字入力変数
    EditText input_MakerName;
    EditText input_CarName;
    EditText input_LicenceNumber;

    static String input_MakerNameStr;
    static String input_CarNameStr;
    static String input_LicenceNumberStr;

    static int _carId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_setting);

        //ボタンオブジェクト取得
        bt_SetRegist = (Button) findViewById(R.id.bt_SetRegist);


        //リスナークラスのインスタンス作成
        SettingListener listener = new SettingListener();

        //ボタンにリスナーを設定
        bt_SetRegist.setOnClickListener(listener);

    }


    private class SettingListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            //画面部品id取得
            int id = view.getId();

            switch(id) {
                //登録ボタン
                case R.id.bt_SetRegist:
                    saveButtonClick();
                    moveCarData();
                    break;

                /*
                //車種データへの画面遷移ボタン
                case R.id.bt_CarDataShow:
                    moveCarData();
                    break;
                */
            }

        }
    }

    public void saveButtonClick() {
        //入力された文字列を取得
        //入力欄のEditTextオブジェクトを取得
        input_MakerName = findViewById(R.id.edit_MakerName);
        input_CarName = findViewById(R.id.edit_CarName);
        input_LicenceNumber = findViewById(R.id.edit_LicenceNumber);

        input_MakerNameStr = input_MakerName.getText().toString();
        input_CarNameStr = input_CarName.getText().toString();
        input_LicenceNumberStr = input_LicenceNumber.getText().toString();

        //データベースヘルパーオブジェクトの作成
        CarDataHelper helper = new CarDataHelper(InitialSetting.this);

        //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
        SQLiteDatabase db = helper.getWritableDatabase();


        try {
            String sqlDelete ="DELETE FROM CarData WHERE _id = ?";

            SQLiteStatement stmt = db.compileStatement(sqlDelete);

            stmt.bindLong(1, _carId);
            stmt.executeUpdateDelete();

            String sqlInsert = "INSERT INTO CarData (_id, makername, carname, licencenumber) VALUES (?, ?, ?, ?)";
            stmt = db.compileStatement(sqlInsert);
            stmt.bindLong(1, _carId);
            stmt.bindString(2, input_MakerNameStr);
            stmt.bindString(3, input_CarNameStr);
            stmt.bindString(4, input_LicenceNumberStr);

            stmt.executeInsert();
        }
        finally {
            db.close();
        }

    }

    //車種データトップ画面に遷移
    public void moveCarData() {
        Intent intent = new Intent(getApplication(), MaintenanceDataShow.class);
        startActivity(intent);
    }

    //オプションメニュー制御
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // オプションメニューを作成する
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // オプションメニュー
        switch (item.getItemId()){
            case R.id.menu_Item1:
                moveToCarDataShow();
                break;
            case R.id.menu_Item2:
                moveToMaintenanceDataShow();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //車種データ画面に遷移
    public void moveToCarDataShow() {
        Intent intent = new Intent(getApplication(), CarDataShow.class);
        startActivity(intent);
    }

    //データ一覧画面に遷移
    public void moveToMaintenanceDataShow() {
        Intent intent = new Intent(getApplication(), MaintenanceDataShow.class);
        startActivity(intent);
    }

    //バックボタン無効化（スプラッシュ画面に戻らない）
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
