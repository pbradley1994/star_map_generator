import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Hashtable;

// Read data from flat files into object arrays.

/**
 * @author kexline
 *
 */

public class Parser {

	final static boolean DEBUG=false;	
	
	// Maintain a list of each type of object.
	ArrayList<Star> listOfStars=new ArrayList<Star>();
	ArrayList<Messier> listOfMessierObjects=new ArrayList<Messier>();
	//ArrayList<Planet> listOfPlanets=new ArrayList<Planet>();
	ArrayList<Constellation> listOfConstellations=new ArrayList<Constellation>();
	Hashtable<Integer,Star> starDictionary=new Hashtable<Integer,Star>();
	
/**
 * Constructor
 *  
 */
public Parser() {
//	ArrayList<File> listOfFiles=findDataFiles("./data");
	if (DEBUG) { System.out.println("Parser:32 - Parser()"); }
	listOfStars=readStars();
 	listOfMessierObjects=readMessier();
 	listOfConstellations=readConstellations();
	//listOfPlanets=readPlanets();
 	starDictionary=makeStarDictionary();
	
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

/**
 * readStars(): Create an ArrayList of stars from provided file handle.
 * @return ArrayList of Star objects populated from CSV
 */
public ArrayList<Star> readStars() {
	
	if (DEBUG) {System.out.println("readStars() start"); }
	
	ArrayList<Star> l=new ArrayList<Star>();
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
        	l.add(s);
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

/** 
 * readMessier():  Create an ArrayList of Messier Deep Space Objects from provided file handle.
 * @return
 */
public ArrayList<Messier> readMessier() {
	
	if (DEBUG) { System.out.println("readMessier() start"); }
	
	ArrayList<Messier> l=new ArrayList<Messier>();
	String fileToParse = "./data/Messier.csv";
	BufferedReader fileReader = null;

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
	     	Messier s = new Messier(l.size(), line);    // use the list index as object id 
	     	l.add(s);
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
	
	 if (DEBUG && l != null) { System.out.println("readMessier: length of l is "+l.size());}
		
	 return l;
}

/** 
 * readConstellations():  Create an ArrayList of Messier Deep Space Objects from provided file handle.
 * @return
 */
public ArrayList<Constellation> readConstellations() {
	
	if (DEBUG) { System.out.println("readMessier() start"); }
	
	ArrayList<Constellation> l=new ArrayList<Constellation>();
	String fileToParse = "./data/Constellation.csv";
	BufferedReader fileReader = null;

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
	     	Constellation c = new Constellation(l.size(), line);    // use the list index as object id 
	     	l.add(c);
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
	
	 if (DEBUG && l != null) { System.out.println("readConstellations: length of l is "+l.size());}
		
	 return l;
}

public Hashtable<Integer,Star> makeStarDictionary() {
	Hashtable<Integer,Star> ht = new Hashtable<Integer,Star>();
	for (Star star : this.listOfStars) {
		ht.put(star.getID(), star);
	}
	return ht;
}


//public ArrayList<Planet> readPlanets() {
//	
//	if (DEBUG ) { System.out.println("readPlanets() start"); }
//	
//	ArrayList<Planet> l=new ArrayList<Planet>();
//	String fileToParse = "./data/Planet.csv";
//    BufferedReader fileReader = null;
//
//    final String DELIMITER = ",";
//    try
//    {
//        String line = "";
//        //Create the file reader
//        fileReader = new BufferedReader(new FileReader(fileToParse));
//         
//        // Throw away the first line, which contains column names.
//        fileReader.readLine();
//        
//        //Read the file line by line
//        while ((line = fileReader.readLine()) != null)
//        {
//            //String[] tokens = line.split(DELIMITER);
//        	Planet p = new Planet(line);
//        	l.add(p);
//        	if (DEBUG ) { System.out.println("readPlanets:  "+line); }
//
//        }
//    }
//    catch (Exception e) {
//        e.printStackTrace();
//    }
//    finally
//    {
//        try {
//            fileReader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    if (DEBUG && l != null) { System.out.println("readPlanets: length of l is "+l.size());}
//	
//    return l;
//}


public ArrayList<Star> getStars() {
	return this.listOfStars;
}

public ArrayList<Messier> getMessierObjects() {
	return this.listOfMessierObjects;
}

//public ArrayList<Planet> getPlanets() {
//	return this.listOfPlanets;
//}

public ArrayList<Constellation> getConstellations() {
	return this.listOfConstellations;
}

public void print(double mag) {
	if (this.listOfStars != null) {

		for (Star s: this.listOfStars) {
			if (s.getMagnitude() < mag) {
				s.print();
			}
		}
  	}
	if (listOfMessierObjects != null) {
		for (Messier m: listOfMessierObjects) {
			m.print();
		}
	}
	
//	if (listOfPlanets != null) {
//		for (Planet p: listOfPlanets) {
//			p.print();
//		}
//	}	
	
}

} // end of Parser.java
