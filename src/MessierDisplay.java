import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


/**
 *
 * @author Patrick
 */
public class MessierDisplay extends ObjDisplay {
    private String label;
    
    public MessierDisplay(double x, double y, String name) {
        super(x, y);
        label = name;
        tag = "messier";
    }
    
    public void draw(Graphics g, double scroll_x, double scroll_y)
    {
        //if(sphere_x == 0 && sphere_y == 0) {g.setColor(Color.red);}
        //else {g.setColor(Color.white);}
        //g.setColor(Color.magenta);
        
        /*Graphics2D g2d = (Graphics2D)g;
        // Assume x, y, and diameter are instance variables.
        int size = 12;
        Ellipse2D.Double circle = new Ellipse2D.Double(grid_x + scroll_x - size/2, grid_y + scroll_y - size/2, size, size);
        g2d.fill(circle);*/
        
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResource("/images/Messier1.png"));
            int size = 16;
            g.drawImage(image, (int) (grid_x + scroll_x - size/2), (int) (grid_y + scroll_y - size/2), size, size, null);
            g.setColor(Color.yellow);
            if (show_labels == true && label != null) {
                g.drawString(label, (int) (grid_x + scroll_x + 2), (int) (grid_y + scroll_y - 2));}
        } catch (IOException ex) {
            Logger.getLogger(MessierDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}
