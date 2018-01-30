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
	 * @comments 从Answer-History文件中找到与aarAns数据相同的序号和题目
	 *           将这些数据封装成GetAATime对象，添加到aat中
	 * @param aat <存放GetAATime对象>
	 * @param arrANS <答案数据的数组>
	 * @param csvlist <存放CSV文件中的数据>
	 */
	protected void getANSlist(ArrayList<GetAATime>aat,String[] arrANS,ArrayList<String[]>csvlist) {
		StuNumClassify snc = new StuNumClassify();
        ArrayList<String[]>Answer = new ArrayList<String[]>();
        Answer = snc.getSQClassfiy(csvlist, arrANS);
		for(int i=0;i<Answer.size();i++) {
			String[] eachAnswer = Answer.get(i);
			GetAATime answerAA = getRecord(1,eachAnswer);
			aat.add(answerAA);
		}
	}
	
	
}
