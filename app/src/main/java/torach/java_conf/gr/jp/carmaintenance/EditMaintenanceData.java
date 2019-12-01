package torach.java_conf.gr.jp.carmaintenance;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.ParseException;


public class EditMaintenanceData extends AppCompatActivity {

    private Button button_datePicker;
    private Button button_updateData;

    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_maintenance_data);



        //アクションバーに戻るを表示
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //リスナー設定
        EditMaintenanceData.SettingListener listener = new EditMaintenanceData.SettingListener();

        // Buttonを取得
        button_datePicker = (Button) findViewById(R.id.bt_datePicker);
        button_updateData = (Button) findViewById(R.id.bt_updateData);

        button_updateData.setOnClickListener(listener);
        button_datePicker.setOnClickListener(listener);

        obtainedDate();

    }

    //ボタンクリック制御
    private class SettingListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            //画面部品id取得
            int funk_id = view.getId();

            switch(funk_id) {
                case R.id.bt_updateData:
                    updateData();
                    moveToMaintenanceDataShow();
                    break;
                case R.id.bt_datePicker:
                    dialog_listener();
                    break;

            }

        }

    }

    //DatePickerDialogFlagmentを呼び出す
    public void dialog_listener() {
        DialogFragment dialog = new DateDialogFragment();
        dialog.show(getSupportFragmentManager(), "dialog_date");
    }


    //該当するデータをインテントで受け渡して表示する。
    public void obtainedDate() {

        //onBindViewHolderのクリックイベントからpositionを受け取る
        Intent intent = getIntent();

        //String obCategory = intent.getStringExtra("iCategory");
        String obDate = intent.getStringExtra("iDate");
        String obPrice = intent.getStringExtra("iPrice");
        String obNotes = intent.getStringExtra("iNotes");

        // 数値を表す文字列（3桁区切り) を 整数値（int型) に変換した上で、カンマなしの文字列に修正し直す
        int obPrice_value;
        String obPrice_str = null;

        try {
            Number number = NumberFormat.getInstance().parse(obPrice);
            obPrice_value = number.intValue();
            obPrice_str = String.valueOf(obPrice_value);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        TextView date_picker = findViewById(R.id.date_pickerShow);
        //Spinner categoryItem = findViewById(R.id.update_category_spinner);
        EditText priceData = findViewById(R.id.update_price);
        EditText notesData = findViewById(R.id.update_notes);

        date_picker.setText(obDate);
        priceData.setText(obPrice_str);
        notesData.setText(obNotes);

    }


    //DBを更新する
    public void updateData() {

        Intent intent = getIntent();

        //データベースのID値を受け取る
        long id = intent.getLongExtra("iPos", 0L);

        TextView up_date_picker = findViewById(R.id.date_pickerShow);
        Spinner up_categoryItem = findViewById(R.id.update_category_spinner);
        EditText up_priceData = findViewById(R.id.update_price);
        EditText up_notesData = findViewById(R.id.update_notes);

        String date_picker_str = up_date_picker.getText().toString();
        String category_str = (String) up_categoryItem.getSelectedItem();
        String priceData_str = up_priceData.getText().toString();
        String notesData_str = up_notesData.getText().toString();


        try {

            MaintenanceDataHelper helper = new MaintenanceDataHelper(this);
            db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();

            int priceData_value;
            String priceData_str2;


            if(date_picker_str.equals("")) {
                final TodayDate todayDate;
                todayDate = new TodayDate();
                date_picker_str = todayDate.today_Date;
            }

            if(priceData_str.equals("")) {
                priceData_str2 = "";
            } else {

                priceData_value = Integer.parseInt(priceData_str);
                priceData_str2 = String.format("%,d", priceData_value);
            }

            values.put("date", date_picker_str);
            values.put("category", category_str);
            values.put("price", priceData_str2);
            values.put("notes", notesData_str);

            db.update("maintenanceDB", values, "_id=?", new String[]{String.valueOf(id)});
            Toast.makeText(this, "データを更新しました。", Toast.LENGTH_SHORT).show();
        }
        finally{
            db.close();
        }

    }





    /*
    //オプションメニュー制御
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // オプションメニューを作成する
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }*/


    /*
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
    }*/


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
