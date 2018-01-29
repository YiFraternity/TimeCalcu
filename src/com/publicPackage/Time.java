package com.publicPackage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {
	
	public Time() {};

	/***
	 * @comments 解析时间，年、月、日,时、分、秒
	 * @param arrLog
	 * @return
	 */
	public String parseActivityDateTime(String[] arrLog) {
		String strDate = arrLog[5];   //日期格式为：2017/9/5
		strDate = strDate.replaceAll("/", "-");
		String strMoment = arrLog[6]; //时间格式：05:46:31.786237+00:00
		String strTime = strMoment.substring(0, 8);//去掉"."后面的内容
		String time = strDate+" "+strTime;
		return time;
	}

	/***
	 * @comments 获得Answer-History-Log的时间
	 * @param arrAnswer
	 * @return
	 */
	public String parseAnswerDateTime(String[] arrAnswer) {
		String strAnswer = arrAnswer[5];//时间格式：2017-03-09 06:14:08.786237+00:00
		String[] arrLog = strAnswer.split(" "); 
		String strDate = arrLog[0];
		String strMoment = arrLog[1]; 
		String strTime = strMoment.substring(0, 8);//去掉"."后面的内容
		String time = strDate+" "+strTime;
		return time;
	}
	/****
	 * @comments 将时间转化成long型，用于比较
	 * @param time
	 * @return 时间的long型
	 */
	public long timeTolong(String time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long ldate=0;
		try{
			Date date = df.parse(time);
			ldate = date.getTime();
		}catch(Exception e){
			e.printStackTrace();
		}
		return ldate;
	}
	
	/***
	 * @comments 将long型的时间转化为Sting型
	 * @param ltime
	 * @return String型的时间
	 */
	public String lTimeToString(long ltime) {
		String time = "";
		long s= ltime/1000;
        time=s+"秒";
        return time;
	}
	/*** 
     * @comments 计算两个时间的时间差 
     * @hint 格式日期格式，在此我用的是"2018-01-24 19:49:50"这种格式  
     *       可以更改为自己使用的格式，例如：yyyy/MM/dd HH:mm:ss 。。。  
     * @param strTime1 
     * @param strTime2 
     * @return 时间差(以秒为单位)
     */  
    public long getTimeDifference(String strTime1,String strTime2) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        long l=0;
           try{  
               Date now = df.parse(strTime1);  
               Date date=df.parse(strTime2);  
               l=now.getTime()-date.getTime(); //获取时间差  
               l=l/1000;                       //以秒为单位
           }catch(Exception e){  
               e.printStackTrace();  
           }
           return l;
    }
    
}
