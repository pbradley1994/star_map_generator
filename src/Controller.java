import java.util.ArrayList;

/**
 * This class is the controller of model/view/controller pattern
 * It receives user input information from the GUI,
 * Gets lists for stars, planets, etc from the Parser,
 * Uses the Calculator to determine plotting points for each list type,
 * and finally passes this information back to the GUI JOGL for drawing.
 * 
 * @author Casey Pence
 */
public class Controller {
	
	/*******************
	* Class Variables
	*******************/
	ArrayList<Star> starList;
	ArrayList<Messier> messierList;
	ArrayList<Planet> planetList;
	Planet earth;
	Moon moon;
	ArrayList<Constellation> constellationList;
	Parser theParser;
	Calculator theCalculator;
	double userLocalTime;  //in decimal form 
	double userLat;		   //in degrees
	double userLong;	   //in degrees
	double userJulianDate;
	double epoch2000JD; 
	double userDecimalDay;
	int userYear;
	
	/************************************************
	 * Class Constructor
	 * - created in main()
	 * 
	 * Creates Parser and Calculator
	 * Calls Parser to get starList, the moon
	 * messierList, and constellationList
	 ***********************************************/
	public Controller()
	{
		theParser = new Parser();
		starList = theParser.getStars();
		messierList = theParser.getMessierObjects();
		moon = new Moon("moon");
		constellationList = theParser.getConstellations();
		planetList = new ArrayList<Planet>();
		theCalculator = new Calculator();
		epoch2000JD = 2451545.0;
	}
        
        /************************************************
         * Return a star object given the star objects id.
         * @param StarID id of star you are looking for 
         ************************************************/
        public Star getStarfromStarID(Integer StarID)
        {
            for (Star star : starList) {
                if (star.id == StarID) {
                    return star;
                }
            }
            System.out.println("StarID " + StarID + " was not found in starList.");
            return null;
        }
	
	/***********************************************************
	 * This function is called by the GUI once the user inputs
	 * their data. It saves the users data to local variables,
	 * and then adjusts star map data based on this information.
	 * @param latitude as user latitude in decimal form
	 * @param longitude as user longitude in decimal form
	 * @param month as user month where 1 = Jan, 12 = Dec
	 * @param day as user day of the month, 1-31
	 * @param year as user year
	 * @param timezone as user time zone offset from GMT
	 * @param hour as the hour given by the user in military time
	 * @param minutes as the minutes of the hour given by the user
	 ************************************************************/
	public void setUserData(double latitude, double longitude, int month, int day, int year, double timezone, int hour, int minutes)
	{
		userLat = latitude;
		userLong = longitude;
		userYear = year;
		//********************************************
		//find local time of the user in decimal form
		//********************************************
		//Note hour must be in military time
		double temp = (double)minutes / 60.0;
		userLocalTime = hour + temp;
		
		//******************************************
		//find GMT time of the user in decimal form
		//******************************************
		double userGMT = userLocalTime - timezone;
		if(userGMT > 24.0)
			userGMT -= 24.0;
		if(userGMT < 0.0)
			userGMT += 24.0;
		
		
		//*****************************************
		//find the Julian Date based on user input
		//*****************************************
		if(month < 3) //adjust date if in Jan or Feb
		{
			userYear -= 1;
			month += 12;
		}
		userDecimalDay = (double)day + (userGMT / 24.0);
		int tempA = (int)(year/100);
		int tempB =  2 - tempA + (int)(tempA/4);
		userJulianDate = (int)(365.25 * userYear) + (int)(30.6001 * (month + 1)) + userDecimalDay + 1720994.5 + tempB;
		
		//*****************************
		//adjust star map data to user
		//*****************************
		adjustStarData();
		adjustMessierData();
		adjustMoonData(); 
		createPlanets();
		adjustPlanetData();	
	}
	
	/*************************************************
	 * This function uses theCalculator to adjust 
	 * fixed star locations based on user input
	 ************************************************/
	private void adjustStarData()
	{
		for(int i = 0; i < starList.size(); i++)
		{
			Star aStar = starList.get(i);
			double rightAsc = aStar.getRA();
			double hourAngle = theCalculator.findHourAngle(rightAsc, userLocalTime);
			aStar.setHourAngle(hourAngle);
			starList.set(i, aStar);
		}
	}
	
	/*************************************************
	 * This function uses theCalculator to adjust 
	 * moon data based on user input
	 ************************************************/
	private void adjustMoonData()
	{
		double toDeg = 180.0 / Math.PI;
		double toRad = Math.PI / 180.0;
		
		//find the number of days since the epoch 2000
		double D = ((userYear - 2000)* 365.25) + userDecimalDay;
		//find geocentric longitude of the sun (in degrees)
		double T = (epoch2000JD - 2415020.0)/36525.0;
		double sunGeoLong = 279.6966778 + (36000.7689 * T) + (T * T *0.0003025);
		//find mean anomaly of the sun (in degrees)
		double sunW = 281.2208444 + (1.719175 * T) + (T * T * 0.000452778);
		double sunMeanAnomaly = (365 / 365.242191) * D + sunGeoLong - sunW;
		
		//find moon mean longitude (in degrees)
		double temp = 13.1763966 * D + 318.351648;
		temp = AdjustDegree(temp);
		moon.setMeanLongitude(temp);
		
		//find moon mean anomaly (in degrees)
		temp = moon.getMeanLongitude() - 0.111404 * D - 36.340410;
		temp = AdjustDegree(temp);
		double moonMeanAnomaly = temp;
		
		//find moon longitude of the node (in degrees)
		temp = 318.510105 - 0.0529539 * D;
		temp = AdjustDegree(temp);
		moon.setLongitudeOfAscendingNode(temp);
		
		//calculate moons correction for evection (in degrees)
		double evectionCorrection = theCalculator.correctMoonEvection(sunGeoLong, moon.getMeanLongitude(), moonMeanAnomaly);
		
		//calculate moons correction for the annual equation
		temp = sunMeanAnomaly * toRad;
		double annualEquation = 0.1858 * Math.sin(temp);
		annualEquation *= toDeg;
		
		//find moon's third correction (in degrees)
		double A3 = 0.37 * Math.sin(temp);
		A3 *= toDeg;
		
		//find moon corrected anomaly (in degrees)
		double correctedAnomaly = moonMeanAnomaly + evectionCorrection - annualEquation - A3;
		
		//find moon's correction for the equation of the centre (in degrees)
		temp = correctedAnomaly * toRad;
		double equationCentre = 6.2886 * Math.sin(temp);
		equationCentre *= toDeg;
		
		//find moon's 4th correction (in degrees)
		double A4 = 0.214 * Math.sin(2.0*temp);
		A4 *= toDeg;
		
		//find moon's corrected longitude (in degrees)
		double correctedLong = moon.getMeanLongitude() + evectionCorrection + equationCentre - annualEquation + A4;
		
		//correct moon's longitude for variation (in degrees)
		temp = (2 * (correctedLong - sunGeoLong)) * toRad;
		double variation = 0.6583 * Math.sin(temp);
		variation *= toDeg;
		
		//find moon's true longitude (in degrees)
		double trueLong = correctedLong + variation;
		
		//calculate moon's corrected longitude of the node (in degrees)
		temp = 0.16 * Math.sin(sunMeanAnomaly * toRad);
		temp *= toDeg;
		double correctedLongNode = moon.getLongitudeOfAscendingNode() - temp;
		
		//find ecliptic longitude of moon (in degrees)
		//find y (in radians)
		double i = 5.145396 * toRad;
		temp = (trueLong - correctedLongNode) * toRad;
		double y = Math.sin(temp) * Math.cos(i);
		//find x (in radians)
		double x = Math.cos(temp);
		double tan = Math.atan2(y, x);
		tan *= toDeg;
		double eclipticLong = tan + correctedLongNode;
		
		//find ecliptic latitude of moon (in degrees)
		double eclipticLat = Math.asin(Math.sin(temp * Math.sin(i)));
		eclipticLat *= toDeg;
		
		//find moon mean obliquity (in degrees)
		double obliquity = 23.439292 - (((46.815 * T) + (T * T * 0.0006) - (T * T * T * 0.00181))/3600.0);
		
		//find moon declination
		double declination = theCalculator.findPlanetDeclination(obliquity, eclipticLat, eclipticLong);
		moon.setDeclination(declination);
		
		//find moon right ascension
		double rightAsc = theCalculator.findPlanetRightAscension(obliquity, eclipticLat, eclipticLong);
		moon.setRightAscension(rightAsc);
		
		//find moon hour angle
		double hourAngle = theCalculator.findHourAngle(rightAsc, userLocalTime);
		moon.setHourAngle(hourAngle);
	}
	/******************************************************
	 *  This function is used to adjust a degree into the
	 *  0 - 360 range.
	 *  @param value as a degree to adjust
	 *  @return value as adjusted degree
	 ******************************************************/
	private double AdjustDegree(double value)
	{
		if(value > 360.0)
		{
			while (value > 360.0)
			{
				value -= 360.0;
			}
		}
		if(value < 0.0)
		{
			while (value < 0.0)
			{
				value += 360.0;
			}
		}
		return value;
	}
	/*************************************************
	 * This function creates the nine planets based on
	 * user input to find each of the planets' constants
	 * Planets are then added to the planetsList
	 * NOTE: Constants are in Degree form
	 *************************************************/
	private void createPlanets()
	{
		double cy = userJulianDate / 36525.0;
		double semimajorAxis;
		double eccentricityOfOrbit;
		double inclinationOnPlane;
		double perihelion;
		double longitudeOfAscendingNode;
		double meanLongitude;
        String unicode_icon;
		
		planetList.clear();
		
		//*****************
		//create Mercury
		//*****************
		Planet mercury = new Planet("Mercury");
		unicode_icon = "\u263f";
		semimajorAxis = 0.38709893 + 0.00000066 * cy;
		eccentricityOfOrbit = 0.20563069 + 0.00002527 * cy;
		inclinationOnPlane = 7.00487  -  23.51 * cy / 3600;
		perihelion = 77.45645  + 573.57 * cy / 3600;
		longitudeOfAscendingNode = 48.33167 - 446.30 * cy / 3600;
		meanLongitude = 252.25084 + 538101628.29 * cy / 3600;
		meanLongitude = AdjustDegree(meanLongitude);	
		
		mercury.setUnicodeIcon(unicode_icon);
		mercury.setSemimajorAxis(semimajorAxis);
		mercury.setEccentricityOfOrbit(eccentricityOfOrbit);
		mercury.setInclinationOnPlane(inclinationOnPlane);
		mercury.setPerihelion(perihelion);
		mercury.setLongitudeOfAscendingNode(longitudeOfAscendingNode);
		mercury.setMeanLongitude(meanLongitude);
		mercury.setOrbitalPeriod(0.24852);
		planetList.add(mercury);
		
		//*****************
		//create Venus
		//*****************
		Planet venus = new Planet("Venus");
		unicode_icon = "\u2640";
		semimajorAxis = 0.72333199 + 0.00000092 * cy;
		eccentricityOfOrbit = 0.00677323 - 0.00004938 * cy;
		inclinationOnPlane = 3.39471 - 2.86 * cy / 3600;
		perihelion = 131.53298 - 108.80 * cy / 3600;
		longitudeOfAscendingNode = 76.68069 - 996.89 * cy / 3600; 
		meanLongitude = 181.97973 + 210664136.06 * cy / 3600;
		meanLongitude = AdjustDegree(meanLongitude);
		
		venus.setUnicodeIcon(unicode_icon);
		venus.setSemimajorAxis(semimajorAxis);
		venus.setEccentricityOfOrbit(eccentricityOfOrbit);
		venus.setInclinationOnPlane(inclinationOnPlane);
		venus.setPerihelion(perihelion);
		venus.setLongitudeOfAscendingNode(longitudeOfAscendingNode);
		venus.setMeanLongitude(meanLongitude);
		venus.setOrbitalPeriod(0.615211);
		planetList.add(venus);
		
		//*****************
		//create Earth
		//*****************
		earth = new Planet("Earth");
		unicode_icon = "\u2695";
		semimajorAxis = 1.00000011 - 0.00000005 * cy;
		eccentricityOfOrbit = 0.01671022 - 0.00003804 * cy;
		inclinationOnPlane = 0.00005 - 46.94 * cy / 3600;
		perihelion = 102.94719 +  1198.28 * cy / 3600;
		longitudeOfAscendingNode = -11.26064 - 18228.25 * cy/ 3600;
		meanLongitude = 100.46435 + 129597740.63 * cy / 3600;
		meanLongitude = AdjustDegree(meanLongitude);
		
		earth.setUnicodeIcon(unicode_icon);
		earth.setSemimajorAxis(semimajorAxis);
		earth.setEccentricityOfOrbit(eccentricityOfOrbit);
		earth.setInclinationOnPlane(inclinationOnPlane);
		earth.setPerihelion(perihelion);
		earth.setLongitudeOfAscendingNode(longitudeOfAscendingNode);
		earth.setMeanLongitude(meanLongitude);
		earth.setOrbitalPeriod(1.00004);
		//earth not added to list, kept as separate object
		
		//*****************
		//create Mars
		//*****************
		Planet mars = new Planet("Mars");
		unicode_icon = "\u2642";
		semimajorAxis = 1.52366231 - 0.00007221 * cy;
		eccentricityOfOrbit = 0.09341233 + 0.00011902 * cy;
		inclinationOnPlane = 1.85061 - 25.47 * cy / 3600;
		perihelion = 336.04084 + 1560.78 * cy / 3600;
		longitudeOfAscendingNode = 49.57854 - 1020.19 * cy / 3600;
		meanLongitude = 355.45332 + 68905103.78 * cy / 3600;
		meanLongitude = AdjustDegree(meanLongitude);
		
		mars.setUnicodeIcon(unicode_icon);
		mars.setSemimajorAxis(semimajorAxis);
		mars.setEccentricityOfOrbit(eccentricityOfOrbit);
		mars.setInclinationOnPlane(inclinationOnPlane);
		mars.setPerihelion(perihelion);
		mars.setLongitudeOfAscendingNode(longitudeOfAscendingNode);
		mars.setMeanLongitude(meanLongitude);
		mars.setOrbitalPeriod(1.880932);
		planetList.add(mars);
		
		//*****************
		//create Jupiter
		//*****************
		Planet jupiter = new Planet("Jupiter");
		unicode_icon = "\u2643";
		semimajorAxis = 5.20336301 + 0.00060737 * cy;
		eccentricityOfOrbit = 0.04839266 - 0.00012880 * cy;
		inclinationOnPlane = 1.30530 -  4.15 * cy / 3600;
		perihelion = 14.75385 +  839.93 * cy / 3600;
		longitudeOfAscendingNode = 100.55615 + 1217.17 * cy / 3600;
		meanLongitude = 34.40438 + 10925078.35 * cy / 3600;
		meanLongitude = AdjustDegree(meanLongitude);
		
		jupiter.setUnicodeIcon(unicode_icon);
		jupiter.setSemimajorAxis(semimajorAxis);
		jupiter.setEccentricityOfOrbit(eccentricityOfOrbit);
		jupiter.setInclinationOnPlane(inclinationOnPlane);
		jupiter.setPerihelion(perihelion);
		jupiter.setLongitudeOfAscendingNode(longitudeOfAscendingNode);
		jupiter.setMeanLongitude(meanLongitude);
		jupiter.setOrbitalPeriod(11.863075);
		planetList.add(jupiter);
		
		//*****************
		//create Saturn
		//*****************
		Planet saturn = new Planet("Saturn");
		unicode_icon = "\u2644";
		semimajorAxis = 9.53707032 - 0.00301530 * cy;
		eccentricityOfOrbit = 0.05415060 - 0.00036762 * cy;
		inclinationOnPlane = 2.48446 +  6.11 * cy / 3600;
		perihelion = 92.43194 - 1948.89 * cy / 3600;
		longitudeOfAscendingNode =  113.71504 - 1591.05 * cy / 3600;
		meanLongitude = 49.94432 + 4401052.95 * cy / 3600;
		meanLongitude = AdjustDegree(meanLongitude);
				
		saturn.setUnicodeIcon(unicode_icon);
		saturn.setSemimajorAxis(semimajorAxis);
		saturn.setEccentricityOfOrbit(eccentricityOfOrbit);
		saturn.setInclinationOnPlane(inclinationOnPlane);
		saturn.setPerihelion(perihelion);
		saturn.setLongitudeOfAscendingNode(longitudeOfAscendingNode);
		saturn.setMeanLongitude(meanLongitude);
		saturn.setOrbitalPeriod(29.471362);
		planetList.add(saturn);
		
		//*****************
		//create Uranus
		//*****************
		Planet uranus = new Planet("Uranus");
		unicode_icon = "\u2645";
		semimajorAxis = 19.19126393 + 0.00152025 * cy;
		eccentricityOfOrbit = 0.04716771 - 0.00019150 * cy;
		inclinationOnPlane = 0.76986  -  2.09 * cy / 3600;
		perihelion = 170.96424  + 1312.56 * cy / 3600;
		longitudeOfAscendingNode = 74.22988  - 1681.40 * cy / 3600;
		meanLongitude = 313.23218 + 1542547.79 * cy / 3600;
		meanLongitude = AdjustDegree(meanLongitude);
		
		uranus.setUnicodeIcon(unicode_icon);
		uranus.setSemimajorAxis(semimajorAxis);
		uranus.setEccentricityOfOrbit(eccentricityOfOrbit);
		uranus.setInclinationOnPlane(inclinationOnPlane);
		uranus.setPerihelion(perihelion);
		uranus.setLongitudeOfAscendingNode(longitudeOfAscendingNode);
		uranus.setMeanLongitude(meanLongitude);
		uranus.setOrbitalPeriod(84.039492);
		planetList.add(uranus);
		
		//*****************
		//create Neptune
		//*****************
		Planet neptune = new Planet("Neptune");
		unicode_icon = "\u2646";
		semimajorAxis = 30.06896348 - 0.00125196 * cy;
		eccentricityOfOrbit = 0.00858587 + 0.00002510 * cy;
		inclinationOnPlane = 1.76917  -  3.64 * cy / 3600;
		perihelion = 44.97135  - 844.43 * cy / 3600;
		longitudeOfAscendingNode = 131.72169 - 151.25 * cy / 3600;
		meanLongitude = 304.88003 + 786449.21 * cy / 3600;
		meanLongitude = AdjustDegree(meanLongitude);
		
		neptune.setUnicodeIcon(unicode_icon);
		neptune.setSemimajorAxis(semimajorAxis);
		neptune.setEccentricityOfOrbit(eccentricityOfOrbit);
		neptune.setInclinationOnPlane(inclinationOnPlane);
		neptune.setPerihelion(perihelion);
		neptune.setLongitudeOfAscendingNode(longitudeOfAscendingNode);
		neptune.setMeanLongitude(meanLongitude);
		neptune.setOrbitalPeriod(164.79246);
		planetList.add(neptune);
		
		//*****************
		//create Pluto
		//*****************
		Planet pluto = new Planet("Pluto");
		unicode_icon = "\u263f";
		semimajorAxis = 39.48168677 - 0.00076912 * cy;
		eccentricityOfOrbit = 0.24880766 + 0.00006465 * cy;
		inclinationOnPlane = 17.14175  +  11.07 * cy / 3600;
		perihelion = 224.06676  - 132.25 * cy / 3600;
		longitudeOfAscendingNode = 110.30347  -  37.33 * cy / 3600;
		meanLongitude = 238.92881 + 522747.90 * cy / 3600;
		meanLongitude = AdjustDegree(meanLongitude);
		
		pluto.setUnicodeIcon(unicode_icon);
		pluto.setSemimajorAxis(semimajorAxis);
		pluto.setEccentricityOfOrbit(eccentricityOfOrbit);
		pluto.setInclinationOnPlane(inclinationOnPlane);
		pluto.setPerihelion(perihelion);
		pluto.setLongitudeOfAscendingNode(longitudeOfAscendingNode);
		pluto.setMeanLongitude(meanLongitude);
		pluto.setOrbitalPeriod(246.77027);
		planetList.add(pluto);
	}
	/*************************************************
	 * This function uses theCalculator to adjust 
	 * planet locations based on user input
	 ************************************************/
	private void adjustPlanetData()
	{
		//variables for function
		double meanAnomalyEarth;
		double helioLongEarth;
		double trueAnomalyEarth;
		double radiusVectorEarth;
		double meanAnomalyPlanet;
		double helioLongPlanet;
		double trueAnomalyPlanet;
		double radiusVectorPlanet;
		double helioLatPlanet;
		double projectedHelioLongPlanet;
		double projectedRadiusVectorPlanet;
		double geoLongPlanet;
		double geoLatPlanet;
		double declinationPlanet;
		double rightAscensionPlanet;
		double hourAnglePlanet;
		Planet planet;
				
		//calculate Mean Anomaly of earth
		meanAnomalyEarth = theCalculator.findMeanAnomaly(earth);
		
		//calculate heliocentric longitude of earth
		helioLongEarth = theCalculator.findHeliocentricLongitude(earth, meanAnomalyEarth);
		
		//calculate true anomaly of earth
		trueAnomalyEarth = theCalculator.findTrueAnomaly(earth, helioLongEarth);
		
		//calculate radius vector length for earth 
		radiusVectorEarth = theCalculator.findVectorRadius(earth, trueAnomalyEarth);
		
		//for each planet except earth
		for(int i = 0; i < planetList.size(); i++)
		{
			//get the Planet
			planet = planetList.get(i);
			
			//calculate mean anomaly
			meanAnomalyPlanet = theCalculator.findMeanAnomaly(planet);
			
			//calculate heliocentric longitude
			helioLongPlanet = theCalculator.findHeliocentricLongitude(planet, meanAnomalyPlanet);
			
			//calculate true anomaly
			trueAnomalyPlanet = theCalculator.findTrueAnomaly(planet, helioLongPlanet);
			
			//calculate radius vector length
			radiusVectorPlanet = theCalculator.findVectorRadius(planet, trueAnomalyPlanet);
			
			//calculate heliocentric latitude 
			helioLatPlanet = theCalculator.findHeliocentricLatitude(planet, helioLongPlanet);
			
			//calculate projected heliocentric longitude
			projectedHelioLongPlanet = theCalculator.findProjectedHelioLong(planet, helioLongPlanet);
			
			//calculate projected radius vector
			projectedRadiusVectorPlanet = theCalculator.findProjectedRadiusVector(radiusVectorPlanet, helioLatPlanet);
			
			//calculate geocentric longitude
			String name = planet.getName();
			geoLongPlanet = theCalculator.findGeocentricLongitude(name, projectedRadiusVectorPlanet, projectedHelioLongPlanet, radiusVectorEarth, helioLongEarth);
			
			//calculate geocentric latitude
			geoLatPlanet = theCalculator.findGeocentricLatitude(projectedRadiusVectorPlanet, helioLatPlanet, geoLongPlanet, projectedHelioLongPlanet, radiusVectorEarth, helioLongEarth);
			
			//calculate and set declination 
			declinationPlanet = theCalculator.findPlanetDeclination(planet.getMeanLongitude(), geoLatPlanet, geoLongPlanet);
			planet.setDeclination(declinationPlanet);
			
			//calculate and set right ascension
			rightAscensionPlanet = theCalculator.findPlanetRightAscension(planet.getMeanLongitude(), geoLatPlanet, geoLongPlanet);
			planet.setRightAscension(rightAscensionPlanet);
			
			//calculate and set hour angle
			hourAnglePlanet = theCalculator.findHourAngle(rightAscensionPlanet, userLocalTime);
			planet.setHourAngle(hourAnglePlanet);
			
			//update planetList
			planetList.set(i,  planet);
		}
	}

	/*************************************************
	 * This function uses theCalculator to adjust 
	 * messier objects locations based on user input
	 ************************************************/
	private void adjustMessierData()
	{
		for(int i = 0; i < messierList.size(); i++)
		{
			Messier aMessier = messierList.get(i);
			double rightAsc = aMessier.getRADecimalHour();
			double hourAngle = theCalculator.findHourAngle(rightAsc, userLocalTime);
			aMessier.setHourAngle(hourAngle);
			messierList.set(i, aMessier);
		}
	}
	
	/*******************************************************
	 * This function is called by the JOGL to get the list
	 * of stars once their calculations have been completed
	 * @return ArrayList<Star> - an array list of modified stars
	 *******************************************************/
	public ArrayList<Star> getModifiedStars()
	{
		return starList;
	}
	
	/*******************************************************
	 * This function is called by the JOGL to get the list
	 * of planets once their calculations have been completed
	 * @return List<Planet> - an array list of modified planets
	 *******************************************************/
	public ArrayList<Planet> getModifiedPlanets()
	{
		return planetList;
	}
	
	/*******************************************************************
	 * This function is called by the JOGL to get the list
	 * of Messier Objects once their calculations have been completed.
	 * @return List<MessierObject> - an array list of modified Messier Objects
	 *******************************************************************/
	public ArrayList<Messier> getModifiedMessierObjects()
	{
		return messierList;
	}
	/*******************************************************
	 * This function is called by the JOGL to get the list
	 * of constellations.
	 * @return List<Constellation> - an array list of constellations
	 *******************************************************/
	public ArrayList<Constellation> getModifiedConstellations()
	{
		return constellationList;
	}
	
	/*******************************************************
	 * This function is called by the JOGL to get the moon
	 * once its calculations have been completed
	 * @return Moon - modified Moon
	 *******************************************************/
	public Moon getModifiedMoon()
	{
		return moon;
	}
}
