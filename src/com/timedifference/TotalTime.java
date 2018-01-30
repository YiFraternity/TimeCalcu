package com.timedifference;

import java.util.ArrayList;

import com.publicPackage.OperateFile;

public class TotalTime {
	
	public TotalTime() {}

	/***
	 * @comments 获得指定同学的总时间
	 * @param index 指定同学
	 * @param csvlist
	 * @return 指定同学及他总时间的数组
	 */
	public String[] getIndexArray(String index,ArrayList<String[]>csvlist){
		String[] eachstudent = new String[2];
		String sn="";
		double totalTime = 0;
		for(int i=0;i<csvlist.size();i++) {
			String[] eachlist = csvlist.get(i);
			sn = eachlist[0];
			if(index.equals(sn)) {
				double td = Double.parseDouble(eachlist[4]); //将时间差转化为double型
				totalTime = totalTime+td;
				csvlist.remove(eachlist); //减少查找次序
				i--;
			}
		}
		eachstudent[0]=index;eachstudent[1]=""+totalTime;
		return eachstudent;
	}
	
	public static void main(String[] args) {
		OperateFile opfl = new OperateFile();
		TotalTime tt = new TotalTime();
		String filepath = "./src/data/log/";
		String tdfile = "Answer-Activity-TimeDifference.csv";
		String sfile = "EachStudent.csv";
		String eachstudent = filepath+sfile;
		tdfile = filepath+tdfile;
		String[] header = {"学生序号","总时间"};
		opfl.clearFile(eachstudent);
		opfl.writeFileToCsv(header, eachstudent);;
		ArrayList<String[]>tdcsv = opfl.readCsvFile(tdfile);
		String[] contents=null;
		while(!tdcsv.isEmpty()){
			String eachsn = tdcsv.get(0)[0];
			contents = tt.getIndexArray(eachsn, tdcsv);
			opfl.writeFileToCsv(contents, eachstudent);
		}
	}
}
