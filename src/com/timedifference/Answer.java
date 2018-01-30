package com.timedifference;

import java.util.ArrayList;

import com.publicPackage.OperateFile;
import com.publicPackage.StuNumClassify;
import com.publicPackage.Time;

public class Answer {

	public Answer() {}
	/****
	 * @comments 获得答案日志里的数据，所有数据都存放一个list中
	 * @return 存放数据的list
	 */
	protected ArrayList<String[]> getAnswerLog() {
		OperateFile opfl = new OperateFile();
		ArrayList<String[]> arrResult = new ArrayList<String[]>();
		String filePath = "./src/data/log/";
        String fileName = "Answer-History-Log-2017-09-07.csv";
        String strFile = filePath+fileName;
        arrResult = opfl.readCsvFile(strFile);
        return arrResult;
	}
	
	/****
	 * @comments 将数据封装到类中
	 * @param TAG
	 * @param record
	 * @return 封装的时间信息
	 */
	protected PackageTime getRecord(int TAG,String[]record) {
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
		PackageTime aa = new PackageTime(TAG,SN,QN,TIME);
		return aa;
	}
	
	/****
	 * @comments 过滤Answer时间，得到5/5-6/18的Answer
	 * @param list
	 * @return 5/5-6/18的Answer的list
	 */
	protected ArrayList<String[]> filterAnswer(ArrayList<String[]>list){
		ArrayList<String[]> result = new ArrayList<String[]>();
		Time t = new Time();
		String lowerlimit = "2017-05-05 0:0:0";
		String upperlimit = "2017-06-18 23:59:59";
		long llower = t.timeTolong(lowerlimit);
		long lupper = t.timeTolong(upperlimit);
		for(int i=0;i<list.size();i++) {
			String[] eachlist = list.get(i);
			String time = t.parseAnswerDateTime(eachlist);
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
	 * @comments 获得与Activity相匹配的answer-Log
	 *           从ANS-log文件中读取与arrACT数据相同学生序号和题号相同的数据
	 *           将数据封装成PackTime类，然后添加数list中
	 * @param aat
	 * @param arrACT
	 * @param ANSArranage
	 */
	protected void getANSlogWithACT(ArrayList<PackageTime>aat,String[] arrACT,ArrayList<ArrayList<String[]>>ANSArranage) {
        String SNofACT = arrACT[0];                    //Activity里的学生序号
        int QNofACT = Integer.parseInt(arrACT[1]);     //Activity数组里题号
        for(int i=0;i<ANSArranage.size();i++) {       //ANSArranage.size()代表有多少学生
        	ArrayList<String[]> eachStudentANS = new ArrayList<String[]>();
        	eachStudentANS = ANSArranage.get(i);    //每位学生ANS记录
        	String SNofNote=eachStudentANS.get(0)[0];//获取ANS里学生序号 	
        	if(SNofNote.equals(SNofACT))               //若Activity里的学生序号与Answer学生序号匹配
        		for(int j=0;j<eachStudentANS.size();j++) {
        			String[] arrNote = eachStudentANS.get(j);
        			int QNofNote = Integer.parseInt(arrNote[1]);
        			if(QNofNote == QNofACT) {
        				PackageTime getNoteLog = getRecord(2,arrNote);
        				aat.add(getNoteLog);
        			}
        		}
        }
	}
}
