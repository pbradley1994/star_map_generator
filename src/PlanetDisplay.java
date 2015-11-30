import java.awt.Color;
import java.awt.Graphics;
/**
 * @author Patrick
 */
public class PlanetDisplay extends ObjDisplay {
    protected String label;
    protected String icon;
    
	final static boolean DEBUG=false;	
    
    public PlanetDisplay(double x, double y, String name, String u_icon) {
        super(x, y);
        label = name;
        icon = u_icon;
        tag = "planet";
    }
    
    public void draw(Graphics g, double scroll_x, double scroll_y)
    {
        if (!draw_me) {return;} 
        g.setColor(Color.red);
        if (icon != null) {
            g.drawString(icon, (int) (grid_x + scroll_x), (int) (grid_y + scroll_y + 4));
        }
        
        g.setColor(Color.cyan);
        if (DEBUG) { System.out.println(label); }
        if (show_labels == true && label != null) {
            g.drawString(label, (int) (grid_x + scroll_x + 2), (int) (grid_y + scroll_y - 2));
        }
    }
    
    
}
