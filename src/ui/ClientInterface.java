package ui;

import network.ManagerClient;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JSeparator;
import java.awt.CardLayout;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ClientInterface {

    private JFrame frameDCMS1;
    private final ButtonGroup buttonGroup = new ButtonGroup();

    private JRadioButton rdbtnAddManager;
    private JRadioButton rdbtnCreateTeacher;
    private JRadioButton rdbtnCreateStudent;
    private JTextArea txtrOutput;
    private JRadioButton rdbtnGetRecordsCount;
    private JRadioButton rdbtnEditARecord;
    private JRadioButton rdbtnReadValueOf;
    private JSeparator separator;
    private JSeparator separator_1;
    private JSeparator separator_2;
    private JTextField txtT1;
    private JTextField txtT2;
    private JTextField txtT3;
    private JTextField txtT4;
    private JTextField txtT6;
    private JLabel lblLabel1;
    private JLabel lblLabel2;
    private JLabel lblLabel3;
    private JLabel lblLabel4;
    private JLabel lblLabel7;
    private JLabel lblLabel8;
    private JLabel lblDay;
    private JLabel lblMonth;
    private JLabel lblYear;
    private JPanel pnlMain;
    private JButton btnExecute;
    private JComboBox<String> cmbManagers;
    private JLabel lblCurrentActiveManager;
    private JLabel lblOutputmessageresult;
    private JCheckBox chckbxActive;
    private JComboBox<Integer> cmbDay;
    private JComboBox<Integer> cmbMonth;
    private JComboBox<Integer> cmbYear;
    private JButton btnAddToList;
    private JLabel lblLabel6;
    private JRadioButton rdbtnField1;
    private JRadioButton rdbtnField2;
    private JRadioButton rdbtnField3;
    private JRadioButton rdbtnField4;
    private JRadioButton rdbtnField5;
    private JLabel lblLabel5;
    private JTextField txtT5;
    private JRadioButton rdbtnField6;
    private HashMap<String, ManagerClient> managers = new HashMap<>();
    private List<String> spec;




    /**
     * Create the application.
     */
    public ClientInterface() {
        initialize();
        invisibleAll();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize()
    {
        frameDCMS1 = new JFrame();
        frameDCMS1.setTitle("Distributed Class Management System");
        frameDCMS1.setBounds(100, 100, 1028, 621);
        frameDCMS1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frameDCMS1.setLocation(dim.width/2-frameDCMS1.getSize().width/2, dim.height/2-frameDCMS1.getSize().height/2);
        frameDCMS1.getContentPane().setLayout(new CardLayout(0, 0));

        pnlMain = new JPanel();
        frameDCMS1.getContentPane().add(pnlMain, "name_75383535468770");
        pnlMain.setLayout(null);

        btnExecute = new JButton("Execute");
        btnExecute.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0)
            {
                if(rdbtnAddManager.isSelected())
                {
                    if (isManagerIdFormatCorrect(txtT1.getText().trim()))
                    {
                        ManagerClient mng = new ManagerClient(txtT1.getText().trim()); // can only connect to MTL server
                        if (mng != null)
                        {
                            managers.put(txtT1.getText().trim(), mng);
                            cmbManagers.addItem(txtT1.getText().trim());
                            txtrOutput.setText(txtT1.getText().trim() + " is created. You may select it as \"Current Active Manage\"");
                        }
                    }
                    else
                    {
                        txtrOutput.setText("Invalid Manager ID");
                    }
                }

                if(rdbtnCreateTeacher.isSelected())
                {
                    spec = new ArrayList<String>();
                    spec.add("Math");
                    spec.add("Computer");
                    try
                    {
                        //txtrOutput.setText(managers.get(cmbManagers.getSelectedItem()).getManagerId());
                        if (managers.get(cmbManagers.getSelectedItem()).createTRecord(txtT1.getText().trim(),
                                txtT1.getText().trim(),
                                txtT3.getText().trim(),
                                Integer.valueOf(txtT4.getText().trim()),
                                spec))
                        {
                            txtrOutput.setText("One Teacher Record Added successfully");
                        }
                        {
                            txtrOutput.setText("Failed! Check Log files for more information");
                        }
                    } catch (RemoteException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

                if(rdbtnCreateStudent.isSelected())
                {
                    List<String> courses = new ArrayList<String>();
                    courses.add("COEN");
                    courses.add("Dist");
                    Date date = new Date();
                    date.getTime();
                    try
                    {
                        if (managers.get(cmbManagers.getSelectedItem()).createSRecord(txtT1.getText().trim(),
                                txtT2.getText().trim(),
                                courses,
                                chckbxActive.isSelected(),
                                date))
                        {
                            txtrOutput.setText("One Student Record Added successfully");
                        }
                    } catch (RemoteException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if(rdbtnGetRecordsCount.isSelected())
                {
                    try
                    {
                        txtrOutput.setText(managers.get(cmbManagers.getSelectedItem()).getRecordCounts());
                    } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if(rdbtnEditARecord.isSelected())
                {

                }
                if(rdbtnReadValueOf.isSelected())
                {

                }
            }
        });
        btnExecute.setFont(new Font("Tahoma", Font.PLAIN, 25));
        btnExecute.setBounds(88, 339, 180, 37);
        pnlMain.add(btnExecute);

        cmbManagers = new JComboBox<String>();
        cmbManagers.setFont(new Font("Tahoma", Font.PLAIN, 25));
        cmbManagers.setBounds(38, 49, 275, 35);
        pnlMain.add(cmbManagers);

        lblCurrentActiveManager = new JLabel("Current Active Manager");
        lblCurrentActiveManager.setForeground(new Color(0, 0, 128));
        lblCurrentActiveManager.setFont(new Font("Tahoma", Font.PLAIN, 25));
        lblCurrentActiveManager.setBounds(38, 11, 275, 29);
        pnlMain.add(lblCurrentActiveManager);

        rdbtnAddManager = new JRadioButton("Add Manager");
        buttonGroup.add(rdbtnAddManager);
        rdbtnAddManager.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                invisibleAll();
                lblLabel1.setText("Manager ID:");
                lblLabel1.setVisible(true);
                txtT1.setVisible(true);
            }
        });
        rdbtnAddManager.setFont(new Font("Tahoma", Font.PLAIN, 25));
        rdbtnAddManager.setBounds(22, 113, 306, 37);
        pnlMain.add(rdbtnAddManager);

        rdbtnCreateTeacher = new JRadioButton("Create Teacher Record");
        buttonGroup.add(rdbtnCreateTeacher);
        rdbtnCreateTeacher.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                txtrOutput.setText("bbbbbbbbbbbbbbbb");
                invisibleAll();
                lblLabel1.setText("First Name:");
                lblLabel2.setText("Last Name:");
                lblLabel3.setText("Address:");
                lblLabel4.setText("Phone:");
                lblLabel5.setText("Location:");
                lblLabel6.setText("Specilizations:");
                lblLabel1.setVisible(true);
                lblLabel2.setVisible(true);
                lblLabel3.setVisible(true);
                lblLabel4.setVisible(true);
                lblLabel5.setVisible(true);
                lblLabel6.setVisible(true);
                txtT1.setVisible(true);
                txtT2.setVisible(true);
                txtT3.setVisible(true);
                txtT4.setVisible(true);
                txtT5.setVisible(true);
                txtT6.setVisible(true);
                btnAddToList.setVisible(true);

            }
        });
        rdbtnCreateTeacher.setFont(new Font("Tahoma", Font.PLAIN, 25));
        rdbtnCreateTeacher.setBounds(22, 147, 306, 37);
        pnlMain.add(rdbtnCreateTeacher);

        rdbtnCreateStudent = new JRadioButton("Create Student Record");
        buttonGroup.add(rdbtnCreateStudent);
        rdbtnCreateStudent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                txtrOutput.setText("ccccccccccccccccc");
                invisibleAll();
                lblLabel1.setText("First Name:");
                lblLabel2.setText("Last Name:");
                lblLabel6.setText("Registerd Courses:");
                lblLabel1.setVisible(true);
                lblLabel2.setVisible(true);
                lblLabel6.setVisible(true);
                txtT1.setVisible(true);
                txtT2.setVisible(true);
                txtT6.setVisible(true);
                btnAddToList.setVisible(true);
                lblLabel7.setText("Active or Inactive?");
                lblLabel8.setText("Date:");
                lblLabel7.setVisible(true);
                lblLabel8.setVisible(true);
                chckbxActive.setVisible(true);
                lblDay.setText("DD");
                lblMonth.setText("MM");
                lblYear.setText("YYYY");
                lblDay.setVisible(true);
                lblMonth.setVisible(true);
                lblYear.setVisible(true);
                cmbDay.setVisible(true);
                cmbMonth.setVisible(true);
                cmbYear.setVisible(true);
            }
        });
        rdbtnCreateStudent.setFont(new Font("Tahoma", Font.PLAIN, 25));
        rdbtnCreateStudent.setBounds(22, 183, 306, 37);
        pnlMain.add(rdbtnCreateStudent);

        txtrOutput = new JTextArea();
        txtrOutput.setEditable(false);
        txtrOutput.setFont(new Font("Monospaced", Font.PLAIN, 25));
        txtrOutput.setBounds(22, 441, 958, 77);
        pnlMain.add(txtrOutput);

        rdbtnGetRecordsCount = new JRadioButton("Get Records Count");
        rdbtnGetRecordsCount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                txtrOutput.setText("zsdgvfxfdbx");
                invisibleAll();
            }
        });
        buttonGroup.add(rdbtnGetRecordsCount);
        rdbtnGetRecordsCount.setFont(new Font("Tahoma", Font.PLAIN, 25));
        rdbtnGetRecordsCount.setBounds(22, 219, 306, 37);
        pnlMain.add(rdbtnGetRecordsCount);

        rdbtnEditARecord = new JRadioButton("Edit a Record");
        rdbtnEditARecord.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                txtrOutput.setText("xdfbcfghn");
                invisibleAll();
                lblLabel1.setText("Record ID:");
                //lblLabel2.setText("Field Name:");
                lblLabel1.setVisible(true);
                //lblLabel2.setVisible(true);
                txtT1.setVisible(true);
                //txtT2.setVisible(true);
            }
        });
        buttonGroup.add(rdbtnEditARecord);
        rdbtnEditARecord.setFont(new Font("Tahoma", Font.PLAIN, 25));
        rdbtnEditARecord.setBounds(22, 257, 306, 37);
        pnlMain.add(rdbtnEditARecord);

        rdbtnReadValueOf = new JRadioButton("Read Value of a Record");
        rdbtnReadValueOf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                txtrOutput.setText("xdfbcfghn");
                invisibleAll();
                lblLabel1.setText("Record ID:");
                lblLabel2.setText("Field Name:");
                lblLabel1.setVisible(true);
                lblLabel2.setVisible(true);
                txtT1.setVisible(true);
                txtT2.setVisible(true);
            }
        });
        buttonGroup.add(rdbtnReadValueOf);
        rdbtnReadValueOf.setFont(new Font("Tahoma", Font.PLAIN, 25));
        rdbtnReadValueOf.setBounds(22, 295, 306, 37);
        pnlMain.add(rdbtnReadValueOf);

        separator = new JSeparator();
        separator.setBounds(11, 108, 327, 2);
        pnlMain.add(separator);

        separator_1 = new JSeparator();
        separator_1.setOrientation(SwingConstants.VERTICAL);
        separator_1.setBounds(351, 11, 2, 380);
        pnlMain.add(separator_1);

        separator_2 = new JSeparator();
        separator_2.setBounds(11, 400, 958, 2);
        pnlMain.add(separator_2);

        txtT1 = new JTextField();
        txtT1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent arg0)
            {
                if(rdbtnEditARecord.isSelected())
                {
                    if(isRecordIdFormatCorrect(txtT1.getText().trim()))
                    {
                        if(txtT1.getText().toUpperCase().contains("TR"))
                        {
                            txtrOutput.setText("Teacher Record");

                            rdbtnField1.setText("Address");
                            rdbtnField2.setText("Phone");
                            rdbtnField3.setText("Location");
                            rdbtnField1.setVisible(true);
                            rdbtnField2.setVisible(true);
                            rdbtnField3.setVisible(true);
                        }

                        if(txtT1.getText().toUpperCase().contains("SR"))
                        {
                            txtrOutput.setText("Student Record");

                            rdbtnField1.setText("Address");
                            rdbtnField2.setText("Phone");
                            rdbtnField3.setText("Location");
                            rdbtnField1.setVisible(true);
                            rdbtnField2.setVisible(true);
                            rdbtnField3.setVisible(true);
                        }
                    }
                    else
                    {
                        rdbtnField1.setVisible(false);
                        rdbtnField2.setVisible(false);
                        rdbtnField3.setVisible(false);
                        txtrOutput.setText("Incorrect ID!");
                    }
                }

            }
        });
        txtT1.setFont(new Font("Tahoma", Font.PLAIN, 25));
        txtT1.setBounds(576, 24, 235, 35);
        pnlMain.add(txtT1);
        txtT1.setColumns(10);

        txtT2 = new JTextField();
        txtT2.setFont(new Font("Tahoma", Font.PLAIN, 25));
        txtT2.setBounds(576, 64, 235, 35);
        pnlMain.add(txtT2);
        txtT2.setColumns(10);

        txtT3 = new JTextField();
        txtT3.setFont(new Font("Tahoma", Font.PLAIN, 25));
        txtT3.setBounds(576, 105, 235, 35);
        pnlMain.add(txtT3);
        txtT3.setColumns(10);

        txtT4 = new JTextField();
        txtT4.setFont(new Font("Tahoma", Font.PLAIN, 25));
        txtT4.setBounds(577, 146, 235, 35);
        pnlMain.add(txtT4);
        txtT4.setColumns(10);

        lblOutputmessageresult = new JLabel("Output/Message/Result:");
        lblOutputmessageresult.setForeground(new Color(0, 0, 128));
        lblOutputmessageresult.setFont(new Font("Tahoma", Font.PLAIN, 25));
        lblOutputmessageresult.setBounds(21, 411, 306, 29);
        pnlMain.add(lblOutputmessageresult);

        txtT6 = new JTextField();
        txtT6.setFont(new Font("Tahoma", Font.PLAIN, 25));
        txtT6.setBounds(576, 229, 235, 35);
        pnlMain.add(txtT6);
        txtT6.setColumns(10);

        chckbxActive = new JCheckBox(" (Check if Active)");
        chckbxActive.setFont(new Font("Tahoma", Font.PLAIN, 25));
        chckbxActive.setBounds(576, 274, 234, 37);
        pnlMain.add(chckbxActive);

        cmbDay = new JComboBox<Integer>();
        cmbDay.setFont(new Font("Tahoma", Font.PLAIN, 25));
        cmbDay.setBounds(576, 355, 66, 35);
        pnlMain.add(cmbDay);

        cmbMonth = new JComboBox<Integer>();
        cmbMonth.setFont(new Font("Tahoma", Font.PLAIN, 25));
        cmbMonth.setBounds(645, 355, 66, 35);
        pnlMain.add(cmbMonth);

        cmbYear = new JComboBox<Integer>();
        cmbYear.setFont(new Font("Tahoma", Font.PLAIN, 25));
        cmbYear.setBounds(714, 355, 97, 35);
        pnlMain.add(cmbYear);

        btnAddToList = new JButton("Add to List");
        btnAddToList.setFont(new Font("Tahoma", Font.PLAIN, 25));
        btnAddToList.setBounds(819, 227, 160, 37);
        pnlMain.add(btnAddToList);

        lblLabel6 = new JLabel("Label6");
        lblLabel6.setForeground(new Color(0, 0, 128));
        lblLabel6.setFont(new Font("Tahoma", Font.PLAIN, 25));
        lblLabel6.setBounds(361, 233, 210, 29);
        pnlMain.add(lblLabel6);

        lblLabel1 = new JLabel("Label1");
        lblLabel1.setForeground(new Color(0, 0, 128));
        lblLabel1.setFont(new Font("Tahoma", Font.PLAIN, 25));
        lblLabel1.setBounds(362, 30, 210, 29);
        pnlMain.add(lblLabel1);

        lblLabel2 = new JLabel("Label2");
        lblLabel2.setForeground(new Color(0, 0, 128));
        lblLabel2.setFont(new Font("Tahoma", Font.PLAIN, 25));
        lblLabel2.setBounds(361, 69, 210, 29);
        pnlMain.add(lblLabel2);

        lblLabel3 = new JLabel("Label3");
        lblLabel3.setForeground(new Color(0, 0, 128));
        lblLabel3.setFont(new Font("Tahoma", Font.PLAIN, 25));
        lblLabel3.setBounds(360, 108, 210, 29);
        pnlMain.add(lblLabel3);

        lblLabel4 = new JLabel("Label4");
        lblLabel4.setForeground(new Color(0, 0, 128));
        lblLabel4.setFont(new Font("Tahoma", Font.PLAIN, 25));
        lblLabel4.setBounds(362, 151, 210, 29);
        pnlMain.add(lblLabel4);

        lblLabel7 = new JLabel("Label7");
        lblLabel7.setForeground(new Color(0, 0, 128));
        lblLabel7.setFont(new Font("Tahoma", Font.PLAIN, 25));
        lblLabel7.setBounds(360, 275, 210, 29);
        pnlMain.add(lblLabel7);

        lblLabel8 = new JLabel("Label8");
        lblLabel8.setForeground(new Color(0, 0, 128));
        lblLabel8.setFont(new Font("Tahoma", Font.PLAIN, 25));
        lblLabel8.setBounds(361, 358, 210, 29);
        pnlMain.add(lblLabel8);

        lblDay = new JLabel("DD");
        lblDay.setFont(new Font("Tahoma", Font.PLAIN, 25));
        lblDay.setForeground(new Color(0, 0, 128));
        lblDay.setBounds(586, 321, 48, 29);
        pnlMain.add(lblDay);

        lblMonth = new JLabel("MM");
        lblMonth.setForeground(new Color(0, 0, 128));
        lblMonth.setFont(new Font("Tahoma", Font.PLAIN, 25));
        lblMonth.setBounds(653, 321, 48, 29);
        pnlMain.add(lblMonth);

        lblYear = new JLabel("YYYY");
        lblYear.setForeground(new Color(0, 0, 128));
        lblYear.setFont(new Font("Tahoma", Font.PLAIN, 25));
        lblYear.setBounds(730, 320, 66, 29);
        pnlMain.add(lblYear);

        rdbtnField1 = new JRadioButton("Field1");
        rdbtnField1.setFont(new Font("Tahoma", Font.PLAIN, 25));
        rdbtnField1.setBounds(829, 7, 235, 37);
        pnlMain.add(rdbtnField1);

        rdbtnField2 = new JRadioButton("Field2");
        rdbtnField2.setFont(new Font("Tahoma", Font.PLAIN, 25));
        rdbtnField2.setBounds(829, 48, 235, 37);
        pnlMain.add(rdbtnField2);

        rdbtnField3 = new JRadioButton("Field3");
        rdbtnField3.setFont(new Font("Tahoma", Font.PLAIN, 25));
        rdbtnField3.setBounds(829, 85, 235, 37);
        pnlMain.add(rdbtnField3);

        rdbtnField4 = new JRadioButton("Field4");
        rdbtnField4.setFont(new Font("Tahoma", Font.PLAIN, 25));
        rdbtnField4.setBounds(829, 125, 235, 37);
        pnlMain.add(rdbtnField4);

        rdbtnField5 = new JRadioButton("Field5");
        rdbtnField5.setFont(new Font("Tahoma", Font.PLAIN, 25));
        rdbtnField5.setBounds(829, 164, 235, 37);
        pnlMain.add(rdbtnField5);

        lblLabel5 = new JLabel("Label5");
        lblLabel5.setForeground(new Color(0, 0, 128));
        lblLabel5.setFont(new Font("Tahoma", Font.PLAIN, 25));
        lblLabel5.setBounds(361, 192, 210, 29);
        pnlMain.add(lblLabel5);

        txtT5 = new JTextField();
        txtT5.setFont(new Font("Tahoma", Font.PLAIN, 25));
        txtT5.setColumns(10);
        txtT5.setBounds(576, 188, 235, 35);
        pnlMain.add(txtT5);

        rdbtnField6 = new JRadioButton("Field6");
        rdbtnField6.setFont(new Font("Tahoma", Font.PLAIN, 25));
        rdbtnField6.setBounds(825, 285, 235, 37);
        pnlMain.add(rdbtnField6);
    }

    private void invisibleAll()
    {
        lblLabel1.setVisible(false);
        lblLabel2.setVisible(false);
        lblLabel3.setVisible(false);
        lblLabel4.setVisible(false);
        lblLabel5.setVisible(false);
        lblLabel6.setVisible(false);
        lblLabel7.setVisible(false);
        lblLabel8.setVisible(false);
        txtT1.setVisible(false);
        txtT2.setVisible(false);
        txtT3.setVisible(false);
        txtT4.setVisible(false);
        txtT5.setVisible(false);
        txtT6.setVisible(false);
        btnAddToList.setVisible(false);
        lblDay.setVisible(false);
        lblMonth.setVisible(false);
        lblYear.setVisible(false);
        chckbxActive.setVisible(false);
        cmbDay.setVisible(false);
        cmbMonth.setVisible(false);
        cmbYear.setVisible(false);
        rdbtnField1.setVisible(false);
        rdbtnField2.setVisible(false);
        rdbtnField3.setVisible(false);
        rdbtnField4.setVisible(false);
        rdbtnField5.setVisible(false);
        rdbtnField6.setVisible(false);
    }

    private boolean isRecordIdFormatCorrect(String id)
    {
        if (id == null)
        {
            return false;
        }

        if (id.length() != 7)
        {
            return false;
        }

        Character ch = id.toUpperCase().charAt(0);
        if(!(ch.equals('T') || ch.equals('S')))
        {
            return false;
        }

        ch = id.toUpperCase().charAt(1);

        if(!(ch.equals('R')))
        {
            return false;
        }

        if(!(id.substring(2, 5).chars().allMatch(Character::isDigit)))
        {
            return false;
        }

        return true;
    }

    private boolean isManagerIdFormatCorrect(String id)
    {
        if (id == null)
        {
            return false;
        }

        if (id.length() != 7)
        {
            return false;
        }

        if(!(id.substring(0, 3).toUpperCase().equals("MTL") ||
                id.substring(0, 3).toUpperCase().equals("LVL") ||
                id.substring(0, 3).toUpperCase().equals("DDO")))
        {
            return false;
        }

        if(!(id.substring(3, 4).chars().allMatch(Character::isDigit)))
        {
            return false;
        }

        return true;
    }

    /**
     * Returns the main frame.
     */
    public JFrame getMainFrame()
    {
        return frameDCMS1;
    }
}
