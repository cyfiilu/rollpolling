package com.iilu.lib_rollpolling;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2018/11/14.
 */
class RollPollingUtil {

    /**
     * 从url中获取图片
     * @param urlString 图片url
     * @return 图片的bitmap对象
     */
    static Bitmap getBitmapFromUrl(String urlString) {
        //URLConnection urlConnection;
        HttpURLConnection httpUrlConnection;
        InputStream is = null;
        Bitmap bitmap;
        try {
            httpUrlConnection = (HttpURLConnection) new URL(urlString).openConnection();
            is = new BufferedInputStream(httpUrlConnection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            httpUrlConnection.disconnect();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
