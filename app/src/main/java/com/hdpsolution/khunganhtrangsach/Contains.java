package com.hdpsolution.khunganhtrangsach;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Contains {
    public static final Integer REQUEST_CODE_PICK_GALERY = 100;
    public static final Integer REQUEST_CODE_CAPTURE_CAMERA = 180;
    public static final String KEY_PUT_BITMAP = "bitmap";
    public static final String KEY_RESULT_PHOTO_FRAME = "data";
    public static final Integer REQUEST_CODE_RESULT_PHOTO_FRAME = 55;
    public static final String DATABASE ="listanh.db";
    public static final String TABLE_DATABASE ="list_anh";
    public static final String F_DATABASE ="urimain";
    public static final String F_DATABASE2 ="uri";

    public static Uri addImageToGallery(ContentResolver cr, String imgType, File filepath) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Khunganhsach");
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "khunganhsach");
        values.put(MediaStore.Images.Media.DESCRIPTION, "");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/" + imgType);
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATA, filepath.toString());
        return cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
    public static String getFilePath(String name) {
        String picturePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
        File pictureFolder = new File(picturePath);
        if (!pictureFolder.exists()) {
            boolean mkdir = pictureFolder.mkdir();
            if (!mkdir) return null;
        }
        File file = new File(picturePath + "/ShareKhungAnhSach");
        if (!file.exists()) {
            boolean mkdir = file.mkdir();
            if (!mkdir) return null;
        }
        return file.getPath() + "/" + name;
    }

    public static void shareImage(Bitmap bitmap, Context context) {
        Intent sharedIntent = new Intent();
        sharedIntent.setAction(Intent.ACTION_SEND);
        sharedIntent.setType("image/*");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        String tenAnh = System.currentTimeMillis() + ".jpg";
        File file = new File(getFilePath(tenAnh));

        try {
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(byteArrayOutputStream.toByteArray());
            outputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        sharedIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

        context.startActivity(Intent.createChooser(sharedIntent, context.getString(R.string.sharevs)));
    }

    public static Uri[] CodeLuuAnh(Bitmap bm, String fileName,String folder,Context context) {
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + folder;
        File dir = new File(dirPath);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            Uri uri = Contains.addImageToGallery(context.getContentResolver(),"imgtype",file);
            final AlertDialog.Builder dialogsave = new AlertDialog.Builder(context);
            dialogsave.setMessage(context.getString(R.string.saveto)+" "+folder);
            dialogsave.setPositiveButton("OK",null);
            dialogsave.show();
            return new Uri[]{Uri.fromFile(file), uri};
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void FullSreen(Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    public static SQLiteDatabase Docfiledb(String name,Context context) {
        String fileName = name;
        File file = context.getDatabasePath(fileName );
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }

            InputStream inputStream = null;
            try {
                inputStream = context.getAssets().open(name);
            } catch (IOException e) {
                e.printStackTrace();
            }
            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byte[] buffer = new byte[1024 * 8];
            int numOfBytesToRead;
            try {
                while((numOfBytesToRead = inputStream.read(buffer)) > 0)
                    outputStream.write(buffer, 0, numOfBytesToRead);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file, null);
        return db;

    }

    public static Uri GetUriBitmap(Bitmap bitmap, Context context) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        String tenAnh = System.currentTimeMillis() + ".jpg";
        File file = new File(getFilePath(tenAnh));

        try {
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(byteArrayOutputStream.toByteArray());
            outputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Uri.fromFile(file);

    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static File createImageFile(Context context) throws IOException {
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                "khunghinhchup",  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        String mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
