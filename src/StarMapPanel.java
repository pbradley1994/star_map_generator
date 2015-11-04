import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author Patrick
 */
public class StarMapPanel extends JPanel {
    /** Globals */
    private Globals globals = new Globals();
    /** List of Star */
    private ArrayList<StarDisplay> stars = new ArrayList();
    /** Camera Position */
    double camera_x = 0;
    double camera_y = 0;
    
    public StarMapPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
        //createStars();
    }
    
    public void createStars() {
        // Create a host of star objects
        for(int x=-80; x<80; x += 10) {
            for(int y=-80; y<80; y += 10) {
                StarDisplay current_star = new StarDisplay(x, y);
                current_star.sphere_to_grid(camera_x, camera_y);
                current_star.print_sphere_coords();
                current_star.print_grid_coords();
                stars.add(current_star);
            }
        }
    }
    
    public void clearStars() {
        stars.clear();
    }
    
    public void setCameraPosition(int x, int y) {
        camera_x = x;
        camera_y = y;
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(Globals.WINWIDTH, Globals.WINHEIGHT);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw black background
        g.setColor(Color.black);
        g.fillRect(0, 0, Globals.WINWIDTH + Globals.GUIWIDTH, Globals.WINHEIGHT);
        // Draw each star
        for (StarDisplay star : stars) {
            star.draw(g);
        }

        g.setColor(Color.white);
        g.drawString("This is my custom panel!", 15, 15);
        //System.out.println("This is a panel!");
    }
}
