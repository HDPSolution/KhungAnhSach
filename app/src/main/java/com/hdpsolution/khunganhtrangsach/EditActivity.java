package com.hdpsolution.khunganhtrangsach;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.clans.fab.FloatingActionButton;
import com.hdpsolution.khunganhtrangsach.TouchHelper.MultiTouchListener;
import com.hdpsolution.khunganhtrangsach.ads.MyAdmobController;
import com.hdpsolution.khunganhtrangsach.ads.MyBaseActivityWithAds;
import com.hdpsolution.khunganhtrangsach.hdp_utils.AppUtils;
import com.hdpsolution.khunganhtrangsach.hdp_utils.FirebaseStorageUtils;
import com.hdpsolution.khunganhtrangsach.hdp_utils.Idelegate;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import static com.hdpsolution.khunganhtrangsach.Contains.FullSreen;


public class EditActivity extends MyBaseActivityWithAds implements View.OnClickListener {

    private ImageView mAnh, mKhung, mThuVien, mSave, mCamera;
    private FloatingActionButton mChonKhung;
    private FrameLayout mLayout;
    private View mView;
    private SQLiteDatabase db;
    private FrameLayout anhchup;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mAnh = findViewById(R.id.anh);
        mKhung = findViewById(R.id.khung);
        mChonKhung = findViewById(R.id.menu_load);
        mThuVien = findViewById(R.id.menu_thuvien);
        mSave = findViewById(R.id.menu_save);
        mCamera = findViewById(R.id.menu_camera);
        mLayout = findViewById(R.id.framelayout);
        mView = findViewById(R.id.idview);
        anhchup = findViewById(R.id.anhchup);
        progressBar = findViewById(R.id.progressbarr);
        mView.setAlpha(0.8f);
        mView.setVisibility(View.GONE);
        db = Contains.Docfiledb(Contains.DATABASE, this);

        FullSreen(getWindow());
        LayDuLieu();
        SetTouch();
        mChonKhung.setOnClickListener(this);
        mThuVien.setOnClickListener(this);
        mSave.setOnClickListener(this);
        mCamera.setOnClickListener(this);


    }

    private void LayDuLieu() {
        Intent intent = getIntent();
        Uri uri = Uri.parse(intent.getStringExtra(Contains.KEY_PUT_BITMAP));
        Glide.with(this)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .dontAnimate()
                .into(mAnh);

    }


    private void SetTouch() {
        mAnh.setOnTouchListener(new MultiTouchListener());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_load:
                MyAdmobController.showAdsFullBeforeDoAction(new Idelegate() {
                    @Override
                    public void callBack(Object value, int where) {
                        Intent intent = new Intent(getApplicationContext(), ListImageActivity.class);
                        startActivityForResult(intent, Contains.REQUEST_CODE_RESULT_PHOTO_FRAME);
                    }
                });

                break;
            case R.id.menu_thuvien:
                ChonHinh();
                break;
            case R.id.menu_save:
                DialogSave();
                break;
            case R.id.menu_camera:
                ChupHinh();
                break;

        }

    }

    private void ChonHinh() {

        Intent ChonHinh = new Intent();
        ChonHinh.setType("image/*");
        ChonHinh.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(ChonHinh, getString(R.string.pickimage)), Contains.REQUEST_CODE_PICK_GALERY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Contains.REQUEST_CODE_PICK_GALERY == requestCode) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Glide.with(this).load(data.getData())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .dontAnimate()
                        .into(mAnh);
            }
        }
        if (Contains.REQUEST_CODE_RESULT_PHOTO_FRAME == requestCode) {
            if (resultCode == Activity.RESULT_OK) {

                String khung = data.getStringExtra(Contains.KEY_RESULT_PHOTO_FRAME);
//                Glide.with(this).load(khung).into(mKhung);
                progressBar.setVisibility(View.VISIBLE);
                FirebaseStorageUtils.displayImageFromFirebaseStorageasProcessbar(this, progressBar, mKhung, khung, R.drawable.ic_load);


            }
        }
        if (requestCode == Contains.REQUEST_CODE_CAPTURE_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {

                Glide.with(this).load(outPutfileUri)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .dontAnimate()
                        .into(mAnh);

            }
        }

    }

    Bitmap bitmap;

    private void DialogSave() {
        mView.setVisibility(View.VISIBLE);
        anhchup.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(anhchup.getDrawingCache());
        anhchup.destroyDrawingCache();
        final Calendar calendar = Calendar.getInstance();

        final Dialog dialog = new Dialog(this, R.style.NewDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_save);
//        dialog.setCancelable(false);
        final ImageView anhLuu = dialog.findViewById(R.id.anhLuu);
        ImageButton share = dialog.findViewById(R.id.btnChiase);
        ImageButton luu = dialog.findViewById(R.id.btnLuu);
        ImageButton rate = dialog.findViewById(R.id.btnRate);


        anhLuu.setImageBitmap(bitmap);

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.goToStore(EditActivity.this, getPackageName());
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contains.shareImage(bitmap, EditActivity.this);
                dialog.dismiss();
            }
        });

        luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri[] uri = Contains.CodeLuuAnh(bitmap, "KhungAnhSach" + calendar.getTimeInMillis()
                        + ".jpeg", "/KhungAnhSach", EditActivity.this);
                ContentValues values = new ContentValues();
                values.put(Contains.F_DATABASE, uri[0].toString());
                values.put(Contains.F_DATABASE2, uri[1].toString());
                db.insert(Contains.TABLE_DATABASE, null, values);
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mView.setVisibility(View.GONE);
                anhLuu.setImageResource(0);
                bitmap = null;
            }
        });


        dialog.show();
    }

    Uri outPutfileUri;

    private void ChupHinh() {

        if (Build.VERSION.SDK_INT < 24) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            String pictureImagePath = storageDir.getAbsolutePath() + "/" + "khunghinhchup.jpg";
            File file = new File(pictureImagePath);
            outPutfileUri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
            startActivityForResult(intent, Contains.REQUEST_CODE_CAPTURE_CAMERA);

        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = Contains.createImageFile(this);
                } catch (IOException ex) {
                }
                if (photoFile != null) {
                    outPutfileUri = FileProvider.getUriForFile(this,
                            getPackageName() + ".fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
                    startActivityForResult(takePictureIntent, Contains.REQUEST_CODE_CAPTURE_CAMERA);
                }
            }

        }

    }
}
