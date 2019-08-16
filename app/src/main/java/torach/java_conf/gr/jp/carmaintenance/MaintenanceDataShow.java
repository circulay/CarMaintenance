package torach.java_conf.gr.jp.carmaintenance;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import java.util.ArrayList;
import java.util.Random;


public class MaintenanceDataShow extends AppCompatActivity {

    private MaintenanceDataHelper helper;
    private Cursor cursor;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private RecyclerView.Adapter adapter;
    private ArrayList<ListItem> data;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_data_show);

        //FloatingActionButtonの設置
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), InputMaintenanceData.class);
                startActivity(intent);
            }
        });


        //SQLiteデータベース読み込み
        readData();

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

    }

    //SQLite保存データ呼び込み
    public void readData() {

            try {
                helper = new MaintenanceDataHelper(MaintenanceDataShow.this);
                db = helper.getReadableDatabase();

                //テーブル内のデータを配列に渡す
                String[] columns = {"date", "category", "price", "notes"};

                cursor = db.query("maintenanceDB", columns, null, null, null, null, null);

                //cursor = db.rawQuery("SELECT _id, date, category, price FROM maintenanceDB", null);

                if(cursor != null && cursor.getCount() > 0) {
                    //int startPosition = cursor.getPosition();
                    //cursor.moveToPosition(-1);
                    //cursor.moveToFirst();

                    data = new ArrayList<>();

                    while (cursor.moveToNext()) {

                        ListItem dataItem = new ListItem();

                        dataItem.setId((new Random()).nextLong());
                        dataItem.setCategory(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_CATEGORY)));
                        dataItem.setDate(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NAME)));
                        dataItem.setPrice(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_PRICE)));
                        dataItem.setNotes(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NOTES)));
                        data.add(dataItem);
                    }
                    //cursor.moveToPosition(startPosition);
                }

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                db.close();
            }

        //リサイクルビューのレイアウト
        recyclerView = (RecyclerView) findViewById(R.id.Recycle_View_Layout);
        recyclerView.setHasFixedSize(true);

        //レイアウトマネージャー作成
        manager  = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        //アダプターをRecyclerManagerに設定
        adapter = new ViewAdapter(data);
        recyclerView.setAdapter(adapter);

    }

    //オプションメニューの制御
    //表示
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // オプションメニューを作成する
        getMenuInflater().inflate(R.menu.option_menu_maintenancedatashow,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //動作処理
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

    //画面遷移
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
