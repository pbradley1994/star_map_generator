import java.util.List;
import java.util.Date;

import javax.swing.text.html.parser.Parser;

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
	List<Star> starList;
	List<Messier> messierList;
	List<Planet> planetList;
	Moon moon;
	List<Constellation> constellationList;
	Parser theParser;
	Calculator theCalculator;
	double userLocalTime;  //in decimal form (LST)
	double userLat;
	double userLong;
	Date userDate;
	
	
	/************************************************
	 * Class Constructor
	 * - created in main()
	 * 
	 * Creates Parser and Calculator
	 * calls Parser to get starList, planetList, 
	 * moon, messierList, and constellationList
	 ***********************************************/
	public Controller()
	{
		theParser = new Parser();
		starList = theParser.getStars();
		messierList = theParser.GetMessierObjects();
		planetList = theParser.GetPlanets();
		moon = theParser.GetMoon();
		constellationList = theParser.GetConstellations();
		theCalculator = new Calculator();
	}
	
	/***********************************************************
	 * This function is called by the GUI once the user inputs
	 * their data. It saves the users data to local variables,
	 * and then adjusts star map data based on this information.
	 ************************************************************/
	public void SetUserData(double localTime, double latitude, double longitude, Date date)
	{
		userLocalTime = localTime;
		userLat = latitude;
		userLong = longitude;
		userDate = date;
		
		AdjustStarData();
		AdjustMessierData();
		AdjustMoonData();
		AdjustPlanetData();	
	}
	
	/*************************************************
	 * This function uses theCalculator to adjust 
	 * fixed star locations based on user input
	 ************************************************/
	private void AdjustStarData()
	{
		for(int i = 0; i < starList.size(); i++)
		{
			Star aStar = starList.get(i);
			double rightAsc = aStar.GetRightAscension();
			double hourAngle = theCalculator.FindHourAngle(rightAsc, userLocalTime);
			aStar.SetHourAngle(hourAngle);
			starList.set(i, aStar);
		}
	}
	
	/*************************************************
	 * This function uses theCalculator to adjust 
	 * moon data based on user input
	 ************************************************/
	private void AdjustMoonData()
	{
		
	}
	
	/*************************************************
	 * This function uses theCalculator to adjust 
	 * planet locations based on user input
	 ************************************************/
	private void AdjustPlanetData()
	{
		
	}
	
	/*************************************************
	 * This function uses theCalculator to adjust 
	 * messier objects locations based on user input
	 ************************************************/
	private void AdjustMessierData()
	{
		for(int i = 0; i < messierList.size(); i++)
		{
			Messier aMessier = messierList.get(i);
			double rightAsc = aMessier.GetRightAscension();
			double hourAngle = theCalculator.FindHourAngle(rightAsc, userLocalTime);
			aMessier.SetHourAngle(hourAngle);
			messierList.set(i, aMessier);
		}
	}
	
	/*******************************************************
	 * This function is called by the JOGL to get the list
	 * of stars once their calculations have been completed
	 * @return List<Star> - a list of modified stars
	 *******************************************************/
	public List<Star> GetModifiedStars()
	{
		return starList;
	}
	
	/*******************************************************
	 * This function is called by the JOGL to get the list
	 * of planets once their calculations have been completed
	 * @return List<Planet> - a list of modified planets
	 *******************************************************/
	public List<Planet> GetModifiedPlanets()
	{
		return planetList;
	}
	
	/*******************************************************************
	 * This function is called by the JOGL to get the list
	 * of Messier Objects once their calculations have been completed.
	 * @return List<MessierObject> - a list of modified Messier Objects
	 *******************************************************************/
	public List<Planet> GetModifiedMessierObjects()
	{
		return messierList;
	}
	/*******************************************************
	 * This function is called by the JOGL to get the list
	 * of constellations.
	 * @return List<Constellation> - a list of constellations
	 *******************************************************/
	public List<Constellation> GetModifiedConstellations()
	{
		return constellationList;
	}
	
	/*******************************************************
	 * This function is called by the JOGL to get the moon
	 * once its calculations have been completed
	 * @return Moon - modified Moon
	 *******************************************************/
	public Moon GetModifiedMoon()
	{
		return moon;
	}
}
