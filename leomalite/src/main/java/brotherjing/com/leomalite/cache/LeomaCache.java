package brotherjing.com.leomalite.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import brotherjing.com.leomalite.LeomaConfig;
import brotherjing.com.leomalite.util.Logger;
import brotherjing.com.leomalite.util.SharedPrefUtil;
import brotherjing.com.leomalite.util.VersionChecker;

/**
 * Created by jingyanga on 2016/7/26.
 */
public class LeomaCache {

    private static final String SP_MANIFEST_VERSION = "sp_manifest_version";
    private static final String SP_RESOURCE_VERSION = "sp_resource_version";
    private static final String SP_RESOURCE_CACHE_PATH = "sp_resource_cache_path";

    private static final int BUFFER_SIZE = 4096;

    private static SharedPreferences spManifestVersion;
    private static SharedPreferences spResourceVersion;
    private static SharedPreferences spResourceCachePath;

    static {
        spManifestVersion = SharedPrefUtil.getSharedPreferences(LeomaConfig.context, SP_MANIFEST_VERSION, Context.MODE_PRIVATE);
        spResourceVersion = SharedPrefUtil.getSharedPreferences(LeomaConfig.context, SP_RESOURCE_VERSION, Context.MODE_PRIVATE);
        spResourceCachePath = SharedPrefUtil.getSharedPreferences(LeomaConfig.context, SP_RESOURCE_CACHE_PATH, Context.MODE_PRIVATE);
    }

    public static boolean isNewVersion(String version, String manifestURLWithoutQuery){
        String oldVersion = SharedPrefUtil.getString(spManifestVersion, manifestURLWithoutQuery,null);
        return TextUtils.isEmpty(oldVersion) || VersionChecker.isNewerVersion(version,oldVersion);
    }

    public static boolean isResourceNewVersion(String version, URL resourceURL){
        String oldVersion = SharedPrefUtil.getString(spResourceVersion,resourceURL.toString(),null);
        return TextUtils.isEmpty(oldVersion)||VersionChecker.isNewerVersion(version,oldVersion);
    }

    public static void storeResourceNewVersion(String version, URL resourceURL){
        SharedPrefUtil.putString(spResourceVersion, resourceURL.toString(), version);
    }

    public static void storeManifestNewVersion(String version, String manifestURLWithoutQuery){
        SharedPrefUtil.putString(spManifestVersion,manifestURLWithoutQuery,version);
    }

    public static File generateFile(URL resourceURL){
        String path = resourceURL.getPath().replaceAll("/*$","");
        StringBuilder filePath = new StringBuilder();
        filePath.append(resourceURL.getHost()).append(path);
        if(filePath.lastIndexOf(".")<filePath.lastIndexOf("/")){
            filePath.append(".html");
        }
        File result = new File(
                LeomaConfig.context.getFilesDir().getAbsolutePath()+"/leomaCache/"+
                filePath.toString());
        File parent = result.getParentFile();
        if(!parent.exists())parent.mkdirs();
        return result;
    }

    public static void storeInFile(InputStream inputStream, File file, URL resourceURL)throws IOException{
        FileOutputStream outputStream = new FileOutputStream(file);
        if(inputStream!=null){
            try {
                byte[] tmp = new byte[BUFFER_SIZE];
                int len;
                long cnt = 0;
                while ((len = inputStream.read(tmp)) != -1 && !Thread.currentThread().isInterrupted()) {
                    cnt += len;
                    outputStream.write(tmp, 0, len);
                }
            }finally {
                inputStream.close();
                outputStream.flush();
                outputStream.close();

                SharedPrefUtil.putString(spResourceCachePath,resourceURL.toString(),file.getAbsolutePath());
            }
        }
    }

    public static InputStream getCachedStream(String url){
        String path = SharedPrefUtil.getString(spResourceCachePath,url,"");
        try {
            return new FileInputStream(new File(path));
        }catch (FileNotFoundException e){
            return null;
        }
    }
}
