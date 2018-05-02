package com.hdpsolution.khunganhtrangsach.hdp_utils;

import android.net.Uri;

/**
 * Created by HP 6300 Pro on 2/26/2018.
 */

public interface OnUploadListener {
    void onFailure(String mss);

    void onSuccess(Uri uri, int i);

    void onProgress(float percent);
}
