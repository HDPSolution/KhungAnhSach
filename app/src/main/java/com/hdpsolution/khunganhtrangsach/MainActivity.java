package com.hdpsolution.khunganhtrangsach;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hdpsolution.khunganhtrangsach.ads.MyAdmobController;
import com.hdpsolution.khunganhtrangsach.ads.MyBaseMainActivity;
import com.hdpsolution.khunganhtrangsach.hdp_utils.AppUtils;
import com.hdpsolution.khunganhtrangsach.hdp_utils.Idelegate;
import com.kobakei.ratethisapp.RateThisApp;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import static com.hdpsolution.khunganhtrangsach.Contains.FullSreen;

public class MainActivity extends MyBaseMainActivity implements View.OnClickListener {

    private ImageButton mCamera, mGalery, mToGalery;
    private Button mRate, mShare;
    private Intent intent;
    private Integer b = 0;
    private ImageView imgnen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FullSreen(getWindow());
        XinQuyenLayAnh();
        GetLocal();
        Contains.Docfiledb(Contains.DATABASE, MainActivity.this);

        mCamera = findViewById(R.id.camera);
        mGalery = findViewById(R.id.galery);
        mShare = findViewById(R.id.shareapp);
        mRate = findViewById(R.id.rateapp);
        mToGalery = findViewById(R.id.togalery);
        imgnen = findViewById(R.id.imgNen);
        Glide.with(this).load(R.drawable.nenluu)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imgnen);
        mGalery.setOnClickListener(this);
        mCamera.setOnClickListener(this);
        mToGalery.setOnClickListener(this);
        mRate.setOnClickListener(this);
        mShare.setOnClickListener(this);

        intent = new Intent(MainActivity.this, EditActivity.class);

        //DuyLH - code Firebase Analytics

        FirebaseAnalytics anl = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("action", "vao_app");

        anl.logEvent("vao_app", params);
        //end

        RateThisApp.Config config = new RateThisApp.Config(1, 5);
        config.setTitle(R.string.my_own_title);
        config.setMessage(R.string.my_own_message);
        config.setYesButtonText(R.string.my_own_rate);
        config.setNoButtonText(R.string.my_own_thanks);
        config.setCancelButtonText(R.string.my_own_cancel);

        String urlRate = "https://play.google.com/store/apps/details?id=" + getPackageName();
        config.setUrl(urlRate);
        RateThisApp.init(config);
        RateThisApp.onCreate(this);

        try {
            RateThisApp.showRateDialogIfNeeded(this);
        } catch (Exception e) {

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.galery:
                ChonHinh();
                break;
            case R.id.camera:
                ChupHinh();
                break;
            case R.id.togalery:
                MyAdmobController.showAdsFullBeforeDoAction(new Idelegate() {
                    @Override
                    public void callBack(Object value, int where) {
                        Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                        startActivity(intent);
                    }
                });

                break;
            case R.id.rateapp:
                AppUtils.goToStore(this, getPackageName());
                break;
            case R.id.shareapp:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share) + getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
        }
    }

    private void ChonHinh() {

        Intent ChonHinh = new Intent();
        ChonHinh.setType("image/*");
        ChonHinh.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(ChonHinh, getString(R.string.pickimage)), Contains.REQUEST_CODE_PICK_GALERY);

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
                            getPackageName()+".fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
                    startActivityForResult(takePictureIntent, Contains.REQUEST_CODE_CAPTURE_CAMERA);
                }
            }

        }
    }


    public void XinQuyenLayAnh() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA))) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        2);
                ;
            }
        } else {
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, getString(R.string.notper), Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Contains.REQUEST_CODE_PICK_GALERY == requestCode) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                intent.putExtra(Contains.KEY_PUT_BITMAP, data.getDataString());
                Log.d("Uri", data.getDataString() + "data \n" + data.getData());

                startActivity(intent);

            }
        }
        if (Contains.REQUEST_CODE_CAPTURE_CAMERA == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                String uri = outPutfileUri.toString();
                intent.putExtra(Contains.KEY_PUT_BITMAP, uri);

                startActivity(intent);

            }
        }
    }


    private void GetLocal() {
        ImageView img = findViewById(R.id.img);
        String deviceLocale = Locale.getDefault().getLanguage();
        if (deviceLocale.equals("vi")) {
            img.setImageResource(R.drawable.tiengviet);
        } else {
            img.setImageResource(R.drawable.tienganh);
        }

    }

}
