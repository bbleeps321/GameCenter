package Avoider;

//import
import java.awt.Color;

/**
 * A Chaser that moves normally but has a chance of changing 
 * its direction and speed on its own.
 */
public class RandomChaser extends Chaser
{
    //constants
    private static final int RANGE = 100; //range of this chaser
    private static final Color COLOR = Color.blue; //default color
    private static final double TURN_CHANCE = .05; //chance of turning    
    
    /**
     * Creates a Chaser at the specified coordinates on the given canvas 
     * with the specified target to chase.
     */
    public RandomChaser(double x, double y, Avoider target, 
                            GameComponents.Canvas acanvas)
                            
    {
        super(x, y, target, acanvas, MIN_SPEED, MAX_SPEED, COLOR);
    }
    
    /**
     * Changes the direction of this chaser at random
     */
    protected void chase()
    {
        if (Math.random() < TURN_CHANCE)
            randomizeDirection();
    }
}