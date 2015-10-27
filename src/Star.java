
// Data structure for stars
// Each object is initially read from flatfile by Parser class.

public class Star {
	String name;
	boolean label;			// is a label required for this object?
	double rasc; 			// right ascension
	double decl; 			// declination
	double eccentricity; 
	double smaxis;          // semimajor axis
	double inclination;
	double perihelion;
	double ascNodeLongi;	// ascending node longitude
	double meanLongi;		// mean longitude
	double hourAngle;	
	
// constructor 
public Star(String vname, boolean vlabel, double vrasc, double vdecl) {
	this.name=vname; 
	this.label=vlabel;
	this.rasc=vrasc;
	this.decl=vdecl;
}

// getters	
public String getName() {
	return name;
}

public double getRA() {
	return rasc;
}

public boolean labelRequired() {
	return label;
}

public double getDeclination() {
	return decl;
}

public double getEccentricity() {
	return eccentricity;
}

public double getSMAxis() {
	return smaxis;
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
	this.rasc=value;
}

public void setLabelRequired(boolean value) {
	this.label=value;
}

public void setDeclination(double value) {
	this.decl=value;
}

public void setEccentricity(double value) {
	this.eccentricity=value;
}

public void getSMAxis(double value) {
	this.smaxis=value;
}

public void getInclination(double value) {
	this.inclination=value;
}

public void getPerihelion(double value) {
	this.perihelion=value;
}

public void getANL(double value) {
	this.ascNodeLongi=value;
}

public void getMeanLong(double value) {
	this.meanLongi=value;
}

public void getHourAngle(double value) {
	this.hourAngle=value;
}



	
}
