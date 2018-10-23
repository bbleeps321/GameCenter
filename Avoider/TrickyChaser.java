package Avoider;

//import
import java.awt.Color;

/**
 * A Chaser that normally moves slowly but speeds up towards 
 * the target when it enters it range.
 */
public class TrickyChaser extends Chaser
{
    //constants
    private static final int RANGE = 100; //range of this chaser
    private static final int MIN_SPEED = 1; //min speed of chaser
    private static final int MAX_SPEED = 5; //normal max speed of chaser
    private static final Color COLOR = Color.yellow; //default color
    private static final int PURSUIT_SPEED = 7; //speed when chasing
    
    /**
     * Creates a Chaser at the specified coordinates on the given canvas 
     * with the specified target to chase.
     */
    public TrickyChaser(double x, double y, Avoider target, 
                            GameComponents.Canvas acanvas)
                            
    {
        super(x, y, target, acanvas, MIN_SPEED, MAX_SPEED, COLOR);
    }
    
    /**
     * Changes the direction of this chaser when/if the target enters 
     * it range.
     */
    protected void chase()
    {
        double EPSILON = 10; //acceptable tolerance range
        
        //calculate distance
        double radicand = Math.pow(myTarget.x() - myX, 2) + 
                            Math.pow(myTarget.y() - myY, 2);
        double dist = Math.sqrt(radicand);
        
        if (dist <= RANGE) //in range
        {
            //calculate x direction
            if (Math.abs(myX - myTarget.x()) <= EPSILON) //about same
                dx = 0;
            else if (myTarget.x() > myX) //target to right
                dx = PURSUIT_SPEED;
            else if (myTarget.x() < myX) //target to left
                dx = -PURSUIT_SPEED;
                
            //calculate y direction
            if (Math.abs(myY - myTarget.y()) <= EPSILON) //about same
                dy = 0;
            else if (myTarget.y() > myY) //target is lower
                dy = PURSUIT_SPEED;
            else if (myTarget.y() < myY) //target is higher
                dy = -PURSUIT_SPEED;
        }
    }
}