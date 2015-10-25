

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
	
	/*****************************
	 * Class Constructor
	 * - created by the controller
	*****************************/
	public Calculator()
	{
		//empty
	}
	
	/*************************************************************
	 * This function calculates the local hour angle of a star or
	 * any other fixed space object. The hour angle is used to track
	 * a star across the sky as the earth rotates.  
	 * Formula: HourAngle = LST - rightAscension
	 * 
	 * @param rightAsc of the star in decimal coordinate form
	 * @param localTime (LST) of the user in decimal hour form
	 * @return hourAngle of the star in decimal coordinate form
	 *************************************************************/
	public double FindHourAngle(double rightAsc, double localTime)
	{
		double hourAngle = localTime - rightAsc;
		if (hourAngle < 0.0)
			hourAngle = hourAngle + 24.0;
		return hourAngle;
	}

}
