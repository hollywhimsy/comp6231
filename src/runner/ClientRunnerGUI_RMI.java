package runner;

import java.awt.*;

import gui.ClientInterfaceRMI;

public class ClientRunnerGUI_RMI {
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ClientInterfaceRMI window = new ClientInterfaceRMI();
                    window.getMainFrame().setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}