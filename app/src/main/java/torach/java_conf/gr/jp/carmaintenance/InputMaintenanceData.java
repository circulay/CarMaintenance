package torach.java_conf.gr.jp.carmaintenance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InputMaintenanceData extends AppCompatActivity {

    Button button_datePicker;
    Button button_saveData;

    EditText input_priceData;
    EditText input_notesData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_maintenance_data);


        // idがbt_datePickerのButtonを取得
        button_datePicker = (Button) findViewById(R.id.bt_datePicker);

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
    }




    // ダイアログで入力した値をtextViewに入れる - ダイアログから呼び出される
    public void setTextView(String value) {
        TextView date_pickerShowText = (TextView) findViewById(R.id.date_pickerShow);
        date_pickerShowText.setText(value);
    }


    //バックボタン無効化（スプラッシュ画面に戻らない）
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}