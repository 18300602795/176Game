package com.i76game.utils;


import android.content.Context;
import android.content.SharedPreferences;


/**
 * SharePreferences操作工具
 */
public class SharePrefUtil {
	private static String tag = SharePrefUtil.class.getSimpleName();
	private final static String SP_NAME = "config";
	private static SharedPreferences sp;


	public interface KEY {
		String function_login_username="login_username";
		String function_login_pwd="login_pwd";
		String function_login_img="login_img";//用户头像路径
		String FIRST_LOGIN="first_login";//判断用户是否是第一次登录的
		String SAVE_ALTER="savealter";//判断用户是否点击了保存修改；
		String NICHENG="nicheng";//保存用户的昵称
		String SEX="sex"; //保存用户的性别
		String HAPPY_BRITHDAY="happy_btithday";//保存用户的生日
		String QIANMING="qianming";//保存用户的签名
		String TIMESTAMP="timestamp";//保存时间戳
		String TIMESTAMPS="timestampS";//保存时间戳
		String IDENTIFIER ="identifier"; //保存用户的注册的密码
		String ACCESSTOKEN ="accesstoken"; //保存用户的注册的账号;
		String EXPAIRE_TIME="expaire_time";//保存用户账号的过期时间
		String ISDELETE="isdelete";//保存用户是否点击了详情页的下载
		String AGENT="agent";//保存渠道id

	}
	/**
	 * 清除sp里面的数据
	 */
	public static void delete(Context context){
		if (sp==null){
			sp=context.getSharedPreferences(SP_NAME,0);
		}
		sp.edit().clear().commit();
	}

	/**
	 * 保存布尔
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveBoolean(Context context, String key, boolean value) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME,0);
		}
		sp.edit().putBoolean(key, value).commit();
	}

	/**
	 * 保存字符
	 *
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveString(Context context, String key, String value) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, 0);
		}
		sp.edit().putString(key, value).commit();
	}
	
	public static void clear(Context context){
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, 0);
		}
		sp.edit().clear().commit();
	}

	/**
	 * 保存long
	 *  @param context
	 * @param key
	 * @param value
	 */
	public static long saveLong(Context context, String key, long value) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, 0);
	    }
		sp.edit().putLong(key, value).commit();

		return value;
	}

	/**
	 * 保存int
	 *  @param context
	 * @param key
	 * @param value
	 */
	public static int saveInt(Context context, String key, int value) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
	    }
		sp.edit().putInt(key, value).commit();
		return sp.getInt(key,-1);
	}

	/**
	 * 保存float
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveFloat(Context context, String key, float value) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, 0);
	    }
		sp.edit().putFloat(key, value).commit();
	}

	/**
	 * 获取字符
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String getString(Context context, String key, String defValue) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, 0);
		}
		return sp.getString(key, defValue);
	}

	/**
	 * 获取int
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static int getInt(Context context, String key, int defValue) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
	    }
		return sp.getInt(key, defValue);
	}

	/**
	 * 获取long
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static long getLong(Context context, String key, long defValue) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, 0);
		}
		return sp.getLong(key, defValue);
	}

	/**
	 * 获取float
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static float getFloat(Context context, String key, float defValue) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, 0);
		}
		return sp.getFloat(key, defValue);
	}

	/**
	 * 获取布尔
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static boolean getBoolean(Context context, String key,
									 boolean defValue) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, 0);
		}
		return sp.getBoolean(key, defValue);
	}

	public static String TouXiangDb(Context context, String pic_Path) {
		String pic_Pathload = null;
		sp = context.getSharedPreferences(SP_NAME, 0);
		SharedPreferences.Editor editor = sp.edit();
		String pic_path = sp.getString("pic_path", null);
		if (pic_path != null) {
			pic_Pathload = pic_path;
			String pic_pathload = "storage/sdcard0/" + pic_Pathload;
			return pic_pathload;
		}
		return pic_Pathload;

	}

}
