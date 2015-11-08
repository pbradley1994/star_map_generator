import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Patrick
 */
public class PlanetDisplay extends ObjDisplay {
    private String label;
    
    public PlanetDisplay(double x, double y, String name) {
        super(x, y);
        label = name;
    }
    
    public void draw(Graphics g, double scroll_x, double scroll_y)
    {
        
        //if(sphere_x == 0 && sphere_y == 0) {g.setColor(Color.red);}
        //else {g.setColor(Color.white);}
        g.setColor(Color.red);
        
        Graphics2D g2d = (Graphics2D)g;
        // Assume x, y, and diameter are instance variables.
        Ellipse2D.Double circle = new Ellipse2D.Double(grid_x + scroll_x, grid_y + scroll_y, 8, 8);
        g2d.fill(circle);
        
        g.setColor(Color.cyan);
        System.out.println(label);
        if (label != null) {
            g.drawString(label, (int) (grid_x + scroll_x), (int) (grid_y + scroll_y));
        }
    }
    
    
}
