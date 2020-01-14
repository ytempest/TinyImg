package com.ytempest.tinyimg;

import com.sun.istack.internal.Nullable;

/**
 * @author heqidu
 * @since 2020/1/12
 */
public class Utils {

    public static int getSize(Object[] objects) {
        return objects != null ? objects.length : 0;
    }

    @Nullable
    public static String get(String[] strings, int index) {
        if (strings != null && strings.length > index) {
            return strings[index];
        }
        return null;
    }
}
