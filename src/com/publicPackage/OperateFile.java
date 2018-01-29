package com.publicPackage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;

import com.csvreader.CsvReader;

public class OperateFile {

	/***
	 * @comments 判读是否为CSV文件
	 * @param fileName
	 * @return
	 */
    private boolean isCsv(String fileName) {  
        return fileName.matches("^.+\\.(?i)(csv)$");  
    }  
    
    /****
     * @comments 读CSV文件,并将读取到的内容返回
     * @param csvList
     * @param strFile
     * @return ArrayList<String[]>
     */
	public ArrayList<String[]> readCsvFile(String strFile) {
		ArrayList<String[]>csvList = new ArrayList<String[]>();
		try {
	        if (isCsv(strFile)) { 
	            CsvReader reader = new CsvReader(strFile, ',', Charset.forName("utf8"));  
	            reader.readHeaders(); // 跳过表头   如果需要表头的话，不要写这句。  
	            while (reader.readRecord()) { //逐行读入除表头的数据  
	                csvList.add(reader.getValues());  
	            }  
	            reader.close();  
	        } else {  
	            System.out.println("此文件不是CSV文件！");  
	        }
		}catch(Exception e) {
			e.printStackTrace();
		}
		return csvList;
	}
	
	/***
	 * @Comments 将字符串a写入文件strFile
	 *           替换掉原有部分
	 *           (使用BufferedWriter创建对象时，注意第二参数为空或者为false)
	 * @param strFile
	 * @param str
	 */
	public void writeToFile(String str,String strFile){
		FileOutputStream o = null;
		byte[] buff = new byte[]{};
		try{
			File file = new File(strFile);
			if(!file.exists()){
				file.createNewFile();
			}
			buff=str.getBytes();
			o=new FileOutputStream(file,true);
			o.write(buff);
			o.flush();
			o.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/***
	 * @Comments 清空文件
	 * @Notice 并不删除文件，只是将文件清空
	 * @param strFile
	 */
	public void clearFile(String strFile){
		File file = new File(strFile);
		try{
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write("");
			fileWriter.flush();
			fileWriter.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//方法测试  
    public static void main(String[] args){  
        String filePath = "./src/data/log/";
        String fileName = "Activity-Log-2017-09-07 .csv";
        String strFile = filePath+fileName;
        OperateFile of = new OperateFile();  
        ArrayList<String[]> list = new ArrayList<String[]>();
        list=of.readCsvFile(strFile);  
        for(int i=0;i<list.size();i++) {
        	String[] arrayCsv = list.get(i);
        	for (int j=0;j<arrayCsv.length;j++) {
        		System.out.println(arrayCsv[j]);
        	}
        }
    }  
}
