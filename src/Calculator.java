/**
 * This class is used by the Controller to perform all of the 
 * calculations needed to create the star map based on user input
 * 
 * @author Casey Pence
 */
public class Calculator {
	
	/************************
	* Class Variables
	************************/
	double ToDeg; //to convert from radians to degrees
	double ToRad; //to convert from degrees to radians
	
	/*****************************
	 * Class Constructor
	 * - created by the controller
	*****************************/
	public Calculator()
	{
		ToDeg = Math.PI / 180.0;
		ToRad = 180.0 / Math.PI;
	}
	
	/*************************************************************
	 * This function calculates the local hour angle of a star or
	 * any other fixed space object. The hour angle is used to track
	 * a star across the sky as the earth rotates.  
	 * 
	 * @param rightAsc of the star in decimal coordinate form
	 * @param localTime (LST) of the user in decimal hour form
	 * @return hourAngle of the star in decimal coordinate form
	 *************************************************************/
	public double findHourAngle(double rightAsc, double localTime) {
		double hourAngle = localTime - rightAsc;
		if (hourAngle < 0.0)
			hourAngle = hourAngle + 24.0;
		return hourAngle;
	}
	
	/**************************************************************
	 * This function finds the mean anomaly of a Planet.
	 * 
	 * @param planet as the planet to find mean anomaly of
	 * @param days as the number of days since epoch 2000
	 * @return mean anomaly of planet in degree form
	 **************************************************************/
	public double findMeanAnomaly(Planet planet, double days) 
	{
		double orbitalPeriod = planet.getOrbitalPeriod();
		double meanLong = planet.getMeanLongitude();
		double perihelion = planet.getPerihelion();
		
		double meanAnomaly = (360.0 / 365.242191) * (days / orbitalPeriod) + meanLong - perihelion;

		return meanAnomaly;
	}

	/*******************************************************************
	 * This function calculators the Heliocentric Longitude of a Planet.
	 * 
	 * @param planet as the planet to find helio long of
	 * @param meanAnomaly of the planet
	 * @param days as the number of days since Epoch 2000
	 * @return heliocentric longitude of the planet in degree form
	 *******************************************************************/
	public double findHeliocentricLongitude(Planet planet, double meanAnomaly, double days) 
	{
		double orbitalPeriod = planet.getOrbitalPeriod();
		double eccentricity = planet.getEccentricityOfOrbit();
		double meanLong = planet.getMeanLongitude();
		//find sin(meanAnomaly) and convert back to degrees
		double radMeanAnomaly = meanAnomaly * ToRad;
		meanAnomaly = Math.sin(radMeanAnomaly);
		meanAnomaly = meanAnomaly * ToDeg;
		
		double helioLong = (360.0 / 365.242191) * (days / orbitalPeriod) + eccentricity * meanAnomaly + meanLong;
		
		//adjust if outside 0 - 360 range
		if(helioLong > 360.0)
			helioLong -= 360.0;
		if(helioLong < 0.0)
			helioLong += 360.0;
		
		return helioLong;
	}

	/*************************************************************************
	 * This function finds the true anomaly of a Planet.
	 * 
	 * @param planet as the planet to find true anomaly of
	 * @param helioLong as the heliocentric longitude of planet in degree form
	 * @return true anomaly of planet in degree form
	 *************************************************************************/
	public double findTrueAnomaly(Planet planet, double helioLong) 
	{
		double perihelion = planet.getPerihelion();
		
		double trueAnomaly = helioLong - perihelion;

		return trueAnomaly;
	}

	/**************************************************************
	 * This function finds the vector radius of a Planet.
	 * 
	 * @param planet as the planet to find the vector radius of
	 * @param trueAnomaly of the planet in degree form
	 * @return vector radius of planet in Astronomical Units (AU)
	 **************************************************************/
	public double findVectorRadius(Planet planet, double trueAnomaly) 
	{
		double axis = planet.getSemimajorAxis();
		double eccentricity = planet.getEccentricityOfOrbit();
		//find cos(trueAnomaly) and convert back to degrees
		double radTrueAnomaly = trueAnomaly * ToRad;
		trueAnomaly = Math.cos(radTrueAnomaly);
		trueAnomaly = trueAnomaly * ToDeg;
		
		double vectorRadius = (axis * (1 - Math.pow(eccentricity, 2.0))) / (1 + eccentricity * trueAnomaly);
		
		return vectorRadius;
	}

	/**************************************************************
	 * This function finds the heliocentric latitude of a Planet.
	 * 
	 * @param planet as the planet to find helio lat of
	 * @param helioLongitude of the planet in degree form
	 * @return helio lat of planet in degree form
	 **************************************************************/
	public double findHeliocentricLatitude(Planet planet, double helioLongitude) 
	{
		double longAscNode = planet.getLongitudeOfAscendingNode();
		double inclination = planet.getInclinationOnPlane();
		//convert everything to radians for calculations
		helioLongitude = ToRad * helioLongitude;
		longAscNode = ToRad * longAscNode;
		inclination = ToRad * inclination;
		
		double temp = Math.sin(helioLongitude - longAscNode) * Math.sin(inclination);
		double helioLat = Math.asin(temp);
		
		//convert back to degrees for return
		helioLat = ToDeg * helioLat;
		
		return helioLat;
	}

	/************************************************************************
	 * This function finds the projected heliocentric longitude of a planet
	 * 
	 * @param planet as the planet to find projected helio long of
	 * @param helioLongitude of the planet in degree form
	 * @return projected helio long of planet in degree form
	 ************************************************************************/
	public double findProjectedHelioLong(Planet planet, double helioLongitude) 
	{
		double longAscNode = planet.getLongitudeOfAscendingNode();
		double inclination = planet.getInclinationOnPlane();
		//convert to radians for calculations
		helioLongitude = ToRad * helioLongitude;
		double radLongAscNode = ToRad * longAscNode;
		inclination = ToRad * inclination;
		
		double x = Math.cos(helioLongitude - radLongAscNode);
		double y = Math.sin(helioLongitude - radLongAscNode) * Math.cos(inclination);
		double temp = Math.atan2(y, x);
		//convert back to degrees to finish out function
		x = ToDeg * x;
		y = ToDeg * y;
		temp = ToDeg * temp;
		
		double projectedHelioLong = temp + longAscNode;

		return projectedHelioLong;
	}

	/**************************************************************
	 * This function finds the projected radius vector of a planet
	 * 
	 * @param radiusVector of the planet in AU
	 * @param helioLatitude of planet in degrees
	 * @return projected radius vector of planet in AU
	 **************************************************************/
	public double findProjectedRadiusVector(double radiusVector, double helioLatitude) 
	{
		//convert to radians for Math.cos
		helioLatitude = ToRad * helioLatitude;
		helioLatitude = Math.cos(helioLatitude);
		helioLatitude = ToDeg * helioLatitude;
		
		double projectedRadiusVector = radiusVector * helioLatitude;
		return projectedRadiusVector;
	}

	/**************************************************************
	 * This function finds the geocentric longitude of a planet
	 * 
	 * @param projectedRadiusVectorPlanet in AU
	 * @param projectedHelioLongPlanet in degrees
	 * @param radiusVectorEarth in AU
	 * @param helioLongEarth in degrees
	 * @return geocentric longitude of planet in degree form
	 **************************************************************/
	public double findGeocentricLongitude(String name, double projectedRadiusVectorPlanet, double projectedHelioLongPlanet,
			double radiusVectorEarth, double helioLongEarth) 
	{
		Boolean inner = false;
		double top;
		double bottom;
		double temp;
		double geoLong;
		//convert everything to radians for Math.cos, etc
		projectedRadiusVectorPlanet = ToRad * projectedRadiusVectorPlanet;
		projectedHelioLongPlanet = ToRad * projectedHelioLongPlanet;
		radiusVectorEarth = ToRad * radiusVectorEarth;
		helioLongEarth = ToRad * helioLongEarth;
		
		if(name == "Mercury" || name == "Venus")
		{
			inner = true;
		}
		if(inner)
		{
			top = projectedRadiusVectorPlanet * Math.sin(helioLongEarth - projectedHelioLongPlanet);
			bottom = radiusVectorEarth - projectedRadiusVectorPlanet * Math.cos(helioLongEarth - projectedHelioLongPlanet);
			temp = Math.atan(top / bottom);
			geoLong = 180.0 + helioLongEarth + temp;
		}
		else //outer planet
		{
			top = radiusVectorEarth * Math.sin(projectedHelioLongPlanet - helioLongEarth);
			bottom = projectedRadiusVectorPlanet - radiusVectorEarth * Math.cos(projectedHelioLongPlanet - helioLongEarth);
			temp = Math.atan(top / bottom);
			geoLong = temp + projectedHelioLongPlanet;
		}
		//convert geoLong back to degrees
		geoLong = ToDeg * geoLong;
		//adjust if outside 0-360 range
		if(geoLong < 0.0)
			geoLong += 360.0;
		if(geoLong > 360.0)
			geoLong -= 360.0;
		
		return geoLong;
	}

	/**************************************************************
	 * This function finds the geocentric latitude of a planet.
	 * 
	 * @param projectedRadiusVectorPlanet in AU
	 * @param helioLatPlanet in degrees
	 * @param geoLongPlanet in degrees
	 * @param projectedHelioLongPlanet in degrees
	 * @param raiusVectorEarth in AU
	 * @param helioLongEarth in degrees
	 * @return geocentric latitude of planet in degree form
	 **************************************************************/
	public double findGeocentricLatitude(double projectedRadiusVectorPlanet, double helioLatPlanet,
			double geoLongPlanet, double projectedHelioLongPlanet, double radiusVectorEarth, double helioLongEarth) 
	{
		//convert to radians for Math.tan, etc
		projectedRadiusVectorPlanet = ToRad * projectedRadiusVectorPlanet;
		helioLatPlanet = ToRad * helioLatPlanet;
		geoLongPlanet = ToRad * geoLongPlanet;
		projectedHelioLongPlanet = ToRad * projectedHelioLongPlanet;
		helioLongEarth = ToRad * helioLongEarth;
		
		double top = projectedRadiusVectorPlanet * Math.tan(helioLatPlanet) * Math.sin(geoLongPlanet - projectedHelioLongPlanet);
		double bottom = radiusVectorEarth * Math.sin(projectedHelioLongPlanet - helioLongEarth);
		double geoLat = Math.atan(top / bottom);
		//convert back to degrees
		geoLat = ToDeg * geoLat;

		return geoLat;
	}

	/**************************************************************
	 * This Function finds the declination of a planet.
	 * 
	 * @param planet as the planet to find declination of
	 * @param geoLatPlanet in degrees
	 * @param geoLongPlanet in degrees
	 * @return declination of planet in degree form
	 **************************************************************/
	public double findPlanetDeclination(Planet planet, double geoLatPlanet, double geoLongPlanet) 
	{		
		double meanLongPlanet = planet.getMeanLongitude();
		//convert to radians for Math.cos, etc
		meanLongPlanet = ToRad * meanLongPlanet;
		geoLatPlanet = ToRad * geoLatPlanet;
		geoLongPlanet = ToRad * geoLongPlanet;
		
		double tempA = Math.sin(geoLatPlanet)*Math.cos(meanLongPlanet);
		double tempB = Math.cos(geoLatPlanet) * Math.sin(meanLongPlanet) * Math.sin(geoLongPlanet);
		double tempC = tempA + tempB;
		double declination = Math.asin(tempC);
		//convert back to degrees
		declination = ToDeg * declination;
		return declination;
	}

	/**************************************************************
	 * This function finds the right ascension of a planet.
	 * 
	 * @param planet as the planet to find the right ascension of
	 * @param geoLatPlanet in degrees
	 * @param geoLongplanet in degrees
	 * @return right ascension of planet in degree form
	 **************************************************************/
	public double findPlanetRightAscension(Planet planet, double geoLatPlanet, double geoLongPlanet) 
	{
		double meanLongPlanet = planet.getMeanLongitude();
		//convert to radians for Math.cos, etc
		meanLongPlanet = ToRad * meanLongPlanet;
		geoLatPlanet = ToRad * geoLatPlanet;
		geoLongPlanet = ToRad * geoLongPlanet;
		
		double x = Math.cos(geoLongPlanet);
		double y = Math.sin(geoLongPlanet) * Math.cos(meanLongPlanet) - Math.tan(geoLatPlanet) * Math.sin(meanLongPlanet);
		double rightAsc = Math.atan2(y, x);
		
		//convert back to degrees
		x = ToDeg * x;
		y = ToDeg * y;
		rightAsc = ToDeg * rightAsc;
		
		//adjust if outside 0-360 range
		if (rightAsc > 360.0)
			rightAsc -= 360.0;
		if (rightAsc < 0.0)
			rightAsc += 360;
		
		rightAsc = rightAsc / 15.0; //to get decimal hours
		
		return rightAsc;
	}
}
