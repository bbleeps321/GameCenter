package Blobert;

//import
import java.awt.Color;
import java.util.ArrayList;

/**
 * A mine that freezes any ghosts that go over it instead 
 * of destroying it. Represented by a cyan ellipse
 */
public class FreezeMine extends Mine
{
    //constants
    private static final Color DEFAULT_COLOR = Color.cyan; //default color of mine
    
    /**
     * Creates a Mine at the specified (x,y) coordinates.
     */
    public FreezeMine(double x, double y, ArrayList<Ghost> someGhosts)
    {
        super(x, y, someGhosts, DEFAULT_COLOR);
    }
    
    /**
     * Detonates the mine, hiding it while freezing the specified
     * ghost.
     */
    public void detonate(Ghost g)
    {
        g.stopMovement();
        hidden = true; //hide mine
    }
}