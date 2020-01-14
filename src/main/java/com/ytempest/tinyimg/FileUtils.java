package com.ytempest.tinyimg;

import com.sun.istack.internal.Nullable;

import java.io.File;
import java.io.FileFilter;

/**
 * @author heqidu
 * @since 2020/1/12
 */
public class FileUtils {

    public static File getCurrentDir() {
        String curDirPath = new File(".").getAbsolutePath();
        return new File(curDirPath.substring(0, curDirPath.length() - 2));
    }

    @Nullable
    public static File[] listImageFile(File file) {
        if (file.isDirectory()) {
            return file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return isImageFile(file);
                }
            });
        }

        if (isImageFile(file)) {
            return new File[]{file};
        }
        return null;
    }

    public static boolean isImageFile(File file) {
        if (!file.isFile()) {
            return false;
        }
        String name = file.getName();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".jpe") || name.endsWith(".jfif")
                || name.endsWith(".png");
    }
}
