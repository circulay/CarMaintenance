package torach.java_conf.gr.jp.carmaintenance;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import static torach.java_conf.gr.jp.carmaintenance.InitialSetting._carId;
import static torach.java_conf.gr.jp.carmaintenance.MaintenanceDataHelper.DB_NAME;
import static torach.java_conf.gr.jp.carmaintenance.MaintenanceDataHelper.ROW_ID;

public class MaintenanceDataShow extends AppCompatActivity {

    private SimpleCursorAdapter adapter;
    private MaintenanceDataHelper helper;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_data_show);

        //FloatingActionButton
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), InputMaintenanceData.class);
                startActivity(intent);
            }
        });


        readData();

    }

    //データ呼び出し
    public void readData() {

        ListView listView = (ListView) findViewById(R.id.listView);

        helper = new MaintenanceDataHelper(MaintenanceDataShow.this);
        SQLiteDatabase db = helper.getReadableDatabase();

        try {

            // テーブル内の全ての項目をString[]の配列columnsに渡す
            String[] columns = new String[]{MaintenanceDataHelper.ROW_ID, MaintenanceDataHelper.ROW_CATEGORY, MaintenanceDataHelper.ROW_NAME, MaintenanceDataHelper.ROW_PRICE};

            // SELECT文を簡単にしてくれるメソッド "address":テーブル名, columns:項目,
            //取得したカーソルをカーソル用のアダプターに設定する。
            cursor = db.query(MaintenanceDataHelper.TABLE_NAME, columns, null, null, null, null, null);

            //cursor.moveToFirst();

            // データベースの項目を決める
            String[] from = new String[]{MaintenanceDataHelper.ROW_CATEGORY, MaintenanceDataHelper.ROW_NAME, MaintenanceDataHelper.ROW_PRICE};
            // layoutファイルの表示箇所を紐付け
            int[] to = new int[]{R.id.content_Data, R.id.date_Data, R.id.price_Data};
            // SimpleCursorAdapter(context, layout, cursorを渡す, データベースの項目取得, 紐付けする)
            adapter = new SimpleCursorAdapter(this, R.layout.data_field, cursor, from, to, 0);
            listView.setAdapter(adapter);

        } finally {
            //cursor.close();
            db.close();
        }
    }



    //オプションメニュー制御
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // オプションメニューを作成する
        getMenuInflater().inflate(R.menu.option_menu_maintenancedatashow,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // オプションメニュー
        switch (item.getItemId()){
            case R.id.menu_Item5:
                moveToCarDataShow();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //車種データ画面に遷移
    public void moveToCarDataShow() {
        Intent intent = new Intent(getApplication(), CarDataShow.class);
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
