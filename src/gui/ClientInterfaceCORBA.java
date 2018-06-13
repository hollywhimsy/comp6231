package gui;

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
import manager.ManagerClientCORBA;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JScrollPane;

public class ClientInterfaceCORBA
{

	private JFrame frameDCMS1;
	private final ButtonGroup rdbtnGroupMainFunctions = new ButtonGroup();

	private JRadioButton rdbtnAddManager;
	private JRadioButton rdbtnCreateTeacher;
	private JRadioButton rdbtnCreateStudent;
	private JTextArea txtrOutput;
	private JRadioButton rdbtnGetRecordsCount;
	private JRadioButton rdbtnEditARecord;
	private JRadioButton rdbtnTransferRecord;
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
	private JButton btnRunBySelected;
	private JComboBox<String> cmbxManagers;
	private JLabel lblCurrentActiveManager;
	private JLabel lblOutputmessageresult;
	private JCheckBox chckbxActive;
	private JComboBox<String> cmbxDay;
	private JComboBox<String> cmbxMonth;
	private JComboBox<String> cmbxYear;
	private JButton btnAddToList;
	private JLabel lblLabel6;
	private JRadioButton rdbtnField1;
	private JRadioButton rdbtnField2;
	private JRadioButton rdbtnField3;
	private JLabel lblLabel5;
	private JTextField txtT5;
	private List<String> managersList = new ArrayList<>();
	private final ButtonGroup rdbtnGroupFields = new ButtonGroup();
	private List<String> specializations = new ArrayList<>();
	private List<String> courses = new ArrayList<>();
	private List<String> coursesNewList = new ArrayList<>();
	private List<String> coursesExisting = new ArrayList<>();
	private JButton btnRunByAll;
	private JButton btnAddManager;
	private JScrollPane scrollPane;

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
					window.frameDCMS1.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientInterfaceCORBA()
	{
		initialize();
		invisibleAll();
		init();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frameDCMS1 = new JFrame();
		frameDCMS1.setTitle("Distributed Class Management System - CORBA");
		frameDCMS1.setBounds(100, 100, 1028, 621);
		frameDCMS1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frameDCMS1.setLocation(dim.width / 2 - frameDCMS1.getSize().width / 2,
				dim.height / 2 - frameDCMS1.getSize().height / 2);
		frameDCMS1.getContentPane().setLayout(new CardLayout(0, 0));
		frameDCMS1.setResizable(false);

		pnlMain = new JPanel();
		frameDCMS1.getContentPane().add(pnlMain, "name_75383535468770");
		pnlMain.setLayout(null);

		btnRunBySelected = new JButton("Run By Selected Manager");
		btnRunBySelected.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (rdbtnCreateTeacher.isSelected())
				{
					createTeacher(false);
				}

				if (rdbtnCreateStudent.isSelected())
				{
					createStudent(false);
				}

				if (rdbtnGetRecordsCount.isSelected())
				{
					getRecordsCount(false);
				}

				if (rdbtnEditARecord.isSelected())
				{
					editARecord(false);
				}
				if (rdbtnTransferRecord.isSelected())
				{
					transferRecord(false);
				}
			}
		});
		btnRunBySelected.setFont(new Font("Tahoma", Font.PLAIN, 25));
		btnRunBySelected.setBounds(22, 343, 317, 37);
		pnlMain.add(btnRunBySelected);

		cmbxManagers = new JComboBox<String>();
		cmbxManagers.setFont(new Font("Tahoma", Font.PLAIN, 25));
		cmbxManagers.setBounds(38, 44, 275, 35);
		pnlMain.add(cmbxManagers);

		lblCurrentActiveManager = new JLabel("Current Active Manager");
		lblCurrentActiveManager.setForeground(new Color(0, 0, 128));
		lblCurrentActiveManager.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblCurrentActiveManager.setBounds(38, 11, 275, 29);
		pnlMain.add(lblCurrentActiveManager);

		rdbtnAddManager = new JRadioButton("Add New Manager");
		rdbtnGroupMainFunctions.add(rdbtnAddManager);
		rdbtnAddManager.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				invisibleAll();
				btnAddManager.setEnabled(true);
				btnRunBySelected.setEnabled(false);
				btnRunByAll.setEnabled(false);

				lblLabel1.setText("Manager ID:");
				lblLabel1.setVisible(true);
				txtT1.setVisible(true);
			}
		});
		rdbtnAddManager.setFont(new Font("Tahoma", Font.PLAIN, 25));
		rdbtnAddManager.setBounds(33, 84, 275, 37);
		pnlMain.add(rdbtnAddManager);

		rdbtnCreateTeacher = new JRadioButton("Create Teacher Record");
		rdbtnGroupMainFunctions.add(rdbtnCreateTeacher);
		rdbtnCreateTeacher.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				invisibleAll();
				btnAddManager.setEnabled(false);
				btnRunBySelected.setEnabled(true);
				btnRunByAll.setEnabled(true);

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

				specializations.clear();
			}
		});
		rdbtnCreateTeacher.setFont(new Font("Tahoma", Font.PLAIN, 25));
		rdbtnCreateTeacher.setBounds(30, 168, 306, 37);
		pnlMain.add(rdbtnCreateTeacher);

		rdbtnCreateStudent = new JRadioButton("Create Student Record");
		rdbtnGroupMainFunctions.add(rdbtnCreateStudent);
		rdbtnCreateStudent.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				invisibleAll();

				lblLabel6.setBounds(361, 109, 210, 29);
				txtT6.setBounds(576, 105, 235, 35);
				btnAddToList.setBounds(819, 103, 160, 37);
				lblLabel7.setBounds(360, 151, 210, 29);
				chckbxActive.setBounds(576, 150, 234, 37);
				lblLabel8.setBounds(361, 234, 210, 29);
				cmbxDay.setBounds(646, 231, 66, 35);
				cmbxMonth.setBounds(578, 231, 66, 35);
				cmbxYear.setBounds(714, 231, 97, 35);
				lblDay.setBounds(664, 198, 48, 29);
				lblMonth.setBounds(593, 198, 48, 29);
				lblYear.setBounds(726, 198, 66, 29);

				btnAddManager.setEnabled(false);
				btnRunBySelected.setEnabled(true);
				btnRunByAll.setEnabled(true);

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
				lblLabel8.setText("Status Date:");
				lblLabel7.setVisible(true);
				lblLabel8.setVisible(true);
				chckbxActive.setVisible(true);
				lblDay.setText("DD");
				lblMonth.setText("MM");
				lblYear.setText("YYYY");
				lblDay.setVisible(true);
				lblMonth.setVisible(true);
				lblYear.setVisible(true);
				cmbxDay.setVisible(true);
				cmbxMonth.setVisible(true);
				cmbxYear.setVisible(true);

				coursesNewList.clear();
			}
		});
		rdbtnCreateStudent.setFont(new Font("Tahoma", Font.PLAIN, 25));
		rdbtnCreateStudent.setBounds(30, 204, 306, 37);
		pnlMain.add(rdbtnCreateStudent);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 467, 958, 83);
		pnlMain.add(scrollPane);

		txtrOutput = new JTextArea();
		scrollPane.setViewportView(txtrOutput);
		txtrOutput.setForeground(new Color(0, 0, 128));
		txtrOutput.setEditable(false);
		txtrOutput.setFont(new Font("Monospaced", Font.BOLD, 25));

		rdbtnGetRecordsCount = new JRadioButton("Get Records Count");
		rdbtnGetRecordsCount.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				invisibleAll();

				btnAddManager.setEnabled(false);
				btnRunBySelected.setEnabled(true);
				btnRunByAll.setEnabled(true);
			}
		});
		rdbtnGroupMainFunctions.add(rdbtnGetRecordsCount);
		rdbtnGetRecordsCount.setFont(new Font("Tahoma", Font.PLAIN, 25));
		rdbtnGetRecordsCount.setBounds(30, 240, 306, 37);
		pnlMain.add(rdbtnGetRecordsCount);

		rdbtnEditARecord = new JRadioButton("Edit a Record");
		rdbtnEditARecord.addActionListener(new ActionListener()
		{
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e)
			{
				invisibleAll();

				btnAddManager.setEnabled(false);
				btnRunBySelected.setEnabled(true);
				btnRunByAll.setEnabled(true);

				lblLabel1.setText("Record ID:");
				lblLabel1.setVisible(true);
				txtT1.setVisible(true);

				Date date1 = new Date();
				date1.getTime();
				cmbxDay.setSelectedItem(date1.getDate() + "");
				cmbxMonth.setSelectedItem((date1.getMonth() + 1) + "");
				cmbxYear.setSelectedItem("2018");
				coursesExisting.clear();
				coursesNewList.clear();
			}
		});
		rdbtnGroupMainFunctions.add(rdbtnEditARecord);
		rdbtnEditARecord.setFont(new Font("Tahoma", Font.PLAIN, 25));
		rdbtnEditARecord.setBounds(30, 274, 306, 37);
		pnlMain.add(rdbtnEditARecord);

		rdbtnTransferRecord = new JRadioButton("Transfer Record");
		rdbtnTransferRecord.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				invisibleAll();

				btnAddManager.setEnabled(false);
				btnRunBySelected.setEnabled(true);
				btnRunByAll.setEnabled(true);

				lblLabel1.setText("Record ID:");
				lblLabel1.setVisible(true);
				txtT1.setVisible(true);
			}
		});
		rdbtnGroupMainFunctions.add(rdbtnTransferRecord);
		rdbtnTransferRecord.setFont(new Font("Tahoma", Font.PLAIN, 25));
		rdbtnTransferRecord.setBounds(30, 306, 306, 37);
		pnlMain.add(rdbtnTransferRecord);

		separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(351, 11, 2, 418);
		pnlMain.add(separator_1);

		separator_2 = new JSeparator();
		separator_2.setBounds(22, 438, 958, 2);
		pnlMain.add(separator_2);

		txtT1 = new JTextField();
		txtT1.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent arg0)
			{
				boolean pass = true;
				if ((rdbtnEditARecord.isSelected()) || (rdbtnTransferRecord.isSelected()))
				{
					if (!isRecordIdFormatCorrect(txtT1.getText().trim()))
					{
						lblLabel2.setVisible(false);
						lblLabel3.setVisible(false);
						lblLabel4.setVisible(false);
						lblLabel5.setVisible(false);
						lblLabel6.setVisible(false);
						lblLabel7.setVisible(false);
						lblLabel8.setVisible(false);
						txtT2.setVisible(false);
						txtT3.setVisible(false);
						txtT4.setVisible(false);
						txtT5.setVisible(false);
						txtT6.setVisible(false);
						btnAddToList.setVisible(false);
						chckbxActive.setVisible(false);
						rdbtnField1.setVisible(false);
						rdbtnField2.setVisible(false);
						rdbtnField3.setVisible(false);
						txtT2.setText("");
						txtT3.setText("");
						txtT4.setText("");
						txtT5.setText("");
						txtT6.setText("");
						chckbxActive.setSelected(false);
						lblDay.setVisible(false);
						lblMonth.setVisible(false);
						lblYear.setVisible(false);
						cmbxDay.setVisible(false);
						cmbxMonth.setVisible(false);
						cmbxYear.setVisible(false);
						rdbtnGroupFields.clearSelection();

						txtrOutput.setText("Incorrect ID format!");

						pass = false;
					} else
					{
						ManagerClientCORBA mng = new ManagerClientCORBA(cmbxManagers.getSelectedItem().toString());
						if (!mng.callRecordExist(txtT1.getText().toUpperCase().trim()))
						{
							lblLabel2.setVisible(false);
							lblLabel3.setVisible(false);
							lblLabel4.setVisible(false);
							lblLabel5.setVisible(false);
							lblLabel6.setVisible(false);
							lblLabel7.setVisible(false);
							lblLabel8.setVisible(false);
							txtT2.setVisible(false);
							txtT3.setVisible(false);
							txtT4.setVisible(false);
							txtT5.setVisible(false);
							txtT6.setVisible(false);
							btnAddToList.setVisible(false);
							chckbxActive.setVisible(false);
							rdbtnField1.setVisible(false);
							rdbtnField2.setVisible(false);
							rdbtnField3.setVisible(false);
							txtT2.setText("");
							txtT3.setText("");
							txtT4.setText("");
							txtT5.setText("");
							txtT6.setText("");
							chckbxActive.setSelected(false);
							lblDay.setVisible(false);
							lblMonth.setVisible(false);
							lblYear.setVisible(false);
							cmbxDay.setVisible(false);
							cmbxMonth.setVisible(false);
							cmbxYear.setVisible(false);
							rdbtnGroupFields.clearSelection();

							txtrOutput.setText("There is no record with this ID in "
									+ cmbxManagers.getSelectedItem().toString().substring(0, 3) + " server");

							pass = false;
						} else
						{
							txtrOutput.setText("Correct and existed record ID. Go ahead ;-)");
						}
					}
				}

				if ((rdbtnEditARecord.isSelected()) && pass)
				{
					if (txtT1.getText().toUpperCase().contains("TR"))
					{
						rdbtnField1.setText("Address");
						rdbtnField2.setText("Phone");
						rdbtnField3.setText("Location");
						rdbtnField1.setBounds(576, 64, 235, 37);
						rdbtnField2.setBounds(576, 105, 235, 37);
						rdbtnField3.setBounds(576, 146, 235, 37);
						rdbtnField1.setVisible(true);
						rdbtnField2.setVisible(true);
						rdbtnField3.setVisible(true);
					}

					if (txtT1.getText().toUpperCase().contains("SR"))
					{
						rdbtnField1.setText("Courses");
						rdbtnField2.setText("Status");
						rdbtnField3.setText("Status Date");
						rdbtnField1.setBounds(576, 64, 235, 37);
						rdbtnField2.setBounds(576, 105, 235, 37);
						rdbtnField3.setBounds(576, 146, 235, 37);
						rdbtnField1.setVisible(true);
						rdbtnField2.setVisible(true);
						rdbtnField3.setVisible(true);

						coursesNewList.clear();
					}
				}

				if ((rdbtnTransferRecord.isSelected()) && pass)
				{
					lblLabel2.setText("City to Transfer:");
					lblLabel2.setVisible(true);
					txtT2.setVisible(true);
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

		lblOutputmessageresult = new JLabel("Message:");
		lblOutputmessageresult.setForeground(new Color(0, 0, 128));
		lblOutputmessageresult.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblOutputmessageresult.setBounds(22, 439, 306, 29);
		pnlMain.add(lblOutputmessageresult);

		txtT6 = new JTextField();
		txtT6.setFont(new Font("Tahoma", Font.PLAIN, 25));
		txtT6.setBounds(576, 229, 235, 35);
		pnlMain.add(txtT6);
		txtT6.setColumns(10);

		chckbxActive = new JCheckBox(" (Check if Active)");
		chckbxActive.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{

			}
		});
		chckbxActive.setFont(new Font("Tahoma", Font.PLAIN, 25));
		chckbxActive.setBounds(576, 274, 234, 37);
		pnlMain.add(chckbxActive);

		cmbxDay = new JComboBox<String>();
		cmbxDay.setFont(new Font("Tahoma", Font.PLAIN, 25));
		cmbxDay.setBounds(646, 355, 66, 35);
		pnlMain.add(cmbxDay);

		cmbxMonth = new JComboBox<String>();
		cmbxMonth.setFont(new Font("Tahoma", Font.PLAIN, 25));
		cmbxMonth.setBounds(578, 355, 66, 35);
		pnlMain.add(cmbxMonth);

		cmbxYear = new JComboBox<String>();
		cmbxYear.setFont(new Font("Tahoma", Font.PLAIN, 25));
		cmbxYear.setBounds(714, 355, 97, 35);
		pnlMain.add(cmbxYear);

		btnAddToList = new JButton("Add to List");
		btnAddToList.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (rdbtnCreateTeacher.isSelected())
				{
					if (txtT6.getText().toString().trim().length() == 0)
					{
						// if (specializations.size() == 0)
						txtrOutput.setText("Error! Please enter the specialization");
					} else
					{
						specializations.add(txtT6.getText().toString().trim());
						String out = "Inserted Specilizations: ";
						for (int i = 0; i < specializations.size(); i++)
						{
							out = out + "[" + specializations.get(i) + "] ";
						}
						txtrOutput.setText(out);
						txtT6.setText("");
					}
				}

				if (rdbtnCreateStudent.isSelected())
				{
					if (txtT6.getText().toString().trim().length() == 0)
					{
						if (courses.size() == 0)
							txtrOutput.setText("Error! Please enter the course.");
					} else
					{
						courses.add(txtT6.getText().toString().trim());
						String out = "Inserted Courses: ";
						for (int i = 0; i < courses.size(); i++)
						{
							out = out + "[" + courses.get(i) + "] ";
						}
						txtrOutput.setText(out);
						txtT6.setText("");
					}
				}

				if (rdbtnEditARecord.isSelected())
				{
					if (txtT6.getText().toString().trim().length() == 0)
					{
						if (courses.size() == 0)
							txtrOutput.setText("Error! Please enter the course.");
					} else
					{
						if (!chckbxActive.isSelected())
						{
							coursesNewList.addAll(coursesExisting);
							coursesExisting.clear();
						} else
						{
							coursesNewList.clear();
						}
						coursesNewList.add(txtT6.getText().toString().trim());
						String out = "Added Courses: ";
						for (int i = 0; i < coursesNewList.size(); i++)
						{
							out = out + "[" + coursesNewList.get(i) + "] ";
						}
						txtrOutput.setText(out);
						txtT6.setText("");
					}
				}
			}
		});
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
		lblLabel1.setBounds(361, 28, 210, 29);
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
		lblDay.setBounds(664, 321, 48, 29);
		pnlMain.add(lblDay);

		lblMonth = new JLabel("MM");
		lblMonth.setForeground(new Color(0, 0, 128));
		lblMonth.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblMonth.setBounds(593, 322, 48, 29);
		pnlMain.add(lblMonth);

		lblYear = new JLabel("YYYY");
		lblYear.setForeground(new Color(0, 0, 128));
		lblYear.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblYear.setBounds(726, 322, 66, 29);
		pnlMain.add(lblYear);

		rdbtnField1 = new JRadioButton("Field1");
		rdbtnField1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (rdbtnEditARecord.isSelected())
				{
					lblLabel2.setVisible(false);
					lblLabel3.setVisible(false);
					lblLabel4.setVisible(false);
					lblLabel5.setVisible(false);
					lblLabel6.setVisible(false);
					lblLabel7.setVisible(false);
					lblLabel8.setVisible(false);
					txtT2.setVisible(false);
					txtT3.setVisible(false);
					txtT4.setVisible(false);
					txtT5.setVisible(false);
					txtT6.setVisible(false);
					btnAddToList.setVisible(false);
					chckbxActive.setVisible(false);
					txtT2.setText("");
					txtT3.setText("");
					txtT4.setText("");
					txtT5.setText("");
					txtT6.setText("");
					chckbxActive.setSelected(false);
					lblDay.setVisible(false);
					lblMonth.setVisible(false);
					lblYear.setVisible(false);
					cmbxDay.setVisible(false);
					cmbxMonth.setVisible(false);
					cmbxYear.setVisible(false);
					txtrOutput.setText("");

					if (txtT1.getText().toString().trim().toUpperCase().contains("TR"))
					{
						lblLabel6.setText("Address:");
						lblLabel6.setVisible(true);
						txtT6.setVisible(true);
					} else
					{
						lblLabel6.setText("Add Course:");
						lblLabel6.setVisible(true);
						txtT6.setVisible(true);
						btnAddToList.setVisible(true);
						lblLabel7.setText("Reset then Add:");
						lblLabel7.setVisible(true);
						chckbxActive.setText("(Check to Rest)");
						chckbxActive.setVisible(true);

						// StudentRecord std = null;
						// try
						// {
						// ManagerClient mng = new
						// ManagerClient(cmbManagers.getSelectedItem().toString());
						// std = (StudentRecord) mng.callReturnRecord(txtT1.getText().toUpperCase());
						// }
						// catch (RemoteException e)
						// {
						// //e.printStackTrace();
						// }
						// coursesExisting = std.getCoursesRegistred();
						//
						// String out = "Registered Courses: ";
						// for (int i = 0; i < coursesExisting.size(); i ++)
						// {
						// out = out + "[" + coursesExisting.get(i) + "] ";
						// }
						// txtrOutput.setText(out);
					}
				}

				if (rdbtnTransferRecord.isSelected())
				{
					lblLabel2.setVisible(false);
					lblLabel3.setVisible(false);
					lblLabel4.setVisible(false);
					lblLabel5.setVisible(false);
					lblLabel6.setVisible(false);
					lblLabel7.setVisible(false);
					lblLabel8.setVisible(false);
					txtT2.setVisible(false);
					txtT3.setVisible(false);
					txtT4.setVisible(false);
					txtT5.setVisible(false);
					txtT6.setVisible(false);
					btnAddToList.setVisible(false);
					chckbxActive.setVisible(false);
					txtT2.setText("");
					txtT3.setText("");
					txtT4.setText("");
					txtT5.setText("");
					txtT6.setText("");
					chckbxActive.setSelected(false);
					lblDay.setVisible(false);
					lblMonth.setVisible(false);
					lblYear.setVisible(false);
					cmbxDay.setVisible(false);
					cmbxMonth.setVisible(false);
					cmbxYear.setVisible(false);
					txtrOutput.setText("");
				}
			}
		});
		rdbtnGroupFields.add(rdbtnField1);
		rdbtnField1.setFont(new Font("Tahoma", Font.PLAIN, 25));
		rdbtnField1.setBounds(829, 7, 235, 37);
		pnlMain.add(rdbtnField1);

		rdbtnField2 = new JRadioButton("Field2");
		rdbtnField2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (rdbtnEditARecord.isSelected())
				{
					lblLabel2.setVisible(false);
					lblLabel3.setVisible(false);
					lblLabel4.setVisible(false);
					lblLabel5.setVisible(false);
					lblLabel6.setVisible(false);
					lblLabel7.setVisible(false);
					lblLabel8.setVisible(false);
					txtT2.setVisible(false);
					txtT3.setVisible(false);
					txtT4.setVisible(false);
					txtT5.setVisible(false);
					txtT6.setVisible(false);
					btnAddToList.setVisible(false);
					chckbxActive.setVisible(false);
					txtT2.setText("");
					txtT3.setText("");
					txtT4.setText("");
					txtT5.setText("");
					txtT6.setText("");
					chckbxActive.setSelected(false);
					lblDay.setVisible(false);
					lblMonth.setVisible(false);
					lblYear.setVisible(false);
					cmbxDay.setVisible(false);
					cmbxMonth.setVisible(false);
					cmbxYear.setVisible(false);
					txtrOutput.setText("");

					if (txtT1.getText().trim().toUpperCase().contains("TR"))
					{
						lblLabel6.setText("Phone:");
						lblLabel6.setVisible(true);
						txtT6.setVisible(true);
					} else
					{
						lblLabel7.setText("Status:");
						lblLabel7.setVisible(true);
						chckbxActive.setText("(Check if Active)");
						chckbxActive.setVisible(true);

						coursesNewList.clear();
					}
				}

				if (rdbtnTransferRecord.isSelected())
				{
					lblLabel2.setVisible(false);
					lblLabel3.setVisible(false);
					lblLabel4.setVisible(false);
					lblLabel5.setVisible(false);
					lblLabel6.setVisible(false);
					lblLabel7.setVisible(false);
					lblLabel8.setVisible(false);
					txtT2.setVisible(false);
					txtT3.setVisible(false);
					txtT4.setVisible(false);
					txtT5.setVisible(false);
					txtT6.setVisible(false);
					btnAddToList.setVisible(false);
					chckbxActive.setVisible(false);
					txtT2.setText("");
					txtT3.setText("");
					txtT4.setText("");
					txtT5.setText("");
					txtT6.setText("");
					chckbxActive.setSelected(false);
					lblDay.setVisible(false);
					lblMonth.setVisible(false);
					lblYear.setVisible(false);
					cmbxDay.setVisible(false);
					cmbxMonth.setVisible(false);
					cmbxYear.setVisible(false);
					txtrOutput.setText("");
				}
			}
		});
		rdbtnGroupFields.add(rdbtnField2);
		rdbtnField2.setFont(new Font("Tahoma", Font.PLAIN, 25));
		rdbtnField2.setBounds(829, 48, 235, 37);
		pnlMain.add(rdbtnField2);

		rdbtnField3 = new JRadioButton("Field3");
		rdbtnField3.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (rdbtnEditARecord.isSelected())
				{
					lblLabel2.setVisible(false);
					lblLabel3.setVisible(false);
					lblLabel4.setVisible(false);
					lblLabel5.setVisible(false);
					lblLabel6.setVisible(false);
					lblLabel7.setVisible(false);
					lblLabel8.setVisible(false);
					txtT2.setVisible(false);
					txtT3.setVisible(false);
					txtT4.setVisible(false);
					txtT5.setVisible(false);
					txtT6.setVisible(false);
					btnAddToList.setVisible(false);
					chckbxActive.setVisible(false);
					txtT2.setText("");
					txtT3.setText("");
					txtT4.setText("");
					txtT5.setText("");
					txtT6.setText("");
					chckbxActive.setSelected(false);
					txtrOutput.setText("");

					if (txtT1.getText().trim().toUpperCase().contains("TR"))
					{
						lblLabel6.setText("Location:");
						lblLabel6.setVisible(true);
						txtT6.setVisible(true);
					} else
					{
						lblLabel7.setText("Status Date:");
						lblLabel7.setVisible(true);
						lblDay.setText("DD");
						lblMonth.setText("MM");
						lblYear.setText("YYYY");
						cmbxDay.setBounds(646, 274, 66, 35);
						cmbxMonth.setBounds(578, 274, 66, 35);
						cmbxYear.setBounds(714, 274, 97, 35);
						lblDay.setBounds(664, 241, 48, 29);
						lblMonth.setBounds(593, 241, 48, 29);
						lblYear.setBounds(726, 241, 66, 29);
						lblDay.setVisible(true);
						lblMonth.setVisible(true);
						lblYear.setVisible(true);
						cmbxDay.setVisible(true);
						cmbxMonth.setVisible(true);
						cmbxYear.setVisible(true);

						coursesNewList.clear();
					}
				}

				if (rdbtnTransferRecord.isSelected())
				{
					lblLabel2.setVisible(false);
					lblLabel3.setVisible(false);
					lblLabel4.setVisible(false);
					lblLabel5.setVisible(false);
					lblLabel6.setVisible(false);
					lblLabel7.setVisible(false);
					lblLabel8.setVisible(false);
					txtT2.setVisible(false);
					txtT3.setVisible(false);
					txtT4.setVisible(false);
					txtT5.setVisible(false);
					txtT6.setVisible(false);
					btnAddToList.setVisible(false);
					chckbxActive.setVisible(false);
					txtT2.setText("");
					txtT3.setText("");
					txtT4.setText("");
					txtT5.setText("");
					txtT6.setText("");
					chckbxActive.setSelected(false);
					txtrOutput.setText("");
				}
			}
		});
		rdbtnGroupFields.add(rdbtnField3);
		rdbtnField3.setFont(new Font("Tahoma", Font.PLAIN, 25));
		rdbtnField3.setBounds(829, 85, 235, 37);
		pnlMain.add(rdbtnField3);

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

		btnAddManager = new JButton("Add");
		btnAddManager.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (rdbtnAddManager.isSelected())
				{
					addManager();
				}
			}
		});
		btnAddManager.setFont(new Font("Tahoma", Font.PLAIN, 25));
		btnAddManager.setBounds(89, 124, 155, 37);
		pnlMain.add(btnAddManager);

		btnRunByAll = new JButton("Run By All Managers");
		btnRunByAll.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (rdbtnCreateTeacher.isSelected())
				{
					createTeacher(true);
				}

				if (rdbtnCreateStudent.isSelected())
				{
					createStudent(true);
				}

				if (rdbtnGetRecordsCount.isSelected())
				{
					getRecordsCount(true);
				}

				if (rdbtnEditARecord.isSelected())
				{
					editARecord(true);
				}
				if (rdbtnTransferRecord.isSelected())
				{
					transferRecord(true);
				}
			}
		});
		btnRunByAll.setFont(new Font("Tahoma", Font.PLAIN, 25));
		btnRunByAll.setBounds(22, 392, 317, 37);
		pnlMain.add(btnRunByAll);
	}

	private void invisibleAll()
	{
		// rdbtnTransferRecord.setVisible(false);

		cmbxDay.setBounds(646, 355, 66, 35);
		cmbxMonth.setBounds(578, 355, 66, 35);
		cmbxYear.setBounds(714, 355, 97, 35);
		lblDay.setBounds(664, 322, 48, 29);
		lblMonth.setBounds(593, 322, 48, 29);
		lblYear.setBounds(726, 322, 66, 29);
		lblLabel6.setBounds(361, 233, 210, 29);
		txtT6.setBounds(576, 229, 235, 35);
		btnAddToList.setBounds(819, 227, 160, 37);
		lblLabel7.setBounds(360, 275, 210, 29);
		chckbxActive.setBounds(576, 274, 234, 37);
		lblLabel8.setBounds(361, 358, 210, 29);

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
		cmbxDay.setVisible(false);
		cmbxMonth.setVisible(false);
		cmbxYear.setVisible(false);
		rdbtnField1.setVisible(false);
		rdbtnField2.setVisible(false);
		rdbtnField3.setVisible(false);
		txtT1.setText("");
		txtT2.setText("");
		txtT3.setText("");
		txtT4.setText("");
		txtT5.setText("");
		txtT6.setText("");
		chckbxActive.setSelected(false);
		txtrOutput.setText("");
		rdbtnGroupFields.clearSelection();
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
		if (!(ch.equals('T') || ch.equals('S')))
		{
			return false;
		}

		ch = id.toUpperCase().charAt(1);

		if (!(ch.equals('R')))
		{
			return false;
		}

		if (!(id.substring(2, 5).chars().allMatch(Character::isDigit)))
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

		if (!(id.substring(0, 3).toUpperCase().equals("MTL") || id.substring(0, 3).toUpperCase().equals("LVL")
				|| id.substring(0, 3).toUpperCase().equals("DDO")))
		{
			return false;
		}

		if (!(id.substring(3, 4).chars().allMatch(Character::isDigit)))
		{
			return false;
		}

		return true;
	}

	@SuppressWarnings("deprecation")
	private void init()
	{
		managersList.add("MTL0001");
		cmbxManagers.addItem("MTL0001");
		managersList.add("LVL0001");
		cmbxManagers.addItem("LVL0001");
		managersList.add("DDO0001");
		cmbxManagers.addItem("DDO0001");

		for (Integer i = 1; i < 32; i++)
		{
			cmbxDay.addItem(i.toString());
		}
		for (Integer i = 1; i < 13; i++)
		{
			cmbxMonth.addItem(i.toString());
		}
		for (Integer i = 2009; i < 2019; i++)
		{
			cmbxYear.addItem(i.toString());
		}
		Date date1 = new Date();
		date1.getTime();
		cmbxDay.setSelectedItem(date1.getDate() + "");
		cmbxMonth.setSelectedItem((date1.getMonth() + 1) + "");
		cmbxYear.setSelectedItem("2018");
	}

	private void addManager()
	{
		boolean pass = true;
		if (!isManagerIdFormatCorrect(txtT1.getText().trim()))
		{
			txtrOutput.setText("Invalid Manager ID");
			pass = false;
		}

		if (managersList.contains(txtT1.getText().trim()))
		{
			txtrOutput.setText(txtT1.getText().trim() + " already exists");
			pass = false;
		}

		if (pass)
		{
			managersList.add(txtT1.getText().trim().toUpperCase());
			cmbxManagers.addItem(txtT1.getText().trim().toUpperCase());
			txtrOutput.setText(txtT1.getText().trim().toUpperCase() + " is added successfuly");
		}
	}

	private void createTeacher(boolean simultaneously)
	{
		boolean pass = true;

		if (!simultaneously)
		{
			if (!cmbxManagers.getSelectedItem().toString().subSequence(0, 3)
					.equals(txtT5.getText().trim().toUpperCase()))
			{
				txtrOutput.setText(
						"Error! Manager " + cmbxManagers.getSelectedItem() + " can only add record to Location "
								+ cmbxManagers.getSelectedItem().toString().substring(0, 3));
				pass = false;
			}
		} else
		{
			if (!(txtT5.getText().trim().toUpperCase().equals("MTL")
					|| (txtT5.getText().trim().toUpperCase().equals("LVL"))
					|| (txtT5.getText().trim().toUpperCase().equals("DDO"))))
			{
				txtrOutput.setText("Error! Location can be only MTL, LVL, or DDO.");
				pass = false;
			}
		}
		if (specializations.size() == 0)
		{
			txtrOutput.setText("Error! You need to add at least one specilization.");
			pass = false;
		}
		if (!(txtT4.getText().toString().chars().allMatch(Character::isDigit)))
		{
			txtrOutput.setText("Error! Phone number can only have digits.");
			pass = false;
		}
		if ((txtT1.getText().toString().length() == 0) || (txtT2.getText().toString().length() == 0)
				|| (txtT3.getText().toString().length() == 0) || (txtT4.getText().toString().length() == 0)
				|| (txtT5.getText().toString().length() == 0))
		{
			txtrOutput.setText("Error! You need to fill all the fields.");
			pass = false;
		}

		if (pass)
		{
			if (!simultaneously)
			{
				ManagerClientCORBA mng = new ManagerClientCORBA("CreateTeacher",
						cmbxManagers.getSelectedItem().toString(), txtT1.getText().trim(), txtT2.getText().trim(),
						txtT3.getText().trim(), Integer.valueOf(txtT4.getText().trim()), specializations);

				if (mng.callCreateTRecord())
				{
					txtrOutput.setText("Teacher record created by calling remote server");
				} else
				{
					txtrOutput.setText("Error! For more information see the log files");
				}
			} else
			{
				for (int i = 0; i < cmbxManagers.getItemCount(); i++)
				{
					if (cmbxManagers.getItemAt(i).toUpperCase().subSequence(0, 3)
							.equals(txtT5.getText().trim().toUpperCase()))
					{
						ManagerClientCORBA mng = new ManagerClientCORBA("CreateTeacher", cmbxManagers.getItemAt(i),
								txtT1.getText().trim(), txtT2.getText().trim(), txtT3.getText().trim(),
								Integer.valueOf(txtT4.getText().trim()), specializations);

						mng.start();
					}
				}
				txtrOutput.setText("Concurrent call by \"ALL\" the Managers on the city \""
						+ txtT5.getText().trim().toUpperCase() + "\"!\n" + "To see the results, check the log files.");
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void createStudent(boolean simultaneously)
	{
		boolean pass = true;
		if ((Integer.parseInt(cmbxMonth.getSelectedItem().toString()) == 2)
				&& (Integer.parseInt(cmbxDay.getSelectedItem().toString()) > 28))
		{
			txtrOutput.setText("Error! 2nd month of year doesn't have more than 28 days!");
			pass = false;
		}
		if (((Integer.parseInt(cmbxMonth.getSelectedItem().toString()) == 4)
				|| ((Integer.parseInt(cmbxMonth.getSelectedItem().toString()) == 6))
				|| ((Integer.parseInt(cmbxMonth.getSelectedItem().toString()) == 9))
				|| ((Integer.parseInt(cmbxMonth.getSelectedItem().toString()) == 11)))
				&& (Integer.parseInt(cmbxDay.getSelectedItem().toString()) > 30))
		{
			txtrOutput.setText("Error! " + cmbxMonth.getSelectedItem().toString()
					+ "th month of year doesn't have more than 30 days!");
			pass = false;
		}
		if (courses.size() == 0)
		{
			txtrOutput.setText("Error! You need to add at least one course.");
			pass = false;
		}
		if ((txtT1.getText().toString().length() == 0) || (txtT2.getText().toString().length() == 0))
		{
			txtrOutput.setText("Error! You need to fill all the fields.");
			pass = false;
		}

		if (pass)
		{
			Date date = new Date();
			date.getTime();
			date.setDate(Integer.parseInt(cmbxDay.getSelectedItem().toString()));
			date.setMonth(Integer.parseInt(cmbxMonth.getSelectedItem().toString()));
			if (!simultaneously)
			{
				ManagerClientCORBA mng = new ManagerClientCORBA("CreateStudent",
						cmbxManagers.getSelectedItem().toString(), txtT1.getText().trim(), txtT2.getText().trim(),
						courses, chckbxActive.isSelected(), date);

				if (mng.callCreateSRecord())
				{
					txtrOutput.setText("Student record created by calling remote server");
				} else
				{
					txtrOutput.setText("Error! For more information see the log files");
				}
			} else
			{
				for (int i = 0; i < cmbxManagers.getItemCount(); i++)
				{
					ManagerClientCORBA mng = new ManagerClientCORBA("CreateStudent", cmbxManagers.getItemAt(i),
							txtT1.getText().trim(), txtT2.getText().trim(), courses, chckbxActive.isSelected(), date);

					mng.start();
				}
				txtrOutput.setText("Concurrent call by ALL the existing Managers in the list.\n"
						+ "To see the results, check the log files.");
			}
		}
	}

	private void getRecordsCount(boolean simultaneously)
	{
		if (!simultaneously)
		{
			ManagerClientCORBA mng = new ManagerClientCORBA("GetCounts", cmbxManagers.getSelectedItem().toString());
			txtrOutput.setText(mng.callGetRecordCounts());
		} else
		{
			for (int i = 0; i < cmbxManagers.getItemCount(); i++)
			{
				ManagerClientCORBA mng = new ManagerClientCORBA("GetCounts", cmbxManagers.getItemAt(i));
				mng.start();
			}
			txtrOutput.setText("Concurrent call by ALL the existing Managers in the list\n"
					+ "To see the results, check the log files.");
		}
	}

	@SuppressWarnings("deprecation")
	private void editARecord(boolean simultaneously)
	{
		if (txtT1.getText().toString().length() == 0)
		{
			txtrOutput.setText("Error! You need to insert a correct recordID");
			return;
		}

		if (txtT1.getText().trim().toUpperCase().contains("SR")) // Student record should change
		{
			if (rdbtnField1.isSelected()) // Course Edit
			{
				if ((coursesNewList.size() == 0) && !chckbxActive.isSelected())
				{
					txtrOutput.setText("Error! You need to add at least one course before change.");
				} else
				{
					if (!simultaneously)
					{
						ManagerClientCORBA mng = new ManagerClientCORBA("EditRecords",
								cmbxManagers.getSelectedItem().toString(), txtT1.getText().trim().toUpperCase(),
								"coursesRegistred", coursesNewList);
						if (mng.callEditRecord())
						{
							txtrOutput.setText("The record is edited by calling remote server");
						} else
						{
							txtrOutput.setText("Error! For more information see the log files");
						}
					} else
					{
						for (int i = 0; i < cmbxManagers.getItemCount(); i++)
						{
							if (cmbxManagers.getItemAt(i).subSequence(0, 3)
									.equals(cmbxManagers.getSelectedItem().toString().subSequence(0, 3)))
							{
								ManagerClientCORBA mng = new ManagerClientCORBA("EditRecords",
										cmbxManagers.getItemAt(i), txtT1.getText().trim().toUpperCase(),
										"coursesRegistred", coursesNewList);
								mng.start();
							}

						}
						txtrOutput.setText("Concurrent call by \"ALL\" the Managers on the city \""
								+ cmbxManagers.getSelectedItem().toString().subSequence(0, 3) + "\"!\n"
								+ "To see the results, check the log files.");
					}
				}
			}

			if (rdbtnField2.isSelected()) // Status Edit
			{
				if (!simultaneously)
				{
					ManagerClientCORBA mng = new ManagerClientCORBA("EditRecords",
							cmbxManagers.getSelectedItem().toString(), txtT1.getText().trim().toUpperCase(), "status",
							chckbxActive.isSelected());
					if (mng.callEditRecord())
					{
						txtrOutput.setText("The record is edited by calling remote server");
					} else
					{
						txtrOutput.setText("Error! For more information see the log files");
					}
				} else
				{
					for (int i = 0; i < cmbxManagers.getItemCount(); i++)
					{
						if (cmbxManagers.getItemAt(i).subSequence(0, 3)
								.equals(cmbxManagers.getSelectedItem().toString().subSequence(0, 3)))
						{
							ManagerClientCORBA mng = new ManagerClientCORBA("EditRecords", cmbxManagers.getItemAt(i),
									txtT1.getText().trim().toUpperCase(), "status", chckbxActive.isSelected());
							mng.start();
						}
					}
					txtrOutput.setText("Concurrent call by \"ALL\" the Managers on the city \""
							+ cmbxManagers.getSelectedItem().toString().subSequence(0, 3) + "\"!\n"
							+ "To see the results, check the log files.");
				}
			}

			if (rdbtnField3.isSelected()) // Status Date Edit
			{
				Date date2 = new Date();
				date2.getTime();
				date2.setDate(Integer.parseInt(cmbxDay.getSelectedItem().toString()));
				date2.setMonth(Integer.parseInt(cmbxMonth.getSelectedItem().toString()));
				date2.setYear(Integer.parseInt(cmbxYear.getSelectedItem().toString()));

				if (!simultaneously)
				{
					ManagerClientCORBA mng = new ManagerClientCORBA("EditRecords",
							cmbxManagers.getSelectedItem().toString(), txtT1.getText().trim().toUpperCase(),
							"statusDate", date2);
					if (mng.callEditRecord())
					{
						txtrOutput.setText("The record is edited by calling remote server");
					} else
					{
						txtrOutput.setText("Error! For more information see the log files");
					}
				} else
				{
					for (int i = 0; i < cmbxManagers.getItemCount(); i++)
					{
						if (cmbxManagers.getItemAt(i).subSequence(0, 3)
								.equals(cmbxManagers.getSelectedItem().toString().subSequence(0, 3)))
						{
							ManagerClientCORBA mng = new ManagerClientCORBA("EditRecords", cmbxManagers.getItemAt(i),
									txtT1.getText().trim().toUpperCase(), "statusDate", date2);
							mng.start();
						}
					}
					txtrOutput.setText("Concurrent call by \"ALL\" the Managers on the city \""
							+ cmbxManagers.getSelectedItem().toString().subSequence(0, 3) + "\"!\n"
							+ "To see the results, check the log files.");
				}
			}
		}

		if (txtT1.getText().trim().toUpperCase().contains("TR")) // Teacher record should change
		{
			if (rdbtnField1.isSelected()) // Address Edit
			{
				if (txtT6.getText().trim().length() > 0)
				{
					if (!simultaneously)
					{
						ManagerClientCORBA mng = new ManagerClientCORBA("EditRecords",
								cmbxManagers.getSelectedItem().toString(), txtT1.getText().trim().toUpperCase(),
								"address", txtT6.getText().trim());
						if (mng.callEditRecord())
						{
							txtrOutput.setText("The record is edited by calling remote server");
						} else
						{
							txtrOutput.setText("Error! For more information see the log files");
						}
					} else
					{
						for (int i = 0; i < cmbxManagers.getItemCount(); i++)
						{
							if (cmbxManagers.getItemAt(i).subSequence(0, 3)
									.equals(cmbxManagers.getSelectedItem().toString().subSequence(0, 3)))
							{
								ManagerClientCORBA mng = new ManagerClientCORBA("EditRecords",
										cmbxManagers.getItemAt(i), txtT1.getText().trim().toUpperCase(), "address",
										txtT6.getText().trim());
								mng.start();
							}
						}
						txtrOutput.setText("Concurrent call by \"ALL\" the Managers on the city \""
								+ cmbxManagers.getSelectedItem().toString().subSequence(0, 3) + "\"!\n"
								+ "To see the results, check the log files.");
					}
				} else
				{
					txtrOutput.setText("Error! Enter the value.");
				}
			}

			if (rdbtnField2.isSelected()) // phoneNumber Edit
			{
				if (txtT6.getText().trim().length() > 0)
				{
					if (!(txtT6.getText().toString().chars().allMatch(Character::isDigit)))
					{
						txtrOutput.setText("Error! Phone number can only have digits.");
					} else
					{
						if (!simultaneously)
						{
							ManagerClientCORBA mng = new ManagerClientCORBA("EditRecords",
									cmbxManagers.getSelectedItem().toString(), txtT1.getText().trim().toUpperCase(),
									"phoneNumber", Integer.valueOf(txtT6.getText().trim()));
							if (mng.callEditRecord())
							{
								txtrOutput.setText("The record is edited by calling remote server");
							} else
							{
								txtrOutput.setText("Error! For more information see the log files");
							}
						} else
						{
							for (int i = 0; i < cmbxManagers.getItemCount(); i++)
							{
								if (cmbxManagers.getItemAt(i).subSequence(0, 3)
										.equals(cmbxManagers.getSelectedItem().toString().subSequence(0, 3)))
								{
									ManagerClientCORBA mng = new ManagerClientCORBA("EditRecords",
											cmbxManagers.getItemAt(i), txtT1.getText().trim().toUpperCase(),
											"phoneNumber", Integer.valueOf(txtT6.getText().trim()));
									mng.start();
								}
							}
							txtrOutput.setText("Concurrent call by \"ALL\" the Managers on the city \""
									+ cmbxManagers.getSelectedItem().toString().subSequence(0, 3) + "\"!\n"
									+ "To see the results, check the log files.");
						}
					}
				} else
				{
					txtrOutput.setText("Error! Enter the value.");
				}
			}

			if (rdbtnField3.isSelected()) // location Edit
			{
				if (txtT6.getText().trim().toUpperCase().equals("MTL")
						|| txtT6.getText().trim().toUpperCase().equals("LVL")
						|| txtT6.getText().trim().toUpperCase().equals("DDO"))
				{
					if (!simultaneously)
					{
						ManagerClientCORBA mng = new ManagerClientCORBA("EditRecords",
								cmbxManagers.getSelectedItem().toString(), txtT1.getText().trim().toUpperCase(),
								"location", txtT6.getText().trim().toUpperCase());
						if (mng.callEditRecord())
						{
							txtrOutput.setText("The record is edited by calling remote server");
						} else
						{
							txtrOutput.setText("Error! For more information see the log files");
						}
					} else
					{
						for (int i = 0; i < cmbxManagers.getItemCount(); i++)
						{
							if (cmbxManagers.getItemAt(i).subSequence(0, 3)
									.equals(cmbxManagers.getSelectedItem().toString().subSequence(0, 3)))
							{
								ManagerClientCORBA mng = new ManagerClientCORBA("EditRecords",
										cmbxManagers.getItemAt(i), txtT1.getText().trim().toUpperCase(), "location",
										txtT6.getText().trim().toUpperCase());
								mng.start();
							}
						}
						txtrOutput.setText("Concurrent call by \"ALL\" the Managers on the city \""
								+ cmbxManagers.getSelectedItem().toString().subSequence(0, 3) + "\"!\n"
								+ "To see the results, check the log files.");
					}
				} else
				{
					txtrOutput.setText("Error! Only MTL, LVL, or DDO are valid for location.");
				}
			}
		}
	}

	private void transferRecord(boolean simultaneously)
	{
		if (txtT1.getText().toString().trim().length() == 0)
		{
			txtrOutput.setText("Error! You need to insert a correct recordID");
			return;
		}

		if (!(txtT2.getText().trim().toUpperCase().equals("MTL") || txtT2.getText().trim().toUpperCase().equals("LVL")
				|| txtT2.getText().trim().toUpperCase().equals("DDO")))
		{
			txtrOutput.setText("Error! The city can be only MTL, LVL, or DDO");
			return;
		}

		if (!simultaneously)
		{
			ManagerClientCORBA mng = new ManagerClientCORBA("TransferRecord", cmbxManagers.getSelectedItem().toString(),
					txtT1.getText().trim().toUpperCase(), txtT2.getText().trim().toUpperCase());
			if (mng.callTransferRecord())
			{
				txtrOutput.setText("The record is transfered by calling remote server");
			} else
			{
				txtrOutput.setText("Error! For more information see the log files");
			}
		} else
		{
			for (int i = 0; i < cmbxManagers.getItemCount(); i++)
			{
				if (cmbxManagers.getItemAt(i).subSequence(0, 3)
						.equals(cmbxManagers.getSelectedItem().toString().subSequence(0, 3)))
				{
					ManagerClientCORBA mng = new ManagerClientCORBA("TransferRecord", cmbxManagers.getItemAt(i),
							txtT1.getText().trim().toUpperCase(), txtT2.getText().trim().toUpperCase());
					mng.start();
				}

			}
			txtrOutput.setText("Concurrent call by \"ALL\" the Managers on the city \""
					+ cmbxManagers.getSelectedItem().toString().subSequence(0, 3) + "\"!\n"
					+ "To see the results, check the log files.");
		}
	}

	// Returns the main frame
	public JFrame getMainFrame()
	{
		return frameDCMS1;
	}
}
