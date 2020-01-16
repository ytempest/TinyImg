package com.ytempest.tinyimg;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author heqidu
 * @since 2020/1/11
 */
public class TinyImg {

    private static final OptionsHandler handler;

    static {
        HashMap<String, Boolean> support = new HashMap<>();
        support.put("-h", false);
        support.put("-i", true);
        support.put("-o", true);
        support.put("-r", false);
        support.put("-k", true);
        handler = new OptionsHandler(support);
    }

    private static void printHelp() {
        LogUtils.d("Compress image by TinyPng, which only support JPEG and PNG.");
        LogUtils.d("Before use this script, you need to apply an API_KEY from TinyPNG");
        LogUtils.d("Link : https://tinypng.com/developers");
        LogUtils.d("Usage:");
        LogUtils.d("==============================================================");
        LogUtils.d("java -jar tinyimg.jar [OPTIONS] [ARGS]");
        LogUtils.d("    -k      [API_KEY]         The key you apply from TinyPNG, if not set use built-in key that not sure whether effective");
        LogUtils.d("    -i      [INPUT_PATH]      Image file or directory include image file which need compress. Default use current directory if not set");
        LogUtils.d("    -o      [OUTPUT_PATH]     Output of compress, it will be override original file if not set");
        LogUtils.d("    -r                        When input is directory, set this option will be compress all the image under directory and child directory");
    }

    public static void main(String[] args) {
        Map<String, String> options = null;
        try {
            options = handler.handleOptions(args);
        } catch (IllegalArgumentException e) {
            LogUtils.e(e.getMessage());
            System.exit(-1);
        }

        String input = null;
        String output = null;
        boolean isRecursive = false;
        for (Map.Entry<String, String> entry : options.entrySet()) {
            String option = entry.getKey();
            switch (option) {
                case "-h":
                    printHelp();
                    System.exit(0);
                    break;

                case "-i":
                    input = entry.getValue();
                    break;

                case "-o":
                    output = entry.getValue();
                    break;

                case "-r":
                    isRecursive = true;
                    break;

                case "-k":
                    String key = entry.getValue();
                    LogUtils.d("Use key : " + key);
                    TinyHelper.setKey(key);
                    break;

                default:
                    LogUtils.e("unknown error");
                    return;
            }
        }

        File inputFile = input != null ? new File(input) : FileUtils.getCurrentDir();
        File outputFile = output != null ? new File(output) : inputFile;

        // 检查输入文件或目录是否存在
        if (input != null && !new File(input).exists()) {
            LogUtils.e(input + " didn't exist!!!");
            return;
        }

        if (inputFile.isFile()) {
            compressFile(inputFile, outputFile);

        } else if (inputFile.isDirectory()) {
            compressDirectory(inputFile, outputFile, isRecursive);

        } else {
            LogUtils.e("Can't be identify the file : " + inputFile.getAbsolutePath());
        }
        System.exit(1);
    }

    private static void compressFile(File inputFile, File outputFile) {
        List<File> files = new LinkedList<>();
        files.add(inputFile);
        compress(files, new Callback() {
            @Override
            public File getOutFile(File inFile) {
                return new File(outputFile.getAbsolutePath());
            }
        });
    }

    private static void compressDirectory(File inputDir, final File outputDir, boolean isRecursive) {
        if (outputDir.exists() && outputDir.isFile()) {
            LogUtils.e("Output is file, please set the directory");
            return;
        }

        if (!outputDir.exists() && !outputDir.mkdirs()) {
            LogUtils.e("Fail to create output directory");
            return;
        }

        List<File> srcFileList = FileUtils.listImageFile(inputDir, isRecursive);
        if (srcFileList.size() == 0) {
            LogUtils.e(inputDir.getAbsolutePath() + " didn't have image file");
            return;
        }

        LogUtils.d("Input Directory : " + inputDir.getAbsolutePath());
        LogUtils.d("Output Directory : " + outputDir.getAbsolutePath());

        compress(srcFileList, new Callback() {
            @Override
            public File getOutFile(File inFile) {
                // 获取输入文件和输出路径的不同
                String relativePath = inFile.getAbsolutePath().replace(inputDir.getAbsolutePath(), "");
                File relativeFile = new File(relativePath);
                // 获取输入文件相对于输出文件的路径
                File outDir = new File(outputDir.getAbsolutePath() + relativeFile.getParent());
                if (!outDir.exists() && !outDir.mkdirs()) {
                    LogUtils.e("Skip file " + inFile.getAbsolutePath() + " by reason of fail to create directory : " + outDir.getAbsolutePath());
                    return null;
                }
                return new File(outDir, inFile.getName());
            }
        });
    }

    private static void compress(List<File> inFiles, final Callback callback) {
        LogUtils.d("===============start compress===============");

        final CountDownLatch latch = new CountDownLatch(inFiles.size());
        ExecutorService executor = Executors.newCachedThreadPool();
        for (final File inFile : inFiles) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    File outFile = callback.getOutFile(inFile);
                    if (outFile != null) {
                        TinyHelper.compress(inFile.getAbsolutePath(), outFile.getAbsolutePath());
                    }
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
