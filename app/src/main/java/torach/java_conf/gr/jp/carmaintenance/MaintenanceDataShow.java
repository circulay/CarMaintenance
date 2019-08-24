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
import java.util.Random;

import static torach.java_conf.gr.jp.carmaintenance.MaintenanceDataHelper.ROW_ID;
import static torach.java_conf.gr.jp.carmaintenance.MaintenanceDataHelper.TABLE_NAME;


public class MaintenanceDataShow extends AppCompatActivity {

    private MaintenanceDataHelper helper;
    private Cursor cursor;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private RecyclerView.Adapter adapter;
    private ArrayList<ListItem> data;
    private SQLiteDatabase db;

    protected int positionDelete;


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

        //スワイプ削除読み出し
        listsSwipe();

    }

    //SQLite保存データ呼び込み
    public void readData() {

            try {
                helper = new MaintenanceDataHelper(MaintenanceDataShow.this);
                db = helper.getReadableDatabase();

                //テーブル内のデータを配列に渡す
                String[] columns = {"date", "category", "price", "notes"};

                cursor = db.query("maintenanceDB", columns, null, null, null, null, null);

                if(cursor != null && cursor.getCount() > 0) {

                    data = new ArrayList<>();

                    while (cursor.moveToNext()) {

                        ListItem dataItem = new ListItem();

                        //dataItem.setId(cursor.getInt(cursor.getColumnIndex(MaintenanceDataHelper.ROW_ID)));
                        //dataItem.setId((new Random()).nextLong());

                        dataItem.setCategory(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_CATEGORY)));
                        dataItem.setDate(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NAME)));
                        dataItem.setPrice(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_PRICE)));
                        dataItem.setNotes(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NOTES)));
                        data.add(dataItem);
                    }
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


    //データの削除
    /*private void deleteDataRecord() {
        db.delete(MaintenanceDataHelper.TABLE_NAME, MaintenanceDataHelper.ROW_ID + "=" + positionDelete, null);

        try {
            helper = new MaintenanceDataHelper(MaintenanceDataShow.this);
            db = helper.getReadableDatabase();

            db.execSQL("DELETE FROM maintenanceDB WHERE _id = positionDelete");
        }
        finally {
            db.close();
        }
    }*/



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

    //ItemTouchHelper.Simpleを使ったリストのスワイプ削除
    protected void listsSwipe() {
        ItemTouchHelper mIth = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        //final int fromPos = viewHolder.getAdapterPosition();
                        //final int toPos = target.getAdapterPosition();
                        //adapter.notifyItemMoved(fromPos, toPos);// move item in `fromPos` to `toPos` in adapter.
                        return false;// true if moved, false otherwise
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        final int position = viewHolder.getAdapterPosition();
                        data.remove(position);
                        adapter.notifyItemRemoved(position);

                        positionDelete = position;

                        //int id = (int) viewHolder.itemView.getTag();

                        try {
                            //helper = new MaintenanceDataHelper(MaintenanceDataShow.this);
                            db = helper.getWritableDatabase();

                            //db.execSQL("DELETE FROM maintenanceDB WHERE _id = positionDelete null");
                            db.delete(TABLE_NAME, ROW_ID + "="+ positionDelete, null);

                        }
                        finally {
                            db.close();
                        }


                        //deleteDataRecord();

                        adapter.notifyDataSetChanged();
                    }

                });
                    mIth.attachToRecyclerView(recyclerView);

    }

}
