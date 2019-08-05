package torach.java_conf.gr.jp.carmaintenance;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

public class InputMaintenanceData extends AppCompatActivity {

    public Button button_datePicker;
    public Button button_saveData;

    public TextView date_picker;
    public Spinner categoryItem;
    public EditText priceData;
    public EditText notesData;

    public String date_picker_str;
    public String category_str;
    public String priceData_str;
    public String notesData_str;

    public MaintenanceDataHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_maintenance_data);

        //アクションバーに戻るを表示
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //リスナー設定
        SettingListener listener = new SettingListener();

        // idがbt_datePickerのButtonを取得
        button_datePicker = (Button) findViewById(R.id.bt_datePicker);
        button_saveData = (Button) findViewById(R.id.bt_saveData);

        button_saveData.setOnClickListener(listener);

        // clickイベント追加
        button_datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            // クリックしたらダイアログを表示する処理
            public void onClick(View v) {
                // ダイアログクラスをインスタンス化
                CustomDialogFlagment dialog = new CustomDialogFlagment();
                // 表示  getFagmentManager()は固定、sampleは識別タグ
                dialog.show(getFragmentManager(), "sample");
            }
        });


        //DB作成
        helper = new MaintenanceDataHelper(getApplicationContext());

    }

    //ボタンクリック制御
    private class SettingListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            //画面部品id取得
            int id = view.getId();

            switch(id) {
                //カメラ作動
                case R.id.bt_saveData:
                    saveData();
                    moveToMaintenanceDataShow();
                    break;
            }

        }

    }

    //DBに保存する
    public void saveData() {

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();

        date_picker = findViewById(R.id.date_pickerShow);
        categoryItem = findViewById(R.id.category_spinner);
        priceData = findViewById(R.id.input_price);
        notesData = findViewById(R.id.input_notes);


        date_picker_str = date_picker.getText().toString();
        category_str = (String) categoryItem.getSelectedItem();
        priceData_str = priceData.getText().toString();
        notesData_str = notesData.getText().toString();

        try {
            values.put("date", date_picker_str);
            values.put("category", category_str);
            values.put("price", priceData_str);
            values.put("notes", notesData_str);

            db.insert("maintenanceDB", null, values);
        }
        finally{
            db.close();
        }

    }


    // ダイアログで入力した値をtextViewに入れる - ダイアログから呼び出される
    public void setTextView(String value) {
        TextView date_pickerShowText = (TextView) findViewById(R.id.date_pickerShow);
        date_pickerShowText.setText(value);
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


    /*
    //バックボタン無効化
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    */


}