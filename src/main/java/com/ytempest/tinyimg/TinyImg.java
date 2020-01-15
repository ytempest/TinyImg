package com.ytempest.tinyimg;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author heqidu
 * @since 2020/1/11
 */
public class TinyImg {

    private static boolean printHelp(String[] args) {
        if (Utils.getSize(args) == 0) {
            return false;
        }

        if (!"-h".equals(args[0]) && !"--help".equals(args[0])) {
            return false;
        }

        LogUtils.d("Compress image by TinyPng, which only support JPEG and PNG.");
        LogUtils.d("Before use this script, you need to apply an API_KEY from TinyPNG");
        LogUtils.d("Link : https://tinypng.com/");
        LogUtils.d("Usage:");
        LogUtils.d("==============================================================");
        LogUtils.d("script [INPUT] [OUTPUT]");
        LogUtils.d("    [INPUT]    Image file or directory of image file which need compress. Default use current directory if not set");
        LogUtils.d("    [OUTPUT]   The result of compress you want to save.");
        LogUtils.d("               1. If input is file, which request output to file. Default override image file if not set");
        LogUtils.d("               2. If input is directory, which request output directory. Default override image of directory if not set");
        return true;
    }

    public static void main(String[] args) {
        if (printHelp(args)) {
            return;
        }

        // 检查输入文件或目录是否存在
        String inputPath = Utils.get(args, 0);
        if (inputPath != null && !new File(inputPath).exists()) {
            LogUtils.d("error: " + inputPath + " not exist!!!");
            return;
        }

        if (Utils.getSize(args) >= 1) {
            String srcFilePath = Utils.get(args, 0);
            String tarFilePath = Utils.get(args, 1);
            if (new File(srcFilePath).isFile()) {
                if (tarFilePath == null) {
                    tarFilePath = srcFilePath;
                }
                LogUtils.d("Input File : " + srcFilePath);
                LogUtils.d("Output File : " + tarFilePath);
                compressFile(srcFilePath, tarFilePath);
                System.exit(1);
            }
        }

        File inputDir;
        File outputDir;
        if (Utils.getSize(args) == 0) {
            inputDir = FileUtils.getCurrentDir();
            outputDir = inputDir;

        } else if (Utils.getSize(args) == 1) {
            inputDir = new File(args[0]);
            outputDir = inputDir;

        } else {
            inputDir = new File(args[0]);
            outputDir = new File(args[1]);
            if (!outputDir.exists() && !outputDir.mkdirs()) {
                LogUtils.d("error: Output Directory error!!!");
                return;
            }
        }

        File[] srcFileList = FileUtils.listImageFile(inputDir);
        if (srcFileList == null || srcFileList.length == 0) {
            LogUtils.d("error: " + inputDir.getAbsolutePath() + " didn't have image file");
            return;
        }

        LogUtils.d("Input Directory : " + inputDir.getAbsolutePath());
        LogUtils.d("Output Directory : " + outputDir.getAbsolutePath());
        compressFile(srcFileList, outputDir);
        System.exit(1);
    }

    private static void compressFile(String srcFilePath, final String tarFilePath) {
        File[] files = {new File(srcFilePath)};
        compress(files, new Callback() {
            @Override
            public File getOutFile(File inFile) {
                return new File(tarFilePath);
            }
        });
    }

    private static void compressFile(File[] files, final File outputDir) {
        compress(files, new Callback() {
            @Override
            public File getOutFile(File inFile) {
                return new File(outputDir, inFile.getName());
            }
        });
    }

    private static void compress(File[] inFiles, final Callback callback) {
        LogUtils.d("===============start compress===============");

        final CountDownLatch latch = new CountDownLatch(inFiles.length);
        ExecutorService executor = Executors.newCachedThreadPool();
        for (final File inFile : inFiles) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    File outFile = callback.getOutFile(inFile);
                    TinyHelper.compress(inFile.getAbsolutePath(), outFile.getAbsolutePath());
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();

            LogUtils.d("Already used count : " + TinyHelper.getUsedCount());
            LogUtils.d("===============finish compress===============");
        } catch (Exception e) {
            LogUtils.e("unknown error : " + e.getMessage());
        }
    }
}
