import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

// Data structure for Constellations
// Each object is initially read from flatfile by Parser class.
public class Constellation {

	    
	String name;					// familiar name, usually the Latin
	String shortName;				// 3 letter abbreviation
	boolean zodiac;					// is it a member of the zodiac?
	double rasc; 					// right ascension
	double decl; 					// declination
	double ascNodeLongi;	 		// ascending node longitude
	double meanLongi;		 		// mean longitude
	double hourAngle;				// hour angle
	ArrayList<Pair<Integer>> ast;	// Unbounded list of pairs of StarIDs	
	
	public Constellation(String s) {
    	String delimiter = ",";
        String[] tokens = s.split(delimiter);
		
		this.name=tokens[0];
		this.shortName=tokens[1];
		this.zodiac=Boolean.parseBoolean(tokens[5]);
		this.rasc=Double.parseDouble(tokens[3]);
		this.decl=Double.parseDouble(tokens[4]);
		if (tokens[6] != null) {
			this.ast=stringToPairs(tokens[0]);
		}
	} // constructor
	
	public Constellation(String n, boolean z, double ra, double dec, String asterisms) {
		this.name=n;
		this.zodiac=z;
		this.rasc=ra;
		this.decl=dec;
		this.ast=stringToPairs(asterisms);
	} // constructor

	public Constellation(String n, boolean z, double ra, double dec) {
		this.name=n;
		this.zodiac=z;
		this.rasc=ra;
		this.decl=dec;
	} // constructor, no asterisms
	
	private ArrayList<Pair<Integer>> stringToPairs(String s) {
		ArrayList<Pair<Integer>> a = new ArrayList<Pair<Integer>>();
		
    	String delimiter = " ";
        String[] tokens = s.split(delimiter);
        for (int i=0; i<tokens.length; i++) {
        	
        }
		
		return a;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean inZodiac() {
		return this.zodiac;
	}
	
	public String getAbbreviation() {
		if (this.shortName != null ) { return this.shortName; }
		else { return ""; }
	}
	
	public ArrayList<Pair<Integer>> getAsterisms() {
		return this.ast;
	}
	
	// Ascending node longitude is not guaranteed to be set.
	// Since 0 is a valid value, will default to 190 instead.
	
	public double getAscendingNodeLongitude() {
		return this.ascNodeLongi; 
	}
	// Mean longitude is not guaranteed to be set.
	// Since 0 is a valid value, will default to 500 instead.
	public double getMeanLongitude() {
		return this.meanLongi;
	}
	
	// hourAngle  is not guaranteed to be set.
	// Since 0 is a valid value, will default to 500 instead.
	public double getHourAngle() {
		return hourAngle;
	}

	public void setHourAngle(double ha) {
		this.hourAngle=ha;
	}
	
	public void setMeanLongitude(double ml) {
		this.meanLongi=ml;
	}
	
	public void setAscendingNodeLongitude(double an) {
		this.ascNodeLongi=an;
	}
	
}
