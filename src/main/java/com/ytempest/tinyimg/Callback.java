package com.ytempest.tinyimg;

import java.io.File;

/**
 * @author heqidu
 * @since 2020/1/13
 */
public interface Callback {
    File getOutFile(File inFile);
}
