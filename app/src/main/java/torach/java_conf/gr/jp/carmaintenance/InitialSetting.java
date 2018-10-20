package torach.java_conf.gr.jp.carmaintenance;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Date;

import java.text.SimpleDateFormat;

public class InitialSetting extends AppCompatActivity  {

    //変数宣言
    //ボタン変数
    Button bt_SetRegist;
    Button bt_TakePic;

    //文字入力変数
    EditText input_MakerName;
    EditText input_CarName;
    String input_MakerNameStr;
    String input_CarNameStr;

    //ストレージ保存画像URI格納
    private Uri _imageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_setting);

        //ボタンオブジェクト取得
        bt_SetRegist = (Button) findViewById(R.id.bt_SetRegist);
        bt_TakePic = (Button) findViewById(R.id.bt_TakePic);

        //リスナークラスのインスタンス作成
        SettingListener listener = new SettingListener();

        //ボタンにリスナーを設定
        bt_SetRegist.setOnClickListener(listener);
        bt_TakePic.setOnClickListener(listener);


    }


    private class SettingListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            //入力欄のEditTextオブジェクトを取得
            input_MakerName = findViewById(R.id.edit_MakerName);
            input_CarName = findViewById(R.id.edit_CarName);

            //画面部品id取得
            int id = view.getId();

            switch(id) {
                //カメラ作動
                case R.id.bt_TakePic:
                    TakePictures();
                    break;

                //登録ボタン
                case R.id.bt_SetRegist:
                //入力された文字列を取得
                    input_MakerNameStr = input_MakerName.getText().toString();
                    input_CarNameStr = input_CarName.getText().toString();
                    break;
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //カメラアプリとの連携からの戻りでかつ撮影成功の場合
        if(requestCode == 200 && resultCode == RESULT_OK) {
            //画像を表示するImageView取得
            ImageView ivCamera = findViewById(R.id.ivCamera);
            //フイールドの画像URIをImageViewに設定
            ivCamera.setImageURI(_imageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if(requestCode == 2000 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            //再度カメラ起動
            TakePictures();
        }
    }


    public void TakePictures() {
        //Write_EXTERNAL_STORAGE許可あり
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            //write_external_permission許可ダイアログ表示 リスエストコードは2000
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissions, 2000);
        }

        //日時データ
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        //現在日時取得
        Date now = new Date(System.currentTimeMillis());

        //取得データをyyyyMMddHHmmssに整形した文字列を生成
        String nowStr = dateFormat.format(now);

        //ストレージに格納する画像のファイル名を生成
        String fileName = "PhotoData_" + nowStr + ".jpg";

        //contentValuesオブジェクト
        ContentValues values = new ContentValues();

        //画像ファイル名設定
        values.put(MediaStore.Images.Media.TITLE, fileName);

        //画像フィル種類設定
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        //ContentResolverオブジェクト生成
        ContentResolver resolver = getContentResolver();

        //ContentResolverを使ってURLオブジェクトを生成
        _imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        //intentオブジェクト生成
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Extra情報として_imageUriを設定
        intent.putExtra(MediaStore.EXTRA_OUTPUT, _imageUri);

        //アクティビティ起動
        startActivityForResult(intent, 200);
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
