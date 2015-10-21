import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.SystemColor;
import javax.swing.JButton;
import java.awt.Canvas;
import javax.swing.JSeparator;
import java.awt.Button;
import java.awt.Panel;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;


public class SkyMap_gui {

	private JFrame frmSkymap;
	private JTextField txtLatitude;
	private JTextField txtLongitude;
	private JTextField txtYear;
	private JTextField txtTime;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SkyMap_gui window = new SkyMap_gui();
					window.frmSkymap.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SkyMap_gui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSkymap = new JFrame();
		frmSkymap.getContentPane().setBackground(Color.BLACK);
		frmSkymap.setTitle("SkyMap");
		frmSkymap.setBounds(100, 100, 900, 615);
		frmSkymap.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSkymap.getContentPane().setLayout(null);
		
		txtLatitude = new JTextField();
		txtLatitude.setForeground(SystemColor.scrollbar);
		txtLatitude.setText("34.7114\u00B0 N");
		txtLatitude.setBounds(17, 94, 146, 20);
		frmSkymap.getContentPane().add(txtLatitude);
		txtLatitude.setColumns(10);
		
		JLabel lblLatitude = new JLabel("Latitude");
		lblLatitude.setForeground(new Color(255, 255, 255));
		lblLatitude.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		lblLatitude.setBounds(17, 70, 127, 20);
		frmSkymap.getContentPane().add(lblLatitude);
		
		JLabel lblLongitude = new JLabel("Longitude");
		lblLongitude.setForeground(new Color(255, 255, 255));
		lblLongitude.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		lblLongitude.setBounds(17, 113, 127, 31);
		frmSkymap.getContentPane().add(lblLongitude);
		
		txtLongitude = new JTextField();
		txtLongitude.setText("86.6542\u00B0 W");
		txtLongitude.setForeground(SystemColor.scrollbar);
		txtLongitude.setColumns(10);
		txtLongitude.setBounds(17, 143, 146, 20);
		frmSkymap.getContentPane().add(txtLongitude);
		
		JLabel lblDate = new JLabel("Date");
		lblDate.setForeground(new Color(255, 255, 255));
		lblDate.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		lblDate.setBounds(17, 167, 127, 20);
		frmSkymap.getContentPane().add(lblDate);
		
		txtYear = new JTextField();
		txtYear.setText("1900");
		txtYear.setForeground(SystemColor.scrollbar);
		txtYear.setColumns(10);
		txtYear.setBounds(128, 192, 64, 20);
		frmSkymap.getContentPane().add(txtYear);
		
		JButton btnGenerate = new JButton("Generate Map");
		btnGenerate.setForeground(Color.WHITE);
		btnGenerate.setBackground(Color.BLACK);
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnGenerate.setBounds(17, 312, 101, 23);
		frmSkymap.getContentPane().add(btnGenerate);
		
		JButton btnPrint = new JButton("Print Map");
		btnPrint.setForeground(Color.WHITE);
		btnPrint.setBackground(Color.BLACK);
		btnPrint.setBounds(17, 521, 89, 23);
		frmSkymap.getContentPane().add(btnPrint);
		
		JLabel lblCurrentLatitude = new JLabel("Current Latitude:");
		lblCurrentLatitude.setForeground(new Color(255, 255, 255));
		lblCurrentLatitude.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		lblCurrentLatitude.setBounds(17, 367, 156, 20);
		frmSkymap.getContentPane().add(lblCurrentLatitude);
		
		JLabel lblLat = new JLabel("LAT");
		lblLat.setForeground(new Color(255, 255, 255));
		lblLat.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		lblLat.setBounds(27, 386, 91, 20);
		frmSkymap.getContentPane().add(lblLat);
		
		JComboBox comboBoxMonth = new JComboBox();
		comboBoxMonth.setFont(new Font("Arial", Font.PLAIN, 12));
		comboBoxMonth.setBounds(17, 192, 54, 20);
		frmSkymap.getContentPane().add(comboBoxMonth);
		comboBoxMonth.setModel(new DefaultComboBoxModel(new String[] {"Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"}));
		
		JComboBox comboBoxDay = new JComboBox();
		comboBoxDay.setFont(new Font("Arial", Font.PLAIN, 12));
		comboBoxDay.setModel(new DefaultComboBoxModel(new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"}));
		comboBoxDay.setBounds(74, 192, 44, 20);
		frmSkymap.getContentPane().add(comboBoxDay);
		
		txtTime = new JTextField();
		txtTime.setText("23:59");
		txtTime.setForeground(SystemColor.scrollbar);
		txtTime.setColumns(10);
		txtTime.setBounds(17, 239, 64, 20);
		frmSkymap.getContentPane().add(txtTime);
		
		JLabel lblTime = new JLabel("Time");
		lblTime.setForeground(Color.WHITE);
		lblTime.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		lblTime.setBounds(17, 215, 127, 20);
		frmSkymap.getContentPane().add(lblTime);
		
		JLabel lblCurrentLongitude = new JLabel("Current Longitude:");
		lblCurrentLongitude.setForeground(Color.WHITE);
		lblCurrentLongitude.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		lblCurrentLongitude.setBounds(17, 417, 156, 20);
		frmSkymap.getContentPane().add(lblCurrentLongitude);
		
		JLabel lblLong = new JLabel("LONG");
		lblLong.setForeground(Color.WHITE);
		lblLong.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		lblLong.setBounds(27, 436, 91, 20);
		frmSkymap.getContentPane().add(lblLong);
		
		JLabel lblCurrentDatetime = new JLabel("Current Date/Time:");
		lblCurrentDatetime.setForeground(Color.WHITE);
		lblCurrentDatetime.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		lblCurrentDatetime.setBounds(17, 464, 156, 20);
		frmSkymap.getContentPane().add(lblCurrentDatetime);
		
		JLabel label_1 = new JLabel("01/01/1900, 23:59");
		label_1.setForeground(Color.WHITE);
		label_1.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		label_1.setBounds(27, 483, 165, 20);
		frmSkymap.getContentPane().add(label_1);
		
		JComboBox comboBoxTimezone = new JComboBox();
		comboBoxTimezone.setFont(new Font("Arial", Font.PLAIN, 12));
		comboBoxTimezone.setModel(new DefaultComboBoxModel(new String[] {"GMT - Greenwich Mean Time", "GMT - Universal Coordinated Time", "GMT+1:00 - European Central Time", "GMT+2:00 - Eastern European Time", "GMT+2:00 - (Arabic) Egypt Standard Time", "GMT+3:00 - Eastern African Time", "GMT+3:30 - Middle East Time", "GMT+4:00 - Near East Time", "GMT+5:00 - Pakistan Lahore Time", "GMT+5:30 - India Standard Time", "GMT+6:00 - Bangladesh Standard Time", "GMT+7:00 - Vietnam Standard Time", "GMT+8:00 - China Taiwan Time", "GMT+9:00 - Japan Standard Time", "GMT+9:30 - Australia Central Time", "GMT+10:00 - Australia Eastern Time", "GMT+11:00 - Solomon Standard Time", "GMT+12:00 - New Zealand Standard Time", "GMT-11:00 - Midway Islands Time", "GMT-10:00 - Hawaii Standard Time", "GMT-9:00 - Alaska Standard Time", "GMT-8:00 - Pacific Standard Time", "GMT-7:00 - Phoenix Standard Time", "GMT-7:00 - Mountain Standard Time", "GMT-6:00 - Central Standard Time", "GMT-5:00 - Eastern Standard Time", "GMT-5:00 - Indiana Eastern Standard Time", "GMT-4:00 - Puerto Rico and US Islands Time", "GMT-3:30 - Canada Newfoundland Time", "GMT-3:00 - Argentina Standard Time", "GMT-3:00 - Brazil Eastern Time", "GMT-1:00 - Central African Time"}));
		comboBoxTimezone.setBounds(17, 271, 245, 20);
		frmSkymap.getContentPane().add(comboBoxTimezone);
		
		JLabel lblSkymap = new JLabel("SkyMap");
		lblSkymap.setFont(new Font("Century Gothic", Font.PLAIN, 40));
		lblSkymap.setForeground(Color.WHITE);
		lblSkymap.setBounds(17, 8, 176, 55);
		frmSkymap.getContentPane().add(lblSkymap);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon("C:\\Users\\Christy\\workspace2\\SkyMap\\src\\map.png"));
		label.setBounds(299, 0, 585, 577);
		frmSkymap.getContentPane().add(label);
		
		JLabel label_2 = new JLabel("");
		label_2.setIcon(new ImageIcon("C:\\Users\\Christy\\workspace2\\SkyMap\\src\\ex.png"));
		label_2.setBounds(0, 0, 292, 587);
		frmSkymap.getContentPane().add(label_2);
	}
}
