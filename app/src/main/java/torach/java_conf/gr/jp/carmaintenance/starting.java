package torach.java_conf.gr.jp.carmaintenance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class starting extends AppCompatActivity {

    //初回起動の判断

    private final String data = "MAIN_SETTING";
    private final String dataIntPreTag = "dataIPT";
    private SharedPreferences startJudge;
    private SharedPreferences.Editor edit;
    private int dataCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);


        Handler hdl = new Handler();

        //秒数指定
        hdl.postDelayed(new splashHandler(), 1600);
    }

    class splashHandler implements Runnable {
        public void run() {

            startJudge = getSharedPreferences(data, MODE_PRIVATE);
            dataCount = startJudge.getInt(dataIntPreTag, 0);
            dataCount++;
            edit = startJudge.edit();

            if (dataCount == 1) {
                //初期設定画面への遷移
                Intent intent1 = new Intent(starting.this, InitialSetting.class);
                startActivity(intent1);
                edit.putInt(dataIntPreTag, dataCount).apply();
            } else {
                Intent intent2 = new Intent(starting.this, MaintenanceDataTop.class);
                startActivity(intent2);
                edit.putInt(dataIntPreTag, dataCount).apply();
            }


        }
    }

}
