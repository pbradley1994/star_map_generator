/***********************************************************
 * This class is used as the data structure for the planets
 * Author: Casey Pence
 ***********************************************************/
public class Planet {
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
	double m_orbitalPeriod; //in tropical years
	
	
	/*****************************
	 * Class Constructor
	 * - created by the controller
	*****************************/
	public Planet(String name)
	{
		m_name = name;
	}
	
	/*******************************
	 * Planet Getters
	 *******************************/
	public String getName()
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
}
