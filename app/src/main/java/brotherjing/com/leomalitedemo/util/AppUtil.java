package brotherjing.com.leomalitedemo.util;

import android.os.Build;

import brotherjing.com.leomalitedemo.App;

/**
 * Created by jingyanga on 2016/7/25.
 */
public class AppUtil {

    public static String getUserAgent(){
        StringBuilder builder = new StringBuilder();
        builder.append(DeviceUtil.getBrand()).append("|")
                .append(DeviceUtil.getDevice()).append("|")
                .append(DeviceUtil.getManufacturer()).append("|")
                .append(DeviceUtil.getProduct()).append(",Android,")
                .append(DeviceUtil.getReleaseVersion()).append(",")
                .append(DeviceUtil.getVersionName(App.getAppContext()))
                .append(",corpctrip,slide,html5Cache,Contacts,File.01,CustomStorage,Leoma,GPS,NativeLocation.1v,WVBridge,AllSite,PartiCache,NativeShare,PasteBoard,Alipay,MutliPages,NoteNative")
                .append(",Language.").append(DeviceUtil.getSystemLanguage())
                .append(",Lancount=2c,");
        return builder.toString();
    }

}
