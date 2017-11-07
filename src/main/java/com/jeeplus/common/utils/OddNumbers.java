package com.jeeplus.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 单号生成
 * @author hxting
 *
 */
public class OddNumbers {
	private static long orderNum = 0l;  
    private static String date ; 
    
    /** 
     * 生成单号编号 
     * @return 
     */  
    public static synchronized String getOrderNo() {  
        String str = new SimpleDateFormat("yyMMddHHmm").format(new Date());  
        if(date==null||!date.equals(str)){  
            date = str;  
            orderNum  = 0l;  
        }  
        orderNum ++;  
        long orderNo = Long.parseLong((date)) * 10000;  
        orderNo += orderNum;;  
        return orderNo+"";  
    }  
	

}
