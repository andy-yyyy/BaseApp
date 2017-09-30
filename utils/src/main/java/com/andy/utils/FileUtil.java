package com.andy.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


/**
 * 文件操作工具类
 */
public class FileUtil {
    private static final String TAG = FileUtil.class.getSimpleName();
    private static final String CACHE_FILE_NAME_PREFIX = "plugins";

    private static final String OPTIMIZED_NAME_PREFIX = "plugsout";

    private static final String DEV_CACHE_FILE_NAME = "testplugin";

    private static final String DEV_CACHE_FILE_PATH = "/facishare/plugins_";
    private static final String DOWNLOAD_PLUGIN_FILE_PATH = "/facishare/plugins_download";

    public static void clearCache(Context context, int currentVersionCode, int lastVersionCode) {
        if (currentVersionCode > lastVersionCode) {
            File cacheDir = context.getDir(CACHE_FILE_NAME_PREFIX, Context.MODE_PRIVATE);

            if (lastVersionCode == 0) {
                File[] files = cacheDir.listFiles();
                int maxVersionCode = 0;
                for (File f : files) {
                    if (f.getName().startsWith(CACHE_FILE_NAME_PREFIX)) {
                        String[] names = f.getName().split("_");
                        if (names.length > 1) {
                            try {
                                int versionCode = Integer.parseInt(names[1]);
                                if (maxVersionCode < versionCode) {
                                    maxVersionCode = versionCode;
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                lastVersionCode = maxVersionCode;
            }

            File lastCacheFile =
                    new File(cacheDir, String.format("%s_%d", CACHE_FILE_NAME_PREFIX, lastVersionCode));
            if (lastCacheFile.exists()) {
                FileUtil.deleteDir(lastCacheFile);
            }

            String innerSDCardPath = FileUtil.getInnerSDCardPath();
            deleteSDCard(lastVersionCode, innerSDCardPath);

            String externalSDCardPath = FileUtil.getExternalSDCardPath();
            deleteSDCard(lastVersionCode, externalSDCardPath);
        }
    }

    private static void deleteSDCard(int lastVersionCode, String sSDCardPath) {
        File lastCacheFile;
        if (!TextUtils.isEmpty(sSDCardPath)) {
            File cacheSDDir = new File(sSDCardPath + File.separator + DEV_CACHE_FILE_NAME);
            lastCacheFile =
              new File(cacheSDDir, String.format("%s_%d", CACHE_FILE_NAME_PREFIX, lastVersionCode));
            if (lastCacheFile.exists()) {
                FileUtil.deleteDir(lastCacheFile);
            }
        }
    }


    public static File getSuggestionCacheFile(Context context) {
        File cacheFile;
        String sdcard = FileUtil.getSDCardPath();
        if (TextUtils.isEmpty(sdcard)) {
            if (android.os.Build.VERSION.SDK_INT <= 10) {
                //throw new PluginInitException("Please insert the sdcard to use again");
                cacheFile = null;
            } else {
                cacheFile = FileUtil.getInnerCacheDir(context);
            }
        } else {
            if (android.os.Build.VERSION.SDK_INT <= 10) {
                cacheFile = FileUtil.getExternalCacheDir(context);
            } else {
                cacheFile = FileUtil.getInnerCacheDir(context);
            }
        }
        return cacheFile;
    }

    public static File getOptimizedDexPathFile(Context context) {
        File optimizedDexPathFile = context.getDir(OPTIMIZED_NAME_PREFIX, Context.MODE_PRIVATE);
        if (!optimizedDexPathFile.exists()) {
            if (!optimizedDexPathFile.mkdirs()) {
                return null;
            } else {
                return optimizedDexPathFile;
            }
        } else {
            return optimizedDexPathFile;
        }
    }

    public static File getExternalCacheDir(Context context) {
        String sdcardPath = FileUtil.getSDCardPath();
        if (!TextUtils.isEmpty(sdcardPath)) {
            File file = new File(sdcardPath + DEV_CACHE_FILE_PATH + getVersionCode(context));
            if (!file.exists()){
                file.mkdirs();
            }
            return file;
        } else {
            return null;
        }
    }

    public static File getInnerCacheDir(Context context) {
        checkInnerFreeSize();
        return getInnerCache(context);
    }

    public static File getDownloadPluginDir() {
        String sdcardPath = FileUtil.getSDCardPath();
        if (!TextUtils.isEmpty(sdcardPath)) {
            File file = new File(sdcardPath + DOWNLOAD_PLUGIN_FILE_PATH);
            if (!file.exists()){
                file.mkdirs();
            }
            return file;
        } else {
            return null;
        }
    }

    File file = new File(Environment.getExternalStorageDirectory(), "/facishare/emoticon");

    public static File getInnerCache(Context context) {
        File temp = context.getDir(CACHE_FILE_NAME_PREFIX, Context.MODE_PRIVATE);
        File ret = new File(temp, CACHE_FILE_NAME_PREFIX + "_" + getVersionCode(context));
        if (!ret.exists()) {
            ret.mkdirs();
        }
        return ret;
    }

    public static boolean checkFreeSize(long freeSize){
        if (freeSize < 50) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkExternalFreeSize(File file) {
        long sdcardFreeSize = FileUtil.getExternalFreeSize(file);
        return checkFreeSize(sdcardFreeSize);
    }

    public static boolean checkInnerFreeSize(){
        long systemFreeSize = FileUtil.getInnerFreeSize();
        return checkFreeSize(systemFreeSize);
    }

    public static long getInnerFreeSize() {
        return getPathFreeSize(Environment.getDataDirectory());
    }

    public static long getExternalFreeSize(File file) {
        return getPathFreeSize(file);
    }

    public static long getPathFreeSize(File file) {
        if (!file.exists()) {
            return 0;
        }

        StatFs sf = new StatFs(file.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        //return freeBlocks * blockSize;  //单位Byte
        //return (freeBlocks * blockSize)/1024;   //单位KB
        long ret=(freeBlocks * blockSize) / 1024 / 1024; //单位MB
        return ret;
    }

    public static String getVersionCodeString(Context ctx) {
        return getVersionCode(ctx) + "";
    }

    public static int getVersionCode(Context ctx) {
        int ret = 0;
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo packageInfo =
                    pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            ret = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static boolean isSDAvailable() {
        if (Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getSDCardPath() {
        String innerSDCard = FileUtil.getInnerSDCardPath();
        if (checkExternalFreeSize(new File(innerSDCard))) {
            return innerSDCard;
        }

        String externalSDCard = FileUtil.getExternalSDCardPath();
        if (checkExternalFreeSize(new File(externalSDCard))) {
            return externalSDCard;
        }
        return "";
    }

    public static String getInnerSDCardPath() {
        String ret = "";
        if (isSDAvailable()) {
            ret = Environment.getExternalStorageDirectory().getPath();
        }
        return ret;
    }

    private static String getExternalSDCardPath() {
        List<String> lResult = new ArrayList<>();
        try {
            Runtime rt = Runtime.getRuntime();
            Process process = rt.exec("mount");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("extSdCard")) {
                    String[] arr = line.split(" ");
                    String path = arr[1];
                    File file = new File(path);
                    if (file.isDirectory()) {
                        lResult.add(path);
                    }
                }
            }
            isr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lResult.size() > 0 ? lResult.get(0) : "";
    }

    public static void writeToFile(InputStream dataIns, File target) throws IOException {
        if (!target.getParentFile().exists()) {
            target.getParentFile().mkdirs();
        }
        final int BUFFER = 2048;
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(target));
        try {
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = dataIns.read(data, 0, BUFFER)) != -1) {
                bos.write(data, 0, count);
            }
        } finally {
            bos.close();
        }
    }

    public static String getFileStr(File file) {
        String error = "";
        FileInputStream inStream=null;
        ByteArrayOutputStream bos=null;

        try

        {

            //FileInputStream 用于读取诸如图像数据之类的原始字节流。要读取字符流，请考虑使用 FileReader。

            inStream=new FileInputStream(file);

            bos = new ByteArrayOutputStream();

            byte[] buffer=new byte[1024];

            int length=-1;

            while( (length = inStream.read(buffer)) != -1)

            {

                bos.write(buffer,0,length);

                // .write方法 SDK 的解释是 Writes count bytes from the byte array buffer starting at offset index to this stream.

                //  当流关闭以后内容依然存在

            }



            error= bos.toString();

            // 为什么不一次性把buffer得大小取出来呢？为什么还要写入到bos中呢？ return new(buffer,"UTF-8") 不更好么?

            // return new String(bos.toByteArray(),"UTF-8");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos!=null){

                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (inStream!=null){

                    inStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return error;
    }

    public static void writeToFile(byte[] data, File target) throws IOException {
        FileOutputStream fo = null;
        try {
            ReadableByteChannel src = Channels.newChannel(new ByteArrayInputStream(data));
            fo = new FileOutputStream(target);
            FileChannel out = fo.getChannel();
            out.transferFrom(src, 0, data.length);
        } finally {
            if (fo != null) {
                fo.close();
            }
        }
    }

    public static void copyFile(File source, File target) throws IOException {
        copyFile(new FileInputStream(source), target);
    }

    public static File copyFile(InputStream ins, File targetFile) throws IOException {
        if (ins.available() == 0) {
            throw new IOException("FileInputStream is not available");
        }

        File parentFile = targetFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdir();
        }

        FileOutputStream fos = new FileOutputStream(targetFile);
        try {
            byte[] buf = new byte[2048];
            int l;
            while ((l = ins.read(buf)) != -1) {
                fos.write(buf, 0, l);
            }
        } finally {
            fos.close();
        }
        return targetFile;
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static boolean checkIsSameFile(String smd5, String tmd5) {
        return TextUtils.equals(smd5, tmd5);
    }

    public static String getFileMD5(File file) throws Exception {
        if (file == null || !file.isFile() || file.length() == 0L) {
            return "";
        }

        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            return getFileMD5(in);
        } catch (FileNotFoundException e) {
            throw new Exception(String.format("%s not found"), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static String getFileMD5(InputStream in) throws Exception {
        byte buffer[] = new byte[1024];
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            int len;
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            //      BigInteger bigInt = new BigInteger(1, digest.digest());
            //      return bigInt.toString(16);
            return FileUtil.asHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("acquire MD5 failed", e);
        } catch (IOException e) {
            throw new Exception(String.format("%s not found", in.toString()));
        }
    }

    /**
     * 将字节数组转换成16进制字符串
     *
     * @param buf
     *
     * @return
     */
    public static String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;
        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10)//小于十前面补零
            {
                strbuf.append("0");
            }
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }
        return strbuf.toString();
    }
}
