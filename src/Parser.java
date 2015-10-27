import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.FileReader;

// Read data from flat files into object arrays.


public class Parser {
	
	
// constructor 
public Parser() {

}

public List<File> findDataFiles() {
	File f = new File("./data");
	ArrayList<File> files = new ArrayList<File>(Arrays.asList(f.listFiles()));
	return files;
}

public List<Star> getStars() {

}

public List<Messier> getMessierObjects() {
	
}

public List<Planet> getPlanets() {
	
}

public List<Constellation> getConstellations() {
	
}

private List<Star> loadStars(String filename) { 

}

} // end of Parser.java
