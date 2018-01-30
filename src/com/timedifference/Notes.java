package com.timedifference;

import java.util.ArrayList;

import com.publicPackage.OperateFile;
import com.publicPackage.StuNumClassify;
import com.publicPackage.Time;

public class Notes {

	public Notes() {}
	/****
	 * @comments 获得答案日志里的数据，所有数据都存放一个list中
	 * @return 存放数据的list
	 */
	protected ArrayList<String[]> getNotesLog() {
		OperateFile opfl = new OperateFile();
		ArrayList<String[]> arrResult = new ArrayList<String[]>();
		String filePath = "./src/data/log/";
        String fileName = "Student-Notes-2017-09-11.csv";
        String strFile = filePath+fileName;
        arrResult = opfl.readCsvFile(strFile);
        return arrResult;
	}
	/****
	 * @comments 获取Activity日志里的数据，将数据存放在list中，返回
	 * @return 存放Activity日志的list
	 */
	protected ArrayList<String[]> getActivitlog(){
        OperateFile opfl = new OperateFile();
		ArrayList<String[]> arrResult = new ArrayList<String[]>();
		String filePath = "./src/data/log/";
        String fileName =  "Activity-Log-2017-09-07.csv";
        String strFile = filePath+fileName;
        arrResult = opfl.readCsvFile(strFile);
        return arrResult;
	}
	
	/****
	 * @comments 过滤Activity时间，得到5/5-6/18的Activity
	 * @param list
	 * @return 5/5-6/18的Activity的list
	 */
	protected ArrayList<String[]> filterActivity(ArrayList<String[]>list){
		ArrayList<String[]> result = new ArrayList<String[]>();
		Time t = new Time();
		String lowerlimit = "2017-05-05 0:0:0";
		String upperlimit = "2017-06-18 23:59:59";
		long llower = t.timeTolong(lowerlimit);
		long lupper = t.timeTolong(upperlimit);
		for(int i=0;i<list.size();i++) {
			String[] eachlist = list.get(i);
			String time = t.parseActivityDateTime(eachlist);
			long ltime = t.timeTolong(time);
			if((ltime>=llower)&&(ltime<=lupper)) {
				result.add(eachlist);
			}
		}
		list=null;
		t=null;
		return result;
	}
	/****
	 * @comments 过滤Notes时间，得到5/5-6/18的Notes
	 * @param list
	 * @return 5/5-6/18的Notes的list
	 */
	protected ArrayList<String[]> filterNotes(ArrayList<String[]>list){
		ArrayList<String[]> result = new ArrayList<String[]>();
		Time t = new Time();
		String lowerlimit = "2017-05-05 0:0:0";
		String upperlimit = "2017-06-18 23:59:59";
		long llower = t.timeTolong(lowerlimit);
		long lupper = t.timeTolong(upperlimit);
		for(int i=0;i<list.size();i++) {
			String[] eachlist = list.get(i);
			String time = t.parseNotesDateTime(eachlist);
			long ltime = t.timeTolong(time);
			if((ltime>=llower)&&(ltime<=lupper)) {
				result.add(eachlist);
			}
		}
		list=null;
		t=null;
		return result;
	}
	/****
	 * @comments 根据时间先后，对其进行排序
	 * @param aat
	 * @return 顺序从小到大一次排列
	 */
	protected GetAATime[] timeSort(ArrayList<GetAATime> aat) {
		Time timeObject = new Time();
		int length = aat.size();
		GetAATime[] aaResult = new GetAATime[length];
		int i=0;
		while(aat.size()>0&&i<length) {
			GetAATime minAA = aat.get(0);
			String strTime = minAA.getTime();     //将动态数组的第一个作为最小值
			long minTime = timeObject.timeTolong(strTime);
			for(int j=1;j<aat.size();j++) {
				GetAATime aa = aat.get(j);
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
			TIME = timeObject.parseNotesDateTime(record);
		}
		GetAATime aa = new GetAATime(TAG,SN,QN,TIME);
		return aa;
	}	
	
	/****
	 * @comments 获得与答案相匹配的ActivityLog
	 *           从Activity-Log文件中读取与arrANS数据相同学生序号和题号相同的数据
	 *           将数据封装成GetAATime类，然后添加数list中
	 * @param aat
	 * @param arrANS
	 */
	protected void getACTLogWithANS(ArrayList<GetAATime>aat,String[] arrANS,ArrayList<ArrayList<String[]>> ACTarrange) {
        String SNofANS = arrANS[0];                  //答案里的学生序号
        int QNofANS = Integer.parseInt(arrANS[1]);   //答案数组里题号
        for(int i=0;i<ACTarrange.size();i++) {       //ACTarrange.size()代表有多少学生
        	ArrayList<String[]> arrEachSTUACT = new ArrayList<String[]>();
        	arrEachSTUACT = ACTarrange.get(i);       //每位学生Activity记录
        	String SNofACT=arrEachSTUACT.get(0)[0];  //获取Activity里学生序号 	
        	if((SNofACT.equals(SNofANS))) {          //若答案里的学生序号与活动学生序号匹配
        		for(int j=0;j<arrEachSTUACT.size();j++) {
        			String[] arrACT = arrEachSTUACT.get(j);
        			int QNofACT = Integer.parseInt(arrACT[1]);
        			if(QNofANS == QNofACT) {
        				GetAATime getACTLog = getRecord(0,arrACT);
        				aat.add(getACTLog);
        			}
        		}
        	}
        }
	}
	
	/****
	 * @comments 从Notes-History文件中找到与aarAns数据相同的序号和题目
	 *           将这些数据封装成GetAATime对象，添加到aat中
	 * @param aat <存放GetAATime对象>
	 * @param arrANS <答案数据的数组>
	 * @param csvlist <存放CSV文件中的数据>
	 */
	protected void getANSlist(ArrayList<GetAATime>aat,String[] arrANS,ArrayList<String[]>csvlist) {
		StuNumClassify snc = new StuNumClassify();
        ArrayList<String[]>Notes = new ArrayList<String[]>();
        Notes = snc.getSQClassfiy(csvlist, arrANS);
		for(int i=0;i<Notes.size();i++) {
			String[] eachNotes = Notes.get(i);
			GetAATime answerAA = getRecord(1,eachNotes);
			aat.add(answerAA);
		}
	}
	
	/***
	 * @comments tag代表此时是Notes时间还是Activity时间
	 *           TAG=0 //Activity时间
	 *           TAG=1 //Notes时间
	 *           如果tag发生变化，从0->1(1->0)，则将变化之前加入
	 *           判断最后一个是否为Activity时间，若为Activity时间，则不加入；若为Notes时间，则加入
	 * @example A={0,0,0,1,0,0,1,1,1,0}
	 *          则取A[2],A[3],A[5],A[8]
	 *          
	 * @param allTime
	 * @return 存放需要的Activity时间和Notes时间的动态数组
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
			resultTime.add(allTime[i-1]);//若最后一个是Notes时间，则加入
		}
		return resultTime;
	}
}
