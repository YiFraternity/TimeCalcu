package com.timedifference;

import java.util.ArrayList;

import com.publicPackage.OperateFile;
import com.publicPackage.Time;

public class Activity {

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
}
