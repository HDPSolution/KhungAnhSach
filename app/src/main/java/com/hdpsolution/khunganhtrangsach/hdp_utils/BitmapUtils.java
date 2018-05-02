package com.hdpsolution.khunganhtrangsach.hdp_utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;

/**
 * Created by HP 6300 Pro on 1/24/2018.
 */

public class BitmapUtils {

    private static final int W = 720;
    private static final int H = 1280;
    private static final String TAG = BitmapUtils.class.getSimpleName();

    public static Bitmap mosaicBitmapAverage(Bitmap bmp, int level) {
        long start = System.currentTimeMillis();
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);

        int pixRowColor = 0, rowR = 0, rowG = 0, rowB = 0;
        int pixCurColor = 0, curR = 0, curG = 0, curB = 0;

        int newR = 0, newG = 0, newB = 0;
        int aveR = 0, aveG = 0, aveB = 0;
        int sumR = 0, sumG = 0, sumB = 0;

        int i = 0, k = 0, m = 0, n = 0;
        int length = height - 1;
        int len = width - 1;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);

        for (i = 0; i < length; i++) {
            for (k = 0; k < len; k++) {
                if (i % level == 0) {
                    if (k % level == 0) {
                        // caluate the avergae color
                        for (m = 0; m < level && (i + m) < length; m++) {
                            for (n = 0; n < level && (k + n) < len; n++) {
                                pixCurColor = pixels[(i + m) * width + (k + n)];
                                curR = Color.red(pixCurColor);
                                curG = Color.green(pixCurColor);
                                curB = Color.blue(pixCurColor);

                                sumR += curR;
                                sumG += curG;
                                sumB += curB;
                            }
                        }

                        // remember average color
                        aveR = sumR / (level * level);
                        aveG = sumG / (level * level);
                        aveB = sumB / (level * level);

                        // reset color
                        sumR = 0;
                        sumG = 0;
                        sumB = 0;
                    }

                    // use average color
                    newR = aveR;
                    newG = aveG;
                    newB = aveB;
                } else {
                    // copy color in the same position of last row
                    pixRowColor = pixels[(i - 1) * width + k];
                    rowR = Color.red(pixRowColor);
                    rowG = Color.green(pixRowColor);
                    rowB = Color.blue(pixRowColor);

                    newR = rowR;
                    newG = rowG;
                    newB = rowB;
                }

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);

                newR = 0;
                newG = 0;
                newB = 0;
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        bmp.recycle();
        Log.e(TAG, "mosaicBitmapAverage00: end: " + (System.currentTimeMillis() - start));
        return bitmap;
    }

//    public static Bitmap mosaicBitmapAverage2(Bitmap bmp, int level) {
//        long start = System.currentTimeMillis();
//        int w111 = bmp.getWidth();
//        int h111 = bmp.getHeight();
//        bmp = getHDBitmap(bmp);
//        int width = bmp.getWidth();
//        int height = bmp.getHeight();
//        Bitmap bitmap = Bitmap.createBitmap(width, height,
//                Bitmap.Config.ARGB_8888);
////        Bitmap bitmap = getHDTranBitmap(bmp);
//        int pixRowColor = 0, rowR = 0, rowG = 0, rowB = 0;
//        int pixCurColor = 0, curR = 0, curG = 0, curB = 0;
//
//        int newR = 0, newG = 0, newB = 0;
//        int aveR = 0, aveG = 0, aveB = 0;
//        int sumR = 0, sumG = 0, sumB = 0;
//
//        int i = 0, k = 0, m = 0, n = 0;
//        int length = height - 1;
//        int len = width - 1;
//        int[] pixels = new int[width * height];
//        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
//
//        for (i = 0; i < length; i++) {
//            for (k = 0; k < len; k++) {
//                if (i % level == 0) {
//                    if (k % level == 0) {
//                        // caluate the avergae color
//                        for (m = 0; m < level && (i + m) < length; m++) {
//                            for (n = 0; n < level && (k + n) < len; n++) {
//                                pixCurColor = pixels[(i + m) * width + (k + n)];
//                                curR = Color.red(pixCurColor);
//                                curG = Color.green(pixCurColor);
//                                curB = Color.blue(pixCurColor);
//
//                                sumR += curR;
//                                sumG += curG;
//                                sumB += curB;
//                            }
//                        }
//
//                        // remember average color
//                        aveR = sumR / (level * level);
//                        aveG = sumG / (level * level);
//                        aveB = sumB / (level * level);
//
//                        // reset color
//                        sumR = 0;
//                        sumG = 0;
//                        sumB = 0;
//                    }
//
//                    // use average color
//                    newR = aveR;
//                    newG = aveG;
//                    newB = aveB;
//                } else {
//                    // copy color in the same position of last row
//                    pixRowColor = pixels[(i - 1) * width + k];
//                    rowR = Color.red(pixRowColor);
//                    rowG = Color.green(pixRowColor);
//                    rowB = Color.blue(pixRowColor);
//
//                    newR = rowR;
//                    newG = rowG;
//                    newB = rowB;
//                }
//
//                newR = Math.min(255, Math.max(0, newR));
//                newG = Math.min(255, Math.max(0, newG));
//                newB = Math.min(255, Math.max(0, newB));
//                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
//
//                newR = 0;
//                newG = 0;
//                newB = 0;
//            }
//        }
//        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//        bmp.recycle();
//
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, w111, h111, true);
//        Log.e(TAG, "mosaicBitmapAverage11: end: " + (System.currentTimeMillis() - start));
//        return scaledBitmap;
//    }

//    private static Bitmap getHDTranBitmap(Bitmap bitmap) {
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//        float r = 1.0f * height / width;
//        float r1 = 1.0f * H / W;
//        if (height > width) {
//            if (r > r1) {
//                height = H;
//                width = (int) (height / r);
//            } else {
//                width = W;
//                height = (int) (width * r);
//            }
//        } else {
//            if (r > 1 / r1) {
//                height = W;
//                width = (int) (height / r);
//            } else {
//                width = H;
//                height = (int) (width * r);
//            }
//        }
//        return Bitmap.createBitmap(width, height,
//                Bitmap.Config.ARGB_8888);
//    }

    public static Bitmap blurBitmapAverage(Bitmap srcBitmap, int radius) {
        int scale = 1;
        int width = Math.round(srcBitmap.getWidth() * scale);
        int height = Math.round(srcBitmap.getHeight() * scale);
        srcBitmap = Bitmap.createScaledBitmap(srcBitmap, width, height, false);

        Bitmap bitmap = srcBitmap.copy(srcBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

//    public static Bitmap blur2(Bitmap bitmap, int radius) {
//        if (radius < 1) {
//            return (null);
//        }
//
//        int w = bitmap.getWidth();
//        int h = bitmap.getHeight();
//
//        int[] pix = new int[w * h];
//        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
//
//        int wm = w - 1;
//        int hm = h - 1;
//        int wh = w * h;
//        int div = radius + radius + 1;
//
//        int r[] = new int[wh];
//        int g[] = new int[wh];
//        int b[] = new int[wh];
//        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
//        int vmin[] = new int[Math.max(w, h)];
//
//        int divsum = (div + 1) >> 1;
//        divsum *= divsum;
//        int dv[] = new int[256 * divsum];
//        for (i = 0; i < 256 * divsum; i++) {
//            dv[i] = (i / divsum);
//        }
//
//        yw = yi = 0;
//
//        int[][] stack = new int[div][3];
//        int stackpointer;
//        int stackstart;
//        int[] sir;
//        int rbs;
//        int r1 = radius + 1;
//        int routsum, goutsum, boutsum;
//        int rinsum, ginsum, binsum;
//
//        for (y = 0; y < h; y++) {
//            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
//            for (i = -radius; i <= radius; i++) {
//                p = pix[yi + Math.min(wm, Math.max(i, 0))];
//                sir = stack[i + radius];
//                sir[0] = (p & 0xff0000) >> 16;
//                sir[1] = (p & 0x00ff00) >> 8;
//                sir[2] = (p & 0x0000ff);
//                rbs = r1 - Math.abs(i);
//                rsum += sir[0] * rbs;
//                gsum += sir[1] * rbs;
//                bsum += sir[2] * rbs;
//                if (i > 0) {
//                    rinsum += sir[0];
//                    ginsum += sir[1];
//                    binsum += sir[2];
//                } else {
//                    routsum += sir[0];
//                    goutsum += sir[1];
//                    boutsum += sir[2];
//                }
//            }
//            stackpointer = radius;
//
//            for (x = 0; x < w; x++) {
//
//                r[yi] = dv[rsum];
//                g[yi] = dv[gsum];
//                b[yi] = dv[bsum];
//
//                rsum -= routsum;
//                gsum -= goutsum;
//                bsum -= boutsum;
//
//                stackstart = stackpointer - radius + div;
//                sir = stack[stackstart % div];
//
//                routsum -= sir[0];
//                goutsum -= sir[1];
//                boutsum -= sir[2];
//
//                if (y == 0) {
//                    vmin[x] = Math.min(x + radius + 1, wm);
//                }
//                p = pix[yw + vmin[x]];
//
//                sir[0] = (p & 0xff0000) >> 16;
//                sir[1] = (p & 0x00ff00) >> 8;
//                sir[2] = (p & 0x0000ff);
//
//                rinsum += sir[0];
//                ginsum += sir[1];
//                binsum += sir[2];
//
//                rsum += rinsum;
//                gsum += ginsum;
//                bsum += binsum;
//
//                stackpointer = (stackpointer + 1) % div;
//                sir = stack[(stackpointer) % div];
//
//                routsum += sir[0];
//                goutsum += sir[1];
//                boutsum += sir[2];
//
//                rinsum -= sir[0];
//                ginsum -= sir[1];
//                binsum -= sir[2];
//
//                yi++;
//            }
//            yw += w;
//        }
//        for (x = 0; x < w; x++) {
//            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
//            yp = -radius * w;
//            for (i = -radius; i <= radius; i++) {
//                yi = Math.max(0, yp) + x;
//
//                sir = stack[i + radius];
//
//                sir[0] = r[yi];
//                sir[1] = g[yi];
//                sir[2] = b[yi];
//
//                rbs = r1 - Math.abs(i);
//
//                rsum += r[yi] * rbs;
//                gsum += g[yi] * rbs;
//                bsum += b[yi] * rbs;
//
//                if (i > 0) {
//                    rinsum += sir[0];
//                    ginsum += sir[1];
//                    binsum += sir[2];
//                } else {
//                    routsum += sir[0];
//                    goutsum += sir[1];
//                    boutsum += sir[2];
//                }
//
//                if (i < hm) {
//                    yp += w;
//                }
//            }
//            yi = x;
//            stackpointer = radius;
//            for (y = 0; y < h; y++) {
//                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
//                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];
//
//                rsum -= routsum;
//                gsum -= goutsum;
//                bsum -= boutsum;
//
//                stackstart = stackpointer - radius + div;
//                sir = stack[stackstart % div];
//
//                routsum -= sir[0];
//                goutsum -= sir[1];
//                boutsum -= sir[2];
//
//                if (x == 0) {
//                    vmin[y] = Math.min(y + r1, hm) * w;
//                }
//                p = x + vmin[y];
//
//                sir[0] = r[p];
//                sir[1] = g[p];
//                sir[2] = b[p];
//
//                rinsum += sir[0];
//                ginsum += sir[1];
//                binsum += sir[2];
//
//                rsum += rinsum;
//                gsum += ginsum;
//                bsum += binsum;
//
//                stackpointer = (stackpointer + 1) % div;
//                sir = stack[stackpointer];
//
//                routsum += sir[0];
//                goutsum += sir[1];
//                boutsum += sir[2];
//
//                rinsum -= sir[0];
//                ginsum -= sir[1];
//                binsum -= sir[2];
//
//                yi += w;
//            }
//        }
//
//        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
//        return bitmap;
//    }

    public static Bitmap getHDBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float r = 1.0f * height / width;
        float r1 = 1.0f * H / W;
        if (height > width) {
            if (r > r1) {
                height = H;
                width = (int) (height / r);
            } else {
                width = W;
                height = (int) (width * r);
            }
        } else {
            if (r > 1 / r1) {
                height = W;
                width = (int) (height / r);
            } else {
                width = H;
                height = (int) (width * r);
            }
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    public static Bitmap getCropBitmap(Bitmap bitmap, int dstW, int dstH) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float r = 1.0f * height / width;
        float R = 1.0f * dstH / dstW;
        if (r > R) {
            height = (int) (width * R);
        } else {
            width = (int) (height / R);
        }
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        return Bitmap.createScaledBitmap(bitmap, dstW, dstH, true);
    }

    public static Bitmap decodeScaleBitmapFromPath(String path, int reqHeight, int reqWidth) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeFile(path, options);
        } catch (OutOfMemoryError e) {
            Log.e(TAG, "decodeScaleBitmapFromPath: " + e.getMessage());
            return null;
        }

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        try {
            return getRotateIfNeedBitmap(BitmapFactory.decodeFile(path, options), path);
        } catch (OutOfMemoryError e) {
            Log.e(TAG, "decodeScaleBitmapFromPath: " + e.getMessage());
            return null;
        }
    }

    private static Bitmap getRotateIfNeedBitmap(Bitmap bitmap, String path) {
        int degree = 0;
        try {
            ExifInterface mExI = new ExifInterface(path);
            int attribute = mExI.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            switch (attribute) {
                case 1:
                    degree = 0;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    break;
            }
            if (degree > 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(degree);
                if (bitmap != null)
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return bitmap;
        }
    }
}
