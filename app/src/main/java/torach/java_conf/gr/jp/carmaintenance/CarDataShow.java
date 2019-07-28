package torach.java_conf.gr.jp.carmaintenance;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
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


    //バックボタン無効化（スプラッシュ画面に戻らない）
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
