package com.yy.camerademo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import java.io.File;

public class MainActivity extends Activity {

    private static final int RC_TAKE_PHOTO = 0X001;
    private static final int RC_CHOOSE_PHOTO = 0X002;
    private static final int RC_CROP_PHOTO = 0X003;

    private Uri imageUri;
    private TextView textView;
    File photoFile;
    Uri desUri;
    File finalFile;
    File originalFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_album).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                openAlbum();
            }
        });
        findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                takePic();
            }
        });
        textView = findViewById(R.id.text_view);
    }

    public void takePic() {
        if (requestPermission(RC_TAKE_PHOTO)) {
            //已授权，获取照片
            takePhoto();
        }
    }

    private void takePhoto() {
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = new File(Environment.getExternalStorageDirectory().getPath()+"/photo/test/"
            + Math.random()
            + "photo.jpeg");
        if (photoFile.exists()) {
            photoFile.delete();
        }
        imageUri = FileProvider7.getUriForFile(this, photoFile);
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentToTakePhoto, RC_TAKE_PHOTO);
    }

    public void openAlbum() {
        if (requestPermission(RC_CHOOSE_PHOTO)) {
            //已授权，获取照片
            choosePhoto();
        }
    }

    private void choosePhoto() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);
    }

    private void crop() {
        if (requestPermission(RC_CROP_PHOTO)) {
            desUri = Uri.fromFile(new File(
                Environment.getExternalStorageDirectory().getPath()+"/photo/test/"
                    + "crop_"
                    + Math.random()
                    + ".jpg"));
            //desUri = FileProvider7.getUriForFile(this,
            //    new File(fileDir, "crop_" + Math.random() + ".jpg"));
            Uri originUri = FileProvider7.getUriForFile(this,
                new File(originalFile.getPath()));

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            cropIntent.setDataAndType(originUri, "image/*");
            cropIntent.putExtra("crop", "true");
            //将剪切的图片保存到目标Uri中
            cropIntent.putExtra("return-data", false);
            cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, desUri);
            startActivityForResult(cropIntent, RC_CROP_PHOTO);
        }
    }

    private boolean requestPermission(int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = {
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    ActivityCompat.requestPermissions(this,
                        permissions, requestCode);
                    return false;
                }
            }
        }
        return true;
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        switch (requestCode) {
            case RC_TAKE_PHOTO:   //拍照权限申请返回
                if (grantResults.length == 2
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    takePic();
                }
                break;
            case RC_CHOOSE_PHOTO:   //相册选择照片权限申请返回
                choosePhoto();
                break;
            case RC_CROP_PHOTO:
                crop();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            RequestOptions requestOptions =
                new RequestOptions().skipMemoryCache(true).diskCacheStrategy(
                    DiskCacheStrategy.NONE);
            switch (requestCode) {
                case RC_TAKE_PHOTO:
                    //将图片显示在ivImage上
                    Glide.with(this).load(photoFile).apply(requestOptions).into(
                        (ImageView) findViewById(R.id.img_result));
                    textView.setText(photoFile.getAbsolutePath());
                    Log.e("tag", photoFile.getPath()
                        + "   "
                        + photoFile.getAbsolutePath()
                    );
                    originalFile = photoFile;
                    crop();
                    break;
                case RC_CHOOSE_PHOTO:
                    Uri uri = data.getData();
                    String filePath = FileUtil.getFilePathByUri(this, uri);
                    if (!TextUtils.isEmpty(filePath)) {
                        //将照片显示在 ivImage上
                        Glide.with(this)
                            .load(filePath)
                            .apply(requestOptions)
                            .into((ImageView) findViewById(R.id.img_result));
                        textView.setText(filePath);
                        Log.e("tag", filePath);
                    }
                    originalFile = new File(filePath);
                    crop();
                    break;
                case RC_CROP_PHOTO:
                    //if (data != null) {
                    //    if (data.getData() != null) {
                    //        finalFile = new File(data.getData().getPath());
                    //    } else if (data.getAction() != null) {
                    //        finalFile = new File(data.getAction());
                    //    }
                    //} else {
                    //
                    //}
                    finalFile = new File(desUri.getPath());
                    Glide.with(this)
                        .load(finalFile)
                        .apply(requestOptions)
                        .into((ImageView) findViewById(R.id.img_result));
                    break;
            }
        }
    }
}
