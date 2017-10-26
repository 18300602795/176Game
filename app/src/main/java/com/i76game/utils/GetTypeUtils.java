package com.i76game.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/5/10.
 */

public class GetTypeUtils {

        public static String[] getType(String typeStr) {
            String[] typeArr={"","角色","格斗","休闲", "竞速","策略","射击", "其它"};
            String[] typeIndexs = typeStr.split(",");
            String[] returnStr=new String[3];
            try {
                for(int i=0;i<typeIndexs.length;i++){
                    int index=Integer.parseInt(typeIndexs[i]);
                    returnStr[i]=typeArr[index];

//                textContent.append(typeArr[index]);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            if(TextUtils.isEmpty(returnStr[0])){
                returnStr[0]="其它";
            }
            return returnStr;
        }

    public static String getContentType(String typeStr) {
        String[] typeArr={"","角色","格斗","休闲", "竞速","策略","射击", "其它"};
        String[] typeIndexs = typeStr.split(",");


        StringBuilder sb=new StringBuilder();
        try {
            for(int i=0;i<typeIndexs.length;i++){
                int index=Integer.parseInt(typeIndexs[i]);
                if (i==1){
                    sb.append(",");
                }
                sb.append(typeArr[index]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if(TextUtils.isEmpty(sb)){
            sb.append(typeArr[7]);
        }
        return sb.toString();
    }

    //返回数组，下标1代表大小，下标2代表单位 KB/MB
    public static String getFormatSize(double size){
        String str="";
        if(size>=1024){
            str="KB";
            size/=1024;
            if(size>=1024){
                str="MB";
                size/=1024;
            }
        }
        DecimalFormat formatter=new DecimalFormat();
        formatter.setGroupingSize(3);
        return (formatter.format(size)+str);
    }
}
