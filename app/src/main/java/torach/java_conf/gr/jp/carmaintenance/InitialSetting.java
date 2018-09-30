package torach.java_conf.gr.jp.carmaintenance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
                    TakePictues();
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
            //撮影された画像のビットマップデータ取得
            Bitmap bitmap = data.getParcelableExtra("data");
            //画像を表示するImageView
            ImageView ivCamera = findViewById(R.id.ivCamera);
            //撮影された画像をImageViewに設定
            ivCamera.setImageBitmap(bitmap);
        }
    }

    /*
    public void onCameraImageClick(View view) {
        //インテントオブジェクト生成
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //アクティビティ起動
        startActivityForResult(intent, 200);
    }*/

    public void TakePictues() {
        //インテントオブジェクト生成
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //アクティビティ起動
        startActivityForResult(intent, 200);
    }

}
