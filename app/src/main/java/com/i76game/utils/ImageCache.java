package com.i76game.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * author janecer
 * 2014-3-17下午2:53:51
 */
public class ImageCache {
   /**
    * 将bitmap写入到sdcard
    * @param imgurl
    * @param ctx
    */
   public static void write2sdcard(String imgurl,Bitmap bm,Context ctx){
	   if(TextUtils.isEmpty(imgurl)||null==bm){
		   return;
	   }
	   String imgpath=Environment.getExternalStorageDirectory().getPath()+File.separator+ctx.getPackageName()+File.separator+"image"+File.separator+Md5Util.md5(imgurl);
	   if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
		   File file=new File(imgpath);
		   File fileParent=file.getParentFile();
		   if(!fileParent.exists()){
			   fileParent.mkdirs();
		   }
		   FileOutputStream fos=null;
		   try {
			  file.setLastModified(System.currentTimeMillis());
			  fos=new FileOutputStream(file);
			  bm.compress(CompressFormat.PNG,100,fos);
    		} catch (Exception e) {
			  e.printStackTrace();
	     	} finally{
				try {
					if(null!=fos){
					  fos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	   }
   }

   /**
    * 将图片写到sdcard里面 然后再返回路径
    * @param bm
    * @param ctx
    * @return
    */
   public static String write2sdcard(Bitmap bm,Context ctx){
	   if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
		   return "";
	   }
	   //写入sdcard的路径
	   String imgpath=Environment.getExternalStorageDirectory().getPath()+File.separator+ctx.getPackageName()+
			   File.separator+"image"+File.separator+System.currentTimeMillis();
	   
	   ByteArrayOutputStream baos=new ByteArrayOutputStream();
	   bm.compress(CompressFormat.PNG, 100, baos);
	   
	   FileOutputStream fos=null;
	   File file=new File(imgpath);
	   try{
		   fos=new FileOutputStream(file);
		   fos.write(baos.toByteArray());
		   fos.flush();
		   fos.close();
		   return file.getPath();//将图片的路径返回出去
	   }catch(IOException e){
		   e.printStackTrace();
	   }finally{
		   if(null!=fos){
			   try {
				fos.close();
			   } catch (IOException e) {
				e.printStackTrace();
			   }
		   }if(null!=baos){
			   try{
				   baos.close();
			   }catch(IOException e){
				   e.printStackTrace();
			   }
		   }
	   }
	   return "";
   }
   
   /**
    * 根据路径删除文件
    * @param path
    * @return
    */
   public static boolean deleteFile(String path){
	   if(TextUtils.isEmpty(path)){
		   return false;
	   }
	   File file=new File(path);
	   if(file.exists()){
		   return file.delete();
	   }
	   return false;
   }


   public interface ImageCallBack{
	   /**
	    * 从网络成功获取图片的回调
	    * @param bm
	    */
	   void LoadImageFromIntenet(Bitmap bm);
	   
	   /**
	    * 加载图片默认实现的图片
	    * @param bm
	    */
	   void LoadImageBefore(Bitmap bm);
   }




	/**
	 * 对图片进行等比例缩放
	 */

	public static void scaleLoad(View v, int imageView, Context context ) {
		// 得到图片的宽和高
		BitmapFactory.Options opts = new BitmapFactory.Options(); opts.inJustDecodeBounds = true;
		// 加载器不加载图片, 而是把图片 的 out(宽和高)的字段信息取出来
		Bitmap bit = BitmapFactory.decodeResource(context.getResources(), imageView, opts);

		int imageWidth = opts.outWidth;
		int imageHeight = opts.outHeight;
		System.out.println("图片的宽和高: " + imageWidth + " * " + imageHeight);
		// 得到屏幕的宽和高
		int screenWidth = DimensionUtil.getWidth(context);
		int screenHeight = DimensionUtil.getHeight(context);
		System.out.println("屏幕的宽和高: " + screenWidth + " * " + screenHeight);
		// 计算缩放比例
		int widthScale = imageWidth / screenWidth;
		int heightScale = imageHeight / screenHeight;
		System.out.println("==========="+heightScale);
		int scale = widthScale > heightScale ? widthScale : heightScale;
		System.out.println("缩放比例为: " + scale);
		// 使用缩放比例进行缩放加载图片
		opts.inJustDecodeBounds = false;
		// 加载器就会返回图片了 opts.inSampleSize = scale;
		bit=BitmapFactory.decodeResource(context.getResources(), imageView, opts);
		// 显示在屏幕上
		((ImageView) v).setImageBitmap(bit);
	}
}
