package com.ytempest.tinyimg;

import com.tinify.AccountException;
import com.tinify.ClientException;
import com.tinify.ConnectionException;
import com.tinify.Result;
import com.tinify.ServerException;
import com.tinify.Source;
import com.tinify.Tinify;

import java.io.File;

/**
 * @author heqidu
 * @since 2020/1/13
 */
public class TinyHelper {

    static {
        setKey("FC8dkDxbNVlJHL9JpmfCT0YzzRgVXZXT");
    }

    public static void setKey(String key) {
        Tinify.setKey(key);
    }

    public static int getUsedCount() {
        return Tinify.compressionCount();
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
        String relativePah = getRelativePah(srcFilePath);
        try {
            LogUtils.d("compress : " + relativePah);
            long beforeSize = new File(srcFilePath).length();
            Source source = Tinify.fromFile(srcFilePath);
            Result result = source.result();

            LogUtils.d("download : " + relativePah);
            long afterSize = result.size();
            result.toFile(tarFilePath);

            LogUtils.d(String.format("finish compress : %s, size: %skb -> %skb", relativePah, beforeSize, afterSize));
        } catch (Exception e) {
            LogUtils.e("Fail to compress : " + relativePah);
            LogUtils.e("The error message is: " + e.getMessage());

            if (e instanceof AccountException) {
                LogUtils.e("Please verify your API key and account limit");

            } else if (e instanceof ClientException) {
                LogUtils.e("Please check your source image and request options");

            } else if (e instanceof ServerException) {
                LogUtils.e("Temporary issue with the Tinify API");

            } else if (e instanceof ConnectionException) {
                LogUtils.e("A network connection error occurred, please try again");

            } else {
                LogUtils.e("Something else went wrong, unrelated to the Tinify API");
            }
        }
    }

    private static String getRelativePah(String path) {
        String curPath = FileUtils.getCurrentDir().getPath();
        return path.replace(curPath + File.separator, "");
    }
}
