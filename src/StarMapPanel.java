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
    private ArrayList<ObjDisplay> objects = new ArrayList();
    /** Camera Position */
    double camera_x = 0;
    double camera_y = 0;
    
    /** scroll position */
    double scroll_x = 0;
    double scroll_y = 0;
    
    public StarMapPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
        //createStars();
    }
    
    /*public void createStars() {
        //Create a host of star objects
        for(int x=-80; x<80; x += 10) {
            for(int y=-80; y<80; y += 10) {
                ObjDisplay current_star = new ObjDisplay(x, y);
                current_star.sphere_to_grid(camera_x, camera_y);
                current_star.print_sphere_coords();
                current_star.print_grid_coords();
                objects.add(current_star);
            }
        }
    }*/
    
    public void createObjects(Controller the_controller) {
    	// Note from Christy:
    	// When adding the planets/constellations, the string needs to be null here
    	// Then just add that code into the Label methods below
        for (Messier messier : the_controller.messierList) {
        	MessierDisplay current_object = new MessierDisplay(messier.getRADecimalDegree(), messier.getDeclination(), null);
            current_object.sphere_to_grid(camera_x, camera_y);
            objects.add(current_object);
        }
        for (Star star : the_controller.starList) {
        	StarDisplay current_object = new StarDisplay(star.getRA()*15, star.getDeclination(), star.getMagnitude(), null);
            current_object.sphere_to_grid(camera_x, camera_y);
            objects.add(current_object);
        }
        for (Planet planet : the_controller.planetList) {
            PlanetDisplay current_object = new PlanetDisplay(planet.getHourAngle()*15, planet.getDeclination(), planet.getName(), planet.getIcon());
            current_object.sphere_to_grid(camera_x, camera_y);
            objects.add(current_object);
        }
    }
    
    public void StarLabels(Controller the_controller) {
        for (Star star : the_controller.starList) {
            StarDisplay current_object = new StarDisplay(star.getRA()*15, star.getDeclination(), star.getMagnitude(), star.getName());
            current_object.sphere_to_grid(camera_x, camera_y);
            objects.add(current_object);
        }
    }
    
    public void MessierLabels(Controller the_controller) {
    	 for (Messier messier : the_controller.messierList) {
        	MessierDisplay current_object = new MessierDisplay(messier.getRADecimalDegree(), messier.getDeclination(), messier.getName());
            current_object.sphere_to_grid(camera_x, camera_y);
            objects.add(current_object);
        }
    }
    
    public void PlanetLabels(Controller the_controller) {
      	 // Planet label method
      }
    
    public void ConstLabels(Controller the_controller) {
    	// Constellation label method
   }
    
    public void clearObjects() {
        objects.clear();
    }
    
    public void setCameraPosition(int x, int y) {
        camera_x = x;
        camera_y = y;
    }
    
    public void setScroll(int x, int y)
    {
        scroll_x = x;
        scroll_y = y;
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
        for (ObjDisplay object : objects) {
            object.draw(g, scroll_x, scroll_y);
        }

        //g.setColor(Color.white);
        //g.drawString("This is my custom panel!", 15, 15);
        //System.out.println("This is a panel!");
    }
   
}
