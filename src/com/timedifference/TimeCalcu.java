package com.timedifference;

import java.util.ArrayList;

import com.publicPackage.OperateFile;
import com.publicPackage.StuNumClassify;
import com.publicPackage.Time;
import com.math.Statistic;

public class TimeCalcu {
	
	public TimeCalcu() {}
	
	/****
	 * @comments 将数据封装到类中
	 * @param TAG
	 * @param record
	 * @return 封装的时间信息
	 */
	protected GetAATime getRecord(int TAG,String[]record) {
		Time timeObject = new Time();
		String SN = record[0];
		int QN = Integer.parseInt(record[1]);
		String TIME=null;
		if(TAG == 0) {
			TIME = timeObject.parseActivityDateTime(record);
		}else if(TAG == 1)
		{
			TIME = timeObject.parseAnswerDateTime(record);
		}
		GetAATime aa = new GetAATime(TAG,SN,QN,TIME);
		return aa;
	}
	
	/***
	 * @comments 求指定列的平均值，中位数，方差
	 */
	private static void getAMV(int index, String file) {
		OperateFile opfl = new OperateFile();
		Statistic statistic = new Statistic();
		String amvfile = "./src/data/log/AMV.csv";
		String[] header = {"平均值","中位数","方差"};    //先写入标题头
		String[] content = new String[3];
		opfl.writeFileToCsv(header, amvfile);
		ArrayList<Long>list = opfl.readIndexColumn(index, file);
		String avg = statistic.getAverage(list);  //求平均值
		String var = statistic.getVariance(list); //求方差
		String med = statistic.getMedian(list);   //求中位数
		content[0]=avg;content[1]=med;content[2]=var;
		opfl.writeFileToCsv(content, amvfile);
	}
	public static void main(String[] args) {
		Answer atc = new Answer();
		StuNumClassify snc = new StuNumClassify();
		Time timeObject = new Time();
		OperateFile opfl = new OperateFile();
		
		ArrayList<String[]>answerlist = atc.getAnswerLog();//读取所有Answer
		answerlist=atc.filterAnswer(answerlist);           //获取过滤后Answer的list
		ArrayList<String[]>activitylist = atc.getActivitlog();//读取所有的Activity
		activitylist=atc.filterActivity(activitylist);     //获取过滤后Activity的list
		
		ArrayList<ArrayList<String[]>> actArrange = snc.getSNClassfiy(activitylist); //对activity进行整理
		
		String filename = "./src/data/log/Answer-Activity-TimeDifference.csv";  //将要写入结果的文件
		opfl.clearFile(filename);
		String[] header = {"学生序号","题号","Activity时间","Answer时间","时间差"};    //先写入标题头
		opfl.writeFileToCsv(header, filename);
		
		while(!answerlist.isEmpty()) {
			ArrayList<GetAATime> aatlist = new ArrayList<GetAATime>();
			ArrayList<Long> list = new ArrayList<Long>();
			String[] arrayAnswer = answerlist.get(0);                //得到学生序号
			atc.getANSlist(aatlist, arrayAnswer, answerlist);        //在Activity list中找到和学生序号相同的数据，封装到aatlist中
			atc.getACTLogWithANS(aatlist, arrayAnswer,actArrange);
			GetAATime[] aatlistsort = atc.timeSort(aatlist);         //将aatlist按照时间先后进行排序
			ArrayList<GetAATime> aattime = atc.getTimeofNeedCalcu(aatlistsort);//读取变化的时间点
			String[] content=new String[5];
			int QN=0; String SN="";
			for(int j=0;j<aattime.size();j=j+2) {
				GetAATime activityAAT= aattime.get(j);
				GetAATime answerAAT = aattime.get(j+1);
				QN = activityAAT.getQN();
				SN = activityAAT.getSN();
				String actTIME = activityAAT.getTime();
				String ansTIME = answerAAT.getTime();
				long timeDifference = timeObject.getTimeDifference(ansTIME, actTIME);
				String result="";
				if(timeDifference<1283)
				{
					list.add(timeDifference);
					result = SN +","+ QN +","+actTIME+","+ansTIME+","+timeDifference;
					System.out.println(result);
					content[0]=SN;content[1]=""+QN;content[2]=actTIME;content[3]=ansTIME;content[4]=""+timeDifference;
					opfl.writeFileToCsv(content, filename);
				}
			}
			aatlist = null;
		}
		getAMV(4, filename);
	}
	
	/***
	 * @comments tag代表此时是Answer时间还是Activity时间
	 *           TAG=0 //Activity时间
	 *           TAG=1 //Answer时间
	 *           如果tag发生变化，从0->1(1->0)，则将变化之前加入
	 *           判断最后一个是否为Activity时间，若为Activity时间，则不加入；若为Answer时间，则加入
	 * @example A={0,0,0,1,0,0,1,1,1,0}
	 *          则取A[2],A[3],A[5],A[8]
	 *          
	 * @param allTime
	 * @return 存放需要的Activity时间和Answer时间的动态数组
	 */
	protected ArrayList<GetAATime> getTimeofNeedCalcu(GetAATime[] allTime) {
		ArrayList<GetAATime> resultTime = new ArrayList<GetAATime>();
		int i=1;
		for(;i<allTime.length;i++) {
			GetAATime nowaatime = allTime[i];
			GetAATime preaatime = allTime[i-1];
			int nowTag = nowaatime.getTag();
			int preTag = preaatime.getTag();
			if(nowTag!=preTag) {
				resultTime.add(preaatime);
			}else {
				continue;
			}
		}
		if(allTime[i-1].getTag() == 1)
		{
			resultTime.add(allTime[i-1]);//若最后一个是Answer时间，则加入
		}
		return resultTime;
	}
}
