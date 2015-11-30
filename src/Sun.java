
public class Sun extends Planet
{
	double m_magnitude;
	
	public Sun(String name) {
		super(name);
	}
	
	public void setMagnitude(double magnitude)
	{
		m_magnitude = magnitude;
	}
	
	public double getMagnitude()
	{
		return m_magnitude;
	}

}