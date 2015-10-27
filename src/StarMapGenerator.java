package starmapgenerator;

// Imports
import java.util.ArrayList;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
/**
 * @author Patrick Bradley
 */
public class StarMapGenerator extends BasicGame {
    /** The container holding this test */
    private AppGameContainer app;
    /** The input system being polled */
    private Input input;
    /** Globals */
    private Globals globals = new Globals();
    /** List of Star */
    private ArrayList<StarDisplay> stars = new ArrayList();
    
    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new StarMapGenerator());
            container.setDisplayMode(Globals.WINWIDTH + Globals.GUIWIDTH, Globals.WINHEIGHT, false);
            container.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public StarMapGenerator() {
        super("StarMapGenerator");
    }
     /**
     * @see org.newdawn.slick.BasicGame#init(org.newdawn.slick.GameContainer)
     */
    public void init(GameContainer container) throws SlickException {
            container.setShowFPS(false);
            if (container instanceof AppGameContainer) {
                    app = (AppGameContainer) container;
            }

            input = container.getInput();
            
            // Create a host of star objects
            for(int x=-80; x<110; x += 10) {
                for(int y=-80; y<80; y += 10) {
                    StarDisplay current_star = new StarDisplay(x, y);
                    double camera_x = 30;
                    double camera_y = 30;
                    current_star.sphere_to_grid(camera_x, camera_y);
                    current_star.print_sphere_coords();
                    current_star.print_grid_coords();
                    stars.add(current_star);
                }
            }
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        int a = 1;
    }

    @Override
    public void render(GameContainer gc, Graphics grphcs) throws SlickException {
        // Draw black background
        grphcs.setColor(Color.black);
        grphcs.fillRect(0, 0, Globals.WINWIDTH + Globals.GUIWIDTH, Globals.WINHEIGHT);
        // Draw each star
        for (StarDisplay star : stars) {
            star.draw(grphcs);
        }
        // Draw Gui, for now just a gray background
        grphcs.setColor(Color.gray);
        grphcs.fillRect(Globals.WINWIDTH, 0, Globals.GUIWIDTH, Globals.GUIHEIGHT);
    }
    
}
