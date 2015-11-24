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
        for (int i=0; i<length(tokens); i++) {
        	
        }
		
		
		return a;
	}
	
	
}
