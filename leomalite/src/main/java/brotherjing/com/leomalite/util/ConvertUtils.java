package brotherjing.com.leomalite.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 类型转换工具
 * 
 * @author yynie
 */
public class ConvertUtils {

	public static final String DATE_FORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String TIME_ZONE_GMT8 = "GMT+8";

	/**
	 * 转换String至指定类型的值
	 * 
	 * @param string
	 * @param cls
	 *            指定类型
	 * @return
	 */
	public static Object toValue(String string, Class<?> cls) {
		try {
			if (cls.equals(boolean.class) || cls.equals(Boolean.class)) {
				return Boolean.parseBoolean(string);
			} else if (cls.equals(byte.class) || cls.equals(Byte.class)) {
				return Byte.parseByte(string);
			} else if (cls.equals(int.class) || cls.equals(Integer.class)) {
				return Integer.parseInt(string);
			} else if (cls.equals(long.class) || cls.equals(Long.class)) {
				return Long.parseLong(string);
			} else if (cls.equals(short.class) || cls.equals(Short.class)) {
				return Short.parseShort(string);
			} else if (cls.equals(float.class) || cls.equals(Float.class)) {
				return Float.parseFloat(string);
			} else if (cls.equals(double.class) || cls.equals(Double.class)) {
				return Double.parseDouble(string);
			}
			return null;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * 像素转为dip
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int pxToDip(Context context, float pxValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * dip转为像素
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dipToPx(Context context, float dipValue) {
		return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue,
				context.getResources().getDisplayMetrics()) + 0.5f);
	}

	/**
	 * 字符串转毫秒时间(默认时区)
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static long toMilliseconds(String date, String pattern) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			sdf.setTimeZone(TimeZone.getDefault());
			return sdf.parse(date).getTime();
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 字符串转毫秒时间(指定时区)
	 * 
	 * @param date
	 * @param pattern
	 * @param timeZone
	 * @return
	 */
	public static long toMilliseconds(String date, String pattern, String timeZone) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
			return sdf.parse(date).getTime();
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Calendar转字符串(默认时区)
	 * 
	 * @param calendar
	 * @param pattern
	 * @return
	 */
	public static String toDateString(Calendar calendar, String pattern) {
		return toDateString(calendar.getTimeInMillis(), pattern);
	}

	/**
	 * 毫秒时间转字符串(默认时区)
	 * 
	 * @param milliseconds
	 * @param pattern
	 */
	public static String toDateString(long milliseconds, String pattern) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			sdf.setTimeZone(TimeZone.getDefault());
			return sdf.format(milliseconds);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 毫秒时间转字符串(指定时区)
	 * 
	 * @param milliseconds
	 * @param pattern
	 * @param timeZone
	 */
	public static String toDateString(long milliseconds, String pattern, String timeZone) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
			return sdf.format(milliseconds);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 毫秒时间转Calendar(默认时区)
	 * 
	 * @param milliseconds
	 */
	public static Calendar toCalendar(long milliseconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliseconds);
		calendar.setTimeZone(TimeZone.getDefault());
		return calendar;
	}

	/**
	 * 毫秒时间转Calendar(指定时区)
	 * 
	 * @param milliseconds
	 * @param timeZone
	 */
	public static Calendar toCalendar(long milliseconds, String timeZone) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliseconds);
		calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
		return calendar;
	}

	/**
	 * 增加或减少时间
	 * 
	 * @param calendar
	 * @param type
	 * @param value
	 * @return
	 */
	public static Calendar addDate(Calendar calendar, int type, int value) {
		Calendar cNew = (Calendar) calendar.clone();
		cNew.add(type, value);
		return cNew;
	}

	/**
	 * 返回两日期之间相差的天数
	 * 
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	public static int getIntervalDays(Calendar startDay, Calendar endDay) {
		/*
		 * if (startDay.after(endDay)) { Calendar swap = startDay; startDay =
		 * endDay; endDay = swap; }
		 */
		long start = startDay.getTimeInMillis();
		long end = endDay.getTimeInMillis();
		long interval = end - start;
		return (int) (interval / (1000 * 60 * 60 * 24));
	}

	/**
	 * 返回两日期之间相差的天数
	 * 
	 * @param startday
	 * @param endday
	 * @return
	 */
	public static int getIntervalDays(Date startday, Date endday) {
		if (startday.after(endday)) {
			Date swap = startday;
			startday = endday;
			endday = swap;
		}
		long start = startday.getTime();
		long end = endday.getTime();
		long interval = end - start;
		return (int) (interval / (1000 * 60 * 60 * 24));
	}

	/**
	 * 返回两日期之间相差的精确天数
	 * 
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	public static int getAccurateIntervalDays(Calendar startDay, Calendar endDay) {
		if (startDay.after(endDay)) {
			Calendar swap = startDay;
			startDay = endDay;
			endDay = swap;
		}
		int days = endDay.get(Calendar.DAY_OF_YEAR) - startDay.get(Calendar.DAY_OF_YEAR);
		int endYear = endDay.get(Calendar.YEAR);
		if (startDay.get(Calendar.YEAR) != endYear) {
			startDay = (Calendar) startDay.clone();
			do {
				days += startDay.getActualMaximum(Calendar.DAY_OF_YEAR); // 得到当年的实际天数
				startDay.add(Calendar.YEAR, 1);
			} while (startDay.get(Calendar.YEAR) != endYear);
		}
		return days;
	}

	public static String getMD5(String val) {
		return getMD5(val.getBytes());
	}

	public static String getMD5(byte[] val) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");

			md5.update(val);
			byte[] m = md5.digest();// 加密
			StringBuffer sb = new StringBuffer();
			for (byte b : m) {
				// 数byte 类型转换为无符号的整数
				int n = b & 0XFF;
				// 将整数转换为16进制
				String s = Integer.toHexString(n);
				// 如果16进制字符串是一位，那么前面补0
				if (s.length() == 1) {
					sb.append("0" + s);
				} else {
					sb.append(s);
				}
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public static String getMD5(File file) {
		FileInputStream fin = null;
		try {
			StringBuffer sb = new StringBuffer();
			MessageDigest digest = MessageDigest.getInstance("md5");
			fin = new FileInputStream(file);
			int len = -1;
			byte[] buffer = new byte[1024];// 设置输入流的缓存大小 字节
			// 将整个文件全部读入到加密器中
			while ((len = fin.read(buffer)) != -1) {
				digest.update(buffer, 0, len);
			}
			fin.close();

			// 对读入的数据进行加密
			byte[] bytes = digest.digest();
			for (byte b : bytes) {
				// 数byte 类型转换为无符号的整数
				int n = b & 0XFF;
				// 将整数转换为16进制
				String s = Integer.toHexString(n);
				// 如果16进制字符串是一位，那么前面补0
				if (s.length() == 1) {
					sb.append("0" + s);
				} else {
					sb.append(s);
				}
			}
			return sb.toString();
		} catch (Exception e) {
			try {
				fin.close();
			} catch (Exception e1) {
			}
		}
		return "";
	}

	public static byte[] bitMaptoBytes(Bitmap map) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		map.compress(Bitmap.CompressFormat.PNG, 100, stream);
		return stream.toByteArray();
	}

	public static byte[] FileToByteArray(File file) {
		if (file == null || !file.exists() || !file.canRead())
			return null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[2048];
			while (fis.read(buffer) != -1) {
				out.write(buffer);
			}
			fis.close();
			out.close();
			return out.toByteArray();
		} catch (Exception e) {
			return null;
		}
	}
}
