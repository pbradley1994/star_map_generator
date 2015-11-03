//TO DO: clean up what you actually use 
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

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.MaskFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * This class builds and implements the GUI.
 * The GUI will take input from the user for latitude, longitude, date, time, and timezone.
 * Using this input, this class converts the variables for use (through action handlers)
 * and will pass the variables to other classes that need them. The StarMapGenerator
 * class's output will be contained in a canvas, but other classes do not send
 * information to the GUI.
 * 
 * @author Christy Lafayette
 */


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
		/** Begin Window Basics */	
		frmSkymap = new JFrame();
		frmSkymap.getContentPane().setBackground(Color.BLACK);
		frmSkymap.setTitle("SkyMap");
		frmSkymap.setBounds(100, 100, 900, 615);
		frmSkymap.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSkymap.getContentPane().setLayout(null);

		// "Logo" label -- "SkyMap"
		JLabel lblSkymap = new JLabel("SkyMap");
		lblSkymap.setFont(new Font("Century Gothic", Font.PLAIN, 40));
		lblSkymap.setForeground(Color.WHITE);
		lblSkymap.setBounds(17, 8, 176, 55);
		frmSkymap.getContentPane().add(lblSkymap);
		
		
		/** Begin Lat/Long */	
		// Latitude Field -- takes text input
		txtLatitude = new JTextField();
		txtLatitude.setForeground(Color.DARK_GRAY);
		txtLatitude.setText("+/- 90");
		txtLatitude.setBounds(17, 94, 146, 20);
		frmSkymap.getContentPane().add(txtLatitude);
		txtLatitude.setColumns(10);
		
		// Longitude Field -- takes text input
		txtLongitude = new JTextField();
		txtLongitude.setText("+/- 180");
		txtLongitude.setForeground(Color.DARK_GRAY);
		txtLongitude.setColumns(10);
		txtLongitude.setBounds(17, 143, 146, 20);
		frmSkymap.getContentPane().add(txtLongitude);
		
		// Latitude Label
		JLabel lblLatitude = new JLabel("Latitude");
		lblLatitude.setForeground(new Color(255, 255, 255));
		lblLatitude.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		lblLatitude.setBounds(17, 70, 127, 20);
		frmSkymap.getContentPane().add(lblLatitude);
		
		// Longitude Label
		JLabel lblLongitude = new JLabel("Longitude");
		lblLongitude.setForeground(new Color(255, 255, 255));
		lblLongitude.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		lblLongitude.setBounds(17, 113, 127, 31);
		frmSkymap.getContentPane().add(lblLongitude);
		
		
		/** Begin Date */
		// Date Label
		JLabel lblDate = new JLabel("Date");
		lblDate.setForeground(new Color(255, 255, 255));
		lblDate.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		lblDate.setBounds(17, 167, 127, 20);
		frmSkymap.getContentPane().add(lblDate);
		

		/**
		 * Begin Month dropdown menu.
		 * Creates a vector (Month) with elements that are Item datatypes. Item contains
		 * the index, the offset (not used--just reusing the GMT type), and the descriptive 
		 * text which is just for the user.
		 */
		Vector Month = new Vector();
			Month.addElement( new Item(0, 0.0, "Mo." ) );
			Month.addElement( new Item(1, 0.0, "Jan." ) );
			Month.addElement( new Item(2, 0.0, "Feb." ) );
			Month.addElement( new Item(3, 0.0, "March" ) );
			Month.addElement( new Item(4, 0.0, "April" ) );
			Month.addElement( new Item(5, 0.0, "May" ) );
			Month.addElement( new Item(6, 0.0, "June" ) );
			Month.addElement( new Item(7, 0.0, "July" ) );
			Month.addElement( new Item(8, 0.0, "Aug." ) );
			Month.addElement( new Item(9, 0.0, "Sept." ) );
			Month.addElement( new Item(10, 0.0, "Oct." ) );
			Month.addElement( new Item(11, 0.0, "Nov." ) );
			Month.addElement( new Item(12, 0.0, "Dec." ) );

		// Dropdown: Month
		JComboBox comboBoxMonth = new JComboBox();
		comboBoxMonth.setFont(new Font("Arial", Font.PLAIN, 12));
		comboBoxMonth = new JComboBox( Month );
		comboBoxMonth.setRenderer( new ItemRenderer() );
		comboBoxMonth.setBounds(17, 192, 54, 20);
		frmSkymap.getContentPane().add(comboBoxMonth);
		
		// Action Listener: When an item is selected, the InputItem variable is sent to the corresponding
		// menu item's Item attributes
		comboBoxMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JComboBox comboBoxMonth = (JComboBox)arg0.getSource();
		        final Item InputItem = (Item)comboBoxMonth.getSelectedItem();
		        // DEBUG: just making sure it's setting to the variables like it should
		        System.out.println("ID: " + InputItem.getId() + 
		        		" OFFSET: " + InputItem.getOffset() + 
		        		" MONTH: " + InputItem.getDescription());
			}
		});
		
		// Dropdown: Day
		JComboBox comboBoxDay = new JComboBox();
		comboBoxDay.setFont(new Font("Arial", Font.PLAIN, 12));
		comboBoxDay.setModel(new DefaultComboBoxModel(new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"}));
		comboBoxDay.setBounds(74, 192, 44, 20);
		frmSkymap.getContentPane().add(comboBoxDay);
		
		// Year Field -- takes text input
		txtYear = new JTextField();
		txtYear.setText("1900");
		txtYear.setForeground(Color.DARK_GRAY);
		txtYear.setColumns(10);
		txtYear.setBounds(128, 192, 64, 20);
		frmSkymap.getContentPane().add(txtYear);
	
		
		/** Begin Time */
		// Time Field -- takes text input
		txtTime = new JTextField();
		txtTime.setText("00:00");
		txtTime.setForeground(Color.DARK_GRAY);
		txtTime.setColumns(10);
		txtTime.setBounds(17, 239, 64, 20);
		frmSkymap.getContentPane().add(txtTime);
		
		// Time Label
		JLabel lblTime = new JLabel("Time");
		lblTime.setForeground(Color.WHITE);
		lblTime.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		lblTime.setBounds(17, 215, 127, 20);
		frmSkymap.getContentPane().add(lblTime);
		
	
		/**
		 * Begin Timezone dropdown menu.
		 * Creates a vector (GMT) with elements that are Item datatypes. Item contains
		 * the index, the offset (used for calculations), and the descriptive text which is 
		 * just for the user.
		 */
		
		Vector GMT = new Vector();
			GMT.addElement( new Item(-1, 0.0, "Select a Timezone." ) );
			GMT.addElement( new Item(0, 0.0, "GMT - Greenwich Mean Time" ) );
			GMT.addElement( new Item(1, 0.0, "GMT - Universal Coordinated Time" ) );
			GMT.addElement( new Item(2, 1.0, "GMT+1:00 - European Central Time" ) );
			GMT.addElement( new Item(3, 2.0, "GMT+2:00 - Eastern European Time" ) );
			GMT.addElement( new Item(4, 2.0, "GMT+2:00 - (Arabic) Egypt Standard Time" ) );
			GMT.addElement( new Item(5, 3.0, "GMT+3:00 - Eastern African Time" ) );
			GMT.addElement( new Item(6, 3.5, "GMT+3:30 - Middle East Time" ) );
			GMT.addElement( new Item(7, 4.0, "GMT+4:00 - Near East Time" ) );
			GMT.addElement( new Item(8, 5.0, "GMT+5:00 - Pakistan Lahore Time" ) );
			GMT.addElement( new Item(9, 5.5, "GMT+5:30 - India Standard Time" ) );
			GMT.addElement( new Item(10, 6.0, "GMT+6:00 - Bangladesh Standard Time" ) );
			GMT.addElement( new Item(11, 7.0, "GMT+7:00 - Vietnam Standard Time" ) );
			GMT.addElement( new Item(12, 8.0, "GMT+8:00 - China Taiwan Time" ) );
			GMT.addElement( new Item(13, 9.0, "GMT+9:00 - Japan Standard Time" ) );
			GMT.addElement( new Item(14, 9.5, "GMT+9:30 - Australia Central Time" ) );
			GMT.addElement( new Item(15, 10.0, "GMT+10:00 - Australia Eastern Time" ) );
			GMT.addElement( new Item(16, 11.0, "GMT+11:00 - Solomon Standard Time" ) );
			GMT.addElement( new Item(17, 12.0, "GMT+12:00 - New Zealand Standard Time" ) );
			GMT.addElement( new Item(18, -11.0, "GMT-11:00 - Midway Islands Time" ) );
			GMT.addElement( new Item(19, -10.0, "GMT-10:00 - Hawaii Standard Time" ) );
			GMT.addElement( new Item(20, -9.0, "GMT-9:00 - Alaska Standard Time" ) );
			GMT.addElement( new Item(21, -8.0, "GMT-8:00 - Pacific Standard Time" ) );
			GMT.addElement( new Item(22, -7.0, "GMT-7:00 - Phoenix Standard Time" ) );
			GMT.addElement( new Item(23, -7.0, "GMT-7:00 - Mountain Standard Time" ) );
			GMT.addElement( new Item(24, -6.0, "GMT-6:00 - Central Standard Time" ) );
			GMT.addElement( new Item(25, -5.0, "GMT-5:00 - Eastern Standard Time" ) );
			GMT.addElement( new Item(26, -5.0, "GMT-5:00 - Indiana Eastern Standard Time" ) );
			GMT.addElement( new Item(27, -4.0, "GMT-4:00 - Puerto Rico and US Islands Time" ) );
			GMT.addElement( new Item(28, -3.5, "GMT-3:30 - Canada Newfoundland Time" ) );
			GMT.addElement( new Item(29, -3.0, "GMT-3:00 - Argentina Standard Time" ) );
			GMT.addElement( new Item(30, -3.0, "GMT-3:00 - Brazil Eastern Time" ) );
			GMT.addElement( new Item(31, -1.0, "GMT-1:00 - Central African Time" ) );

		// Dropdown: Timezone
		JComboBox comboBoxTimezone = new JComboBox();
		comboBoxTimezone.setFont(new Font("Arial", Font.PLAIN, 12));
		comboBoxTimezone = new JComboBox( GMT );
		comboBoxTimezone.setRenderer( new ItemRenderer() );
		comboBoxTimezone.setBounds(17, 271, 245, 20);
		frmSkymap.getContentPane().add(comboBoxTimezone);
		
		// Action Listener: When an item is selected, the InputItem variable is sent to the corresponding
		// menu item's Item attributes
		comboBoxTimezone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JComboBox comboBoxTimezone = (JComboBox)arg0.getSource();
		        final Item InputItem = (Item)comboBoxTimezone.getSelectedItem();
		        // DEBUG: just making sure it's setting to the variables like it should
		        System.out.println("ID: " + InputItem.getId() + 
		        		" OFFSET: " + InputItem.getOffset() + 
		        		" TIMEZONE: " + InputItem.getDescription());
			}
		});
		
		
		// Button: Generate Map -- takes input and converts it to useable formats
		JButton btnGenerate = new JButton("Generate Map");
		btnGenerate.setForeground(Color.WHITE);
		btnGenerate.setBackground(Color.BLACK);
		btnGenerate.setBounds(17, 312, 101, 23);
		frmSkymap.getContentPane().add(btnGenerate);
		
		// Action Listener: When an item is selected, the InputItem variable is sent to the corresponding
		// menu item's Item attributes
		btnGenerate.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e)
			    {
			    	/**
			    	 * On Button click, set user input input to variables. Drop-down menus
			    	 * are handled elsewhere since it sets them immediately.
			    	 */
			    	
			    	// Try-Catch for Latitude validation
			    	// If the input can't be cast to an double, throws exception
			    	/** TO-DO: Number validation isn't working */
			    	
				    try {
				    	double MIN_LAT = -90.000;
					    double MAX_LAT = 90.000;
					    
				    	final double testLat = Double.parseDouble((String)txtLatitude.getText());
				    	
					    if ((testLat > MIN_LAT) || (testLat < MAX_LAT)) {
				            double inputLat = testLat;
				          //DEBUG: remove. just outputs the variables so that I know it's
				            System.out.println("INPUT LATITUDE: " + inputLat);
					    }
					    else if (testLat < MIN_LAT){
					    	JOptionPane.showMessageDialog(null,"This is not a valid latitude. Please input a value between -90.0 and 90.0.");
					    }
					    else if (testLat < MAX_LAT){
					    	JOptionPane.showMessageDialog(null,"This is not a valid latitude. Please input a value between -90.0 and 90.0.");
					    }
				    }
				    catch(NumberFormatException ex) {
				    	 JOptionPane.showMessageDialog(null,"This is not a valid latitude. Please input a value between -90.0 and 90.0.");
				    	}
				    
			    	// Try-Catch for Longitude validation
			    	// If the input can't be cast to an double, throws exception
			    	/** TO-DO: Number validation isn't working */
			    	
				    try {
				    	double MIN_LONG = -180.000;
					    double MAX_LONG = 180.000;
					    
				    	final double testLong = Double.parseDouble((String)txtLongitude.getText());
				    	
					    if ((testLong > MIN_LONG) || (testLong < MAX_LONG)) {
				            double inputLong = testLong;
				          //DEBUG: remove. just outputs the variables so that I know it's
				            System.out.println("INPUT Longitude: " + inputLong);
					    }
					    else if (testLong < MIN_LONG){
					    	JOptionPane.showMessageDialog(null,"This is not a valid longitude. Please input a value between -180.0 and 180.0.");
					    }
					    else if (testLong < MAX_LONG){
					    	JOptionPane.showMessageDialog(null,"This is not a valid longitude. Please input a value between -180.0 and 180.0.");
					    }
				    }
				    catch(NumberFormatException ex) {
				    	 JOptionPane.showMessageDialog(null,"This is not a valid longitude. Please input a value between -180.0 and 180.0.");
				    	}
			    	
				    // Day is converted to int for use
			    	final int inputDay = Integer.parseInt((String)comboBoxDay.getSelectedItem());

				    // Try-Catch for Year validation
			    	// If the input can't be cast to an int, throws exception
			    	/** TO-DO: Year validation isn't working */
			    	
				    try {
				    	int MIN_YEAR = 1900;
					    int MAX_YEAR = 2100;
					    
				    	final int testYear = Integer.parseInt((String)txtYear.getText());
				    	
					    if ((testYear > MIN_YEAR) || (testYear < MAX_YEAR)) {
				            int inputYear = testYear;
				          //DEBUG: remove. just outputs the variables so that I know it's
				            System.out.println("INPUT YEAR: " + inputYear);
					    }
					    else if (testYear < MIN_YEAR){
					    	JOptionPane.showMessageDialog(null,"This is not a valid year. Please input a year between 1900 and 2100.");
					    }
					    else if (testYear < MAX_YEAR){
					    	JOptionPane.showMessageDialog(null,"This is not a valid year. Please input a year between 1900 and 2100.");
					    }
				    }
				    catch(NumberFormatException ex) {
				    	 JOptionPane.showMessageDialog(null,"This is not a valid year. Please input a year between 1900 and 2100.");
				    	}


				    // Try-Catch for Time validation
			    	// If the input can't be cast, throws exception
			    	/** TO-DO: time validation */
				    
			    	final String inputTime = txtTime.getText();
				    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
				    //format.parse(inputTime);
				    try {
				    	format.parse(inputTime);
				    }
				    catch(ParseException ex) {
				    	 JOptionPane.showMessageDialog(null,"This is not a valid time. Please enter a time between 00:00 and 24:00.");
				    	}
			    	
			    	//DEBUG: remove. just outputs the variables so that I know they're set.
			    	System.out.println("TIME: " + inputTime);
			    	System.out.println("DAY: " + inputDay);

			    }
		});

		
		/** Begin Section: Current Map's Output */
		// Label: Current Latitute
		JLabel lblCurrentLatitude = new JLabel("Current Latitude:");
		lblCurrentLatitude.setForeground(new Color(255, 255, 255));
		lblCurrentLatitude.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		lblCurrentLatitude.setBounds(17, 367, 156, 20);
		frmSkymap.getContentPane().add(lblCurrentLatitude);
		
		// Label: Current Longitude
		JLabel lblCurrentLongitude = new JLabel("Current Longitude:");
		lblCurrentLongitude.setForeground(Color.WHITE);
		lblCurrentLongitude.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		lblCurrentLongitude.setBounds(17, 417, 156, 20);
		frmSkymap.getContentPane().add(lblCurrentLongitude);
		
		// Label: Current Date Parameters
		JLabel lblCurrentDatetime = new JLabel("Current Date/Time:");
		lblCurrentDatetime.setForeground(Color.WHITE);
		lblCurrentDatetime.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		lblCurrentDatetime.setBounds(17, 464, 156, 20);
		frmSkymap.getContentPane().add(lblCurrentDatetime);
		
		/** TO-DO: output the "current" variables */
		// Current Latitude
		JLabel lblLat = new JLabel("LAT");
		lblLat.setForeground(new Color(255, 255, 255));
		lblLat.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		lblLat.setBounds(27, 386, 91, 20);
		frmSkymap.getContentPane().add(lblLat);
		
		// Current Longitude
		JLabel lblLong = new JLabel("LONG");
		lblLong.setForeground(Color.WHITE);
		lblLong.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		lblLong.setBounds(27, 436, 91, 20);
		frmSkymap.getContentPane().add(lblLong);
		
		// Current Date Parameters
		JLabel label_1 = new JLabel("01/01/1900, 23:59");
		label_1.setForeground(Color.WHITE);
		label_1.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		label_1.setBounds(27, 483, 165, 20);
		frmSkymap.getContentPane().add(label_1);
		
		// Button: Print Map
		/** TO-DO: Event handler */
		JButton btnPrint = new JButton("Print Map");
		btnPrint.setForeground(Color.WHITE);
		btnPrint.setBackground(Color.BLACK);
		btnPrint.setBounds(17, 521, 89, 23);
		frmSkymap.getContentPane().add(btnPrint);
		
		// Background image
		/** TO-DO: Make this not-a-filepath so that they'll actually work on another computer */
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon("C:\\Users\\Christy\\workspace2\\SkyMap\\src\\map.png"));
		label.setBounds(299, 0, 585, 577);
		frmSkymap.getContentPane().add(label);
		
		// Starmap will go here
		JLabel label_2 = new JLabel("");
		label_2.setIcon(new ImageIcon("C:\\Users\\Christy\\workspace2\\SkyMap\\src\\ex.png"));
		label_2.setBounds(0, 0, 292, 587);
		frmSkymap.getContentPane().add(label_2);
	}
}

/**
 * Makes the dropdown menus work since the vectors totally break the designer view. 
 * Adapted from code by camickr @ stackoverflow, aka my personal savior.
 */

class ItemRenderer extends BasicComboBoxRenderer
{
    public Component getListCellRendererComponent(
        JList list, Object value, int index,
        boolean isSelected, boolean cellHasFocus)
    {
        super.getListCellRendererComponent(list, value, index,
            isSelected, cellHasFocus);

        if (value != null)
        {
            Item item = (Item)value;
            setText( item.getDescription() );
        }
        
        return this;
    }
}

/**
 * Just a simple class to hold the Dropdown menus' Vector stuff.
 * Does this need to be its own thing? Not sure if it'll be needed outside of
 * here so I just tossed it at the end.
 */

class Item
{
    private int id;
    private double offset;
    private String description;

    public Item(int id, double offset, String description) {
        this.id = id;
        this.offset = offset;
        this.description = description;
    }

    public int getId() {
        return id;
    }
    
    public double getOffset() {
        return offset;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return description;
    }
}
