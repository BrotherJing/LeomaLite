package brotherjing.com.leomalite.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Android prefence文件读写操作工具类
 * 
 * @author yynie
 * 
 */
public final class SharedPrefUtil {

	public static SharedPreferences getSharedPreferences(Context context, String name) {
		return context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
	}

	public static SharedPreferences getSharedPreferences(Context context, String name, int mode) {
		return context.getSharedPreferences(name, mode);
	}

	public static SharedPreferences getLeomaSharedPreferences(Context context, String name) {
		return context.getSharedPreferences(
				"LeomaCache" + name, Context.MODE_PRIVATE);
	}

	public static SharedPreferences getLeomaSharedPreferences(Context context, String name,
															  int mode) {
		return context.getSharedPreferences(
				"LeomaCache" + name, mode);
	}

	public static boolean clear(Context context) {
		return clear(PreferenceManager.getDefaultSharedPreferences(context));
	}

	public static boolean clear(SharedPreferences pref) {
		SharedPreferences.Editor editor = pref.edit();
		editor.clear();
		return editor.commit();
	}

	public static boolean remove(Context context, String key) {
		return remove(PreferenceManager.getDefaultSharedPreferences(context),
				key);
	}

	public static boolean remove(SharedPreferences pref, String key) {
		SharedPreferences.Editor editor = pref.edit();
		editor.remove(key);
		return editor.commit();
	}

	public static boolean contains(SharedPreferences pref, String key) {
		return pref.contains(key);
	}

	/*--------------------------------------------------------------------------
	| 读数据
	--------------------------------------------------------------------------*/

	public static int getInt(Context context, String key, int defValue) {
		return getInt(PreferenceManager.getDefaultSharedPreferences(context),
				key, defValue);
	}

	public static int getInt(SharedPreferences pref, String key, int defValue) {
		return pref.getInt(key, defValue);
	}

	public static long getLong(Context context, String key, long defValue) {
		return getLong(PreferenceManager.getDefaultSharedPreferences(context),
				key, defValue);
	}

	public static long getLong(SharedPreferences pref, String key, long defValue) {
		return pref.getLong(key, defValue);
	}

	public static String getString(Context context, String key, String defValue) {
		return getString(
				PreferenceManager.getDefaultSharedPreferences(context), key,
				defValue);
	}

	public static String getString(SharedPreferences pref, String key,
								   String defValue) {
		return pref.getString(key, defValue);
	}

	public static boolean getBoolean(Context context, String key,
									 boolean defValue) {
		return getBoolean(
				PreferenceManager.getDefaultSharedPreferences(context), key,
				defValue);
	}

	public static boolean getBoolean(SharedPreferences pref, String key,
									 boolean defValue) {
		return pref.getBoolean(key, defValue);
	}

	/*--------------------------------------------------------------------------
	| 写数据
	--------------------------------------------------------------------------*/

	public static boolean putInt(Context context, String key, int value) {
		return putInt(PreferenceManager.getDefaultSharedPreferences(context),
				key, value);
	}

	public static boolean putInt(SharedPreferences pref, String key, int value) {
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, value);
		return editor.commit();
	}

	public static boolean putLong(Context context, String key, long value) {
		return putLong(PreferenceManager.getDefaultSharedPreferences(context),
				key, value);
	}

	public static boolean putLong(SharedPreferences pref, String key, long value) {
		SharedPreferences.Editor editor = pref.edit();
		editor.putLong(key, value);
		return editor.commit();
	}

	public static boolean putString(Context context, String key, String value) {
		return putString(
				PreferenceManager.getDefaultSharedPreferences(context), key,
				value);
	}

	public static boolean putString(SharedPreferences pref, String key,
									String value) {
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, value);
		return editor.commit();
	}

	public static boolean putBoolean(Context context, String key, boolean value) {
		return putBoolean(
				PreferenceManager.getDefaultSharedPreferences(context), key,
				value);
	}

	public static boolean putBoolean(SharedPreferences pref, String key,
									 boolean value) {
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}
}
