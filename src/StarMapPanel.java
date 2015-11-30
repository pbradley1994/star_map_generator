import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author Patrick
 */
public class StarMapPanel extends JPanel implements Printable {

	/** Debug */
	final static boolean DEBUG=false;	

    /** Globals */
    private Globals globals = new Globals();
    
    /** List of Solitary Objects */
    private ArrayList<ObjDisplay> objects = new ArrayList();
    private ArrayList<ConstellationDisplay> constellations = new ArrayList();
    
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
        // Add Messier Objects to screen
        for (Messier messier : the_controller.messierList) {
            MessierDisplay current_object = new MessierDisplay(messier.getHourAngle()*15, messier.getDeclination(), messier.getName());
            if (DEBUG) { System.out.println("Messier: " + messier.getHourAngle()*15 + ", " + messier.getDeclination()); }
            current_object.sphere_to_grid(camera_x, camera_y);
            objects.add(current_object);
        }
        // Add Stars to screen
        for (Star star : the_controller.starList) {
            StarDisplay current_object = new StarDisplay(star.getHourAngle()*15, star.getDeclination(), star.getMagnitude(), star.getName());
            current_object.sphere_to_grid(camera_x, camera_y);
            objects.add(current_object);
        }
        // Add Constellations to screen
        for (Constellation constellation : the_controller.constellationList) {
            // Convert asterisms starID's to starObjects
            if(!constellation.isPlottable()) {continue;}
            ArrayList<Pair<Star>> star_list = new ArrayList<Pair<Star>>();
            for (Pair<Integer> asterism_line : constellation.getAsterisms()) {
                Integer StarID1 = asterism_line.p1;
                Integer StarID2 = asterism_line.p2;
                if (DEBUG) { System.out.println(constellation.getName() + " " + StarID1 + " " + StarID2); }
                if (StarID1 == -1 || StarID2 == -1) {continue;}
                Star star1 = the_controller.getStarfromStarID(StarID1);
                Star star2 = the_controller.getStarfromStarID(StarID2);
                star_list.add(new Pair<Star>(star1,star2));
            }
            ConstellationDisplay current_object = new ConstellationDisplay(star_list, constellation.getHourAngle()*15, constellation.getDeclination()*15, constellation.getName());
            current_object.calculate_grid_positions(camera_x, camera_y);
            constellations.add(current_object);
        }
        // Add Planets (Excluding Earth) to screen
        for (Planet planet : the_controller.planetList) {
            PlanetDisplay current_object = new PlanetDisplay(planet.getHourAngle()*15, planet.getDeclination(), planet.getName(), planet.getIcon());
            current_object.sphere_to_grid(camera_x, camera_y);
            objects.add(current_object);
        }
        // Add Moon to screen
        MoonDisplay current_object = new MoonDisplay(the_controller.moon.getHourAngle()*15, the_controller.moon.getDeclination(), the_controller.moon.getIcon());
        current_object.sphere_to_grid(camera_x, camera_y);
        objects.add(current_object);
        // Add Sum to screen
        SunDisplay current_sun = new SunDisplay(the_controller.sun.getHourAngle()*15, the_controller.sun.getDeclination(), the_controller.sun.getMagnitude());
        current_sun.sphere_to_grid(camera_x, camera_y);
        objects.add(current_sun);
    }
    
    public void setLabels(boolean star_label, boolean messier_label, boolean planet_label, boolean constellation_label) {
        for (ObjDisplay m_object : objects) {
            if (m_object.getTag() == "star") {m_object.show_labels = star_label;}
            else if (m_object.getTag() == "messier") {m_object.show_labels = messier_label;}
            else if (m_object.getTag() == "planet") {m_object.show_labels = planet_label;}
        }
        for (ConstellationDisplay m_constellation: constellations) {
            m_constellation.show_labels = constellation_label;
        }
    }
    
    public void clearObjects() {
        objects.clear();
        constellations.clear();
    }
    
    public void setCameraPosition(double x, double y) {
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
        g.fillRect(0, 0, Globals.WINWIDTH + Globals.GUIWIDTH, (Globals.WINHEIGHT)*2);
        // Draw each constellation
        for (ConstellationDisplay constellation : constellations) {
            constellation.draw(g, scroll_x, scroll_y);
        }
        // Draw each solitary object
        for (ObjDisplay object : objects) {
            object.draw(g, scroll_x, scroll_y);
        }
    }

    @Override
    public int print(Graphics grphcs, PageFormat pf, int page) throws PrinterException {
        if (page > 0) {
            return NO_SUCH_PAGE;
        }
        
        // perform rendering - this might work?
        paintComponent(grphcs);
        
        return PAGE_EXISTS;
    }
   
}
