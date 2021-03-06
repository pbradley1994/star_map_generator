import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

// DOES NOT EXTEND OBJECT DISPLAY. AS A CONSTELLATION IS NOT ONE OBJECT BUT MANY!
/**
 * @author Patrick
 */
public class ConstellationDisplay {
	
	final static boolean DEBUG=false;	
	
    private String label;
    private Double label_x;
    private Double label_y;
    private String tag;
    private ArrayList<Pair<Star>> line_pairs;
    private ArrayList<Pair<Pair<Integer>>> grid_coords = new ArrayList<>();
    //private ArrayList<Pair<Pair<Double>>> sphere_coords;
    boolean show_labels = false;
    boolean draw_me = true;
    
    public ConstellationDisplay(ArrayList<Pair<Star>> asterisms, Double name_x, Double name_y, String name) {
        label = name;
        tag = "constellation";
        line_pairs = asterisms;
        label_x = name_x;
        label_y = name_y;
    }
    
    public void calculate_grid_positions(double camera_sphere_x, double camera_sphere_y) {
        for (Pair<Star> line_pair : line_pairs) {
            Pair<Integer> grid_coords1 = sphere_to_grid(camera_sphere_x, camera_sphere_y, line_pair.p1.getHourAngle()*15, line_pair.p1.getDeclination());
            Pair<Integer> grid_coords2 = sphere_to_grid(camera_sphere_x, camera_sphere_y, line_pair.p2.getHourAngle()*15, line_pair.p2.getDeclination());
            Pair<Pair<Integer>> grid_line = new Pair<>(grid_coords1, grid_coords2);
            grid_coords.add(grid_line);
        }    
    }
    
    public Pair<Integer> sphere_to_grid(double camera_sphere_x, double camera_sphere_y, double sphere_x, double sphere_y)
    {
        // http://mathworld.wolfram.com/GnomonicProjection.html
        // Converts spherical coordinates to 2d cartesian coordinates. Uses center_x, center_y as origin.
        // Assumes spherical coords are in degrees
        // Must convert to radians for below equation
        double camera_x_rad = Math.toRadians(camera_sphere_x);
        double camera_y_rad = Math.toRadians(camera_sphere_y);
        double center_x = Globals.WINWIDTH/2;
        double center_y = Globals.WINHEIGHT/2;
        double sphere_x_rad = Math.toRadians(sphere_x);
        double sphere_y_rad = Math.toRadians(sphere_y);
        //System.out.println(camera_sphere_x + ", " + camera_x_rad + ", " + camera_sphere_y + ", " + camera_y_rad);
        //System.out.println(sphere_x + ", " + sphere_x_rad + ", " + sphere_y + ", " + sphere_y_rad);
        
        // c = The angular distance of the point x, y from the center of the projection
        double cos_c = Math.sin(camera_y_rad)*Math.sin(sphere_y_rad) + Math.cos(camera_y_rad)*Math.cos(sphere_y_rad)*Math.cos(sphere_x_rad-camera_x_rad);
        if (cos_c <= 0) {draw_me = false;}
        double offset_x = Math.cos(sphere_y_rad)*Math.sin(sphere_x_rad-camera_x_rad) / cos_c;
        double offset_y = (Math.cos(camera_y_rad)*Math.sin(sphere_y_rad) - Math.sin(camera_y_rad)*Math.cos(sphere_y_rad)*Math.cos(sphere_x_rad-camera_x_rad)) / cos_c;

        Integer grid_x = (int)(offset_x * center_x + center_x);
        Integer grid_y = (int)(-offset_y * center_y + center_y);
        return new Pair<>(grid_x, grid_y);
        
        //my_sprite = new Circle(grid_x, grid_y, 5);
    }
    
    public void draw(Graphics g, double scroll_x, double scroll_y)
    {
        if (!draw_me) {return;}
        g.setColor(Color.white);
        int avg_x = 0;
        int avg_y = 0;
        int count = 0; // Also finds average position of constellation stars
        for (Pair<Pair<Integer>> grid_coord : grid_coords) {
            // Draw line from  grid_coord.p1 to grid_coord.p2
            if (DEBUG) { System.out.println(label + ", " + grid_coord.p1.p1 + ", " + grid_coord.p1.p2 + ", " + grid_coord.p2.p1 + ", " + grid_coord.p2.p2); }
            g.drawLine((int)(grid_coord.p1.p1 + scroll_x), (int)(grid_coord.p1.p2 + scroll_y), (int)(grid_coord.p2.p1 + scroll_x), (int)(grid_coord.p2.p2 + scroll_y));
            avg_x += grid_coord.p1.p1 + grid_coord.p2.p1;
            avg_y += grid_coord.p1.p2 + grid_coord.p2.p2;
            count += 2;
        }
        avg_x /= count;
        avg_y /= count;
        if (DEBUG) { System.out.println("Position of Label: " + label + ": " + avg_x  + ", " + avg_y + ", " + show_labels); }
        g.setColor(Color.yellow);
        if (show_labels == true && label != null) {
            g.drawString(label, (int) (avg_x + scroll_x), (int) (avg_y + scroll_y));
            //g.drawString(label, (int) (grid_x + scroll_x + 2), (int) (grid_y + scroll_y - 2));
        }
    }
}