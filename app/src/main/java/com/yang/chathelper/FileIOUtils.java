package com.yang.chathelper;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

/**
 */
public final class FileIOUtils {

    private FileIOUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }
    private static final String LINE_SEP = System.getProperty("line.separator");
    public static String getStringDateTime() {
        String date_format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(date_format);
        return format.format(System.currentTimeMillis());
    }
    /**
     * 获取sd卡绝对路径
     */
    public static String getDownloadPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/";
    }

    /**
     * 将字符串写入文件
     *
     * @param filePath 文件路径
     * @param content  写入内容
     * @param append   是否追加在文件末
     * @return {@code true}: 写入成功<br>{@code false}: 写入失败
     */
    public static boolean writeFileFromString(final String filePath, final String content, final boolean append) {
        return writeFileFromString(getFileByPath(filePath), content, append);
    }


    public static boolean writeDownloadFile(String path, String content){
        return writeFileFromString(FileIOUtils.getDownloadPath()+path,content,false);
    }

    /**
     * 将字符串写入文件
     *
     * @param file    文件
     * @param content 写入内容
     * @param append  是否追加在文件末
     * @return {@code true}: 写入成功<br>{@code false}: 写入失败
     */
    public static boolean writeFileFromString(final File file, final String content, final boolean append) {
        if (file == null || content == null) {
            return false;
        }
        if (!createOrExistsFile(file)) {
            return false;
        }
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file, append));
            bw.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeIO(bw);
        }
    }

    private static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    private static boolean createOrExistsFile(final File file) {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return file.isFile();
        }
        if (!createOrExistsDir(file.getParentFile())) {
            return false;
        }
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }


    private static boolean isSpace(final String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    /**
     * 关闭IO
     *
     * @param closeables closeables
     */
    public static void closeIO(final Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取文件到字符串中
     *
     * @param filePath 文件路径
     * @return 字符串
     */
    public static String readFile2String(final String filePath) {
        return readFile2String(getFileByPath(getDownloadPath()+filePath), null);
    }

    /**
     * 读取文件到字符串中
     *
     * @param filePath    文件路径
     * @param charsetName 编码格式
     * @return 字符串
     */
    public static String readFile2String(final String filePath, final String charsetName) {
        return readFile2String(getFileByPath(getDownloadPath()+filePath), charsetName);
    }

    private static boolean isFileExists(final File file) {
        return file != null && file.exists();
    }

    /**
     * 读取文件到字符串中
     *
     * @param file        文件
     * @param charsetName 编码格式
     * @return 字符串
     */
    public static String readFile2String(final File file, final String charsetName) {
        if (!isFileExists(file)) {

            return null;
        }

        BufferedReader reader = null;
        try {
            StringBuilder sb = new StringBuilder();
            if (isSpace(charsetName)) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            } else {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
            }
            String line;
            if ((line = reader.readLine()) != null) {

                sb.append(line);
                while ((line = reader.readLine()) != null) {
                    sb.append(LINE_SEP).append(line);
                }
            }
            return sb.toString();
        } catch (IOException e) {

            e.printStackTrace();
            return null;
        } finally {
            closeIO(reader);
        }
    }

}
