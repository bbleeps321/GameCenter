package Asteroids;

//import
import java.awt.Color;

/**
 * A bomb is just like a shot but larger, slower, and more 
 * powerful.
 */
public class Bomb extends Shot
{
    //constants
    private static final Color COLOR = Color.red; //bomb's color
    private static final int SIZE = 10; //size of bomb
    private static final int MOVES_PER_STEP = 1; //how many moves per step
    
    /**
     * Creates a bomb with all fields uninitialized.
     */
    public Bomb()
    {
        super();
    }
    
    /**
     * Creates a bomb at the specified coordinates on the canvas 
     * with the initial directional facing.
     */
    public Bomb(double x, double y, double ang, GameComponents.Canvas acanvas)
    {
        super(x, y, ang, acanvas, COLOR, SIZE, MOVES_PER_STEP);
    }
    
    /**
     * Returns the cost of the shot.
     */
    public int cost()
    {
        return 3;
    }
    
    /**
     * Returns the power of the shot (how much it damages an asteroid).
     */
    public int power()
    {
        return 2;
    }
}