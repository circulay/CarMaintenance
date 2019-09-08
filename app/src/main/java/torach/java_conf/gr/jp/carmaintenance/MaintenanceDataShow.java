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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MaintenanceDataShow extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager manager;
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

        //区切り線
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);


        //スワイプ削除読み出し
        listsSwipe();

    }

    //SQLite保存データ呼び込み
    public void readData() {

        //テーブル内のデータを配列に渡す
        String[] columns = {"_id", "date", "category", "price", "notes"};

        Cursor cursor;


            try {
                MaintenanceDataHelper helper = new MaintenanceDataHelper(MaintenanceDataShow.this);
                db = helper.getReadableDatabase();

                cursor = db.query("maintenanceDB", columns, null, null, null, null, null);

                if(cursor != null && cursor.getCount() > 0) {

                    data = new ArrayList<>();

                    while (cursor.moveToNext()) {

                        ListItem dataItem = new ListItem();

                        dataItem.setId(cursor.getLong(cursor.getColumnIndex(MaintenanceDataHelper.ROW_ID)));
                        //long idd = cursor.getLong(cursor.getColumnIndex(MaintenanceDataHelper.ROW_ID));
                        //dataItem.setId(String.valueOf(idd));

                        dataItem.setCategory(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_CATEGORY)));
                        dataItem.setDate(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NAME)));
                        dataItem.setPrice(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_PRICE)));
                        dataItem.setNotes(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NOTES)));
                        data.add(dataItem);
                    }
                }

            } finally {
                cursor = db.query("maintenanceDB", columns, null, null, null, null, null);
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

    //ItemTouchHelper.Simpleを使ったリストのスワイプ削除
    public void listsSwipe() {
        ItemTouchHelper.SimpleCallback mIth = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        final int position = viewHolder.getAdapterPosition();
                        long id = adapter.getItemId(position);
                        data.remove(position);
                        adapter.notifyDataSetChanged();


                        try {

                            MaintenanceDataHelper helper = new MaintenanceDataHelper(MaintenanceDataShow.this);
                            db = helper.getWritableDatabase();

                            //db.delete(MaintenanceDataHelper.TABLE_NAME, MaintenanceDataHelper.ROW_ID + "= ?", new String[]{String.valueOf(id)});
                            db.delete("maintenanceDB", "_id=?", new String[]{String.valueOf(id)});
                        }
                        finally {
                            db.close();
                        }

                    }

                };

        new ItemTouchHelper(mIth).attachToRecyclerView(recyclerView);

    }

    //修理データ抽出
    public void selectRepairData() {

        //テーブル内のデータを配列に渡す
        String[] columns = {"_id", "date", "category", "price", "notes"};

        Cursor cursor;


        try {
            MaintenanceDataHelper helper = new MaintenanceDataHelper(MaintenanceDataShow.this);
            db = helper.getReadableDatabase();

            cursor = db.query("maintenanceDB", columns, "category = ?", new String[]{"修理"}, null, null, null, null);

            if(cursor != null && cursor.getCount() > 0) {

                data = new ArrayList<>();

                while (cursor.moveToNext()) {

                    ListItem dataItem = new ListItem();

                    dataItem.setId(cursor.getLong(cursor.getColumnIndex(MaintenanceDataHelper.ROW_ID)));
                    //long idd = cursor.getLong(cursor.getColumnIndex(MaintenanceDataHelper.ROW_ID));
                    //dataItem.setId(String.valueOf(idd));

                    dataItem.setCategory(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_CATEGORY)));
                    dataItem.setDate(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NAME)));
                    dataItem.setPrice(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_PRICE)));
                    dataItem.setNotes(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NOTES)));
                    data.add(dataItem);
                }
            }

        } finally {
            cursor = db.query("maintenanceDB", columns, "category = ?", new String[]{"修理"}, null, null, null, null);
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

    //車検データ抽出
    public void selectCheckData() {

        //テーブル内のデータを配列に渡す
        String[] columns = {"_id", "date", "category", "price", "notes"};

        Cursor cursor;


        try {
            MaintenanceDataHelper helper = new MaintenanceDataHelper(MaintenanceDataShow.this);
            db = helper.getReadableDatabase();

            cursor = db.query("maintenanceDB", columns, "category = ?", new String[]{"車検"}, null, null, null, null);

            if(cursor != null && cursor.getCount() > 0) {

                data = new ArrayList<>();

                while (cursor.moveToNext()) {

                    ListItem dataItem = new ListItem();

                    dataItem.setId(cursor.getLong(cursor.getColumnIndex(MaintenanceDataHelper.ROW_ID)));
                    //long idd = cursor.getLong(cursor.getColumnIndex(MaintenanceDataHelper.ROW_ID));
                    //dataItem.setId(String.valueOf(idd));

                    dataItem.setCategory(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_CATEGORY)));
                    dataItem.setDate(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NAME)));
                    dataItem.setPrice(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_PRICE)));
                    dataItem.setNotes(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NOTES)));
                    data.add(dataItem);
                }
            }

        } finally {
            cursor = db.query("maintenanceDB", columns, "category = ?", new String[]{"車検"}, null, null, null, null);
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

    //事故データ抽出
    public void selectAccidentData() {

        //テーブル内のデータを配列に渡す
        String[] columns = {"_id", "date", "category", "price", "notes"};

        Cursor cursor;


        try {
            MaintenanceDataHelper helper = new MaintenanceDataHelper(MaintenanceDataShow.this);
            db = helper.getReadableDatabase();

            cursor = db.query("maintenanceDB", columns, "category = ?", new String[]{"事故"}, null, null, null, null);

            if(cursor != null && cursor.getCount() > 0) {

                data = new ArrayList<>();

                while (cursor.moveToNext()) {

                    ListItem dataItem = new ListItem();

                    dataItem.setId(cursor.getLong(cursor.getColumnIndex(MaintenanceDataHelper.ROW_ID)));
                    //long idd = cursor.getLong(cursor.getColumnIndex(MaintenanceDataHelper.ROW_ID));
                    //dataItem.setId(String.valueOf(idd));

                    dataItem.setCategory(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_CATEGORY)));
                    dataItem.setDate(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NAME)));
                    dataItem.setPrice(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_PRICE)));
                    dataItem.setNotes(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NOTES)));
                    data.add(dataItem);
                }
            }

        } finally {
            cursor = db.query("maintenanceDB", columns, "category = ?", new String[]{"事故"}, null, null, null, null);
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
            case R.id.menu_Item6:
                selectRepairData();
                break;
            case R.id.menu_Item7:
                selectCheckData();
                break;
            case R.id.menu_Item8:
                selectAccidentData();
                break;
            case R.id.menu_Item9:
                readData();
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
