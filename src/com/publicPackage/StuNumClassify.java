package com.publicPackage;

import java.util.ArrayList;

public class StuNumClassify {
	public StuNumClassify() {}
	
	/***
	 * @comments 判读字符串是否在数组中
	 * @param arr
	 * @param str
	 * @return true or false
	 */
	private boolean isExistArray(ArrayList<String>arr,String str) {
		boolean tag =false;
		for(int i = 0;i<arr.size();i++) {
			String strEach = arr.get(i);
			if(str.equals(strEach)) {
				tag=true;
			}
		}
		return tag;
	}
	
	/***
	 * @comments 读取学生的序号，将不同学生序号放入一个list返回
	 * @param csvlist <存放所有数据>
	 * @return 不同学生的序号
	 */
	private ArrayList<String> getStudentNumber(ArrayList<String[]>csvlist){
		ArrayList<String> arrNumber = new ArrayList<String>();
		arrNumber.add(csvlist.get(0)[0]);
		for(int i=1;i<csvlist.size();i++) {
			String[] arrString = csvlist.get(i);
			String strNumber = arrString[0];   //get Student Number
			if(!isExistArray(arrNumber,strNumber)) {
				arrNumber.add(strNumber);
			}
		}
		csvlist = null;
		return arrNumber;
	}
	
	/***
	 * @comments 将题号从小到大以此排序，返回排序后的题号
	 * @param arrList
	 * @return
	 */
	private ArrayList<String[]> titleSort(ArrayList<String[]>arrList) {
		ArrayList<String[]> arrResult = new ArrayList<String[]>();
		while(arrList.size()>0)
		{
			ArrayList<Integer> arrTitle = new ArrayList<Integer>();
			for(int i=0;i<arrList.size();i++) {
				String[] arrEach = arrList.get(i);
				String strTitle = arrEach[1];
				int title = Integer.parseInt(strTitle);
				arrTitle.add(title);
			}
			//由于ArrayList无法赋值，因此重新构造一个ArrayList,将最小的依次写入新的数组
			int temp=0;
			for (int i = 1; i < arrTitle.size(); i++) {
				if(arrTitle.get(i)<arrTitle.get(temp)) {
					temp = i;
				}
			}
			String[] arrTemp = arrList.get(temp);
			arrResult.add(arrTemp);
			arrList.remove(arrList.get(temp));
			arrTitle = null;
		}
		arrList = null;
		return arrResult;
	}
	
	/****
	 * @comment 将学生按序号将文件读取到的内容归类
	 * @result 学生序号从小到大，在学生序号内部，题目从小到大排列
	 * @param csvlist <存放所有数据>
	 * @return 存放排序后一组学生信息的list
	 */
	public ArrayList<ArrayList<String[]>> getStudentClassfiy(ArrayList<String[]>csvlist){
		ArrayList<ArrayList<String[]>> arrayResult = new ArrayList<ArrayList<String[]>>();
		ArrayList<String> arrNumber = new ArrayList<String>();  //存放所有学生的序号
		arrNumber = getStudentNumber(csvlist); 
		for(int i=0;i<arrNumber.size();i++) {
			ArrayList<String[]> arrcsv = new ArrayList<String[]>();
			String number = arrNumber.get(i);
			for(int j=0;j<csvlist.size();j++) {
				String[] strCsv = csvlist.get(j);
				if(strCsv[0].equals(number)) {
					arrcsv.add(strCsv);
				}
			}
			arrcsv=titleSort(arrcsv);
			arrayResult.add(arrcsv);
			arrcsv =null;
		}
		return arrayResult;
	}
	
	/****
	 * @comments 将学生和问题进行归类，同一个学生且同一道题目则放到一个list，将这list返回
	 * @param csvlist <存放CSV数据>
	 * @param arrSource <需要查找源数据的数组>
	 * @return 学生和问题归类后的结果
	 */
	public ArrayList<String[]> getSQClassfiy(ArrayList<String[]>csvlist,String[] arrSource){
		ArrayList<String[]> SQlist = new ArrayList<String[]>();  //用来存放归类后的结果
		String SN = arrSource[0];
		int QN = Integer.parseInt(arrSource[1]);
		for(int i=0;i<csvlist.size();i++) {
			String[] eachCsvList = csvlist.get(i);
			String csvSN = eachCsvList[0]; //CSV文件中数据的学生序号
			int csvQN = Integer.parseInt(eachCsvList[1]);//CSV文件中数据的题号 
			if((csvSN.equals(SN))&&(csvQN==QN)) {
				SQlist.add(eachCsvList);
				csvlist.remove(eachCsvList);
				i--;
			}
		}
		return SQlist;
	}
	
	/****
	 * @comments 用来将学生进行按list分类
	 * @param csvlist<存放所有数据>
	 * @return  返回一组学生list
	 */
	public ArrayList<ArrayList<String[]>> getSNClassfiy(ArrayList<String[]>csvlist){
		ArrayList<ArrayList<String[]>> arrayResult = new ArrayList<ArrayList<String[]>>();
		ArrayList<String> arrNumber = new ArrayList<String>();
		arrNumber = getStudentNumber(csvlist);
		for(int i=0;i<arrNumber.size();i++) {
			ArrayList<String[]> arrcsv = new ArrayList<String[]>();
			String number = arrNumber.get(i);
			for(int j=0;j<csvlist.size();j++) {
				String[] strCsv = csvlist.get(j);
				if(strCsv[0].equals(number)) {
					arrcsv.add(strCsv);
				}
			}
			arrayResult.add(arrcsv);
			arrcsv =null;
		}
		return arrayResult;
	}
	public static void main(String[] args) {
		StuNumClassify snc = new StuNumClassify();
		OperateFile opfl = new OperateFile();
		ArrayList<ArrayList<String[]>> arrResult = new ArrayList<ArrayList<String[]>>();
		String filePath = "./src/data/log/";
        String fileName = "Activity-Log-2017-09-07.csv";
        String writeFileName = "Activity-Log-2017-09-07.txt";
        String strWriteFile = filePath+writeFileName;
        opfl.clearFile(strWriteFile);
        String strFile = filePath+fileName;
        ArrayList<String[]>csvlist=opfl.readCsvFile(strFile);
        arrResult=snc.getSNClassfiy(csvlist);
        for(int i=0;i<arrResult.size();i++) {
        	ArrayList<String[]> arr = new ArrayList<String[]>();
        	arr = arrResult.get(i);
        	for(int j = 0;j<arr.size();j++) {
        		String[] strResult = arr.get(j);
        		for(int t=0;t<strResult.length;t++)
        		{
        			System.out.print(strResult[t]+" ");
        			opfl.writeToFile(strResult[t]+" ", strWriteFile);
        		}
        		System.out.println();
        		
    			opfl.writeToFile("\n", strWriteFile);
        	}
        }
        
	}
	
}
