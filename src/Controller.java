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
	Sun sun;
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
	double userDecimalYear;
	int userYear;
	int userMonth;
	
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
		sun = new Sun("sun");
		constellationList = theParser.getConstellations();
		planetList = new ArrayList<Planet>();
		theCalculator = new Calculator();
		epoch2000JD = 2451545.0;
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
		userMonth = month;
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
			year -= 1;
			month += 12;
		}
		userDecimalDay = (double)day + (userGMT / 24.0);
		int tempA = (int)(year/100);
		int tempB =  2 - tempA + (int)(tempA/4);
		userJulianDate = (int)(365.25 * year) + (int)(30.6001 * (month + 1)) + userDecimalDay + 1720994.5 + tempB;
		userDecimalYear = year + (((30.6001 * (month + 1)) + userDecimalDay)/365.25);
		//*****************************
		//adjust star map data to user
		//*****************************
		adjustStarData();
		adjustMessierData();
		adjustSunAndMoonData(); 
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
	private void adjustSunAndMoonData()
	{
		double toRad = Math.PI / 180.0;
		
		//find the number of days since the epoch 2000
		double D = ((userYear - 1990)* 365.3) + userDecimalDay + ((userMonth - 1) * 30);
		double sunEg = 279.403303; //at epoch
		double sunE = 0.016713;
		double sunW = 282.768422;
		//find mean anomaly of the sun (in degrees)		
		double sunMeanAnomaly = ((360 / 365.242191) * D) + sunEg - sunW;
		sunMeanAnomaly = AdjustDegree(sunMeanAnomaly);
		//find true anomaly of the sun
		double temp = sunMeanAnomaly * toRad;
		temp = Math.sin(temp);
		double sunEc = (360 / Math.PI) * sunE * temp;
		double sunTrueAnomaly = sunMeanAnomaly + sunEc;
		sunTrueAnomaly = AdjustDegree(sunTrueAnomaly);
		//find ecliptic longitude of the sun (in degrees)
		double sunEcLong = sunTrueAnomaly + sunW;
		sunEcLong = AdjustDegree(sunEcLong);
		//find sun declination and right ascension
		temp = theCalculator.findPlanetDeclination(sunEg, 0.0, sunEcLong);
		sun.setDeclination(temp);
		double rightAsc = theCalculator.findPlanetRightAscension(sunEg, 0.0, sunEcLong);
		sun.setRightAscension(rightAsc);
		temp = theCalculator.findHourAngle(rightAsc, userLocalTime);
		sun.setMagnitude(-26.0);
		
		//find moon mean longitude (in degrees)
		temp = 13.1763966 * D + 318.351648;
		temp = AdjustDegree(temp);
		moon.setMeanLongitude(temp);
		
		//find moon mean anomaly (in degrees)
		temp = moon.getMeanLongitude() - (0.1114041 * D) - 36.340410;
		temp = AdjustDegree(temp);
		double moonMeanAnomaly = temp;
		
		//find moon longitude of the node (in degrees)
		temp = 318.510105 - 0.0529539 * D;
		temp = AdjustDegree(temp);
		moon.setLongitudeOfAscendingNode(temp);
		
		//calculate moons correction for evection (in degrees)
		double evectionCorrection = theCalculator.correctMoonEvection(sunEcLong, moon.getMeanLongitude(), moonMeanAnomaly);
		
		//calculate moons correction for the annual equation
		temp = sunMeanAnomaly * toRad;
		double annualEquation = 0.1858 * Math.sin(temp);
		
		//find moon's third correction (in degrees)
		double A3 = 0.37 * Math.sin(temp);
		
		//find moon corrected anomaly (in degrees)
		double correctedAnomaly = moonMeanAnomaly + evectionCorrection - annualEquation - A3;
		
		//find moon's correction for the equation of the centre (in degrees)
		temp = correctedAnomaly * toRad;
		double equationCentre = 6.2886 * Math.sin(temp);
		
		//find moon's 4th correction (in degrees)
		double A4 = 0.214 * Math.sin(2.0*temp);
		
		//find moon's corrected longitude (in degrees)
		double correctedLong = moon.getMeanLongitude() + evectionCorrection + equationCentre - annualEquation + A4;
		
		//correct moon's longitude for variation (in degrees)
		temp = (2 * (correctedLong - sunEcLong)) * toRad;
		double variation = 0.6583 * Math.sin(temp);
		
		//find moon's true longitude (in degrees)
		double trueLong = correctedLong + variation;
		
		//calculate moon's corrected longitude of the node (in degrees)
		temp = 0.16 * Math.sin(sunMeanAnomaly * toRad);
		double correctedLongNode = moon.getLongitudeOfAscendingNode() - temp;
		
		//find ecliptic longitude of moon (in degrees)
		//find y (in radians)
		double i = 5.145396 * toRad;
		temp = (trueLong - correctedLongNode) * toRad;
		double y = Math.sin(temp) * Math.cos(i);
		//find x (in radians)
		double x = Math.cos(temp);
		double tan = Math.atan2(y, x);
		double eclipticLong = tan + correctedLongNode;
		
		//find ecliptic latitude of moon (in degrees)
		double eclipticLat = Math.asin(Math.sin(temp * Math.sin(i)));
		
		//find moon mean obliquity (in degrees)
		double T = (userJulianDate - epoch2000JD) / 36525.0;
		double obliquity = 23.439292 - (((46.815 * T) + (T * T * 0.0006) - (T * T * T * 0.00181))/3600.0);
		
		//find moon declination
		double declination = theCalculator.findPlanetDeclination(obliquity, eclipticLat, eclipticLong);
		moon.setDeclination(declination);
		
		//find moon right ascension
		rightAsc = theCalculator.findPlanetRightAscension(obliquity, eclipticLat, eclipticLong);
		moon.setRightAscension(rightAsc);
		
		//find moon hour angle
		double hourAngle = theCalculator.findHourAngle(rightAsc, userLocalTime);
		moon.setHourAngle(hourAngle);
		
		//find moon phase - book's method
		double phase = Math.abs((trueLong - sunEcLong) * toRad);
				
		//find moon phase
		if ( phase > 4.71 ) //third quarter to new moon
		{			 
			if (phase <= 4.72) //third quarter
				moon.setUnicodeIcon("m17.png");
			if (phase <= 5.03) 
				moon.setUnicodeIcon("m18.png");
			if (phase <= 5.34) 
				moon.setUnicodeIcon("m19.png");
			if (phase <= 5.65) 
				moon.setUnicodeIcon("m20.png");
			if (phase <= 5.97) 
				moon.setUnicodeIcon("m21.png");
			if (phase <= 6.28) 
				moon.setUnicodeIcon("m22.png");
			else
				moon.setUnicodeIcon("m00.png"); //new moon
		}   
		else if ( phase > 3.14 ) //full moon to third quarter
		{			
			if (phase <= 3.16) //full
				moon.setUnicodeIcon("m10.png");
			if (phase <= 3.45) 
				moon.setUnicodeIcon("m11.png");
			if (phase <= 3.77) 
				moon.setUnicodeIcon("m12.png");
			if (phase <= 4.08) 
				moon.setUnicodeIcon("m13.png");
			if (phase <= 4.40) 
				moon.setUnicodeIcon("m14.png");
			if (phase <= 2.83) 
				moon.setUnicodeIcon("m15.png");
			else
				moon.setUnicodeIcon("m16.png"); //third quarter
		} 
		else if ( phase > 1.57 ) //first quarter to full moon
		{			
			if (phase <= 1.58) //new moon
				moon.setUnicodeIcon("m05.png");
			if (phase <= 1.88) 
				moon.setUnicodeIcon("m06.png");
			if (phase <= 2.20) 
				moon.setUnicodeIcon("m07.png");
			if (phase <= 2.25) 
				moon.setUnicodeIcon("m08.png");
			if (phase <= 2.83) 
				moon.setUnicodeIcon("m09.png");
			else
				moon.setUnicodeIcon("m10.png"); //full moon
		} 
		else 	 //new moon to first quarter
		{			
			if (phase <= 0.1) //new moon
				moon.setUnicodeIcon("m00.png");
			if (phase <= 0.314) 
				moon.setUnicodeIcon("m01.png");
			if (phase <= 0.628) 
				moon.setUnicodeIcon("m02.png");
			if (phase <= 0.942) 
				moon.setUnicodeIcon("m03.png");
			if (phase <= 1.256) 
				moon.setUnicodeIcon("m04.png");
			else
				moon.setUnicodeIcon("m05.png"); //first quarter
		} 
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
		meanLongitude = 60.750646;
		semimajorAxis = 0.387099;
		eccentricityOfOrbit = 0.205633;
		inclinationOnPlane = 7.004540;
		perihelion = 77.299833;
		longitudeOfAscendingNode = 48.212740;
		
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
		meanLongitude = 88.455855;
		semimajorAxis = 0.723332;
		eccentricityOfOrbit = 0.006778;
		inclinationOnPlane = 3.394535;
		perihelion = 131.430236;
		longitudeOfAscendingNode = 76.589820;
		
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
		meanLongitude = 99.403308;
		semimajorAxis = 1.000;
		eccentricityOfOrbit = 0.016713;
		inclinationOnPlane = 1.00;
		perihelion = 102.768413;
		longitudeOfAscendingNode = 1.00;
		
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
		meanLongitude = 240.739474;
		semimajorAxis = 1.523688;
		eccentricityOfOrbit = 0.093396;
		inclinationOnPlane = 1.849736;
		perihelion = 335.874939;
		longitudeOfAscendingNode = 49.480308;
		
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
		meanLongitude = 90.638185;
		semimajorAxis = 5.202561;
		eccentricityOfOrbit = 0.048482;
		inclinationOnPlane = 1.303613;
		perihelion = 14.170747;
		longitudeOfAscendingNode = 100.353142;
		
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
		meanLongitude = 287.690033;
		semimajorAxis = 9.554747;
		eccentricityOfOrbit = 0.055581;
		inclinationOnPlane = 2.488980;
		perihelion = 92.861407;
		longitudeOfAscendingNode = 113.576139;
				
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
		meanLongitude = 271.063148;
		semimajorAxis = 19.21814;
		eccentricityOfOrbit = 0.046321;
		inclinationOnPlane = 0.773059;
		perihelion = 172.884833;
		longitudeOfAscendingNode = 73.926961;
		
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
		meanLongitude = 282.349556;
		semimajorAxis = 30.109570;
		eccentricityOfOrbit = 0.009003;
		inclinationOnPlane = 1.770646;
		perihelion = 48.009758;
		longitudeOfAscendingNode = 131.670599;
		
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
		meanLongitude = 246.77027;
		semimajorAxis = 39.3414;
		eccentricityOfOrbit = 0.24624;
		inclinationOnPlane = 17.1420;
		perihelion = 224.133;
		longitudeOfAscendingNode = 110.144;
		
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
		double geoLongPlanet;
		double declinationPlanet;
		double rightAscensionPlanet;
		double hourAnglePlanet;
		Planet planet;
		double days;
		double n;
		
		//find number of days since epoch
		days = ((userYear - 1990)* 365.25) + userDecimalDay + ((userMonth - 1) * 30);
		
		//normalize the number of days to the orbital period of the planet
		n = theCalculator.findN(earth.getOrbitalPeriod(), days);
				
		//calculate Mean Anomaly of earth
		meanAnomalyEarth = theCalculator.findMeanAnomaly(earth, n);
		
		//calculate heliocentric longitude of earth
		helioLongEarth = theCalculator.findHeliocentricLongitude(earth, meanAnomalyEarth, n);
		
		//calculate true anomaly of earth
		trueAnomalyEarth = theCalculator.findTrueAnomaly(earth, helioLongEarth);
		
		//calculate radius vector length for earth 
		radiusVectorEarth = theCalculator.findVectorRadius(earth, trueAnomalyEarth);
		
		//for each planet except earth
		for(int i = 0; i < planetList.size(); i++)
		{
			//get the Planet
			planet = planetList.get(i);
			
			//normalize days
			n = theCalculator.findN(planet.getOrbitalPeriod(), days);
			
			//calculate mean anomaly
			meanAnomalyPlanet = theCalculator.findMeanAnomaly(planet, n);
			
			//calculate heliocentric longitude
			helioLongPlanet = theCalculator.findHeliocentricLongitude(planet, meanAnomalyPlanet, n);
			
			//calculate true anomaly
			trueAnomalyPlanet = theCalculator.findTrueAnomaly(planet, helioLongPlanet);
			
			//calculate radius vector length
			radiusVectorPlanet = theCalculator.findVectorRadius(planet, trueAnomalyPlanet);
						
			//calculate geocentric longitude
			String name = planet.getName();
			geoLongPlanet = theCalculator.findGeocentricLongitude(name, radiusVectorPlanet, helioLongPlanet, radiusVectorEarth, helioLongEarth);
			
			//calculate and set declination 
			declinationPlanet = theCalculator.findPlanetDeclination(planet.getMeanLongitude(), 0.0, geoLongPlanet);
			planet.setDeclination(declinationPlanet);
			
			//calculate and set right ascension
			rightAscensionPlanet = theCalculator.findPlanetRightAscension(planet.getMeanLongitude(), 0.0, geoLongPlanet);
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
	/************************************************
-	 * Return a star object given the star objects id.
-	 * @param StarID id of star you are looking for 
-	 ************************************************/
-	// Changed use the parser's faster method
-    public Star getStarfromStarID(Integer starID)
-    {
-    	Star s = theParser.getStarfromStarID(starID);
-    	if (s.getID() == starID) {
-            return s;
-        }
-        System.out.println("StarID " + starID + "was not found in starList.");
-        return null;
-    }
}
