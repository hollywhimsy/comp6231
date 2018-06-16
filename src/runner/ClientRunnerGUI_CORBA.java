package runner;

import java.awt.*;
import gui.ClientInterfaceCORBA;
/*
 * change add to list into add this specialization
 * concurrent call: execute all existing managers for creating student records, get record count
 * but limited to location(managers from the same city center): create teacher records, transfer record
 * 
 */
public class ClientRunnerGUI_CORBA
{
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					ClientInterfaceCORBA window = new ClientInterfaceCORBA();
					window.getMainFrame().setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
}