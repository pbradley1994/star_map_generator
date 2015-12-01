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
		ToDeg = 180.0 / Math.PI;
		ToRad = Math.PI / 180.0;
	}
	/***************************************************
	 *  This function is used below in calculating
	 *  the Mean Longitude of each planet
	 *  Note: Given by Dr. Coleman
	 *  @param value as a mean long in radians
	 *  @return value as adjusted mean long in degrees
	 ***************************************************/
	private double mod2pi(double value)
	{
		double absB;
		double B = value / (2 * Math.PI);
		
		if(B >= 0)
			absB = Math.floor(B);
		else
			absB = Math.ceil(B);
		
		double A = (2 * Math.PI) * (B - absB);
		if(A < 0) 
			A = (2 * Math.PI) + A;
		value = A;

		return value;
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
	/************************************************************
	 * This function normalizes days for a planet according to its
	 * orbital period
	 * 
	 * @param orbitalPeriod of the planet
	 * @param days as number of days since the epoch
	 * @return n as the normalized days
	 ************************************************************/
	public double findN(double orbitalPeriod, double days) 
	{
		double n = (360/365.242191) * (days / orbitalPeriod);
		if(n > 360.0)
		{
			while (n > 360.0)
			{
				n -= 360.0;
			}
		}
		if(n < 0.0)
		{
			while (n < 0.0)
			{
				n += 360.0;
			}
		}
		return n;
	}
	/**************************************************************
	 * This function finds the mean anomaly of a Planet.
	 * 
	 * @param planet as the planet to find mean anomaly of
	 * @param n 
	 * @param days as the number of days since epoch 2000
	 * @return mean anomaly of planet in degree form
	 **************************************************************/
	public double findMeanAnomaly(Planet planet, double n) 
	{
		double meanLong = planet.getMeanLongitude();
		double perihelion = planet.getPerihelion();
		
		double meanAnomaly = n + meanLong - perihelion;

		return meanAnomaly;
	}

	/*******************************************************************
	 * This function calculators the Heliocentric Longitude of a Planet.
	 * 
	 * @param planet as the planet to find helio long of
	 * @param meanAnomaly of the planet
	 * @param n 
	 * @param days as the number of days since Epoch 2000
	 * @return heliocentric longitude of the planet in degree form
	 *******************************************************************/
	public double findHeliocentricLongitude(Planet planet, double meanAnomaly, double n) 
	{
		double eccentricity = planet.getEccentricityOfOrbit();
		double meanLong = planet.getMeanLongitude();
		//find sin(meanAnomaly) and convert back to degrees
		double radMeanAnomaly = meanAnomaly * ToRad;
		meanAnomaly = Math.sin(radMeanAnomaly);
//		meanAnomaly = meanAnomaly * ToDeg;
		
		double helioLong = n + ((360/Math.PI) * eccentricity * meanAnomaly) + meanLong;
		
		//adjust if outside 0 - 360 range
		if(helioLong > 360.0 || helioLong < 0.0)
		{
			helioLong = helioLong * ToRad;
			helioLong = mod2pi(helioLong);
			helioLong = helioLong * ToDeg;
		}
		
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
		
		double vectorRadius = (axis * (1 - Math.pow(eccentricity, 2.0))) / (1 + eccentricity * trueAnomaly);
		
		return vectorRadius;
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
	public double findGeocentricLongitude(String name, double radiusVectorPlanet, double helioLongPlanet,
			double radiusVectorEarth, double helioLongEarth) 
	{
		Boolean inner = false;
		double top;
		double bottom;
		double temp;
		double geoLong;
		//convert everything to radians for Math.cos, etc
		double radHelioLongPlanet = ToRad * helioLongPlanet;
		double radHelioLongEarth = ToRad * helioLongEarth;
		
		if(name == "Mercury" || name == "Venus")
		{
			inner = true;
		}
		if(inner)
		{
			top = radiusVectorEarth * Math.sin(radHelioLongEarth - radHelioLongPlanet);
			bottom = 1.0 - radiusVectorEarth * Math.cos(radHelioLongEarth - radHelioLongPlanet);
			temp = Math.atan2(top, bottom);
			geoLong = temp + helioLongEarth + 180.0;
		}
		else //outer planet
		{
			top = Math.sin(radHelioLongPlanet - radHelioLongEarth);
			bottom = radiusVectorEarth - Math.cos(radHelioLongPlanet - radHelioLongEarth);
			geoLong = Math.atan2(top, bottom);
			geoLong = geoLong + helioLongPlanet;
		}
		//adjust if outside 0-360 range
		if(geoLong > 360.0)
		{
			while (geoLong > 360.0)
			{
				geoLong -= 360.0;
			}
		}
		if(geoLong < 0.0)
		{
			while (geoLong < 0.0)
			{
				geoLong += 360.0;
			}
		}
		
		return geoLong;
	}

	/**************************************************************
	 * This Function finds the declination of a planet.
	 * 
	 * @param meanLongPlanet in degrees
	 * @param geoLatPlanet in degrees
	 * @param geoLongPlanet in degrees
	 * @return declination of planet in degree form
	 **************************************************************/
	public double findPlanetDeclination(double meanLongPlanet, double geoLatPlanet, double geoLongPlanet) 
	{		
		//convert to radians for Math.cos, etc
		double radMeanLongPlanet = ToRad * meanLongPlanet;
		double radGeoLatPlanet = ToRad * geoLatPlanet;
		double radGeoLongPlanet = ToRad * geoLongPlanet;
		double declination = 0.0;

			double tempA = Math.sin(radGeoLatPlanet)*Math.cos(radMeanLongPlanet);
			double tempB = Math.cos(radGeoLatPlanet) * Math.sin(radMeanLongPlanet) * Math.sin(radGeoLongPlanet);
			double tempC = tempA + tempB;
			declination = Math.asin(tempC);
			declination = mod2pi(declination); //adjust if needed

		return declination;
	}

	/**************************************************************
	 * This function finds the right ascension of a planet.
	 * 
	 * @param meanLongPlanet in degrees
	 * @param geoLatPlanet in degrees
	 * @param geoLongplanet in degrees
	 * @return right ascension of planet in degree form
	 **************************************************************/
	public double findPlanetRightAscension(double meanLongPlanet, double geoLatPlanet, double geoLongPlanet) 
	{
		//convert to radians for Math.cos, etc
		double radMeanLongPlanet = ToRad * meanLongPlanet;
		double radGeoLatPlanet = ToRad * geoLatPlanet;
		double radGeoLongPlanet = ToRad * geoLongPlanet;
		double rightAsc = 0.0;
		
		double x = Math.cos(radGeoLongPlanet);
		double y = Math.sin(radGeoLongPlanet) * Math.cos(radMeanLongPlanet) - Math.tan(radGeoLatPlanet) * Math.sin(radMeanLongPlanet);
		rightAsc = Math.atan2(y, x);
				
		//adjust if outside 0-360 range
		rightAsc = mod2pi(rightAsc);
		
		rightAsc = ToDeg * rightAsc;
		rightAsc = rightAsc / 15.0; //to get decimal hours
		
		return rightAsc;
	}
	/*************************************************************************
	 * This function corrects for the moon evection when finding its position
	 * @param sunGeoLong in degrees
	 * @param meanLongitude in degrees
	 * @param moonMeanAnomaly in degrees
	 * @return correction in degrees
	 *************************************************************************/
	public double correctMoonEvection(double sunGeoLong, double meanLongitude, double moonMeanAnomaly) 
	{
		double radSunGeoLong = sunGeoLong * ToRad;
		double radMeanLong = meanLongitude * ToRad;
		double radMoonMeanAnomaly = moonMeanAnomaly * ToRad;
		double correction = 1.2739 * Math.sin(2 * (radMeanLong - radSunGeoLong) - radMoonMeanAnomaly);
		return correction;
	}
}
