package common;

import java.io.*;
import java.util.Date;

public class Logger
{

	private String path = null;
	private String fileName = null;

	public Logger(String fileName)
	{
		new File(System.getProperty("user.dir") + File.separator + "logs").mkdirs();
		this.fileName = fileName;
		this.path = System.getProperty("user.dir") + File.separator + "logs" + File.separator; // Get current working director + "logs"
	}

	public synchronized void logToFile(String message)
	{
		String fileAddress = path.trim() + fileName.trim();

		try
		{
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileAddress, true), "UTF8"));
			PrintWriter out = new PrintWriter(bw);

			Date date = new Date();
			date.getTime();
			out.println(date.toString() + " ==> " + message);

			out.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
