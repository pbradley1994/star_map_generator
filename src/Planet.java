import java.io.BufferedReader;
import java.io.FileReader;

/***********************************************************
 * This class is used as the data structure for the planets
 * Author: Casey Pence
 ***********************************************************/

public class Planet {
	
	/*
	 * Schema:
	 * 0            1       2       3       4       5       6
	 * PlanetName	Lscal	Lprop	Ascal	Aconst	Escal	Eprop	
	 * 7		8       9       10      11      12
	 * Iscal	Iprop	Wscal	Wprop	Oscal	Oprop
	 */
	
	final static boolean DEBUG=true;	
	
	/************************
	* Class Variables
	************************/
	String m_name;
	
	double m_rightAscension;
	double m_declination;
	double m_hourAngle;
	
	//constants based on each planet and a Julian Date
	double m_semimajorAxis;
	double m_eccentricityOfOrbit;
	double m_inclinationOnPlane;
	double m_perihelion;
	double m_longitudeOfAscendingNode;
	double m_meanLongitude;
	double m_orbitalPeriod;             //in tropical years
	
	// information to read from file;
	boolean label=true;          // Is a label required for this planet?  Assume it is.
	double semimajorS;		     // Ascal - semimajor axis - scalar   (a = semimajorS + semimajorP*cy)
	double semimajorP;			 // Aprop - semimajor axis - proportional (a = semimajorS + semimajorP*cy)
	double eccentricityS;        // Escal - eccentricity   - scalar       (e = eccentricityS + eccentricityP * cy)
	double eccentricityP;        // Eprop - eccentricity   - proportional (e = eccentricityS + eccentricityP * cy)
	double inclinationS;         // Iscal - inclination    - scalar       (i = ( Iscal - Iprop * cy / 3600) * RADS)
	double inclinationP;         // Iprop - inclination    - proportional (i = ( Iscal - Iprop * cy / 3600) * RADS)
	double perihelionS;          // Wscal - arg of perihelion - scalar         (ω = (Wscal + Wprop * cy / 3600) * RADS)
	double perihelionP;          // Wprop - arg of perihelion - proportional   (ω = (Wscal + Wprop * cy / 3600) * RADS)
	double longOfAscNodeS;       // Oscal - longitude of ascending node - scalar        (Ω = (Oscal - Oprop * cy / 3600) * RADS)
	double longOfAscNodeP;       // Oprop - longitude of ascending node - proportional  (Ω = (Oscal - Oprop * cy / 3600) * RADS)
	double meanLongitudeS;       // Lscal - mean longitude  - scalar         L=Mod2Pi ((Lscal + Lprop * cy / 3600) * RADS)
	double meanLongitudeP;       // Lprop - mean longitude  - proportional   L=Mod2Pi ((Lscal + Lprop * cy / 3600) * RADS)

	/*****************************
	 * Class Constructor
	 * - created by the controller
	*****************************/
	public Planet(String csvString)
	{
		//Get all tokens available in line
		String delimiter = ",";
		String[] tokens = csvString.split(delimiter);	
		
		try
	    {		
			if (tokens.length == 13) {
				m_name=tokens[0];
				semimajorS=Double.parseDouble(tokens[3]);		   
				semimajorP=Double.parseDouble(tokens[4]);		
				eccentricityS=Double.parseDouble(tokens[5]);        
				eccentricityP=Double.parseDouble(tokens[6]);       
				inclinationS=Double.parseDouble(tokens[7]);     
				inclinationP=Double.parseDouble(tokens[8]);         
				perihelionS=Double.parseDouble(tokens[9]);        
				perihelionP=Double.parseDouble(tokens[10]);         
				longOfAscNodeS=Double.parseDouble(tokens[11]);     
				longOfAscNodeP=Double.parseDouble(tokens[12]);      
				meanLongitudeS=Double.parseDouble(tokens[1]);      
				meanLongitudeP=Double.parseDouble(tokens[2]); 
			}
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	/*******************************
	 * Planet Getters
	 *******************************/
	public String getPlanetName()
	{
		return m_name;
	}
	public double getRightAscension()
	{
		return m_rightAscension;
	}
	public double getDeclination()
	{
		return m_declination;
	}
	public double getHourAngle()
	{
		return m_hourAngle;
	}
	public double getSemimajorAxis()
	{
		return m_semimajorAxis;
	}
	public double getEccentricityOfOrbit()
	{
		return m_eccentricityOfOrbit;
	}
	public double getInclinationOnPlane()
	{
		return m_inclinationOnPlane;
	}
	public double getPerihelion()
	{
		return m_perihelion;
	}
	public double getLongitudeOfAscendingNode()
	{
		return m_longitudeOfAscendingNode;
	}
	public double getMeanLongitude()
	{
		return m_meanLongitude;
	}
	public double getOrbitalPeriod()
	{
		return m_orbitalPeriod;
	}
	/*******************************
	 * Planet Setters
	 *******************************/
	public void setRightAscension(double rightAsc)
	{
		m_rightAscension = rightAsc;
	}
	public void setDeclination(double declination)
	{
		m_declination = declination;
	}
	public void setHourAngle(double hourAngle)
	{
		m_hourAngle = hourAngle;
	}
	public void setSemimajorAxis(double semimajorAxis)
	{
		m_semimajorAxis = semimajorAxis;
	}
	public void setEccentricityOfOrbit(double eccentricityOfOrbit)
	{
		m_eccentricityOfOrbit = eccentricityOfOrbit;
	}
	public void setInclinationOnPlane(double inclination)
	{
		m_inclinationOnPlane = inclination;
	}
	public void setPerihelion(double perihelion)
	{
		m_perihelion = perihelion;
	}
	public void setLongitudeOfAscendingNode(double longitudeOfAscNode)
	{
		m_longitudeOfAscendingNode = longitudeOfAscNode;
	}
	public void setMeanLongitude(double meanLongitude)
	{
		m_meanLongitude = meanLongitude;
	}
	public void setOrbitalPeriod(double orbitalPeriod)
	{
		m_orbitalPeriod = orbitalPeriod;
	}

	// Other methods
	
	/**
	 * calculateAttributes
	 * @param cy
	 * @param rads
	 */
	public void calculateAttributes(int cy, double rads) {
		this.m_semimajorAxis=( this.semimajorS + (this.semimajorP*cy) );                               // a = semimajorS + semimajorP*cy
		this.m_eccentricityOfOrbit=(  this.eccentricityS + (this.eccentricityP*cy) );                  // e = eccentricityS + eccentricityP * cy
		this.m_inclinationOnPlane=( this.inclinationS - ((this.inclinationP*cy)/3600.0) * rads );      // I = Iscal - Iprop * cy / 3600) * RADS)
		this.m_perihelion=(perihelionS + ((perihelionP * cy)/3600.0) * rads);                           //(ω = (Wscal + Wprop * cy / 3600) * RADS)
		this.m_longitudeOfAscendingNode=( longOfAscNodeP - ((longOfAscNodeP*cy)/3600) * rads);         //Ω = (Oscal - Oprop * cy / 3600) * RADS
		//this.m_meanLongitude=Calculator.mod2pi( meanLongitudeS + ((meanLongitudeP*cy)/3600.0) * rads ); //L=Mod2Pi ((Lscal + Lprop * cy / 3600) * RADS)
	}
	
	public void print() {
			System.out.print("Name:  "+this.m_name+"  ");
			System.out.print("SMA params:  "+this.semimajorS+", "+this.semimajorP+"  ");
			System.out.print("Peri params:  "+this.perihelionS+", "+this.perihelionP+"  ");
			System.out.print("RA:    "+this.m_rightAscension+"  ");
			System.out.println("Dec:    "+this.m_declination+"   ");
	}
	
}
