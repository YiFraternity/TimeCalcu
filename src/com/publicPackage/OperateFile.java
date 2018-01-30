package com.publicPackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

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
	/****
	 * @comments 读取CSV文件指定列的所有值
	 * @param index
	 * @param file
	 * @return 
	 */
	public ArrayList<Long> readIndexColumn(int index,String file) {
		ArrayList<String[]>csvList = new ArrayList<String[]>();
		ArrayList<Long> value = new ArrayList<Long>();
		try {
	        if (isCsv(file)) { 
	            CsvReader reader = new CsvReader(file, ',', Charset.forName("utf8"));    
	            reader.readHeaders(); // 跳过表头   如果需要表头的话，不要写这句。  
	            while (reader.readRecord())  //逐行读入除表头的数据  
	                csvList.add(reader.getValues());  
	            reader.close();
	        } else {  
	            System.out.println("此文件不是CSV文件！");  
	        }
		}catch(Exception e) {
			e.printStackTrace();
		}
		int csvSize = csvList.size();
		
		for(int i=0;i<csvSize;i++) {
			value.add(Long.parseLong(csvList.get(i)[index]));
		}
		return value;
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
	/****
	 * @comments 写CSV文件
	 * @param str
	 * @param file
	 */
	public void writeFileToCsv(String[] str, String file) {
		File f = new File(file);
		try {
			FileOutputStream filestream = new FileOutputStream(f,true);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(filestream,Charset.forName("utf-8")));
			CsvWriter cwriter = new CsvWriter(writer,',');
			cwriter.writeRecord(str,false);
			cwriter.close();
        } catch (IOException e) {
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
    	OperateFile opfl = new OperateFile();
        String filePath = "./src/data/log/";
        String fileName = "Answer-Activity-TimeDifference.csv";
        String strFile = filePath+fileName;
        ArrayList<Long> value=null;
        value =opfl.readIndexColumn(4, strFile);
        for(int i=0;i<value.size();i++) {
        	System.out.print(value.get(i)+" ");
        }
    }  
}
