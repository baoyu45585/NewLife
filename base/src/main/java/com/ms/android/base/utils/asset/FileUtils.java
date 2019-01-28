package com.ms.android.base.utils.asset;
/**
 * Created by del on 17/4/6.
 */

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * created by lbw at 17/4/6
 */
public class FileUtils {
    /**
     * 将文本文件中的内容读入到buffer中
     * @param buffer buffer
     * @param filePath 文件路径
     * @throws IOException 异常
     * @author cn.outofmemory
     * @date 2013-1-7
     */
    public static void readToBuffer(StringBuffer buffer, String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        InputStreamReader streamReader= new InputStreamReader(is);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(streamReader);

        //读取有时候内容不完整
//        line = reader.readLine(); // 读取第一行
//        while (line != null) { // 如果 line 为空说明读完了
//            buffer.append(line); // 将读到的内容添加到 buffer 中
//            buffer.append("\n"); // 添加换行符
//            line = reader.readLine(); // 读取下一行
//        }

        char[] arr = new char[512];
        int read;

        while (true) {

            read = reader.read(arr, 0, arr.length);

            if (read < 0) {
                break;
            }

            buffer.append(new String(arr, 0, read));
        }

        reader.close();
        streamReader.close();
        is.close();


    }

    /**
     * 读取文本文件内容
     * @param filePath 文件所在路径
     * @return 文本内容
     * @throws IOException 异常
     * @author cn.outofmemory
     * @date 2013-1-7
     */
    public static String readFile(String filePath) throws IOException {
        StringBuffer sb = new StringBuffer();
        FileUtils.readToBuffer(sb, filePath);
        return sb.toString();
    }


    public static String  readAssertsFile(Context context,String path)
    {
        try {
            //Return an AssetManager instance for your application's package
            InputStream is = context.getAssets().open(path);
            int size = is.available();

            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // Convert the buffer into a string.
            String text = new String(buffer, "UTF-8");

            return text;
            // Finally stick the string into the text view.
        } catch (IOException e) {
            // Should never happen!
            throw new RuntimeException(e);
        }
    }


    public static void WriteStringToFile(String filePath,String str) {
        try {
            File file = new File(filePath);
            FileOutputStream out= new FileOutputStream(file);
            PrintStream ps = new PrintStream(out);
            ps.println(str);// 往文件里写入字符串
            ps.close();
            out.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 判断文件是否存在
     * @return
     */
    public static boolean isFileExist(String filePath){
        File file = new File(filePath);
        return file.exists();
    }

}
