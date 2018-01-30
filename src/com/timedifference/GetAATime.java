package com.timedifference;

/***
 * @comments 得到Answer&Activity的时间
 * @JDK 1.8.0_162
 * @version 1.0.0
 * @author lyh
 */
public class GetAATime {

	public GetAATime() {};
	private int tag;   //用作标记
	private int qN;    //题号
	private String strSN; //学生序号
	private String time;  //时间
	/***
	 * @comments Tag为0，代表此时的时间是Activity时间。Tag为1，代表此时的时间是Answer的时间
	 * @param Tag
	 * @param SN
	 * @param QN
	 * @param Time
	 */
	public GetAATime(int Tag,String SN,int QN,String Time) {
		super();
		this.tag=Tag;
		this.strSN=SN;
		this.qN=QN;
		this.time=Time;
	}
	/***
	 * @comments 获取时间
	 * @return time
	 */
	public String getTime() {
		return this.time;
	}
	public String getSN() {
		return this.strSN;
	}
	public int getTag() {
		return this.tag;
	}
	public int getQN() {
		return this.qN;
	}
}
