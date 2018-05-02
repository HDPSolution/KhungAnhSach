package com.hdpsolution.khunganhtrangsach.hdp_utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



/**
 * Created by HP 6300 Pro on 12/29/2017.
 */

public class AppUtils {

    private static final String TAG = AppUtils.class.getSimpleName();

    public static String getConversation(String userId, String u_id) {
        if (userId.compareTo(u_id) < 0) return userId + "_" + u_id;
        else return u_id + "_" + userId;
    }



    public static void saveBool(Context context, String name, boolean val) {
        SharedPreferences preferences = context.getSharedPreferences("bool", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(name, val);
        edit.apply();
    }

    public static boolean getBool(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences("bool", Context.MODE_PRIVATE);
        boolean val = preferences.getBoolean(name, false);
        Log.e(TAG, "getBool: " + val);
        return val;
    }



    public static String getEmoj(int unicode) {
        return new String(Character.toChars(unicode));
    }

    public static void hideKeyboard(Context context) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            inputManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException e) {
            Log.e("error", e.getMessage() + "");
        }
    }

    public static String getTimeInHouse(long l) {
        Date date = new Date(l);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static void input(EditText editText, String text) {
        if (editText == null || TextUtils.isEmpty(text)) {
            return;
        }

        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        if (start < 0) {
            editText.append(text);
        } else {
            editText.getText().replace(Math.min(start, end), Math.max(start, end), text, 0, text.length());
        }
    }

    public static String getConfessionId(String yourId, String time_stamp) {
        return yourId + "_" + time_stamp;
    }

    public static String summaryText(String confesstionText) {

        return " - " + confesstionText.substring(0, 10) + "...";
    }

    public static int getNotifiId(String notifiId) {
        if (!TextUtils.isEmpty(notifiId) && notifiId.length() >= 6) {
            notifiId = notifiId.substring(notifiId.length() - 6);
            return Integer.parseInt(notifiId);
        }
        return 1111;
    }

    public static boolean isServiceRunning(Context context, Class<?> myClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = manager.getRunningServices(Integer.MAX_VALUE);
        if (null != services) {
            for (ActivityManager.RunningServiceInfo sv : services) {
                if (myClass.getSimpleName().equals(sv.service.getClassName()) && sv.pid != 0)
                    return true;
            }
        }
        return false;
    }



    //    share plantext
    public static void shareText(Context ctx, String text, String title) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
        sharingIntent.setType("text/plain");
        ctx.startActivity(Intent.createChooser(sharingIntent, title));
    }



    public static boolean checkNetworkConnect(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null;
    }

    public static boolean checkInternetConnect(Context context) {
        if (!checkNetworkConnect(context)) return false;
//        try {
//            InetAddress inetAddress = InetAddress.getByName("google.com");
//            return !inetAddress.equals("");
//        } catch (UnknownHostException e) {
//            Log.e("checkInternet", "not connect");
//            return false;
//        }
        return true;
    }





    public static void startService(Context mContext, Intent service) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mContext.startForegroundService(service);
        } else {
            mContext.startService(service);
        }
    }

    public static final String MARKET_DETAILS_ID = "market://details?id=";
    public static final String PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=";

    public static void goToStore(Context context, String appId) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_DETAILS_ID + appId)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_LINK + appId)));
        }
    }

    public static int getDp(Context context, int dp) {
        final Resources res = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
        return Math.round(px);
    }

    public static String getRealPath(Context context, String path) {
        Log.e(TAG, "getRealPath before: " + path);
        path = URLDecoder.decode(path);
        if (!path.contains(":")) return path;
        String id = path.split(":")[1];
        String[] column = {MediaStore.Images.Media.DATA};
        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = context.getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);
        String filePath = "";
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        Log.e(TAG, "getRealPath after: " + filePath);
        return filePath;
    }

    public static boolean hasImage(List<String> mImgPaths) {
        if (mImgPaths == null || mImgPaths.size() == 0) return false;
        else {
            for (int i = 0; i < mImgPaths.size(); i++) {
                if (!TextUtils.isEmpty(mImgPaths.get(i))) return true;
            }
        }
        return false;
    }
}
