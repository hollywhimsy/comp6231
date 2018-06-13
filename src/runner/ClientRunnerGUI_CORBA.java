package runner;

import java.awt.*;
import gui.ClientInterfaceCORBA;

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