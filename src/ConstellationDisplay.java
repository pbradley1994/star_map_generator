
import java.awt.Color;
import java.awt.Graphics;

// DOES NOT EXTEND OBJECT DISPLAY. AS A CONSTELLATION IS NOT ONE OBJECT BUT MANY!
public class ConstellationDisplay {
    private String label;
    private String tag;
    boolean show_labels = false;
    
    public ConstellationDisplay(String name) {
        label = name;
        tag = "constellation";
    }
    
    public void draw(Graphics g, double scroll_x, double scroll_y)
    {
        /*//if(sphere_x == 0 && sphere_y == 0) {g.setColor(Color.red);}
        //else {g.setColor(Color.white);}
        g.setColor(Color.red);
        if (icon != null) {
            g.drawString(icon, (int) (grid_x + scroll_x), (int) (grid_y + scroll_y + 4));
        }
        
        g.setColor(Color.cyan);
        System.out.println(label);
        */
        //if (show_labels == true && label != null) {
        //    g.drawString(label, (int) (grid_x + scroll_x + 2), (int) (grid_y + scroll_y - 2));
        //}
    }
}