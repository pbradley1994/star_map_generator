import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

// Data structure for Constellations
// Each object is initially read from flatfile by Parser class.
public class Constellation {
	
	final static boolean DEBUG=false;	

	int constID;					// arbitrary index ID  
	String name;					// familiar name, usually the Latin
	String shortName;				// 3 letter abbreviation
	boolean zodiac;					// is it a member of the zodiac?
	boolean plottable;				// Has at least one asterism link
	double rasc; 					// right ascension
	double decl; 					// declination
	double ascNodeLongi;	 		// ascending node longitude
	double meanLongi;		 		// mean longitude
	double hourAngle;				// hour angle
	ArrayList<Pair<Integer>> ast;	// Unbounded list of pairs of StarIDs	
	
	public Constellation(int id, String s) {
    	String delimiter = ",";
        String[] tokens = s.split(delimiter);
        
		this.constID=id;
		if (DEBUG) { System.out.println("ConstellationID "+id); }
        if (tokens.length >= 2) {
    		this.name=tokens[0];
    		this.shortName=tokens[1];
        }	
    	if (tokens.length >= 6) {
    		this.zodiac=Boolean.parseBoolean(tokens[5]);
    		this.rasc=Double.parseDouble(tokens[3]);
    		this.decl=Double.parseDouble(tokens[4]);	
        }
        // Asterisms are not guaranteed by datasource
        if (tokens.length >= 7) {
    		if ( tokens[6].trim() != "") {
    			this.ast=stringToPairs(tokens[0]);
    			this.plottable=true;
    		}
    		else { this.plottable=false; }
    		if (this.ast.isEmpty() ) { this.plottable=false; }
        }
		else { this.plottable = false; }

		
	} // constructor
	
	public Constellation(int id, String n, boolean z, double ra, double dec, String asterisms) {
		this.constID=id;
		this.name=n;
		this.zodiac=z;
		this.rasc=ra;
		this.decl=dec;
		this.ast=stringToPairs(asterisms);
		if (this.ast.size() > 0) {
			this.plottable=true;
		}
	} // constructor

	public Constellation(int id, String n, boolean z, double ra, double dec) {
		this.constID=id;
		this.name=n;
		this.zodiac=z;
		this.rasc=ra;
		this.decl=dec;
		this.plottable=false;
	} // constructor, no asterisms
	
	private ArrayList<Pair<Integer>> stringToPairs(String s) {
		ArrayList<Pair<Integer>> a = new ArrayList<Pair<Integer>>();
		
    	final String space = " ";
        String[] tokens = s.split(space);
        for (int i=0; i<tokens.length; i++) {
        	final String colon = ":";
            String[] endpoints = tokens[i].split(colon);
            Pair<Integer> p;
            if (endpoints.length >= 2) {
            	Integer e0 = Integer.parseInt(endpoints[0]);
            	Integer e1 = Integer.parseInt(endpoints[1]);
            	p = new Pair<Integer>(e0,e1);
            }
            else {
            	p = new Pair<Integer>(-1,-1);
            }
            a.add(p);
        }
		
		return a;
	}
	
	public boolean isPlottable() {
		return this.plottable;
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
	
	public boolean verifyPlottable() {
		if (this.ast.size() > 0) {
			this.plottable=true;
		}
		else {
			this.plottable=false;
		}
		return this.plottable;
	}
	
}
