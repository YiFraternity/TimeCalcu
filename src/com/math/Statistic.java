package com.math;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Statistic {

	public Statistic(){}
	
	/****
	 * @comments 计算一个list中的平均值
	 * @param list
	 * @return 返回不高于两位小数的平均值
	 */
	public String getAverage(ArrayList<Long> list) {
		DecimalFormat df= new DecimalFormat("#.##");
		long sum = 0;
		for(int i =0;i<list.size();i++) {
			sum = sum + list.get(i);
		}
		double d= (double)sum/(double)list.size();
		String avg = df.format(d);
		System.out.println("平均值为："+avg);
		return avg;
	}
	
	/****
	 * @comments 求一组数的中位数
	 * @param list
	 * @return 返回不高于两位小数的中位数
	 */
	public String getMedian(ArrayList<Long> list) {
		DecimalFormat df = new DecimalFormat("#.##");
		double d = 0;
		long[] l = new long[list.size()];
		int t=0;
		while(!list.isEmpty()){
			long min = list.get(0);
			for(int i=1;i<list.size();i++) {
				if(min>list.get(i))
					min=list.get(i);
			}
			l[t]=min;
			list.remove(min);
			t++;
		}
		if(t%2==0) {
			d=l[(t/2)-1]+l[t/2];
			d=d/2;
		}else
			d = l[t/2];
		String med = df.format(d);
		System.out.println("中位数："+med);
		return med;
	}
	/****
	 * @comments 求list的方差
	 * @param list
	 * @return 不超过两位小数的方差
	 */
	public String getVariance(ArrayList<Long>list) {
		DecimalFormat df = new DecimalFormat("#.##");
		String strVar = "";
		double sum = 0;
		for(int i=0;i<list.size();i++) {
			sum = sum + list.get(i);
		}
		double avg = sum/(double)list.size();
		double var = 0;
		int listSize = list.size();
		for(int i=0;i<listSize;i++) {
			var = var+(list.get(i)-avg)*(list.get(i)-avg);
		}
		var = var/listSize;
		var = Math.sqrt(var);
		strVar = df.format(var);
		System.out.println("方差为:"+strVar);
		return strVar;
	}
	/***
	 * @comments test用例
	 * @param args
	 */
	public static void main(String[] args) {
		Statistic statistic = new Statistic();
		long[] test = {50,60,100,58,68,89,90};
		ArrayList<Long>list = new ArrayList<Long>();
		for(int i=0;i<test.length;i++) {
			list.add(test[i]);
		}
		statistic.getAverage(list);
		//由于在求中位数时，把list清空，为了方便，先求方差
		statistic.getVariance(list);
		statistic.getMedian(list);
	}
}
