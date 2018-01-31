package amendtimedifference;

import java.util.ArrayList;

import com.publicPackage.OperateFile;
import com.publicPackage.StuNumClassify;
import com.publicPackage.Time;

public class AmendTimeCalcu {
	
	public AmendTimeCalcu() {}

	/****
	 * @comments 根据时间先后，对其进行排序
	 * @param aat
	 * @return 顺序从小到大一次排列
	 */
	protected ArrayList<PackageData> timeSort(ArrayList<PackageData> aat) {
		Time timeObject = new Time();
		ArrayList<PackageData>aaResult = new ArrayList<PackageData>();
		while(aat.size()>0) {
			PackageData minAA = aat.get(0);
			String strTime = minAA.getTIME(); //将动态数组的第一个作为最小值
			long minTime = timeObject.timeTolong(strTime);
			for(int j=1;j<aat.size();j++) {
				PackageData aa = aat.get(j);
				String time = aa.getTIME();
				long lTime = timeObject.timeTolong(time);
				if(minTime>lTime) {    //获得时间最小值
					minAA = aa;
					minTime=lTime;
				}
			}
			aaResult.add(minAA);
			aat.remove(minAA);
		}
		aat=null;
		return aaResult;
	}
	
	/***
	 * @comments tag代表此时是Answer时间，Activity时间还是Notes时间
	 *           TAG=0 //Activity时间
	 *           TAG=1 //Answer时间
	 *           如果TAG有很多连续很多0，则只是保留最后一个
	 * @example A={0,0,0,1,1,0,0,1,1,1,1,0}
	 *          则取A[2],A[3],A[4],A[6],A[7],A[8],A[9],A[10]
	 *          
	 * @param allTime
	 * @return 存放需要的Activity时间和Answer时间的动态数组
	 */
	private void rmlogBasedTAG(ArrayList<PackageData> allTime) {
		int i=1;
		for(;i<allTime.size();i++) {
			PackageData nowaatime = allTime.get(i);
			PackageData preaatime = allTime.get(i-1);
			int nowTag = nowaatime.getTAG();
			int preTag = preaatime.getTAG();
			if(nowTag==0&&preTag==0) {
				allTime.remove(preaatime);
				i--;
			}
		}
		if(allTime.get(i-1).getTAG() == 0)
			allTime.remove(i-1);//若最后一个是Activity时间，直接删除
	}
	/****
	 * @comments 将此次做题，第一次答案正确以后的所有记录删除
	 *           若没有做到正确答案，则不删除
	 * @param alllog
	 */
	private void rmlogBasedFLAG(ArrayList<PackageData>alllog) {
		int i=0;
		while(i<alllog.size()&&alllog.get(i).getFLAG()!=1) {
			i++;
		}
		if(alllog.get(i-1).getFLAG()==1)
		{
			i++;
			for(;i<alllog.size();i++) {
				alllog.remove(i);
				i--;
			}
		}
	}
	/*****
	 * @comments 将数据分段
	 * @example {0 1 0 1 0 1 1 0 1 1 1 0 1}
	 *        ->{{0 1},{0 1},{0 1 1},{0 1 1 1},{0 1}}
	 * @param pt
	 * @return 分段后动态数组
	 */
	private ArrayList<ArrayList<PackageData>> getSegment(ArrayList<PackageData>pt){
		ArrayList<ArrayList<PackageData>> seg = new ArrayList<ArrayList<PackageData>>();
		ArrayList<PackageData> eachSeg = null;
		for(int i=0;i<pt.size();i++) {
			PackageData eachpt =pt.get(i);
			int tag = eachpt.getTAG();
			if(tag==0) {
				if(eachSeg!=null)  //主要是第一个为空
					seg.add(eachSeg);
				eachSeg = new ArrayList<PackageData>();
				eachSeg.add(eachpt);
			}else {
				eachSeg.add(eachpt);
			}
		}
		if(eachSeg!=null)
			seg.add(eachSeg);
		return seg;
	}
	
	public static void main(String[] args) {
		AmendTimeCalcu atc = new AmendTimeCalcu();
		Answer ans = new Answer();
		Activity act = new Activity();
		StuNumClassify snc = new StuNumClassify();
		Time timeObject = new Time();
		OperateFile opfl = new OperateFile();
		
		/*****获取过滤后的Activity,Answer,Notes的list******/
		ArrayList<String[]>answerlist = ans.getAnswerLog();//读取所有Answer
		answerlist=ans.filterAnswer(answerlist);           //获取过滤后Answer的list
		ArrayList<String[]>activitylist = act.getActivitlog();//读取所有的Activity
		activitylist=act.filterActivity(activitylist);     //获取过滤后Activity的list
		
		ArrayList<ArrayList<String[]>> ansArrange = new ArrayList<ArrayList<String[]>>();
		ansArrange=snc.getSNClassfiy(answerlist);          //对answer进行整理
		
		String filename = "./src/data/Amend/TimeDifference.csv";//将要写入结果的文件
		opfl.clearFile(filename);
		String[] header = {"学生序号","题号","Activity时间","上一次Answer时间","Answer时间","修改时间差"};//先写入标题头
		opfl.writeFileToCsv(header, filename);
		
		while(!activitylist.isEmpty()) {
			ArrayList<PackageData> datapack = new ArrayList<PackageData>();
			String[] actfirstlog = activitylist.get(0);                //得到第一条记录
			act.getACTlist(datapack, actfirstlog, activitylist);       //在Answer list中找到与此学生序号相同的数据，封装到datapack中
			ans.getANSlogWithACT(datapack, actfirstlog,ansArrange);    //在Activity list中找到与此学生序号相同的数据，封装到datapack中
			ArrayList<PackageData>datapacksort = atc.timeSort(datapack); //将datapack按照时间先后进行排序
			atc.rmlogBasedTAG(datapacksort);//读取变化的时间点
			ArrayList<ArrayList<PackageData>>seg = atc.getSegment(datapacksort);//将其进行分段
			for(int i=0;i<seg.size();i++){
				ArrayList<PackageData>eachSeg = seg.get(i);//每一段
				atc.rmlogBasedFLAG(eachSeg);//删除每一段正确后log 
				if(eachSeg.size()<3)
					continue;
				String QN=""; String SN="";String thisActMoment = "";
				String lastAnsMoment = "";String thisAnsMoment = "";
				SN = eachSeg.get(0).getSN();
				QN = eachSeg.get(0).getQN();
				thisActMoment=eachSeg.get(0).getTIME();
				thisAnsMoment=eachSeg.get(1).getTIME(); //此时的Answer时刻
				lastAnsMoment=eachSeg.get(1).getTIME(); //上一次Answer时刻
				String[] content=new String[6];
				long TD=0;
				for(int j=2;j<eachSeg.size();j=j+1) {
					PackageData lastdatapack = eachSeg.get(j-1);
					PackageData thisdatapack = eachSeg.get(j);
					lastAnsMoment = lastdatapack.getTIME();
					thisAnsMoment = thisdatapack.getTIME();
					TD=timeObject.getTimeDifference(thisAnsMoment, lastAnsMoment);
					String result="";
					//打印学生序号,题号,Activity时间,Answer时间,修改时间差
					result = SN +","+ QN +","+thisActMoment+","+thisAnsMoment;
					System.out.println(result);
					content[0]=SN;content[1]=""+QN;content[2]=thisActMoment;
					content[3]=lastAnsMoment;content[4]=thisAnsMoment;
					content[5]=String.valueOf(TD);
					opfl.writeFileToCsv(content, filename);
				}
				
			}
			datapack = null;
		}
		/*****求标准差，中位数，平均值*****/
	}
}
