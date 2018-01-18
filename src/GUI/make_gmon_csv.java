package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import Algorithms.Algo1;
import Algorithms.Algo2;
import Algorithms.WeightRecord;
import Database.CsvFile;
import Database.Wigle_ToCsv;
import Filters.Filters;
import SQL.MySql;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Label;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.DropMode;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ButtonGroup;
import javax.swing.JTable;
import javax.swing.JList;

/**
 * This is the main class
 * Store GUI and running the program;
 * @author Maor_Bakshi and Max
 *
 */
public class make_gmon_csv {

	private JFrame frame;
	private JTextField algo1_path_txt;
	private JButton algo_1_go_Btn;
	private JTextField algo2_path_txt1;
	private JLabel algo1_label_suc;
	private JTextField algo1_mac_txt;
	private JTextField algo2_path_txt2;
	private JTextField filter_adding_lat_min;
	private JTextField filter_adding_lat_max;
	private JTextField filter_adding_lon_min;
	private JTextField filter_adding_lon_max;
	private JTextField filter_adding_alt_min;
	private JTextField filter_adding_alt_max;
	private JTextField filter_adding_date_max;
	private JTextField filter_adding_date_min;
	private JTextField filter_adding_time_min;
	private JTextField filter_adding_time_max;
	private JTextField filter_adding_device;
	private JTextField path_txt_folder;
	private JTextField path_txt_file_import;
	public JLabel records_count_lbl;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					make_gmon_csv window = new make_gmon_csv();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	String root_dir = System.getProperty("user.dir");
	CsvFile csv_file = null;
	String db_file_path = root_dir + "\\DB\\DB.csv";
	String db_directory_path = root_dir + "\\DB";
	private JTextField merge_filter_file_path;
	private JRadioButton and_radio_btn;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	public make_gmon_csv() {
		initialize();
		try {
		
			File dir_file = new File(db_directory_path);
			boolean isExists = dir_file.exists();
			if (isExists == false || dir_file.isDirectory() == false) {
				dir_file.mkdir();
				
				PrintWriter pw = null;
				try {
				    pw = new PrintWriter(new File(db_file_path));
				} catch (FileNotFoundException e) {
				    e.printStackTrace();
				}
				pw.write("");
		        pw.close();
			} else {
				File file_file = new File(db_file_path);
				boolean isFileExists = file_file.exists();
				if (isFileExists == false) {
					PrintWriter pw = null;
					try {
					    pw = new PrintWriter(new File(db_file_path));
					} catch (FileNotFoundException e) {
					    e.printStackTrace();
					}
					pw.write("");
			        pw.close();
				}
			}
			
			csv_file = new CsvFile(db_file_path,false);
			int records_count =  csv_file.getRecords().size();
			records_count_lbl.setText("" + records_count);
		} catch(Exception ex ) {
			
		}
		
	}

	/**
	 * Initialize the contents of the frame.
	 * Also store the gui data and functionallity.
	 */
	
	
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				csv_file.writeToCsv(db_file_path);
				MySql.getInstance().UpdateSqlTable(csv_file.getRecords());
				MySql.getInstance().Close();
				
			}
		});
		frame.setBounds(100, 100, 975, 549);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(12, 13, 933, 476);
		frame.getContentPane().add(tabbedPane);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Data", null, panel_2, null);
		panel_2.setLayout(null);
		
		JLabel label_25 = new JLabel("\u05D1\u05D7\u05E8 \u05EA\u05D9\u05E7\u05D9\u05D9\u05D4 \u05E9\u05DB\u05D5\u05DC\u05DC\u05EA \u05D1\u05EA\u05D5\u05DB\u05D4 \u05E7\u05D1\u05E6\u05D9 \u05D0\u05E4\u05DC\u05D9\u05E7\u05E6\u05D9\u05D9\u05EA Wigle:");
		label_25.setHorizontalAlignment(SwingConstants.RIGHT);
		label_25.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_25.setBounds(22, 53, 321, 22);
		panel_2.add(label_25);
		
		path_txt_folder = new JTextField();
		path_txt_folder.setColumns(10);
		path_txt_folder.setBounds(34, 112, 166, 22);
		panel_2.add(path_txt_folder);
		
		JCheckBox is_space_checkbox = new JCheckBox("\u05E2\u05DD \u05E8\u05D5\u05D5\u05D7 ?");
		is_space_checkbox.setBounds(109, 334, 91, 25);
		panel_2.add(is_space_checkbox);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Algo", null, panel, null);
		panel.setLayout(null);
		
		JButton btnSelectFolder_wigles = new JButton("\u05D1\u05D7\u05E8 \u05EA\u05D9\u05E7\u05D9\u05D9\u05D4..");
		btnSelectFolder_wigles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select a csvs folder");
			    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    chooser.setAcceptAllFileFilterUsed(false);
			    
			    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			        path_txt_folder.setText(chooser.getSelectedFile().toString()); 
			    } else {
			        
			    }
			}
		});
		btnSelectFolder_wigles.setBounds(212, 111, 127, 25);
		panel_2.add(btnSelectFolder_wigles);
		
		JButton Go_btn_folder = new JButton("Go");
		Go_btn_folder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = path_txt_folder.getText();
				path_txt_folder.setText("");
				Wigle_ToCsv wigleToCsv = new Wigle_ToCsv(path);
				csv_file.addRecords(wigleToCsv.records);
				int count_records = csv_file.getRecords().size();
				records_count_lbl.setText("" + count_records);
			}
		});
		Go_btn_folder.setBounds(141, 167, 97, 25);
		panel_2.add(Go_btn_folder);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Csv", null, panel_3, null);
		panel_3.setLayout(null);
		
		
		JList csv_list = new JList();
		csv_list.setVisibleRowCount(12);
		csv_list.setBounds(24, 30, 876, 383);
		panel_3.add(csv_list);
		
		JLabel label_26 = new JLabel("\u05D9\u05D9\u05D1\u05D0 \u05DE\u05D1\u05E0\u05D4 \u05E0\u05EA\u05D5\u05E0\u05D9\u05DD \u05E7\u05D9\u05D9\u05DD (\u05D1\u05E4\u05D5\u05E8\u05DE\u05D8 \u05E9\u05DC\u05E0\u05D5):");
		label_26.setHorizontalAlignment(SwingConstants.RIGHT);
		label_26.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_26.setBounds(99, 268, 244, 22);
		panel_2.add(label_26);
		
		JButton btnSelectFile_import = new JButton("\u05D1\u05D7\u05E8 \u05E7\u05D5\u05D1\u05E5..");
		btnSelectFile_import.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select a csv file");
			    //FileNameExtensionFilter filter = new FileNameExtensionFilter("csv");
			    //chooser.setFileFilter(filter);
			    chooser.setAcceptAllFileFilterUsed(false);
			    
			    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			    	path_txt_file_import.setText(chooser.getSelectedFile().toString()); 
			    } else {
			        
			    }
			}
		});
		btnSelectFile_import.setBounds(212, 303, 127, 25);
		panel_2.add(btnSelectFile_import);
		
		path_txt_file_import = new JTextField();
		path_txt_file_import.setColumns(10);
		path_txt_file_import.setBounds(34, 303, 166, 22);
		panel_2.add(path_txt_file_import);
		
		JButton Go_btn_file_import = new JButton("Go");
		Go_btn_file_import.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean isColumnSpace = is_space_checkbox.isSelected();
				String file_path = path_txt_file_import.getText();
				path_txt_file_import.setText("");
				try {
					CsvFile file = new CsvFile(file_path, isColumnSpace);
					csv_file.addRecords(file.getRecords());
					int count_records = csv_file.getRecords().size();
					records_count_lbl.setText("" + count_records);
				} catch(Exception ex) {
					
				}
			}
		});
		Go_btn_file_import.setBounds(141, 376, 97, 25);
		panel_2.add(Go_btn_file_import);
		
		JButton btnCsv = new JButton("\u05D9\u05D9\u05E6\u05D0 CSV");
		btnCsv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
				Date date = new Date();
				String date_txt = dateFormat.format(date); //2016/11/16 12:08:43
				String directory_path = root_dir + "\\Export";
				String file_name = directory_path + "\\NewData_" + date_txt + ".csv";
				
				
				File dir_file = new File(directory_path);
				boolean isExists = dir_file.exists();
				if (isExists == false || dir_file.isDirectory() == false) {
					dir_file.mkdir();
				}
				
				csv_file.writeToCsv(file_name);
			}
		});
		btnCsv.setBounds(576, 278, 97, 25);
		panel_2.add(btnCsv);
		
		JButton btnKml = new JButton("\u05D9\u05D9\u05E6\u05D0 KML");
		btnKml.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
				Date date = new Date();
				String date_txt = dateFormat.format(date); //2016/11/16 12:08:43
				String directory_path = root_dir + "\\Export";
				String file_name = directory_path + "\\NewKml_" + date_txt + ".kml";
				
				
				File dir_file = new File(directory_path);
				boolean isExists = dir_file.exists();
				if (isExists == false || dir_file.isDirectory() == false) {
					dir_file.mkdir();
				}
				
				try {
					csv_file.writeToKml(file_name);
				} catch(Exception ex) {
					
				}
				
			}
		});
		btnKml.setBounds(724, 278, 97, 25);
		panel_2.add(btnKml);
		
		JButton delete_db_btn = new JButton("\u05DE\u05D7\u05D9\u05E7\u05EA \u05DE\u05D1\u05E0\u05D4 \u05D4\u05E0\u05EA\u05D5\u05E0\u05D9\u05DD");
		delete_db_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				csv_file.getRecords().clear();
				csv_file.writeToCsv(db_file_path);
			}
		});
		delete_db_btn.setBounds(576, 367, 166, 25);
		panel_2.add(delete_db_btn);
		
		JLabel lblNewLabel = new JLabel("\u05DB\u05DE\u05D5\u05EA \u05D4\u05E8\u05E9\u05D5\u05DE\u05D5\u05EA:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setBounds(702, 70, 103, 16);
		panel_2.add(lblNewLabel);
		
		records_count_lbl = new JLabel("0");
		records_count_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		records_count_lbl.setBounds(650, 70, 57, 16);
		panel_2.add(records_count_lbl);
		
		
		
		JLabel lblSelectCsvFile = new JLabel("\u05D1\u05D7\u05E8 \u05E7\u05D5\u05D1\u05E5 \u05D0\u05E7\u05E1\u05DC");
		lblSelectCsvFile.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSelectCsvFile.setBounds(65, 72, 243, 22);
		panel.add(lblSelectCsvFile);
		lblSelectCsvFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		JLabel lblWelcomeToThe = new JLabel("\u05D0\u05DC\u05D2\u05D5\u05E8\u05D9\u05EA\u05DD 1");
		lblWelcomeToThe.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcomeToThe.setBounds(142, 13, 109, 30);
		panel.add(lblWelcomeToThe);
		lblWelcomeToThe.setForeground(new Color(0, 139, 139));
		lblWelcomeToThe.setFont(new Font("Tahoma", Font.BOLD, 15));
		
		
		algo1_path_txt = new JTextField();
		algo1_path_txt.setBounds(10, 107, 298, 22);
		panel.add(algo1_path_txt);
		algo1_path_txt.setColumns(10);
		
		JButton algo_1_select_Btn = new JButton("\u05D1\u05D7\u05E8 \u05E7\u05D5\u05D1\u05E5..");
		algo_1_select_Btn.setBounds(320, 106, 102, 25);
		panel.add(algo_1_select_Btn);
		

		
		JLabel lblEnterMac = new JLabel("\u05D4\u05DB\u05E0\u05E1 MAC");
		lblEnterMac.setHorizontalAlignment(SwingConstants.TRAILING);
		lblEnterMac.setBounds(330, 141, 83, 22);
		panel.add(lblEnterMac);
		lblEnterMac.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		algo1_mac_txt = new JTextField();
		algo1_mac_txt.setBounds(122, 142, 186, 22);
		panel.add(algo1_mac_txt);
		algo1_mac_txt.setColumns(10);
		
		algo1_label_suc = new JLabel("Success");
		algo1_label_suc.setBounds(20, 142, 69, 21);
		panel.add(algo1_label_suc);
		algo1_label_suc.setFont(new Font("Tahoma", Font.BOLD, 14));
		algo1_label_suc.setForeground(new Color(0, 128, 0));
		
		algo_1_go_Btn = new JButton("Go");
		algo_1_go_Btn.setBounds(167, 191, 97, 25);
		panel.add(algo_1_go_Btn);
		
		Label algo1_result_label = new Label("Lan: 0 Lon: 0 Alt: 0");
		algo1_result_label.setBounds(25, 240, 397, 21);
		panel.add(algo1_result_label);
		algo1_result_label.setAlignment(Label.CENTER);
		algo1_result_label.setFont(new Font("Aharoni", Font.PLAIN, 17));
		
		JLabel lblAlgorithm = new JLabel("\u05D0\u05DC\u05D2\u05D5\u05E8\u05D9\u05EA\u05DD 2");
		lblAlgorithm.setHorizontalAlignment(SwingConstants.CENTER);
		lblAlgorithm.setBounds(618, 13, 109, 30);
		panel.add(lblAlgorithm);
		lblAlgorithm.setForeground(new Color(0, 139, 139));
		lblAlgorithm.setFont(new Font("Tahoma", Font.BOLD, 15));
		
		JLabel lblSelectFolderWhich = new JLabel("\u05D1\u05D7\u05E8 \u05E7\u05D5\u05D1\u05E5 \u05D0\u05E7\u05E1\u05DC \u05E2\u05DD GPS");
		lblSelectFolderWhich.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSelectFolderWhich.setBounds(598, 72, 178, 22);
		panel.add(lblSelectFolderWhich);
		lblSelectFolderWhich.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		algo2_path_txt1 = new JTextField();
		algo2_path_txt1.setBounds(478, 107, 298, 22);
		panel.add(algo2_path_txt1);
		algo2_path_txt1.setColumns(10);
		
		JButton algo_2_select_Btn1 = new JButton("\u05D1\u05D7\u05E8 \u05E7\u05D5\u05D1\u05E5..");
		algo_2_select_Btn1.setBounds(788, 106, 109, 25);
		panel.add(algo_2_select_Btn1);
		
		JLabel lblSelectWithoutGps = new JLabel("\u05D1\u05D7\u05E8 \u05E7\u05D5\u05D1\u05E5 \u05D0\u05E7\u05E1\u05DC \u05D1\u05DC\u05D9 GPS");
		lblSelectWithoutGps.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSelectWithoutGps.setBounds(565, 175, 212, 22);
		panel.add(lblSelectWithoutGps);
		lblSelectWithoutGps.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		algo2_path_txt2 = new JTextField();
		algo2_path_txt2.setBounds(478, 221, 298, 22);
		panel.add(algo2_path_txt2);
		algo2_path_txt2.setColumns(10);
		
		JLabel algo2_label_suc = new JLabel("Success");
		algo2_label_suc.setBounds(488, 267, 83, 25);
		panel.add(algo2_label_suc);
		algo2_label_suc.setForeground(new Color(34, 139, 34));
		algo2_label_suc.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JButton algo_2_select_Btn2 = new JButton("\u05D1\u05D7\u05E8 \u05E7\u05D5\u05D1\u05E5..");
		algo_2_select_Btn2.setBounds(788, 220, 109, 25);
		panel.add(algo_2_select_Btn2);
		
		JButton algo_2_go_Btn2 = new JButton("Go");
		algo_2_go_Btn2.setBounds(618, 268, 97, 25);
		panel.add(algo_2_go_Btn2);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(434, 13, 16, 420);
		panel.add(separator_1);
		separator_1.setOrientation(SwingConstants.VERTICAL);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Filters", null, panel_1, null);
		
	
		
	
		
		JSeparator separator = new JSeparator();
		separator.setBounds(287, 10, 16, 407);
		separator.setOrientation(SwingConstants.VERTICAL);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(620, 10, 16, 407);
		separator_2.setOrientation(SwingConstants.VERTICAL);
		
		JLabel label = new JLabel("\u05D4\u05D5\u05E1\u05E4\u05EA \u05E4\u05D9\u05DC\u05D8\u05E8");
		label.setBounds(77, 10, 121, 32);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.BOLD, 15));
		
		JLabel label_3 = new JLabel("\u05DE\u05D9\u05E7\u05D5\u05DD:");
		label_3.setBounds(216, 69, 48, 16);
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel label_4 = new JLabel("< lat <");
		label_4.setBounds(89, 68, 48, 16);
		label_4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_4.setHorizontalAlignment(SwingConstants.RIGHT);
		
		filter_adding_lat_min = new JTextField();
		filter_adding_lat_min.setBounds(28, 66, 63, 22);
		filter_adding_lat_min.setColumns(10);
		
		filter_adding_lat_max = new JTextField();
		filter_adding_lat_max.setBounds(141, 66, 63, 22);
		filter_adding_lat_max.setColumns(10);
		
		filter_adding_lon_min = new JTextField();
		filter_adding_lon_min.setBounds(28, 97, 63, 22);
		filter_adding_lon_min.setColumns(10);
		
		JLabel label_5 = new JLabel("< lon <");
		label_5.setBounds(89, 99, 48, 16);
		label_5.setHorizontalAlignment(SwingConstants.RIGHT);
		label_5.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		filter_adding_lon_max = new JTextField();
		filter_adding_lon_max.setBounds(141, 97, 63, 22);
		filter_adding_lon_max.setColumns(10);
		
		filter_adding_alt_min = new JTextField();
		filter_adding_alt_min.setBounds(28, 129, 63, 22);
		filter_adding_alt_min.setColumns(10);
		
		JLabel label_6 = new JLabel("< alt <");
		label_6.setBounds(89, 131, 48, 16);
		label_6.setHorizontalAlignment(SwingConstants.RIGHT);
		label_6.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		filter_adding_alt_max = new JTextField();
		filter_adding_alt_max.setBounds(141, 129, 63, 22);
		filter_adding_alt_max.setColumns(10);
		
		JLabel label_7 = new JLabel("\u05D6\u05DE\u05DF:");
		label_7.setBounds(216, 181, 48, 16);
		label_7.setHorizontalAlignment(SwingConstants.RIGHT);
		
		filter_adding_date_max = new JTextField();
		filter_adding_date_max.setBounds(159, 178, 63, 22);
		filter_adding_date_max.setColumns(10);
		
		JLabel label_8 = new JLabel("< date <");
		label_8.setBounds(89, 180, 63, 16);
		label_8.setHorizontalAlignment(SwingConstants.RIGHT);
		label_8.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		filter_adding_date_min = new JTextField();
		filter_adding_date_min.setBounds(28, 178, 63, 22);
		filter_adding_date_min.setColumns(10);
		
		filter_adding_time_min = new JTextField();
		filter_adding_time_min.setBounds(28, 210, 63, 22);
		filter_adding_time_min.setColumns(10);
		
		JLabel label_9 = new JLabel("< time <");
		label_9.setBounds(89, 212, 63, 16);
		label_9.setHorizontalAlignment(SwingConstants.RIGHT);
		label_9.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		filter_adding_time_max = new JTextField();
		filter_adding_time_max.setBounds(159, 210, 63, 22);
		filter_adding_time_max.setColumns(10);
		
		JLabel label_10 = new JLabel("\u05DE\u05D6\u05D4\u05D4 \u05DE\u05DB\u05E9\u05D9\u05E8:");
		label_10.setBounds(183, 267, 81, 16);
		label_10.setHorizontalAlignment(SwingConstants.RIGHT);
		
		filter_adding_device = new JTextField();
		filter_adding_device.setBounds(66, 299, 132, 22);
		filter_adding_device.setColumns(10);
		
		JLabel label_11 = new JLabel("%");
		label_11.setBounds(28, 299, 26, 16);
		label_11.setHorizontalAlignment(SwingConstants.RIGHT);
		label_11.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel label_12 = new JLabel("%");
		label_12.setBounds(193, 299, 26, 16);
		label_12.setHorizontalAlignment(SwingConstants.RIGHT);
		label_12.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel filter_curr_lat_min_lbl = new JLabel("");
		filter_curr_lat_min_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		filter_curr_lat_min_lbl.setBounds(330, 75, 81, 16);
		
		JLabel filter_curr_lat_max_lbl = new JLabel("");
		filter_curr_lat_max_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		filter_curr_lat_max_lbl.setBounds(455, 75, 77, 16);
		
		JLabel filter_curr_lon_min_lbl = new JLabel("");
		filter_curr_lon_min_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		filter_curr_lon_min_lbl.setBounds(330, 105, 81, 16);
		
		JLabel filter_curr_lon_max_lbl = new JLabel("");
		filter_curr_lon_max_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		filter_curr_lon_max_lbl.setBounds(455, 105, 77, 16);
		
		JLabel filter_curr_alt_min_lbl = new JLabel("");
		filter_curr_alt_min_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		filter_curr_alt_min_lbl.setBounds(330, 139, 81, 16);
		
		JLabel filter_curr_alt_max_lbl = new JLabel("");
		filter_curr_alt_max_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		filter_curr_alt_max_lbl.setBounds(455, 139, 77, 16);
		
		JLabel filter_curr_date_min_lbl = new JLabel("");
		filter_curr_date_min_lbl.setBounds(341, 188, 68, 16);
		
		JLabel filter_curr_date_max_lbl = new JLabel("");
		filter_curr_date_max_lbl.setBounds(471, 188, 68, 16);
		
		JLabel filter_curr_time_max_lbl = new JLabel("");
		filter_curr_time_max_lbl.setBounds(471, 219, 68, 16);
		
		JLabel filter_curr_time_min_lbl = new JLabel("");
		filter_curr_time_min_lbl.setBounds(351, 219, 63, 16);
		algo_2_go_Btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String data_file_path = algo2_path_txt1.getText();
				String input_file_path = algo2_path_txt2.getText();
				Algo2 algo2 = new Algo2(data_file_path,input_file_path);
				//CsvFile file = algo2.getCsvInputFile();
				CsvFile file = algo2.calculateNewGPSFile();
				Path p = Paths.get(input_file_path);
				String newFilePath = p.getParent().toString();
				file.writeToCsv(newFilePath + "\\NewData.csv");
				
				algo2_label_suc.setVisible(true);
			}
		});
		algo_2_select_Btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select a csv file");
			    //FileNameExtensionFilter filter = new FileNameExtensionFilter("csv");
			    //chooser.setFileFilter(filter);
			    chooser.setAcceptAllFileFilterUsed(false);
			    
			    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			    	String file_path = chooser.getSelectedFile().toString();
			    	algo2_path_txt2.setText(file_path); 
			    } else {
			        
			    }
			}
		});
		algo2_label_suc.setVisible(false);
		algo_2_select_Btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select a csv file");
			    //FileNameExtensionFilter filter = new FileNameExtensionFilter("csv");
			    //chooser.setFileFilter(filter);
			    chooser.setAcceptAllFileFilterUsed(false);
			    
			    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			    	String file_path = chooser.getSelectedFile().toString();
			    	algo2_path_txt1.setText(file_path); 
			    } else {
			        
			    }
				
			}
		});
		algo_1_go_Btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String file_path = algo1_path_txt.getText();
				String mac = algo1_mac_txt.getText();
				Algo1 algo1 = new Algo1(file_path,mac);
				WeightRecord record = algo1.getAlgoResult();
				algo1_result_label.setText(record.toString());
				
				algo1_label_suc.setVisible(true);
				
			}
		});
		algo1_label_suc.setVisible(false);
		algo_1_select_Btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select a csvs folder");
			    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			    chooser.setAcceptAllFileFilterUsed(false);
			    
			    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			        algo1_path_txt.setText(chooser.getSelectedFile().toString()); 
			    } else {
			        
			    }
				
			}
		});
		
		JButton add_filter_btn = new JButton("\u05D4\u05D5\u05E1\u05E3 \u05E4\u05D9\u05DC\u05D8\u05E8");
		add_filter_btn.setBounds(77, 372, 121, 25);
		add_filter_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Filters filter = new Filters();
				filter.readFiltersValues(filter_adding_lon_min.getText(), filter_adding_lon_max.getText(), 
						filter_adding_lat_min.getText(), filter_adding_lat_max.getText(), 
						filter_adding_alt_min.getText(), filter_adding_alt_max.getText(), 
						filter_adding_date_min.getText(), filter_adding_date_max.getText(), 
						filter_adding_time_min.getText(), filter_adding_time_max.getText(),filter_adding_device.getText());
				
				filter_curr_lon_min_lbl.setText("" +filter.min_lon); filter_curr_lon_max_lbl.setText("" + filter.max_lon);
				filter_curr_lat_min_lbl.setText("" + filter.min_lan); filter_curr_lat_max_lbl.setText(""+ filter.max_lan); 
				filter_curr_alt_min_lbl.setText(""+ filter.min_alt); filter_curr_alt_max_lbl.setText(""+ filter.max_alt); 
				filter_curr_date_min_lbl.setText(""+ filter.min_date); filter_curr_date_max_lbl.setText(""+ filter.max_date); 
				filter_curr_time_min_lbl.setText(""+ filter.min_time); filter_curr_time_max_lbl.setText(""+ filter.max_time);
				
				csv_file.getFilters().reset();
				csv_file.getFilters().addFilters(filter);
				
				CsvFile new_file = csv_file.getFilters().getCsvFile();
				records_count_lbl.setText("" + new_file.getRecords().size());
				
			}
		});
		
		JLabel label_2 = new JLabel("\u05DE\u05D9\u05D6\u05D5\u05D2 \u05E4\u05D9\u05DC\u05D8\u05E8\u05D9\u05DD");
		label_2.setBounds(725, 10, 121, 32);
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		
		and_radio_btn = new JRadioButton("AND");
		and_radio_btn.setSelected(true);
		buttonGroup.add(and_radio_btn);
		and_radio_btn.setBounds(744, 153, 89, 25);
		JRadioButton or_radio_btn = new JRadioButton("OR");
		buttonGroup.add(or_radio_btn);
		or_radio_btn.setBounds(744, 180, 89, 25);
		JRadioButton not_radio_btn = new JRadioButton("NOT");
		buttonGroup.add(not_radio_btn);
		not_radio_btn.setBounds(744, 207, 89, 25);
		
		and_radio_btn.setHorizontalAlignment(SwingConstants.LEFT);
		
		
		or_radio_btn.setHorizontalAlignment(SwingConstants.LEFT);
		
		
		not_radio_btn.setHorizontalAlignment(SwingConstants.LEFT);
		
		merge_filter_file_path = new JTextField();
		merge_filter_file_path.setBounds(648, 93, 132, 22);
		merge_filter_file_path.setColumns(10);
		
		JButton filters_file_btn = new JButton("\u05D1\u05D7\u05E8 \u05E7\u05D5\u05D1\u05E5..");
		filters_file_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select a csv file");
			    chooser.setAcceptAllFileFilterUsed(false);
			    
			    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			    	merge_filter_file_path.setText(chooser.getSelectedFile().toString()); 
			    } else {
			        
			    }
			}
		});
		filters_file_btn.setBounds(796, 90, 104, 25);
		
		JButton merge_filters_btn = new JButton("\u05DE\u05D6\u05D2 \u05E2\u05DD \u05E4\u05D9\u05DC\u05D8\u05E8 \u05E0\u05D5\u05DB\u05D7\u05D9");
		merge_filters_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char logic_gate = '&';
				if(and_radio_btn.isSelected()) {
					logic_gate = '&';
				} else if(or_radio_btn.isSelected()) {
					logic_gate = '|';
				} else if(not_radio_btn.isSelected()) {
					logic_gate = '!';
				}
				
				String file_path = merge_filter_file_path.getText();
				try {
					csv_file.getFilters().merge_filters(file_path, logic_gate);
					CsvFile new_file = csv_file.getFilters().getCsvFile();
					records_count_lbl.setText("" + new_file.getRecords().size());
					
				} catch(Exception ex) {
					
				}
				
				
				
			}
		});
		
		JButton cancel_curr_filter_btn = new JButton("\u05D1\u05D8\u05DC \u05E4\u05D9\u05DC\u05D8\u05E8 \u05E0\u05D5\u05DB\u05D7\u05D9");
		cancel_curr_filter_btn.setBounds(315, 372, 148, 25);
		cancel_curr_filter_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				csv_file.getFilters().reset();
				filter_curr_lon_min_lbl.setText(""); filter_curr_lon_max_lbl.setText("");
				filter_curr_lat_min_lbl.setText(""); filter_curr_lat_max_lbl.setText(""); 
				filter_curr_alt_min_lbl.setText(""); filter_curr_alt_max_lbl.setText(""); 
				filter_curr_date_min_lbl.setText(""); filter_curr_date_max_lbl.setText(""); 
				filter_curr_time_min_lbl.setText(""); filter_curr_time_max_lbl.setText("");
			}
		});
		
		JButton export_curr_filter_btn = new JButton("\u05D9\u05D9\u05E6\u05D0 \u05E4\u05D9\u05DC\u05D8\u05E8");
		export_curr_filter_btn.setBounds(487, 372, 121, 25);
		export_curr_filter_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
				Date date = new Date();
				String date_txt = dateFormat.format(date); //2016/11/16 12:08:43
				String directory_path = root_dir + "\\Export";
				String file_name = directory_path + "\\Filter_" + date_txt + ".txt";
				
				
				File dir_file = new File(directory_path);
				boolean isExists = dir_file.exists();
				if (isExists == false || dir_file.isDirectory() == false) {
					dir_file.mkdir();
				}

				csv_file.getFilters().export_file(file_name);
			}
		});
		
		
		merge_filters_btn.setBounds(691, 269, 163, 25);
		
		JButton btnNewButton = new JButton("\u05D9\u05D9\u05D1\u05D0 \u05E4\u05D9\u05DC\u05D8\u05E8");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String file_path = merge_filter_file_path.getText();
				try {
					csv_file.getFilters().import_filters(file_path);
					
					CsvFile new_file = csv_file.getFilters().getCsvFile();
					records_count_lbl.setText("" + new_file.getRecords().size());
				} catch(Exception ex) {
					
				}
				
			}
		});
		btnNewButton.setBounds(691, 318, 163, 25);
		panel_1.add(btnNewButton);
		
		panel_1.setLayout(null);
		panel_1.add(cancel_curr_filter_btn);
		panel_1.add(export_curr_filter_btn);
		panel_1.add(separator);
		panel_1.add(separator_2);
		panel_1.add(label);
		panel_1.add(label_3);
		panel_1.add(label_4);
		panel_1.add(filter_adding_lat_min);
		panel_1.add(filter_adding_lat_max);
		panel_1.add(filter_adding_lon_min);
		panel_1.add(label_5);
		panel_1.add(filter_adding_lon_max);
		panel_1.add(filter_adding_alt_min);
		panel_1.add(label_6);
		panel_1.add(filter_adding_alt_max);
		panel_1.add(label_7);
		panel_1.add(filter_adding_date_max);
		panel_1.add(label_8);
		panel_1.add(filter_adding_date_min);
		panel_1.add(filter_adding_time_min);
		panel_1.add(label_9);
		panel_1.add(filter_adding_time_max);
		panel_1.add(label_10);
		panel_1.add(filter_adding_device);
		panel_1.add(label_11);
		panel_1.add(label_12);
		panel_1.add(filter_curr_lat_min_lbl);
		panel_1.add(filter_curr_lat_max_lbl);
		panel_1.add(filter_curr_lon_min_lbl);
		panel_1.add(filter_curr_lon_max_lbl);
		panel_1.add(filter_curr_alt_min_lbl);
		panel_1.add(filter_curr_alt_max_lbl);
		panel_1.add(filter_curr_date_min_lbl);
		panel_1.add(filter_curr_date_max_lbl);
		panel_1.add(filter_curr_time_max_lbl);
		panel_1.add(filter_curr_time_min_lbl);
		panel_1.add(add_filter_btn);
		panel_1.add(label_2);
		panel_1.add(and_radio_btn);
		panel_1.add(or_radio_btn);
		panel_1.add(not_radio_btn);
		panel_1.add(merge_filter_file_path);
		panel_1.add(filters_file_btn);
		panel_1.add(merge_filters_btn);
		



		

		

		
		
	}
}
