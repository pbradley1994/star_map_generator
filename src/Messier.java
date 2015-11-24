
// Data structure for Messier Deep Space Objects
// Each object is initially read from flatfile by Parser class.

// Schema:
// 0                 1                   2        3
// Number in Catalog,New Number in order,RA-Hours,RA-Minutes,
// 4          5          6          7          8
// RA-Seconds,RA-Degrees,RA-DegMins,RA-DegSecs,Decl-Degrees,
// 9         10        11
// Decl-Mins,Decl-Secs,Description


/**
 * @author kexline
 *
 */

public class Messier {
	int id;
	String name;				// 
	boolean label;				// is a label required for this object?
	int raHours;				// right ascension RA-Hours
	int raMinutes;				// right ascension RA-Minutes
	int raSeconds;				// right ascension RA-
	double rightAscensionH;        // right ascension in decimal hours
	int raDegrees; 		    	// right ascension degrees
	int raDMinutes; 			// right ascension degree-mins
	int raDSeconds; 			// right ascension degree-secs
	double rightAscensionD;  // right ascension in decimal degrees
	int declDegrees; 			// declination
	int declDMinutes; 			// declination
	int declDSeconds; 			// declination
	double declination;            // declination in decimal degrees
	double hourAngle;
	


/**
 * Messier(int, String): constructor using a single comma separated string.
 * @param serial -- integer to be used as ID number
 * @param csvString
 */
	
public Messier(int serial, String csvString) {
	// Initialize label to false
	this.label=false;

	//Get all tokens available in line
	String delimiter = ",";
	String[] tokens = csvString.split(delimiter);
  
	// Take desired info from token string
	this.id=serial; 			    					 // calling function passes arbitrary id#
	if (tokens[11] != null) {   // this is the only field which may be null
		this.name=tokens[11];                			 // Description
	}
	
	this.raHours=Integer.parseInt(tokens[2]);            // RA in hours
	this.raMinutes=Integer.parseInt(tokens[3]);          //
	this.raSeconds=Integer.parseInt(tokens[4]);          //
	
	this.raDegrees=Integer.parseInt(tokens[5]);          // RA in degrees
	this.raDMinutes=Integer.parseInt(tokens[6]);         // 
	this.raDSeconds=Integer.parseInt(tokens[7]);         // 
	
	this.declDegrees=Integer.parseInt(tokens[8]);        // Declination in degrees
	this.declDMinutes=Integer.parseInt(tokens[9]);       //
	this.declDSeconds=Integer.parseInt(tokens[10]);      //
	
	// Compute decimal positions
	// dd = d + m/60 + s/3600
	this.rightAscensionH=degMinSecToDecimal(raHours, raMinutes, raSeconds);
	this.rightAscensionD=degMinSecToDecimal(raDegrees, raDMinutes, raDSeconds);
	this.declination=degMinSecToDecimal(declDegrees, declDMinutes, declDSeconds);

	//this.rightAscensionH=this.raHours+(this.raMinutes/60.0)+(this.raSeconds/3600.0);
	//this.rightAscensionD=this.raDegrees+(this.raDMinutes/60.0)+(this.raDSeconds/3600.0);
	//this.declination=this.declDegrees+(this.declDMinutes/60.0)+(this.declDSeconds/3600.0);
			
	if (this.name != null) {
		this.label=true;
	}
}

// getters	

/**
 * getName()
 * @return name (description) of object.
 */
public String getName() {
	return name;
}

/**
 * getRADecimalHour()
 * @return right ascension in Hours
 */
public double getRADecimalHour() {
	return rightAscensionH;
}

/**
 * getRADecimalDegree()
 * @return right ascension in degrees
 */
public double getRADecimalDegree() {
	return rightAscensionD;
}

/**
 * getDeclination()
 * @return right declination in degrees
 */
public double getDeclination() {
	return declination;
}

public boolean labelRequired() {
	return label;
}

public double getHourAngle() {
	return hourAngle;
}


// setters
public void setName(String value) {
	this.name=value;
}

public void setRADecimalDegrees(double value) {
	this.rightAscensionD=value;
}

public void setRADecimalHour(double value) {
	this.rightAscensionH=value;
	int[] dms=decimalToHourMinSec(value);
	this.raHours=dms[0];
	this.raMinutes=dms[1];
	this.raSeconds=dms[2];
}

public void setLabelRequired(boolean value) {
	this.label=value;
}

public void setDeclination(double value) {
	this.declination=value;
	int[] dms=decimalToDegMinSec(value);
	this.declDegrees=dms[0];
	this.declDMinutes=dms[1];
	this.declDSeconds=dms[2];
}

public void setHourAngle(double value) {
	this.hourAngle=value;
}

public void print() {
	System.out.print("ID:    "+this.id+"  ");
	System.out.print("RA-H:  "+this.rightAscensionH+"  ");
	System.out.print("RA-D:  "+this.rightAscensionD+"  ");
	System.out.println("Decl:  "+this.declination+"   ");
}

// Conversion between hours/degrees-minutes-seconds and decimal hours/degrees
// http://www.rapidtables.com

public static double degMinSecToDecimal(int degrees, int minutes, int seconds) {
	double dd=0;
	dd=degrees+(minutes/60.0)+(seconds/3600.0);
	return dd;
}

public static double hourMinSecToDecimal(int hours, int minutes, int seconds) {
	return degMinSecToDecimal(hours, minutes, seconds);
}

public static int[] decimalToDegMinSec(double dd) {
	int[] dms={0,0,0};
	
	dms[0]=(int)dd;
	dms[1]=(int)((dd-dms[0])*60);
	dms[2]=(int)((dd-dms[0])-(dms[1]/60))*3600;
	
	return dms;
}

public static int[] decimalToHourMinSec(double dd) {
	int[] dms=decimalToDegMinSec(dd);
	return dms;
}


}
