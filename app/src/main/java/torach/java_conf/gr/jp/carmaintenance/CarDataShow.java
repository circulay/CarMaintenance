package torach.java_conf.gr.jp.carmaintenance;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import static torach.java_conf.gr.jp.carmaintenance.InitialSetting._carId;

public class CarDataShow extends AppCompatActivity {

    TextView textView;
    TextView textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_data_show);


        textView = findViewById(R.id.textView);
        textView3 = findViewById(R.id.textView3);

        CarDataHelper helper = new CarDataHelper(CarDataShow.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            String sql = "Select * From carData Where _id = " + _carId;
            Cursor cursor = db.rawQuery(sql, null);
            String makerName = "";
            String carName = "";

            while(cursor.moveToNext()) {
                int idxMakerName = cursor.getColumnIndex("makerName");
                int idxCarName = cursor.getColumnIndex("carName");
                makerName = cursor.getString(idxMakerName);
                carName = cursor.getString(idxCarName);
            }

            textView.setText(makerName);
            textView3.setText(carName);
        }

        finally {
            db.close();
        }


    }

    //オプションメニュー制御
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // オプションメニューを作成する
        getMenuInflater().inflate(R.menu.option_menu_cardatashow,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // オプションメニュー
        switch (item.getItemId()){
            case R.id.menu_Item3:
                moveToInitialSetting();
                break;
            case R.id.menu_Item4:
                moveToMaintenanceDataShow();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //車種データ画面に遷移
    public void moveToInitialSetting() {
        Intent intent = new Intent(getApplication(), InitialSetting.class);
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
