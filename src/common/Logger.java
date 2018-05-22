package src.common;



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
			pr.println(dateFormat.format(new Date()) + " : " + message);
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		finally {	
			pr.close();
		}
	}
    
}
