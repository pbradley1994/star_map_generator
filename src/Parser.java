import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
// Read data from flat files into object arrays.


/**
 * @author kexline
 *
 */
public class Parser {

	final static boolean DEBUG=true;	
	
	// Maintain a list of each type of object.
	ArrayList<Star> listOfStars=new ArrayList<Star>();
	ArrayList<Messier> listOfMessierObjects=null;
	ArrayList<Planet> listOfPlanets=null;
	ArrayList<Constellation> listOfConstellations=null;
	
/**
 * Constructor
 *  
 */
public Parser() {
	ArrayList<File> listOfFiles=findDataFiles("./data");
	if (DEBUG) { System.out.println("Parser()"); }
	listOfStars=readStars();
    if (DEBUG && listOfStars != null) { System.out.println("Parser(): length of listOfStars is "+listOfStars.size());}
	
//	BufferedReader b=BufferedReader(FileReader(File))
//	String[] thingy = myString.split(",");
//	Class.forName("the.class.full.Name");
}

/**
 * Generate a list of files in the specified directory.
 * @param pathname
 * @return Arraylist of files
 */
private ArrayList<File> findDataFiles(String pathname) {
	File f = new File(pathname);
	ArrayList<File> files = new ArrayList<File>(Arrays.asList(f.listFiles()));
	return files;
}

// Create an ArrayList of stars from provided file handle.
public ArrayList<Star> readStars() {
	
	System.out.println("readStars() start");
	
	ArrayList<Star> l=null;
	String fileToParse = "./data/Star.csv";
    BufferedReader fileReader = null;

    final String DELIMITER = ",";
    try
    {
        String line = "";
        //Create the file reader
        fileReader = new BufferedReader(new FileReader(fileToParse));
         
        // Throw away the first line, which contains column names.
        fileReader.readLine();
        
        //Read the file line by line
        while ((line = fileReader.readLine()) != null)
        {
            //String[] tokens = line.split(DELIMITER);
        	Star s = new Star(line);
        	if (DEBUG) { s.print(); }
        	listOfStars.add(s);
        }
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    finally
    {
        try {
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    if (DEBUG && l != null) { System.out.println("readStars: length of l is "+l.size());}
	
    return l;
}

public ArrayList<Star> getStars() {
	return this.listOfStars;
}

public ArrayList<Messier> getMessierObjects() {
	return this.listOfMessierObjects;
}

public ArrayList<Planet> getPlanets() {
	return this.listOfPlanets;
}

public List<Constellation> getConstellations() {
	return this.listOfConstellations;
}

public void print() {
	if (DEBUG) { System.out.println("Parser.print()"); }
	if (this.listOfStars != null) {

		for (Star s: this.listOfStars) {
			System.out.println("Parser.print() for");
			s.print();
		}
	}
//	if (listOfMessier != null) {
//		for (Messier m: listOfMessier) {
//			m.print();
//		}
//	}
}

} // end of Parser.java
