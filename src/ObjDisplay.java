import java.awt.Graphics;

/**
 * @author Patrick
 */
public class ObjDisplay {
    // Sphere coords (3D) Degrees
    private double sphere_x;
    private double sphere_y;
    private int sphere_z;
    protected String tag;
    int grid_x;
    int grid_y;
    boolean show_labels = false;
    boolean draw_me = true;
    
    public ObjDisplay(double x, double y)
    {
        sphere_x = x;
        sphere_y = y;
        sphere_z = 1;
    }
    
    public void sphere_to_grid(double camera_sphere_x, double camera_sphere_y)
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

        grid_x = (int)(offset_x * center_x + center_x);
        grid_y = (int)(-offset_y * center_y + center_y);
        
        //my_sprite = new Circle(grid_x, grid_y, 5);

    }
    
    public String getTag()
    {
        return tag;
    }
    
    public void print_grid_coords()
    {
        System.out.println(grid_x + ", " + grid_y);
    }
    
    public void print_sphere_coords()
    {
        System.out.println(sphere_x + ", " + sphere_y + ", " + sphere_z);
    }
    
    public void draw(Graphics g, double scroll_x, double scroll_y)
    {
        return;
    }

}