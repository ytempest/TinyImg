package com.ytempest.tinyimg;

import com.sun.istack.internal.NotNull;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @author heqidu
 * @since 2020/1/12
 */
public class FileUtils {

    public static File getCurrentDir() {
        String curDirPath = new File(".").getAbsolutePath();
        return new File(curDirPath.substring(0, curDirPath.length() - 2));
    }

    @NotNull
    public static List<File> listImageFile(File file) {
        return listImageFile(file, false);
    }

    @NotNull
    public static List<File> listImageFile(File file, boolean recursive) {
        List<File> list = new LinkedList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0, len = Utils.getSize(files); i < len; i++) {
                File unknown = files[i];
                if (recursive && unknown.isDirectory()) {
                    list.addAll(listImageFile(unknown, recursive));

                } else if (isImageFile(unknown)) {
                    list.add(unknown);
                }
            }
        }
        return list;
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
