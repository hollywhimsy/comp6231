package runner;

import java.awt.*;

import gui.ClientInterface;

public class ClientRunnerGUI_RMI {
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ClientInterface window = new ClientInterface();
                    window.getMainFrame().setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}