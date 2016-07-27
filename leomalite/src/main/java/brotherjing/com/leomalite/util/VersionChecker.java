package brotherjing.com.leomalite.util;

/**
 * Created by jingyanga on 2016/7/26.
 */
public class VersionChecker {

    public static boolean isNewerVersion(String version, String storedVersion){
        Logger.i("old version is "+storedVersion+", new version is "+version);
        String[] versions = version.split("\\.");
        String[] storedVersions = storedVersion.split("\\.");
        for(int i=0;i<Math.min(versions.length,storedVersions.length);++i){
            int v1 = Integer.parseInt(versions[i]);
            int v2 = Integer.parseInt(storedVersions[i]);
            if(v1>v2)return true;
            else if(v1<v2)return false;
        }
        return versions.length > storedVersions.length;
    }

}
