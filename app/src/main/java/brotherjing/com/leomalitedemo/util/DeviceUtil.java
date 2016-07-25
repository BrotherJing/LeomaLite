package brotherjing.com.leomalitedemo.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StatFs;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.util.DisplayMetrics;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import brotherjing.com.leomalite.util.Logger;
import brotherjing.com.leomalitedemo.App;

/**
 * 设备工具类
 * 
 * @author yynie
 * 
 */
public final class DeviceUtil {

	private static final String TAG = DeviceUtil.class.getName();

	private static final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
	private static final String ACTION_DEL_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";
	private static final String EXTRA_SHORTCUT_DUPLICATE = "duplicate";
	private static final String APK_MIME_TYPE = "application/vnd.android.package-archive";
	private static final int SCREEN_DENSITY_MEDIUM = 160;
	private static final String MIME_TYPE_TEXT = "text/*";
	private static final String MIME_TYPE_EMAIL = "message/rfc822";
	private static final int NETWORK_TYPE_HSPA = 10;
	private static final int NETWORK_TYPE_HSDPA = 8;
	private static final int NETWORK_TYPE_HSUPA = 9;

	/*--------------------------------------------------------------------------
	| 设备信息获取
	--------------------------------------------------------------------------*/
	/**
	 * 获得设备分辨率，eg: dm.widthPixels dm.heightPixels dm.density dm.xdpi dm.ydpi
	 */
	public static DisplayMetrics getDisplayInfo(Context context) {
		return context.getApplicationContext().getResources().getDisplayMetrics();
	}

	/**
	 * 获得设备的DensityDpi
	 * 
	 * @param context
	 * @return
	 */
	public static int getDensityDpi(Context context) {
		int screenDensity = -1;
		try {
			DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
			screenDensity = DisplayMetrics.class.getField("densityDpi").getInt(displayMetrics);
		} catch (Exception e) {
			screenDensity = SCREEN_DENSITY_MEDIUM;
		}
		return screenDensity;
	}

	/**
	 * 获得设备的型号
	 */
	public static String getModel() {
		return Build.MODEL;
	}

	/**
	 * 获得设备的基带信息
	 * 
	 * @return
	 */
	public static String getBrand() {
		return Build.BRAND;
	}

	/**
	 * 获取设备的产品信息
	 * 
	 * @return
	 */
	public static String getProduct() {
		return Build.PRODUCT;
	}

	/**
	 * 获得设备的SDK版本
	 */
	public static int getSdkVersion() {
		return Integer.parseInt(Build.VERSION.SDK);
	}

	/**
	 * 获得设备的SDK版本
	 */
	public static int getIntSdkVersion() {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * 获得设备的系统版本
	 */
	public static String getReleaseVersion() {
		return Build.VERSION.RELEASE;
	}

	/**
	 * @return Build.DEVICE
	 */
	public static String getDevice() {
		return Build.DEVICE;
	}

	/**
	 * 获得设备的IMEI
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	/**
	 * 获得设备的IMSI
	 */
	public static String getIMSI(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getSubscriberId();
	}

	/**
	 * 获得设备的Sim卡序列号
	 */
	public static String getSimSerialNumber(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getSimSerialNumber();
	}

	/**
	 * 获取语言信息
	 * 
	 * @return
	 */
	public static String getLanguage() {
		try {
			Locale locale = Locale.getDefault();
			return locale.getLanguage() + "_" + locale.getCountry();
		} catch (Exception e) {
			log(e);
			return "";
		}
	}
	
	public static boolean isGeKitKat(){
		return getIntSdkVersion() >= 19;
	}

	/**
	 * 获取状态栏高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return statusBarHeight;
	}

	/**
	 * 获得设备IP
	 */
	public static String getLocalIP() {
		try {
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			for (; en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
				for (; enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
			return "";
		} catch (SocketException e) {
			return "";
		}
	}

	/**
	 * 获得DNS信息
	 * 
	 * @return
	 */
	public static String getDNSInfo() {
		String result = "Unknown";
		try {
			Class<?> SystemProperties = Class.forName("android.os.SystemProperties");
			Method method = SystemProperties.getMethod("get", new Class[] { String.class });
			ArrayList<String> servers = new ArrayList<String>();
			for (String name : new String[] { "net.dns1", "net.dns2", "net.dns3", "net.dns4", }) {
				String value = (String) method.invoke(null, name);
				if (value != null && !"".equals(value) && !servers.contains(value))
					servers.add(value);
			}
			StringBuffer sb = new StringBuffer();
			for (String s : servers) {
				sb.append(s).append(";");
			}
			result = sb.toString();
		} catch (Exception e) {
			log(e);
		}
		return result;
	}

	/**
	 * 获得Wifi的MAC地址
	 * 
	 * @param context
	 * @return
	 */
	/*public static String getWifiMAC(Context context) {
		try {
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			if (wifiManager == null) {
				return "";
			}
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			if (wifiInfo == null || wifiInfo.getMacAddress() == null) {
				return null;
			}
			return wifiInfo.getMacAddress().replaceAll(":", "");
		} catch (Exception e) {
			log(e);
			return "";
		}
	}*/

	/**
	 * 获得AndroidId
	 * 
	 * @param context
	 * @return
	 */
	public static String getAndroidId(Context context) {
		return android.provider.Settings.System.getString(context.getContentResolver(),
				android.provider.Settings.System.ANDROID_ID);
	}

	/**
	 * 判断是否模拟器
	 * 
	 * @param context
	 * @return
	 */
	// TODO
	// public static boolean isEmulator(Context context) {
	// try {
	// return Build.MODEL.toLowerCase().indexOf("sdk") > -1
	// || Build.MODEL.toLowerCase().indexOf("generic") > -1
	// || Build.PRODUCT.toLowerCase().indexOf("sdk") > -1
	// || "1".equals(SystemPropertiesProxy.get(context, "ro.kernel.qemu"));
	// } catch (Exception e) {
	// return false;
	// }
	// }

	/**
	 * 获取设备信息串
	 * 
	 * @param context
	 * @return
	 */
	/*public static String getDevString(Context context) {
		try {
			return "osversion:" + Build.VERSION.RELEASE.toLowerCase() + "|MODEL:" + Build.MODEL.toLowerCase()
					+ "|PRODUCT:" + Build.PRODUCT.toLowerCase() + "|BRAND:" + Build.BRAND.toLowerCase() + "|qemu:"
					+ SystemPropertiesProxy.get(context, "ro.kernel.qemu");
		} catch (Exception e) {
			return "";
		}
	}*/

	/*--------------------------------------------------------------------------
	| 软件信息获取
	--------------------------------------------------------------------------*/
	/**
	 * 获得版本号
	 */
	public static int getVersionCode(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return info.versionCode;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 获得版本名称
	 */
	public static String getVersionName(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获得应用名称
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return info.applicationInfo.loadLabel(manager).toString();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获取Application中<meta-data>元素的数据
	 * 
	 * @param context
	 * @return
	 */
	public static Bundle getMetaDataInApplication(Context context) {
		try {
			ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
					PackageManager.GET_META_DATA);
			return applicationInfo.metaData;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取应用程序包名
	 * 
	 * @return
	 */
	public static String getPackageName() {
		try {
			return App.getAppContext().getPackageName();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获取Activity中<meta-data>元素的数据
	 * 
	 * @param activity
	 * @return
	 */
	public static Bundle getMetaDataInActivity(Activity activity) {
		try {
			ActivityInfo activityInfo = activity.getPackageManager().getActivityInfo(activity.getComponentName(),
					PackageManager.GET_META_DATA);
			return activityInfo.metaData;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取Service中<meta-data>元素的数据
	 * 
	 * @param context
	 * @param cls
	 * @return
	 */
	public static Bundle getMetaDataInService(Context context, Class<?> cls) {
		try {
			ComponentName componentName = new ComponentName(context, cls);
			ServiceInfo serviceInfo = context.getPackageManager().getServiceInfo(componentName,
					PackageManager.GET_META_DATA);
			return serviceInfo.metaData;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取Receiver中<meta-data>元素的数据
	 * 
	 * @param context
     * @param cls
	 * @return
	 */
	public static Bundle getMetaDataInReceiver(Context context, Class<?> cls) {
		try {
			ComponentName componentName = new ComponentName(context, cls);
			ActivityInfo activityInfo = context.getPackageManager().getReceiverInfo(componentName,
					PackageManager.GET_META_DATA);
			return activityInfo.metaData;
		} catch (Exception e) {
			return null;
		}
	}

	public static int getNetWorkType(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getNetworkType();
	}

	public static String getManufacturer() {
		return android.os.Build.MANUFACTURER;
	}

	/**
	 * 获得应用是否在前台
	 */
	public static boolean isOnForeground(Context context) {
		try {
			ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
			if (tasksInfo.size() > 0) {
				// 应用程序位于堆栈的顶层
				if (context.getPackageName().equals(tasksInfo.get(0).topActivity.getPackageName())) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 返回软件是否已安装
	 *
	 * @param context
	 * @param uri
	 *            软件包名
	 * @return
	 */
	public static boolean isAppInstalled(Context context, String uri) {
		try {
			PackageManager manager = context.getPackageManager();
			manager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 返回软件的原始签名
	 *
	 * @param context
	 * @return
	 */
	public static String[] getSignature(Context context) {
		try {
			String[] result = null;
			Signature[] signatures = context.getPackageManager().getPackageInfo(context.getPackageName(),
					PackageManager.GET_SIGNATURES).signatures;
			if (signatures != null && signatures.length != 0) {
				result = new String[signatures.length];
				for (int i = 0; i < signatures.length; i++) {
					result[i] = signatures[i].toCharsString();
				}
			}
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 返回软件的签名，经过Hash处理
	 *
	 * @param context
	 * @return
	 */
	public static String[] getSignatureHash(Context context) {
		try {
			String[] result = null;
			Signature[] signatures = context.getPackageManager().getPackageInfo(context.getPackageName(),
					PackageManager.GET_SIGNATURES).signatures;
			if (signatures != null && signatures.length != 0) {
				result = new String[signatures.length];
				for (int i = 0; i < signatures.length; i++) {
					result[i] = Integer.toHexString(signatures[i].toCharsString().hashCode());
				}
			}
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 返回Intent是否可用
	 *
	 * @param context
	 * @param intent
	 * @return
	 */
	public static boolean isIntentEnabled(Context context, Intent intent) {
		try {
			PackageManager packageManager = context.getPackageManager();
			List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
			return list.size() > 0;
		} catch (Exception e) {
			return false;
		}
	}

	/*--------------------------------------------------------------------------
	| 设备状态检查
	--------------------------------------------------------------------------*/
	/**
	 * 检查网络是否可用
	 */
	public static boolean isNetworkEnabled(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo[] infoArray = connectivity.getAllNetworkInfo();
				if (infoArray != null) {
					for (NetworkInfo info : infoArray) {
						if (info.getState() == NetworkInfo.State.CONNECTED) {
							return true;
						}
					}
				}
			}
			return false;
		} catch (Exception e) {
			log(e);
			return false;
		}
	}

	/**
	 * 检查Sim卡状态
	 */
	public static boolean isSimEnabled(Context context) {
		try {
			TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			if (mTelephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 检查移动网络是否可用
	 */
	public static boolean isMobileEnabled(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
					.getState() == NetworkInfo.State.CONNECTED) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 检查WIFI网络是否可用
	 */
	public static boolean isWifiEnabled(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 检查网络是否联通WCDMA
	 */
	public static boolean isWCDMA(Context context) {
		try {
			// 获得手机SIMType
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			int pType = tm.getPhoneType();
			int nType = tm.getNetworkType();
			String operator = tm.getNetworkOperator();
			if (operator.equals("46001")) {
				// 运营商为联通
				if (pType == TelephonyManager.PHONE_TYPE_GSM) {
					if (nType == NETWORK_TYPE_HSPA || nType == NETWORK_TYPE_HSDPA || nType == NETWORK_TYPE_HSUPA) {
						return true;
					}
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 检查是否开通GPS定位或网络定位
	 */
	public static boolean isLocationEnabled(Context context) {
		try {
			LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
					|| lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 检查指定的定位服务是否开通
	 *
	 * @param context
	 * @param provider
	 */
	public static boolean isProviderEnabled(Context context, String provider) {
		try {
			LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			return lm.isProviderEnabled(provider);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 根据指定的Provider获取位置
	 *
	 * @param context
	 * @param provider
	 */
	/*public static Location getLocation(Context context, String provider) {
		try {
			LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			List<String> providers = lm.getProviders(true);
			if (providers != null && providers.contains(provider)) {
				Location locTmp = lm.getLastKnownLocation(provider);
				if (locTmp != null) {
					return locTmp;
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}*/

	/**
	 * 根据指定的Provider获取位置
	 *
	 * @param context
	 * @param provider
	 * @param time
	 *            有效时间界限，获取的位置在此时间之后的为有效位置
	 */
	/*public static Location getLocation(Context context, String provider, long time) {
		try {
			LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			List<String> providers = lm.getProviders(true);
			if (providers != null && providers.contains(provider)) {
				Location locTmp = lm.getLastKnownLocation(provider);
				if (locTmp != null && locTmp.getTime() >= time) {
					return locTmp;
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}*/

	/**
	 * 检查设备存储卡是否装载
	 */
	public static boolean isSDCardMounted() {
		return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 检查设备存储卡的读权限
	 */
	public static boolean isSDCardReadable() {
		try {
			if (!isSDCardMounted()) {
				return false;
			}
			File sdcardDir = Environment.getExternalStorageDirectory();
			if (!sdcardDir.canRead()) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 检查设备存储卡的写权限
	 */
	public static boolean isSDCardWritable() {
		try {
			if (!isSDCardMounted()) {
				return false;
			}
			File sdcardDir = Environment.getExternalStorageDirectory();
			if (!sdcardDir.canWrite()) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 获得SD卡的可用大小(单位为MB)
	 *
	 * @return
	 */
	public static long getSDCardAvailaleSizeInMB() {
		return getSDCardAvailaleSizeInKB() / 1024;
	}

	/**
	 * 获得SD卡的可用大小(单位为KB)
	 *
	 * @return
	 */
	public static long getSDCardAvailaleSizeInKB() {
		return getSDCardAvailaleSize() / 1024;
	}

	/**
	 * 获得SD卡的总大小(单位为MB)
	 *
	 * @return
	 */
	public static long getSDCardTotalSizeInMB() {
		return getSDCardTotalSizeInKB() / 1024;
	}

	/**
	 * 获得SD卡的总大小(单位为KB)
	 *
	 * @return
	 */
	public static long getSDCardTotalSizeInKB() {
		return getSDCardTotalSize() / 1024;
	}

	/**
	 * 获得SD卡的可用大小(单位为Byte)
	 *
	 * @return
	 */
	public static long getSDCardAvailaleSize() {
		File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * 获得SD卡的总大小(单位为Byte)
	 *
	 * @return
	 */
	public static long getSDCardTotalSize() {
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getBlockCount();
		return availableBlocks * blockSize;
	}

	/**
	 * 检查相机是否能使用
	 */
	public static boolean isCameraEnabled(Context context) {
		Camera camera = null;
		try {
			camera = android.hardware.Camera.open();
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (camera != null) {
				camera.release();
			}
		}
	}

	/*public static File getPhotoStorage(CameraOption type) {
		File file = null;
		if (type == null) {
			file = getDCIMCamera();
			if (!file.exists())
				file = getPhotoLibrary();
			return file;
		}
		if (type.getSourceType() == 0)
			file = getDCIMCamera();
		else
			file = getPhotoLibrary();
		if (file.exists())
			return file;
		type.setSourceType(0);
		file = getDCIMCamera();
		if (!file.exists()) {
			type.setSourceType(1);
			file = getPhotoLibrary();
		}
		return file;
	}*/

	private static File getDCIMCamera() {
		File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		return new File(dir, "Camera");
	}

	private static File getPhotoLibrary() {
		return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	}

	/*--------------------------------------------------------------------------
	| 设备硬件操作
	--------------------------------------------------------------------------*/
	/**
	 * 使设备震动
	 */
	public static void vibrate(Context context, long milliseconds) {
		try {
			Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(milliseconds);
		} catch (Exception e) {
			log(e);
		}
	}

	/**
	 * 使设备震动
	 */
	public static void vibrate(Context context, long[] pattern, int repeat) {
		try {
			Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			// long[] pattern = {100,400,100,400}; // OFF/ON/OFF/ON...
			vibrator.vibrate(pattern, repeat);
		} catch (Exception e) {
			log(e);
		}
	}

	/**
	 * 使设备停止震动
	 */
	public static void cancelVibrate(Context context) {
		try {
			Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.cancel();
		} catch (Exception e) {
			log(e);
		}
	}

	/**
	 * 点亮屏幕
	 *
	 * @param context
	 */
	public static void wakeupScreen(Context context) {
		try {
			PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			PowerManager.WakeLock wakeLock = powerManager
					.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
			if (!isScreenOn(powerManager)) {
				wakeLock.setReferenceCounted(false);
				wakeLock.acquire();
			}
			if (null != wakeLock)
				wakeLock.release();
		} catch (Exception e) {
			log(e);
		}
	}

	/**
	 * 返回屏幕是否点亮
	 *
	 * @param powerManager
	 * @return
	 */
	public static boolean isScreenOn(PowerManager powerManager) {
		try {
			Method method = PowerManager.class.getMethod("isScreenOn");
			return (Boolean) method.invoke(powerManager);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 禁用键盘锁
	 *
	 * @param context
	 */
	/*public static void disableKeyguard(Context context) {
		try {
			KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
			KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
			keyguardLock.disableKeyguard();
		} catch (Exception e) {
			log(e);
		}
	}*/

	/**
	 * 启用键盘锁
	 *
	 * @param context
	 */
	/*public static void reenableKeyguard(Context context) {
		try {
			KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
			KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
			keyguardLock.reenableKeyguard();
		} catch (Exception e) {
			log(e);
		}
	}*/

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static boolean isKeyguardLocked(Context context) {
		try {
			KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
			return keyguardManager.isKeyguardLocked();
		} catch (Exception e) {
			log(e);
			return false;
		}
	}

	/*--------------------------------------------------------------------------
	| 系统程序操作
	--------------------------------------------------------------------------*/
	/**
	 * 调出拨号程序
	 *
	 * @param context
	 * @param phone
	 *            电话号码
	 */
	public static void dial(Context context, String phone) throws Exception {
		phone = "tel:" + phone;
		Uri uri = Uri.parse(phone);
		Intent intent = new Intent(Intent.ACTION_DIAL, uri);
		context.startActivity(intent);
	}

	/**
	 * 直接拨打电话
	 *
	 * @param context
	 * @param phone
	 *            电话号码
	 */
	/*public static void call(Context context, String phone) throws Exception {
		phone = "tel:" + phone;
		Uri uri = Uri.parse(phone);
		Intent intent = new Intent(Intent.ACTION_CALL, uri);
		context.startActivity(intent);
	}*/

	/**
	 * 发送短信
	 *
	 * @param context
	 * @param phone
	 *            电话号码
	 * @param content
	 *            短信内容
	 */
	public static void sendSMS(Context context, String phone, String content) throws Exception {
		phone = "smsto:" + phone;
		Uri uri = Uri.parse(phone);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		intent.putExtra("sms_body", content);
		context.startActivity(intent);
	}

	/**
	 * 从本地选取图片，应处理onActivityResult，示例： protected void onActivityResult(int
	 * requestCode, int resultCode, Intent data) { //获得图片的真实地址 String path =
	 * getPathByUri(this, data.getData()); }
	 *
	 * @param activity
	 * @param requestCode
	 */
	public static void pickImage(Activity activity, int requestCode) throws Exception {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 调用拍照程序拍摄图片，返回图片对应的Uri，应处理onActivityResult
	 * ContentResolver的insert方法会默认创建一张空图片，如取消了拍摄，应根据方法返回的Uri删除图片
	 *
	 * @param activity
	 * @param requestCode
	 * @param fileName
	 * @return
	 */
	public static Uri captureImage(Activity activity, int requestCode, String fileName, String desc) throws Exception {
		// 设置文件参数
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, fileName);
		values.put(MediaStore.Images.Media.DESCRIPTION, desc);
		// 获得uri
		Uri imageUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		activity.startActivityForResult(intent, requestCode);
		return imageUri;
	}

	/**
	 * 调用地图程序
	 *
	 * @param activity
	 * @param address
	 * @param placeTitle
	 */
	public static void callMap(Activity activity, String address, String placeTitle) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("geo:0,0?q=");
		sb.append(Uri.encode(address));
		// pass text for the info window
		String titleEncoded = Uri.encode("(" + placeTitle + ")");
		sb.append(titleEncoded);
		// set locale
		sb.append("&hl=" + Locale.getDefault().getLanguage());
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString()));
		activity.startActivity(intent);
	}

	/**
	 * 调用分享程序
	 *
	 * @param activity
	 * @param subject
	 * @param message
	 * @param chooserDialogTitle
	 */
	public static void callShare(Activity activity, String subject, String message, String chooserDialogTitle)
			throws Exception {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		shareIntent.putExtra(Intent.EXTRA_TEXT, message);
		shareIntent.setType(MIME_TYPE_TEXT);
		Intent intent = Intent.createChooser(shareIntent, chooserDialogTitle);
		activity.startActivity(intent);
	}

	/**
	 * 调用发送电子邮件程序
	 *
	 * @param activity
	 * @param address
	 * @param subject
	 * @param body
	 */
	public static void callEmail(Activity activity, String address, String subject, String body) throws Exception {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, body);
		intent.setType(MIME_TYPE_EMAIL);
		activity.startActivity(intent);
	}

	/**
	 * 调用网络搜索
	 *
	 * @param activity
	 * @param keyword
	 * @throws Exception
	 */
	public static void callWebSearch(Activity activity, String keyword) throws Exception {
		Intent search = new Intent(Intent.ACTION_WEB_SEARCH);
		search.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		search.putExtra(SearchManager.QUERY, keyword);
		Bundle appData = activity.getIntent().getBundleExtra(SearchManager.APP_DATA);
		if (appData != null) {
			search.putExtra(SearchManager.APP_DATA, appData);
		}
		activity.startActivity(search);
	}

	/**
	 * 在桌面创建快捷方式
	 */
	/*public static void addShortcut(Context context) {
		try {
			// 点击快捷方式后的Intent
			Intent intentShortcut = new Intent(Intent.ACTION_MAIN);
			intentShortcut.setClass(context, context.getClass());
			intentShortcut.addCategory(Intent.CATEGORY_LAUNCHER);
			// 不需要总是新建任务
			// intentShortcut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// intentShortcut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Intent intentAddShortcut = new Intent(ACTION_ADD_SHORTCUT);
			intentAddShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intentShortcut);
			// 快捷方式显示名
			intentAddShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getAppName(context));
			// 快捷方式icon
			intentAddShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
					Intent.ShortcutIconResource.fromContext(context, R.drawable.icon));
			// 不允许重复创建
			intentAddShortcut.putExtra(EXTRA_SHORTCUT_DUPLICATE, false);
			context.sendBroadcast(intentAddShortcut);
		} catch (Exception e) {
			log(e);
		}
	}*/

	/**
	 * 删除本应用的桌面快捷方式
	 */
	public static void delShortcut(Context context) {
		try {
			// 点击快捷方式后的Intent
			Intent intentShortcut = new Intent(Intent.ACTION_MAIN);
			intentShortcut.setClass(context, context.getClass());
			intentShortcut.addCategory(Intent.CATEGORY_LAUNCHER);
			// intentShortcut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// intentShortcut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Intent intentDelshortcut = new Intent(ACTION_DEL_SHORTCUT);
			intentDelshortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intentShortcut);
			intentDelshortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getAppName(context));
			context.sendBroadcast(intentDelshortcut);
		} catch (Exception e) {
			log(e);
		}
	}

	/**
	 * 去系统定位设置界面
	 */
	public static void gotoLocationSettings(Context context) {
		try {
			Intent settingsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			context.startActivity(settingsIntent);
		} catch (Exception e) {
			log(e);
		}
	}

	/**
	 * 去系统无线设置界面
	 */
	public static void gotoWirelessSettings(Context context) {
		try {
			Intent settingsIntent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
			context.startActivity(settingsIntent);
		} catch (Exception e) {
			log(e);
		}
	}

	/**
	 * 安装apk文件
	 */
	public static void installApk(Context context, File file) {
		try {
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
			Uri data = Uri.fromFile(file);
			intent.setDataAndType(data, APK_MIME_TYPE);
			context.startActivity(intent);
		} catch (Exception e) {
			log(e);
		}
	}

	/**
	 * 卸载apk文件
	 */
	public static void uninstallApk(Context context, String packageName) {
		try {
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
			Uri data = Uri.parse("package:" + packageName);
			intent.setData(data);
			context.startActivity(intent);
		} catch (Exception e) {
			log(e);
		}
	}

	/**
	 * 清空缓存目录下的文件(不清除子文件夹内的文件)
	 */
	public static boolean clearDiskCache(Context context, String path) {
		try {
			File diskCacheDir;
			if (DeviceUtil.isSDCardMounted()) {
				diskCacheDir = new File(android.os.Environment.getExternalStorageDirectory(), path);
			} else {
				diskCacheDir = context.getCacheDir();
			}
			if (diskCacheDir != null) {
				boolean success = true;
				if (!diskCacheDir.exists() || !diskCacheDir.isDirectory()) {
					return true;
				}
				for (File child : diskCacheDir.listFiles()) {
					if (child.isFile()) {
						success = child.delete();
						if (!success) {
							return false;
						}
					}
				}
				return success;
			}
			return false;
		} catch (Exception e) {
			log(e);
			return false;
		}
	}

	/**
	 * 根据Uri获取媒体文件的路径
	 * 
	 * @param activity
	 * @param uri
	 * @return
	 */
	public static String getMediaPathByUri(Activity activity, Uri uri) {
		String result = "";
		try {
			if (uri == null) {
				return result;
			}
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
			if (cursor == null || cursor.isClosed()) {
				return result;
			}
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			result = cursor.getString(column_index);
			cursor.close();
			return result;
		} catch (Exception e) {
			log(e);
			return result;
		}
	}

	/**
	 * 复制文本信息到剪贴板
	 * 
	 * @param context
	 * @param text
	 */
	public static void copyToClipboard(Context context, String text) {
		try {
			ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
			clipboardManager.setText(text);
		} catch (Exception e) {
			log(e);
		}
	}

	/**
	 * 从剪贴板获取文本信息
	 * 
	 * @param context
	 * @return
	 */
	public static CharSequence getFromClipboard(Context context) {
		try {
			ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
			if (clipboardManager.hasText()) {
				return clipboardManager.getText();
			}
			return "";
		} catch (Exception e) {
			log(e);
			return "";
		}
	}

	/**
	 * 记录错误
	 * 
	 * @param tr
	 */
	private static void log(Throwable tr) {
		Logger.i(tr.getLocalizedMessage());
	}

	/**
	 * 获取系统语言
	 */
	public static String getSystemLanguage() {
		Locale locale = App.getAppContext().getResources().getConfiguration().locale;
		String language = locale.getLanguage().toLowerCase();
		String country = locale.getCountry().toUpperCase();
		if (null == language)
			return "chs";
		if (language.equals("zh")) {
			if (country.equals("CN"))
				return "chs";
			else if (country.equals("TW"))
				return "cht";
			else
				return "chs";
		} else {
			return language;
		}
	}

	/**
	 * 更新app语言
	 */
	/*public static void shouldUpdateAppLanguage(String lan, boolean isSys) {
		*//**
		 * 逻辑： 1、未在app内设置语言：SP不更新，每次进入app调用传入(当前语言、true)
		 * 1.1、第一次app内设置语言：更新SP，调用传入（更新的语言、false）
		 * 2、已在app内设置语言：更新SP，每次更新语言，调用传入（更新的语言、false）
		 * 
		 *//*
		String spLan = SharedPrefUtils.getString("app_language", null);
		if (spLan == null) {// 未设置过app内语言
			if (lan == null) {
				return;
			}
			lan = lan.toLowerCase();
			if (!isSys) {// 第一次在app内设置语言
				SharedPrefUtils.putString("app_language", lan);
			}
		} else {// 已设置过app内语言
			if (isSys) {
				if (lan == null) {
					SharedPrefUtils.remove("app_language");
					updateLan(CorpConfig.SYSTEM_LANGUAGE);
					return;
				} else {
					lan = spLan;
				}
				lan = lan.toLowerCase();
			} else {
				if (lan == null) {
					lan = spLan;
				}
				lan = lan.toLowerCase();
				SharedPrefUtils.putString("app_language", lan);
			}
		}
		updateLan(lan);
	}*/

	/*private static void updateLan(String lan) {
		Resources r = CorpEngine.getInstance().commonResources;
		Configuration cfg = r.getConfiguration();
		DisplayMetrics met = r.getDisplayMetrics();
		if (lan.equals("chs")) {
			cfg.locale = Locale.CHINA;
		} else {
			cfg.locale = Locale.ENGLISH;
		}
		r.updateConfiguration(cfg, met);
	}*/
}
