import java.awt.datatransfer.StringSelection;

// Data structure for stars
// Each object is initially read from flatfile by Parser class.

// Schema:  
// 0        1   2   3   4       5               6
// StarID	Hip	HD	HR	Gliese	BayerFlamsteed	ProperName	
// 7    8   9           10  11      12          13
// RA	Dec	Distance	Mag	AbsMag	Spectrum	ColorIndex

public class Star {
	
	final static boolean DEBUG=true;	
	
	String name;
	boolean label;		 	 // is a label required for this object?
	int id;            		 // StarID 
	double magnitude;		 // brightness/magnitude
	double rightAscension; 	 // right ascension
	double declination; 	 // declination
	double eccentricity; 
	double semimajorAxis;    // semimajor axis
	double inclination;
	double perihelion;
	double ascNodeLongi;	 // ascending node longitude
	double meanLongi;		 // mean longitude
	double hourAngle;	
	
// constructor 
public Star(String vname, boolean vlabel, double vrasc, double vdecl) {
	this.name=vname; 
	this.label=vlabel;
	this.rightAscension=vrasc;
	this.declination=vdecl;
}

//constructor using a single comma separated string.
public Star(String csvString) {
	// Initialize label to false
    this.label=false;

    //Get all tokens available in line
	String delimiter = ",";
    String[] tokens = csvString.split(delimiter);
    
    // Take desired info from token string
    this.id=Integer.parseInt(tokens[0]); 				 // StarID
    this.name=tokens[6];                			     // ProperName
    this.rightAscension=Double.parseDouble(tokens[7]);   // RA
    this.declination=Double.parseDouble(tokens[8]);      // Dec
    
    if (this.name != null) {
    	this.label=true;
    }
}

// getters	
public String getName() {
	return name;
}

public double getRA() {
	return rightAscension;
}

public boolean labelRequired() {
	return label;
}

public double getDeclination() {
	return declination;
}

public double getEccentricity() {
	return eccentricity;
}

public double getSMAxis() {
	return semimajorAxis;
}
public double getInclination() {
	return inclination;
}

public double getPerihelion() {
	return perihelion;
}

public double getANL() {
	return ascNodeLongi;
}

public double getMeanLong() {
	return meanLongi;
}

public double getHourAngle() {
	return hourAngle;
}

// setters
public void setName(String value) {
	this.name=value;
}

public void setRA(double value) {
	this.rightAscension=value;
}

public void setLabelRequired(boolean value) {
	this.label=value;
}

public void setDeclination(double value) {
	this.declination=value;
}

public void setEccentricity(double value) {
	this.eccentricity=value;
}

public void setSemimajorAxis(double value) {
	this.semimajorAxis=value;
}

public void setInclination(double value) {
	this.inclination=value;
}

public void setPerihelion(double value) {
	this.perihelion=value;
}

public void setAscendingNodeLongitude(double value) {
	this.ascNodeLongi=value;
}

public void setMeanLongitude(double value) {
	this.meanLongi=value;
}

public void setHourAngle(double value) {
	this.hourAngle=value;
}

public void print() {
	System.out.print("ID:  "+this.id+"  ");
	System.out.print("RA:  "+this.rightAscension+"  ");
	System.out.println("De:  "+this.declination);
}

} // class Star
