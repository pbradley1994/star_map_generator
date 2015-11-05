import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
// Read data from flat files into object arrays.


public class Parser {
	
	// 
	ArrayList<Star> listOfStars=null;
	ArrayList<Messier> listOfMessierObjects=null;
	ArrayList<Planet> listOfPlanets=null;
	ArrayList<Constellation> listOfConstellations=null;

	
// constructor 
public Parser() {
	ArrayList<File> listOfFiles=findDataFiles("./data");
	readStars();
	
//	BufferedReader b=BufferedReader(FileReader(File))
//	String[] thingy = myString.split(",");
//	Class.forName("the.class.full.Name");
	
}

private ArrayList<File> findDataFiles(String pathname) {
	File f = new File(pathname);
	ArrayList<File> files = new ArrayList<File>(Arrays.asList(f.listFiles()));
	return files;
}

// Create an ArrayList of stars from provided file handle.
public ArrayList<Star> readStars() {
	
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
        	Star s = new Star(line);
            //Get all tokens available in line
            String[] tokens = line.split(DELIMITER);
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
	if (listOfStars != null) {
		for (Star s: listOfStars) {
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
