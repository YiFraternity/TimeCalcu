package com.timedifference;

import java.util.ArrayList;

import com.publicPackage.OperateFile;
import com.publicPackage.StuNumClassify;
import com.publicPackage.Time;
import com.math.Statistic;

public class TimeCalcu {
	
	public TimeCalcu() {}
	
	/****
	 * @comments 根据时间先后，对其进行排序
	 * @param aat
	 * @return 顺序从小到大一次排列
	 */
	protected PackageTime[] timeSort(ArrayList<PackageTime> aat) {
		Time timeObject = new Time();
		int length = aat.size();
		PackageTime[] aaResult = new PackageTime[length];
		int i=0;
		while(aat.size()>0&&i<length) {
			PackageTime minAA = aat.get(0);
			String strTime = minAA.getTime();     //将动态数组的第一个作为最小值
			long minTime = timeObject.timeTolong(strTime);
			for(int j=1;j<aat.size();j++) {
				PackageTime aa = aat.get(j);
				String time = aa.getTime();
				long lTime = timeObject.timeTolong(time);
				if(minTime>lTime) {    //获得时间最小值
					minAA = aa;
					minTime=lTime;
				}
			}
			aaResult[i]=minAA;
			aat.remove(minAA);
			i++;
		}
		aat=null;
		return aaResult;
	}
	
	/***
	 * @comments tag代表此时是Answer时间，Activity时间还是Notes时间
	 *           TAG=0 //Activity时间
	 *           TAG=1 //Answer时间
	 *           TAG=2 //Notes时间
	 *           如果tag发生变化，从0->1(1->0)，则将变化之前加入
	 *           判断最后一个是否为Activity时间，若为Activity时间，则不加入；若为Answer时间，则加入
	 * @example A={0,0,0,1,2,0,0,1,1,1,2,0}
	 *          则取A[2],A[3],A[4],A[6],A[9],A[10]
	 *          
	 * @param allTime
	 * @return 存放需要的Activity时间和Answer时间的动态数组
	 */
	private ArrayList<PackageTime> getTimeofNeedCalcu(PackageTime[] allTime) {
		ArrayList<PackageTime> resultTime = new ArrayList<PackageTime>();
		int i=1;
		for(;i<allTime.length;i++) {
			PackageTime nowaatime = allTime[i];
			PackageTime preaatime = allTime[i-1];
			int nowTag = nowaatime.getTag();
			int preTag = preaatime.getTag();
			if(nowTag!=preTag) {
				resultTime.add(preaatime);
			}
		}
		if(allTime[i-1].getTag() != 0)
			resultTime.add(allTime[i-1]);//若最后一个是Answer时间，或者是Notes时间，则直接加入
		return resultTime;
	}
	
	private ArrayList<ArrayList<PackageTime>> getSegment(ArrayList<PackageTime>pt){
		ArrayList<ArrayList<PackageTime>> seg = new ArrayList<ArrayList<PackageTime>>();
		ArrayList<PackageTime> eachSeg = null;
		for(int i=0;i<pt.size();i++) {
			PackageTime eachpt =pt.get(i);
			int tag = eachpt.getTag();
			if(tag==0) {
				if(eachSeg!=null)
					seg.add(eachSeg);
				eachSeg = new ArrayList<PackageTime>();
				eachSeg.add(eachpt);
			}else {
				eachSeg.add(eachpt);
			}
		}
		if(eachSeg!=null)
			seg.add(eachSeg);
		return seg;
	}
	
	/***
	 * @comments 求指定列的平均值，中位数，标准差
	 */
	private static void getAMV(int index, String file) {
		OperateFile opfl = new OperateFile();
		Statistic statistic = new Statistic();
		String amvfile = "./src/data/log/AMV.csv";
		opfl.clearFile(amvfile);
		String[] header = {"平均值","中位数","标准差"};    //先写入标题头
		String[] content = new String[3];
		opfl.writeFileToCsv(header, amvfile);
		ArrayList<Long>list = opfl.readIndexColumn(index, file);
		String avg = statistic.getAverage(list);  //求平均值
		String var = statistic.getVariance(list); //求标准差
		String med = statistic.getMedian(list);   //求中位数
		content[0]=avg;content[1]=med;content[2]=var;
		opfl.writeFileToCsv(content, amvfile);
	}
	
	public static void main(String[] args) {
		TimeCalcu tc = new TimeCalcu();
		Answer ans = new Answer();
		Activity act = new Activity();
		Notes note = new Notes();
		StuNumClassify snc = new StuNumClassify();
		Time timeObject = new Time();
		OperateFile opfl = new OperateFile();
		
		/*****获取过滤后的Activity,Answer,Notes的list******/
		ArrayList<String[]>answerlist = ans.getAnswerLog();//读取所有Answer
		answerlist=ans.filterAnswer(answerlist);           //获取过滤后Answer的list
		ArrayList<String[]>activitylist = act.getActivitlog();//读取所有的Activity
		activitylist=act.filterActivity(activitylist);     //获取过滤后Activity的list
		ArrayList<String[]>noteslist = note.getNotesLog(); //读取所有的Notes
		noteslist=note.filterNotes(noteslist);             //获取过滤后Notes的list
		
		ArrayList<ArrayList<String[]>> ansArrange = new ArrayList<ArrayList<String[]>>();
		ansArrange=snc.getSNClassfiy(answerlist);          //对answer进行整理
		ArrayList<ArrayList<String[]>> noteArrange = new ArrayList<ArrayList<String[]>>();
		noteArrange=snc.getSNClassfiy(noteslist);          //对note进行整理
		
		String filename = "./src/data/log/TimeDifference.csv";//将要写入结果的文件
		opfl.clearFile(filename);
		String[] header = {"学生序号","题号","Activity时间","Answer时间","Notes时间","AA时间差","NA时间差","最终时间差"};//先写入标题头
		opfl.writeFileToCsv(header, filename);
		
		while(!activitylist.isEmpty()) {
			ArrayList<PackageTime> timelist = new ArrayList<PackageTime>();
			String[] actfirstlog = activitylist.get(0);                //得到第一条记录
			act.getACTlist(timelist, actfirstlog, activitylist);       //在Answer list中找到与此学生序号相同的数据，封装到timelist中
			ans.getANSlogWithACT(timelist, actfirstlog,ansArrange);    //在Activity list中找到与此学生序号相同的数据，封装到timelist中
			note.getNotelogWithACT(timelist, actfirstlog, noteArrange);//在Notes list中找到和此学生序号相同的数据，封装到timelist中
			PackageTime[] timelistsort = tc.timeSort(timelist);        //将timelist按照时间先后进行排序
			ArrayList<PackageTime> changetime = tc.getTimeofNeedCalcu(timelistsort);//读取变化的时间点
			ArrayList<ArrayList<PackageTime>>seg = tc.getSegment(changetime);//将其进行分段
			for(int i=0;i<seg.size();i++)
			{
				ArrayList<PackageTime>eachSeg = seg.get(i);   //每一段
				int QN=0; String SN="";
				SN = eachSeg.get(0).getSN();
				QN = eachSeg.get(0).getQN();
				String[] content=new String[8];
				int TAG;
				String thisActMoment = "";//此时的Activity时刻
				String thisAnsMoment = "";//此时的Answer时刻
				String thisNoteMoment= "";//此时的Note时刻
				long AATD=0;              //Answer与Activity时间差
				long NATD=0;              //Notes与Activity时间差
				long TD = 0;              //两者中较大时间差
				for(int j=0;j<eachSeg.size();j=j+1) {
					PackageTime thisTime= eachSeg.get(j);
					TAG= thisTime.getTag();
					switch(TAG)
					{
					case 0:
						thisActMoment = thisTime.getTime();
						break;
					case 1:
						thisAnsMoment = thisTime.getTime();
						AATD = timeObject.getTimeDifference(thisAnsMoment, thisActMoment);
						break;
					case 2:
						thisNoteMoment= thisTime.getTime();
						NATD = timeObject.getTimeDifference(thisNoteMoment, thisActMoment);
						break;
					default:
						System.out.println("ERROR");
						break;
					}
				}
				TD = NATD>AATD?NATD:AATD;
				String result="";
				if(TD>0&&AATD<1577)
				{
					//打印学生序号,题号,Activity时间,Answer时间,Notes时间,Answer和Activity时间差,Notes时间差,最终时间差
					result = SN +","+ QN +","+thisActMoment+","+thisAnsMoment+","+thisNoteMoment+","+AATD+","+NATD+","+TD;
					System.out.println(result);
					content[0]=SN;content[1]=""+QN;
					content[2]=thisActMoment;content[3]=thisAnsMoment;content[4]=thisNoteMoment;
					content[5]=String.valueOf(AATD);content[6]=String.valueOf(NATD);content[7]=String.valueOf(TD);
					opfl.writeFileToCsv(content, filename);
				}
				timelist = null;
			}
		}
		
		/*****求标准差，中位数，平均值*****/
		getAMV(8,filename);
	}
}
