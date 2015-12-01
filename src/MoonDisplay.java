import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * @author Patrick
 */
public class MoonDisplay extends PlanetDisplay {
    public MoonDisplay(double x, double y, String icon) {
        super(x, y, "Moon", icon);
        tag = "Moon";
    }
    
    public void draw(Graphics g, double scroll_x, double scroll_y)
    {
        if(!draw_me) {return;}
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResource("/images/Moon/" + icon));
            int size = 40;
            g.setColor(Color.white);
            g.drawArc((int) (grid_x + scroll_x - size/2), (int) (grid_y + scroll_y - size/2 -1), size+1, size+1, 0, 360);
            g.drawImage(image, (int) (grid_x + scroll_x - size/2), (int) (grid_y + scroll_y - size/2), size, size, null);
            if (tag != null) {
                g.setColor(Color.cyan);
                g.drawString(tag, (int) (grid_x + scroll_x -15), (int) (grid_y + scroll_y +35));
                }
        } catch (IOException ex) {
            Logger.getLogger(MessierDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
}
