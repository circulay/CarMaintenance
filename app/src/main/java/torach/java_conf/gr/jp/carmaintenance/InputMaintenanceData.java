package torach.java_conf.gr.jp.carmaintenance;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class InputMaintenanceData extends AppCompatActivity {

    private Button button_datePicker;
    private Button button_saveData;

    private TextView date_picker = null;
    private Spinner categoryItem = null;
    private EditText priceData = null;
    private EditText notesData = null;

    private String date_picker_str = null;
    private String category_str = null;
    private String priceData_str = null;
    private String notesData_str = null;

    private MaintenanceDataHelper helper = null;
    private SQLiteDatabase db = null;

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


        //DBヘルパーの準備
        //helper = new MaintenanceDataHelper(getApplicationContext());
        helper = new MaintenanceDataHelper(this);
        date_picker = findViewById(R.id.date_pickerShow);
        categoryItem = findViewById(R.id.category_spinner);
        priceData = findViewById(R.id.input_price);
        notesData = findViewById(R.id.input_notes);

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

        /*
        //未入力チェック
        if (date_picker_str.isEmpty() || category_str.isEmpty() || priceData_str.isEmpty() || notesData_str.isEmpty()) {
            button_saveData.setError("未入力のデータがあります");
        }*/

        try {
            db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();

            date_picker_str = date_picker.getText().toString();
            category_str = (String) categoryItem.getSelectedItem();
            priceData_str = priceData.getText().toString();
            notesData_str = notesData.getText().toString();

                values.put("date", date_picker_str);
                values.put("category", category_str);
                values.put("price", priceData_str);
                values.put("notes", notesData_str);

                db.insert("maintenanceDB", null, values);
                Toast.makeText(this, "データを登録しました。", Toast.LENGTH_SHORT).show();
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