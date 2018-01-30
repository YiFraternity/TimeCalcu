package com.timedifference;

import java.util.ArrayList;

import com.publicPackage.OperateFile;
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
			TIME = timeObject.parseNotesDateTime(record);
		}
		PackageTime aa = new PackageTime(TAG,SN,QN,TIME);
		return aa;
	}	
	
	/****
	 * @comments 获得与Activity相匹配的NotesLog
	 *           从Notes-log文件中读取与arrACT数据相同学生序号和题号相同的数据
	 *           将数据封装成PackTime类，然后添加数list中
	 * @param aat
	 * @param arrACT
	 * @param noteArranage
	 */
	protected void getNotelogWithACT(ArrayList<PackageTime>aat,String[] arrACT,ArrayList<ArrayList<String[]>>noteArranage) {
        String SNofACT = arrACT[0];                    //Activity里的学生序号
        int QNofACT = Integer.parseInt(arrACT[1]);     //Activity数组里题号
        for(int i=0;i<noteArranage.size();i++) {       //noteArranage.size()代表有多少学生
        	ArrayList<String[]> eachStudentNotes = new ArrayList<String[]>();
        	eachStudentNotes = noteArranage.get(i);    //每位学生Notes记录
        	String SNofNote=eachStudentNotes.get(0)[0];//获取Notes里学生序号 	
        	if(SNofNote.equals(SNofACT))               //若Activity里的学生序号与notes学生序号匹配
        		for(int j=0;j<eachStudentNotes.size();j++) {
        			String[] arrNote = eachStudentNotes.get(j);
        			int QNofNote = Integer.parseInt(arrNote[1]);
        			if(QNofNote == QNofACT) {
        				PackageTime getNoteLog = getRecord(2,arrNote);
        				aat.add(getNoteLog);
        			}
        		}
        }
	}
	
}
