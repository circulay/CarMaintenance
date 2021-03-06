package torach.java_conf.gr.jp.carmaintenance;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


import java.text.NumberFormat;
import java.util.ArrayList;

public class MaintenanceDataShow extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager manager;
    private ArrayList<ListItem> data;
    private SQLiteDatabase db;

    private AdView mAdView;

    private TextView totalPrice = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_data_show);

        //AdMob広告
        MobileAds.initialize(this, getString(R.string.ads_AppId));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        totalPrice = findViewById(R.id.totalPriceShow);




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

                String order_by = "date ASC";

                cursor = db.query("maintenanceDB", columns, null, null, null, null, order_by);

                if(cursor != null && cursor.getCount() > 0) {

                    data = new ArrayList<>();

                    while (cursor.moveToNext()) {

                        ListItem dataItem = new ListItem();

                        dataItem.setId(cursor.getLong(cursor.getColumnIndex(MaintenanceDataHelper.ROW_ID)));
                        //long idd = cursor.getLong(cursor.getColumnIndex(MaintenanceDataHelper.ROW_ID));
                        //dataItem.setId(String.valueOf(idd));

                        dataItem.setCategory(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_CATEGORY)));
                        dataItem.setDate(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NAME)));
                        dataItem.setPrice(cursor.getInt(cursor.getColumnIndex(MaintenanceDataHelper.ROW_PRICE)));
                        dataItem.setNotes(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NOTES)));
                        data.add(dataItem);
                    }
                }

            } finally {
                String order_by = "date ASC";
                cursor = db.query("maintenanceDB", columns, null, null, null, null, order_by);
                if (cursor != null) {
                    cursor.close();
                }
                db.close();

                totalAllPrice();
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

        final Drawable deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_swipe_delete);

        ItemTouchHelper.SimpleCallback mIth = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {


                        //アラートダイヤログ
                        AlertDialog.Builder alert = new AlertDialog.Builder(MaintenanceDataShow.this);
                        alert.setTitle("削除確認");
                        alert.setMessage("このデータを削除しますか？");
                        alert.setPositiveButton("YES", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {

                            //YESボタンの処理

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
                                readData();
                            }

                         }});

                        alert.setNegativeButton("NO", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                //Noボタンが押された時の処理
                                adapter.notifyDataSetChanged();
                            }});

                        alert.show().setCanceledOnTouchOutside(false);

                    }

                    @Override
                    public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY,actionState, isCurrentlyActive);

                        View itemView =  viewHolder.itemView;

                        //キャンセル
                        if (dX == 0f && isCurrentlyActive) {
                            clearCanvas(c, itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, false);
                            return;
                        }

                        ColorDrawable background = new ColorDrawable();
                        background.setColor(Color.parseColor("#fffacd"));
                        background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                        background.draw(c);

                        int deleteIconTop = itemView.getTop() + (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                        int deleteIconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                        int deleteIconLeft = itemView.getRight() - deleteIconMargin - deleteIcon.getIntrinsicWidth();
                        int deleteIconRight = itemView.getRight() - deleteIconMargin;
                        int deleteIconBottom = deleteIconTop +  deleteIcon.getIntrinsicHeight();

                        deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
                        deleteIcon.draw(c);
                    }


                };

        new ItemTouchHelper(mIth).attachToRecyclerView(recyclerView);

    }




    //給油データ抽出
    public void selectFuelData() {

        removeItems();

        //テーブル内のデータを配列に渡す
        String[] columns = {"_id", "date", "category", "price", "notes"};

        Cursor cursor;


        try {
            MaintenanceDataHelper helper = new MaintenanceDataHelper(MaintenanceDataShow.this);
            db = helper.getReadableDatabase();

            String order_by = "date ASC";

            cursor = db.query("maintenanceDB", columns, "category = ?", new String[]{"給油"}, null, null, order_by, null);

            if(cursor != null && cursor.getCount() > 0) {

                data = new ArrayList<>();

                while (cursor.moveToNext()) {

                    ListItem dataItem = new ListItem();

                    dataItem.setId(cursor.getLong(cursor.getColumnIndex(MaintenanceDataHelper.ROW_ID)));
                    dataItem.setCategory(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_CATEGORY)));
                    dataItem.setDate(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NAME)));
                    dataItem.setPrice(cursor.getInt(cursor.getColumnIndex(MaintenanceDataHelper.ROW_PRICE)));
                    dataItem.setNotes(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NOTES)));
                    data.add(dataItem);
                }
            }

        } finally {
            String order_by = "date ASC";
            cursor = db.query("maintenanceDB", columns, "category = ?", new String[]{"給油"}, null, null, order_by, null);
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

    //洗車データ抽出
    public void selectWashData() {

        removeItems();

        //テーブル内のデータを配列に渡す
        String[] columns = {"_id", "date", "category", "price", "notes"};

        Cursor cursor;


        try {
            MaintenanceDataHelper helper = new MaintenanceDataHelper(MaintenanceDataShow.this);
            db = helper.getReadableDatabase();

            String order_by = "date ASC";

            cursor = db.query("maintenanceDB", columns, "category = ?", new String[]{"洗車"}, null, null, order_by, null);

            if(cursor != null && cursor.getCount() > 0) {

                data = new ArrayList<>();

                while (cursor.moveToNext()) {

                    ListItem dataItem = new ListItem();

                    dataItem.setId(cursor.getLong(cursor.getColumnIndex(MaintenanceDataHelper.ROW_ID)));
                    dataItem.setCategory(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_CATEGORY)));
                    dataItem.setDate(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NAME)));
                    dataItem.setPrice(cursor.getInt(cursor.getColumnIndex(MaintenanceDataHelper.ROW_PRICE)));
                    dataItem.setNotes(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NOTES)));
                    data.add(dataItem);
                }
            }

        } finally {
            String order_by = "date ASC";
            cursor = db.query("maintenanceDB", columns, "category = ?", new String[]{"洗車"}, null, null, order_by, null);
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

    //定期交換データ抽出
    public void selectPartsChangeData() {

        removeItems();

        //テーブル内のデータを配列に渡す
        String[] columns = {"_id", "date", "category", "price", "notes"};

        Cursor cursor;


        try {
            MaintenanceDataHelper helper = new MaintenanceDataHelper(MaintenanceDataShow.this);
            db = helper.getReadableDatabase();

            String order_by = "date ASC";

            cursor = db.query("maintenanceDB", columns, "category = ?", new String[]{"定期交換"}, null, null, order_by, null);

            if(cursor != null && cursor.getCount() > 0) {

                data = new ArrayList<>();

                while (cursor.moveToNext()) {

                    ListItem dataItem = new ListItem();

                    dataItem.setId(cursor.getLong(cursor.getColumnIndex(MaintenanceDataHelper.ROW_ID)));
                    dataItem.setCategory(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_CATEGORY)));
                    dataItem.setDate(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NAME)));
                    dataItem.setPrice(cursor.getInt(cursor.getColumnIndex(MaintenanceDataHelper.ROW_PRICE)));
                    dataItem.setNotes(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NOTES)));
                    data.add(dataItem);
                }
            }

        } finally {
            String order_by = "date ASC";
            cursor = db.query("maintenanceDB", columns, "category = ?", new String[]{"定期交換"}, null, null, order_by, null);
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

    //故障・修理データ抽出
    public void selectRepairData() {

        removeItems();

        //テーブル内のデータを配列に渡す
        String[] columns = {"_id", "date", "category", "price", "notes"};

        Cursor cursor;


        try {
            MaintenanceDataHelper helper = new MaintenanceDataHelper(MaintenanceDataShow.this);
            db = helper.getReadableDatabase();

            String order_by = "date ASC";

            cursor = db.query("maintenanceDB", columns, "category = ?", new String[]{"故障・修理"}, null, null, order_by, null);

            if(cursor != null && cursor.getCount() > 0) {

                data = new ArrayList<>();

                while (cursor.moveToNext()) {

                    ListItem dataItem = new ListItem();

                    dataItem.setId(cursor.getLong(cursor.getColumnIndex(MaintenanceDataHelper.ROW_ID)));
                    dataItem.setCategory(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_CATEGORY)));
                    dataItem.setDate(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NAME)));
                    dataItem.setPrice(cursor.getInt(cursor.getColumnIndex(MaintenanceDataHelper.ROW_PRICE)));
                    dataItem.setNotes(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NOTES)));
                    data.add(dataItem);
                }
            }

        } finally {
            String order_by = "date ASC";
            cursor = db.query("maintenanceDB", columns, "category = ?", new String[]{"故障・修理"}, null, null, order_by, null);
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

    //車検・点検データ抽出
    public void selectCheckData() {

        removeItems();

        //テーブル内のデータを配列に渡す
        String[] columns = {"_id", "date", "category", "price", "notes"};

        Cursor cursor;


        try {
            MaintenanceDataHelper helper = new MaintenanceDataHelper(MaintenanceDataShow.this);
            db = helper.getReadableDatabase();

            String order_by = "date ASC";

            cursor = db.query("maintenanceDB", columns, "category = ?", new String[]{"車検・点検"}, null, null, order_by, null);

            if(cursor != null && cursor.getCount() > 0) {

                data = new ArrayList<>();

                while (cursor.moveToNext()) {

                    ListItem dataItem = new ListItem();

                    dataItem.setId(cursor.getLong(cursor.getColumnIndex(MaintenanceDataHelper.ROW_ID)));
                    dataItem.setCategory(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_CATEGORY)));
                    dataItem.setDate(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NAME)));
                    dataItem.setPrice(cursor.getInt(cursor.getColumnIndex(MaintenanceDataHelper.ROW_PRICE)));
                    dataItem.setNotes(cursor.getString(cursor.getColumnIndex(MaintenanceDataHelper.ROW_NOTES)));
                    data.add(dataItem);
                }
            }

        } finally {
            String order_by = "date ASC";
            cursor = db.query("maintenanceDB", columns, "category = ?", new String[]{"車検・点検"}, null, null, order_by, null);
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


    //給油金額の合計金額
    public void totalFuelPrice() {

        MaintenanceDataHelper helper = new MaintenanceDataHelper(MaintenanceDataShow.this);
        db = helper.getReadableDatabase();

        String sql = "SELECT TOTAL(price) FROM maintenanceDB WHERE category LIKE '給油%';";

        Cursor cursor;

        cursor = db.rawQuery(sql,null);

        int total;

        if(cursor.moveToFirst()) {
            
            total = cursor.getInt(0);

            totalPrice.setText(NumberFormat.getCurrencyInstance().format((total)));
        }
        cursor.close();
        db.close();
    }

    //洗車金額の合計金額
    public void totalWashPrice() {

        MaintenanceDataHelper helper = new MaintenanceDataHelper(MaintenanceDataShow.this);
        db = helper.getReadableDatabase();

        String sql = "SELECT TOTAL(price) FROM maintenanceDB WHERE category LIKE '洗車%';";

        Cursor cursor;

        cursor = db.rawQuery(sql,null);

        int total;

        if(cursor.moveToFirst()) {

            total = cursor.getInt(0);

            totalPrice.setText(NumberFormat.getCurrencyInstance().format((total)));
        }
        cursor.close();
        db.close();
    }

    //定期交換の合計金額
    public void totalPartsChangePrice() {

        MaintenanceDataHelper helper = new MaintenanceDataHelper(MaintenanceDataShow.this);
        db = helper.getReadableDatabase();

        String sql = "SELECT TOTAL(price) FROM maintenanceDB WHERE category LIKE '定期交換%';";

        Cursor cursor;

        cursor = db.rawQuery(sql,null);

        int total;

        if(cursor.moveToFirst()) {

            total = cursor.getInt(0);

            totalPrice.setText(NumberFormat.getCurrencyInstance().format((total)));
        }
        cursor.close();
        db.close();
    }

    //修理の合計金額
    public void totalRepairPrice() {

        MaintenanceDataHelper helper = new MaintenanceDataHelper(MaintenanceDataShow.this);
        db = helper.getReadableDatabase();

        String sql = "SELECT TOTAL(price) FROM maintenanceDB WHERE category LIKE '故障・修理%';";

        Cursor cursor;

        cursor = db.rawQuery(sql,null);

        int total;

        if(cursor.moveToFirst()) {

            total = cursor.getInt(0);

            totalPrice.setText(NumberFormat.getCurrencyInstance().format((total)));
        }
        cursor.close();
        db.close();
    }

    //車検・点検の合計金額
    public void totalCheckPrice() {

        MaintenanceDataHelper helper = new MaintenanceDataHelper(MaintenanceDataShow.this);
        db = helper.getReadableDatabase();

        String sql = "SELECT TOTAL(price) FROM maintenanceDB WHERE category LIKE '車検・点検%';";

        Cursor cursor;

        cursor = db.rawQuery(sql,null);

        int total;

        if(cursor.moveToFirst()) {

            total = cursor.getInt(0);

            totalPrice.setText(NumberFormat.getCurrencyInstance().format((total)));
        }
        cursor.close();
        db.close();
    }

    //車検・点検の合計金額
    public void totalAllPrice() {

        MaintenanceDataHelper helper = new MaintenanceDataHelper(MaintenanceDataShow.this);
        db = helper.getReadableDatabase();

        String sql = "SELECT TOTAL(price) FROM maintenanceDB";

        Cursor cursor;

        cursor = db.rawQuery(sql,null);

        int total;

        if(cursor.moveToFirst()) {

            total = cursor.getInt(0);

            totalPrice.setText(NumberFormat.getCurrencyInstance().format((total)));
        }
        cursor.close();
        db.close();
    }


    public void removeItems() {
        data.clear();
        recyclerView.getAdapter().notifyDataSetChanged();
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
                selectFuelData();
                totalFuelPrice();
                break;
            case R.id.menu_Item7:
                selectWashData();
                totalWashPrice();
                break;
            case R.id.menu_Item8:
                selectPartsChangeData();
                totalPartsChangePrice();
                break;
            case R.id.menu_Item9:
                selectRepairData();
                totalRepairPrice();
                break;
            case R.id.menu_Item10:
                selectCheckData();
                totalCheckPrice();
                break;
            case R.id.menu_Item11:
                readData();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    //Canvas
    private void clearCanvas(Canvas c, int left, int top, int right, int bottom) {
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        c.drawRect(left, top, right, bottom, paint);
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
