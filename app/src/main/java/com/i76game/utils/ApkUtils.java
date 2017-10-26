package com.i76game.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.i76game.MyApplication;
import com.i76game.download.DownloadInfo;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ApkUtils {

	/**
	 * 安装apk
	 * @param downloadInfo
	 */
	public static void install(DownloadInfo downloadInfo){
		if(downloadInfo==null) return;
		String packageNameByApkFile = ApkUtils.getPackageNameByApkFile(MyApplication.getContextObject(), downloadInfo.getFileSavePath());
		boolean installApp = ApkUtils.isInstallApp(MyApplication.getContextObject(), packageNameByApkFile);
		if(installApp&&downloadInfo.getIsInstallSuccess()==1){//从这个盒子安装的
			downloadInfo.setPackageName(packageNameByApkFile);
			try {
				boolean isOpen = ApkUtils.openAppByPackageName(MyApplication.getContextObject(), downloadInfo.getPackageName());
				if(!isOpen){
                    Toast.makeText(MyApplication.getContextObject(),"打开失败！", Toast.LENGTH_SHORT).show();
                }
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//指定apk文件路径,安装apk
		intent.setDataAndType(Uri.parse("file://"+downloadInfo.getFileSavePath()),"application/vnd.android.package-archive");
		//注册安装成功监听
//		IntentFilter filter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
//		MyApplication.getContext().registerReceiver(new InstallReceiver(MyApplication.getContext(),downloadInfo),filter);
		MyApplication.getContextObject().startActivity(intent);
	}
	public static String getPackageNameByApkFile(Context context, String apkFilePath){
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
		ApplicationInfo appInfo = null;
		if (info != null) {
			appInfo = info.applicationInfo;
			String packageName = appInfo.packageName;
			return packageName;
		}
		return null;
	}

	/**
	 * 根据apk名字删掉一个已经或正在下载的apk
	 * @param apkname
	 * @return
	 */
	public static boolean deleteDownloadApk(Context ctx, String apkname){

		File file=new File(MyApplication.apkdownload_path+apkname+".apk");
		if(file.exists()){
			file.delete();//如果文件存在 删掉
			return true;
		}
		return false;
	}



	//返回数组，下标1代表大小，下标2代表单位 KB/MB
	public static String getFormatSize(double size){
		//Logger.msg(TAG, "size:"+size);
		String str="";
		if(size>=1024){
			str="KB";
			size/=1024;
			if(size>=1024){
				str="MB";
				size/=1024;
			}
		}
		DecimalFormat formatter=new DecimalFormat(".00");
		formatter.setGroupingSize(3);
		return (formatter.format(size)+str);
	}


	private ApkUtils() {
		throw new UnsupportedOperationException("u can't fuck me...");
	}

	/**
	 * 安装App
	 * <p>根据路径安装App</p>
	 *
	 * @param context  上下文
	 * @param filePath 文件路径
	 */
//	public static void installApp(Context context, String filePath) {
//		installApp(context, new File(filePath));
//	}

	/**
	 * 安装App
	 * <p>根据文件安装App</p>
	 *
	 * @param context 上下文
	 * @param file    文件
	 */
//	public static void installApp(Context context, File file) {
//		Intent intent = new Intent(Intent.ACTION_VIEW);
//		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(intent);
//	}

	/**
	 * 卸载指定包名的App
	 *
	 * @param context     上下文
	 * @param packageName 包名
	 */
	public static void uninstallApp(Context context, String packageName) {
		Intent intent = new Intent(Intent.ACTION_DELETE);
		intent.setData(Uri.parse("package:" + packageName));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 封装App信息的Bean类
	 */
	public static class AppInfo {

		private String name;
		private Drawable icon;
		private String packageName;
		private String versionName;
		private int versionCode;
		private boolean isSD;
		private boolean isUser;

		public Drawable getIcon() {
			return icon;
		}

		public void setIcon(Drawable icon) {
			this.icon = icon;
		}

		public boolean isSD() {
			return isSD;
		}

		public void setSD(boolean SD) {
			isSD = SD;
		}

		public boolean isUser() {
			return isUser;
		}

		public void setUser(boolean user) {
			isUser = user;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPackageName() {
			return packageName;
		}

		public void setPackageName(String packagName) {
			this.packageName = packagName;
		}

		public int getVersionCode() {
			return versionCode;
		}

		public void setVersionCode(int versionCode) {
			this.versionCode = versionCode;
		}

		public String getVersionName() {
			return versionName;
		}

		public void setVersionName(String versionName) {
			this.versionName = versionName;
		}

		/**
		 * @param name        名称
		 * @param icon        图标
		 * @param packageName 包名
		 * @param versionName 版本号
		 * @param versionCode 版本Code
		 * @param isSD        是否安装在SD卡
		 * @param isUser      是否是用户程序
		 */
		public AppInfo(String name, Drawable icon, String packageName,
					   String versionName, int versionCode, boolean isSD, boolean isUser) {
			this.setName(name);
			this.setIcon(icon);
			this.setPackageName(packageName);
			this.setVersionName(versionName);
			this.setVersionCode(versionCode);
			this.setSD(isSD);
			this.setUser(isUser);
		}

    /*@Override
    public String toString() {
        return getName() + "\n"
                + getIcon() + "\n"
                + getPackagName() + "\n"
                + getVersionName() + "\n"
                + getVersionCode() + "\n"
                + isSD() + "\n"
                + isUser() + "\n";
    }*/
	}

	/**
	 * 获取当前App信息
	 * <p>AppInfo（名称，图标，包名，版本号，版本Code，是否安装在SD卡，是否是用户程序）</p>
	 *
	 * @param context 上下文
	 * @return 当前应用的AppInfo
	 */
	public static AppInfo getAppInfo(Context context) {
		PackageManager pm = context.getPackageManager();
		PackageInfo pi = null;
		try {
			pi = pm.getPackageInfo(context.getApplicationContext().getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return pi != null ? getBean(pm, pi) : null;
	}

	/**
	 * 得到AppInfo的Bean
	 *
	 * @param pm 包的管理
	 * @param pi 包的信息
	 * @return AppInfo类
	 */
	private static AppInfo getBean(PackageManager pm, PackageInfo pi) {
		ApplicationInfo ai = pi.applicationInfo;
		String name = ai.loadLabel(pm).toString();
		Drawable icon = ai.loadIcon(pm);
		String packageName = pi.packageName;
		String versionName = pi.versionName;
		int versionCode = pi.versionCode;
		boolean isSD = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != ApplicationInfo.FLAG_SYSTEM;
		boolean isUser = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != ApplicationInfo.FLAG_SYSTEM;
		return new AppInfo(name, icon, packageName, versionName, versionCode, isSD, isUser);
	}

	/**
	 * 获取所有已安装App信息
	 * <p>AppInfo（名称，图标，包名，版本号，版本Code，是否安装在SD卡，是否是用户程序）</p>
	 * <p>依赖上面的getBean方法</p>
	 *
	 * @param context 上下文
	 * @return 所有已安装的AppInfo列表
	 */
	public static List<AppInfo> getAllAppsInfo(Context context) {
		List<AppInfo> list = new ArrayList<AppInfo>();
		PackageManager pm = context.getPackageManager();
		// 获取系统中安装的所有软件信息
		List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
		for (PackageInfo pi : installedPackages) {
			if (pi != null) {
				list.add(getBean(pm, pi));
			}
		}
		return list;
	}

	/**
	 * 根据包名获取意图
	 *
	 * @param context     上下文
	 * @param packageName 包名
	 * @return 意图
	 */
	private static Intent getIntentByPackageName(Context context, String packageName) {
		return context.getPackageManager().getLaunchIntentForPackage(packageName);
	}

	/**
	 * 根据包名判断App是否安装
	 *
	 * @param context     上下文
	 * @param packageName 包名
	 * @return true: 已安装<br>false: 未安装
	 */
	public static boolean isInstallApp(Context context, String packageName) {
		if(TextUtils.isEmpty(packageName)) return false;
		return getIntentByPackageName(context, packageName) != null;
	}

	/**
	 * 打开指定包名的App
	 *
	 * @param context     上下文
	 * @param packageName 包名
	 * @return true: 打开成功<br>false: 打开失败
	 */
	public static boolean openAppByPackageName(Context context, String packageName) {
		Intent intent = getIntentByPackageName(context, packageName);
		if (intent != null) {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			return true;
		}
		return false;
	}

	/**
	 * 打开指定包名的App应用信息界面
	 *
	 * @param context     上下文
	 * @param packageName 包名
	 */
	public static void openAppInfo(Context context, String packageName) {
		Intent intent = new Intent();
		intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
		intent.setData(Uri.parse("package:" + packageName));
		context.startActivity(intent);
	}

	/**
	 * 可用来做App信息分享
	 *
	 * @param context 上下文
	 * @param info    分享信息
	 */
	public static void shareAppInfo(Context context, String info) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, info);
		context.startActivity(intent);
	}

	/**
	 * 判断当前App处于前台还是后台
	 * <p>需添加权限 android.permission.GET_TASKS</p>
	 * <p>并且必须是系统应用该方法才有效</p>
	 *
	 * @param context 上下文
	 * @return true: 后台<br>false: 前台
	 */
	public static boolean isAppBackground(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		@SuppressWarnings("deprecation")
		List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}




	/**
	 * 根据包名检查应用程序是否在运行
	 * @param context
	 * @param PackageName
	 * @return
	 */
	public static  boolean isServiceStarted(Context context, String PackageName) throws Exception
	{
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
		boolean isAppRunning = false;
		for (ActivityManager.RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(PackageName) || info.baseActivity.getPackageName().equals(PackageName)) {
				isAppRunning = true;
				break;
			}
		}
		//Logger.msg(TAG, PackageName+"  是否正在运行："+isAppRunning);
		return isAppRunning;
	}

}
