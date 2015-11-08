import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Patrick
 */
public class StarDisplay extends ObjDisplay {
    private double star_mag;
    private String label;
    
    public StarDisplay(double x, double y, double magnitude, String name) {
        super(x, y);
        label = name;
        star_mag = magnitude;
    }
    
    public void draw(Graphics g, double scroll_x, double scroll_y)
    {
        // We're not using a sprite yet
        // Don't print if greater than 6 magnitude
        if (star_mag > 6) {return;}
        
        //if(sphere_x == 0 && sphere_y == 0) {g.setColor(Color.red);}
        //else {g.setColor(Color.white);}
        g.setColor(Color.white);
        
        Graphics2D g2d = (Graphics2D)g;
        // Assume x, y, and diameter are instance variables.
        double size = Math.abs(star_mag - 6) + 2;
        Ellipse2D.Double circle = new Ellipse2D.Double(grid_x + scroll_x - size/2, grid_y + scroll_y - size/2, size, size);
        g2d.fill(circle);
        
        g.setColor(Color.yellow);
        if (label != null) {
            g.drawString(label, (int) (grid_x + scroll_x), (int) (grid_y + scroll_y));
        }
    }
    
    
}
