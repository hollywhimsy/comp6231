package common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents the logger class
 * This is to be used by different classes to log messages
 */
public class Logger {
    
	private String path = null;
    private String fileName = null;
    
    public Logger(String path, String fileName) {	
		this.path = path;
		this.fileName = fileName;
	}
    
    public synchronized void write(String message) {
		PrintWriter pr = null;
		File file = new File(path + "/" + fileName + ".log");
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			pr = new PrintWriter(new FileWriter(file.getAbsoluteFile(), true));
			
			DateFormat timeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			pr.println(timeFormat.format(new Date()) + " : " + message);
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		finally {	
			pr.close();
		}
	}
    
}
