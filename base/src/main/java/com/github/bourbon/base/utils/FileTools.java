package com.github.bourbon.base.utils;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/26 15:46
 */
public interface FileTools {

    static String getFileContent(String path) {
        File tFile = new File(path);
        if (!tFile.isFile()) {
            throw new IllegalArgumentException("不是文件");
        } else {
            try (BufferedReader reader = Files.newBufferedReader(Paths.get(path))) {
                return reader.readLine();
            } catch (Exception e) {
                throw new IllegalStateException("获取文件内容失败!", e);
            }
        }
    }

    static void createPathIfNecessary(String filePath) {
        if (filePath != null) {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }
}