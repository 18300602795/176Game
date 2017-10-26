package com.i76game.download;

/**
 * author janecer
 * 2014年4月22日上午11:15:24
 * 
 * 执行下载的实体对象
 */
public class Download {

	public static final String TABLE_NAME="download";
	public static final String BM_URL="bm_url";
	public static final String CURRENT_SIZE="current_size";
	public static final String TOTAL_SIZE="total_size";
	public static final String SPEED="speed";
	public static final String BAOMING="baoming";
	public static final String ULR="url";
	public static final String ISDOWNCOMPLETE="isDownComplete";
	public static final String APKNAME="apkname";
	public static final String NAME="name";
	
	
	public String bm_url;//图片地址
	public String name;
	public float current_size;//当前下载的多少
	public float total_size;//待下载游戏总大小
	public String speed;//下载速度
	
	public String baoming;//游戏包名
	
	public String url;//游戏下载地址
	
	public String apkname;//下载文件的名字
	public boolean isDownComplete=false;//是否下载完成
	public boolean isPause=false;//是否是暂停状态
}
