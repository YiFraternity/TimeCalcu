package com.timedifference;

import java.util.ArrayList;

import com.publicPackage.OperateFile;
import com.publicPackage.Time;

public class Activity {

	public Activity() {}
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
	 * @comments 将数据封装到类中
	 * @param TAG
	 * @param record
	 * @return 封装的时间信息
	 */
	private PackageTime getRecord(int TAG,String[]record) {
		Time timeObject = new Time();
		String SN = record[0];
		int QN = Integer.parseInt(record[1]);
		String TIME=null;
		TIME = timeObject.parseActivityDateTime(record);
		PackageTime aa = new PackageTime(TAG,SN,QN,TIME);
		return aa;
	}
	
	/****
	 * @comments 从Activity-History文件中找到与aarAns数据相同的序号和题目
	 *           将这些数据封装成GetAATime对象，添加到aat中
	 * @param aat <存放GetAATime对象>
	 * @param arrACT <答案数据的数组>
	 * @param csvlist <存放CSV文件中的数据>
	 */
	protected void getACTlist(ArrayList<PackageTime>aat,String[]arrACT,ArrayList<String[]>csvlist) {
		String SN = arrACT[0];
		int QN = Integer.parseInt(arrACT[1]);
		for(int i=0;i<csvlist.size();i++) {
			String[] eachlist = csvlist.get(i);
			String snlist = eachlist[0];
			int qnlist = Integer.parseInt(eachlist[1]);
			if(SN.equals(snlist)&&QN==qnlist) {
				PackageTime pt = getRecord(0,eachlist);
				aat.add(pt);
				csvlist.remove(eachlist);
				i--;
			}
		}
	}
}
