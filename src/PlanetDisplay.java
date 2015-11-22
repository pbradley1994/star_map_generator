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
    private String icon;
    
    public PlanetDisplay(double x, double y, String name, String u_icon) {
        super(x, y);
        label = name;
        icon = u_icon;
        tag = "planet";
    }
    
    public void draw(Graphics g, double scroll_x, double scroll_y)
    {
        
        //if(sphere_x == 0 && sphere_y == 0) {g.setColor(Color.red);}
        //else {g.setColor(Color.white);}
        g.setColor(Color.red);
        if (icon != null) {
            g.drawString(icon, (int) (grid_x + scroll_x), (int) (grid_y + scroll_y + 4));
        }
        
        g.setColor(Color.cyan);
        System.out.println(label);
        if (show_labels == true && label != null) {
            g.drawString(label, (int) (grid_x + scroll_x + 2), (int) (grid_y + scroll_y - 2));
        }
    }
    
    
}
