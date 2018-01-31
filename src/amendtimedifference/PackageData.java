package amendtimedifference;

class PackageData {

	protected PackageData(){}
	
	private int TAG = 0;      //用来标识Activity还是Answer
	private int FLAG = 0 ;    //用来标识Answer正确与否
	private String SN = "";   //学生序号
	private String QN = "";   //题号
	private String TIME = ""; //时间
	
	protected PackageData(int TAG,String SN,String QN,int FLAG,String TIME) {
		this.TAG=TAG;
		this.SN = SN;
		this.QN = QN;
		this.FLAG=FLAG;
		this.TIME=TIME;
	}
	
	protected int getTAG() {
		return this.TAG;
	}
	protected int getFLAG() {
		return this.FLAG;
	}
	protected String getSN() {
		return this.SN;
	}
	protected String getQN() {
		return this.QN;
	}
	protected String getTIME() {
		return this.TIME;
	}
}
