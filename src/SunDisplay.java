import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
/**
 * @author Patrick
 */
public class SunDisplay extends PlanetDisplay {
    private double magnitude;
    
    public SunDisplay(double x, double y, double m_magnitude) {
        super(x, y, "Sun", null);
        tag = "Sun";
        magnitude = m_magnitude;
    }
    
    public void draw(Graphics g, double scroll_x, double scroll_y)
    {
        if(!draw_me) {return;}
        
        g.setColor(Color.yellow);
        
        Graphics2D g2d = (Graphics2D)g;
        // Assume x, y, and diameter are instance variables.
        double size = Math.abs(magnitude - 6) + 2;
        Ellipse2D.Double circle = new Ellipse2D.Double(grid_x + scroll_x - size/2, grid_y + scroll_y - size/2, size, size);
        g2d.fill(circle);
    }
}
