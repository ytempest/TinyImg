package com.ytempest.tinyimg;

import com.tinify.AccountException;
import com.tinify.ClientException;
import com.tinify.ConnectionException;
import com.tinify.ServerException;
import com.tinify.Source;
import com.tinify.Tinify;

/**
 * @author heqidu
 * @since 2020/1/13
 */
public class TinyHelper {

    static {
        // TODO : set your Tinify API
        Tinify.setKey("============your-api============");
        Tinify.client();
    }

    /**
     * Use Tinify compress image. There will may be occur some exception.
     * To ensure the success rate of compress, please check the following points.
     * <p>1. Tinify API</p>
     * <p>2. Network</p>
     * <p>
     * About Tinify : <a href>https://tinypng.com/</a>
     *
     * @param srcFilePath path of image need compress
     * @param tarFilePath output path of image compress success
     */
    public static void compress(String srcFilePath, String tarFilePath) {
        try {
            LogUtils.d("upload and compress : " + srcFilePath);
            Source source = Tinify.fromFile(srcFilePath);

            LogUtils.d("download to : " + srcFilePath);
            source.toFile(tarFilePath);

            LogUtils.d("finish compress : " + srcFilePath);
        } catch (Exception e) {
            LogUtils.e("Fail to compress : " + srcFilePath);
            LogUtils.e("The error message is: " + e.getMessage());

            if (e instanceof AccountException) {
                LogUtils.e("Verify your API key and account limit, please");

            } else if (e instanceof ClientException) {
                LogUtils.e("Check your source image and request options, please");

            } else if (e instanceof ServerException) {
                LogUtils.e("Temporary issue with the Tinify API");

            } else if (e instanceof ConnectionException) {
                LogUtils.e("A network connection error occurred, please try again");

            } else {
                LogUtils.e("Something else went wrong, unrelated to the Tinify API");
            }
        }
    }
}
