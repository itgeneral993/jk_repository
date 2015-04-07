package com.rpframework.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

/**
 * @author <a href="mailto:rplees.i.ly@gmail.com">rplees</a> date 2010-04-06
 *         {@code} 一些常用的文件操作有关的帮助方法
 */
public class FileUtils {
    /**
     * 将文件读出来转化为字符串
     * 
     * @param file
     *            源文件，不能是文件夹
     * @return
     */
    public static String loadFileToString(File file)
	    throws FileNotFoundException, IOException {
	BufferedReader br = null;
	// 字符缓冲流，是个装饰流，提高文件读取速度
	br = new BufferedReader(new FileReader(file));
	String fileToString = buffReaderToString(br);
	br.close();
	return fileToString;
    }

    /**
     * 将文件读出来转化为字符串
     * 
     * @param file
     *            源文件，不能是文件夹
     * @return
     * @throws IOException 
     */
    public static String buffReaderToString(BufferedReader br) throws IOException {
    	StringBuffer sb = new StringBuffer();
    	String line = br.readLine();
    	while (null != line) {
    	    sb.append(line);
    	    line = br.readLine();
    	}
    	return sb.toString();
    }
    
    /**
     * 流拷贝文件
     * 
     * @param tempFile
     * @param newFile
     * @return
     * @throws IOException
     */
    public static long copyFile(File tempFile, File newFile) throws IOException {
	
    	return copyFile(new FileInputStream(tempFile), newFile);
    }
    
    /**
     * 流拷贝文件
     * 
     * @param tempFile
     * @param newFile
     * @return
     * @throws IOException
     */
    public static long copyFile(InputStream is, File newFile) throws IOException {
		OutputStream os = new FileOutputStream(newFile);
		long s = IOUtils.copyLarge(is, os);
		os.flush();
		os.close();
		is.close();
		return s;
    }
    
    

    public static boolean deleteFile(File dir) {
    	if(dir.exists()) {
    		return dir.delete();
    	} else {
    		System.out.println("找不到要删除的文件:" + dir.getPath());
    		return false;
    	}
    }
    
    /**
     *  删除该目录下所有文件及文件夹
     * @param dir
     * @return
     */
    public  static void deleteDir(File... dir) {
	if(null != dir){
	    for(File file:dir){
		if (null !=file  && file.isDirectory()) {
		    String[] children = file.list();
		    // 递归删除目录中的子目录下
		    for (int i = 0; i < children.length; i++) {
			deleteDir(new File(file, children[i]));
		    }
		}
		// 目录此时为空，可以删除
		file.delete();
	    }
	   
	}
    }
    
    public static void saveToFile(String fileName, InputStream in) throws IOException { 
	    FileOutputStream fos = null;    
	    BufferedInputStream bis = null;    
	    int BUFFER_SIZE = 1024; 
	    byte[] buf = new byte[BUFFER_SIZE];    
	    int size = 0;    
	    bis = new BufferedInputStream(in);    
	    fos = new FileOutputStream(fileName);
//		   
	    while ( (size = bis.read(buf)) != -1)     
	      fos.write(buf, 0, size);    
	    fos.close();    
	    bis.close();    
	  } 
}
