package Hunter;

//import
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import GameComponents.Shape2D;
import GameComponents.Steppable;

/**
 * The generic target that, when shot at, is simply destroyed.
 */
public class Target extends Shape2D implements Steppable
{
    //constants
    protected static final double WIDTH = 40; //default width of target
    protected static final double HEIGHT = 40; //default height of target
    protected static final Color COLOR = Color.red; //default color of target
    private static final int MAX_SPEED = 5; //initial max speed of target
    
    protected GameComponents.Canvas canvas; //canvas target is on
    protected double dx, dy; //movement in x and y direction
    protected int myMax; //current max speed of target
    
    protected boolean hidden; //if the target is hidden
    
    /**
     * Creates a target at the specified (x,y) coordinates on the 
     * given canvas.
     */
    public Target(double x, double y, GameComponents.Canvas acanvas)
    {
        super(x, y, WIDTH, HEIGHT, COLOR);
        
        canvas = acanvas;
        
        myMax = MAX_SPEED;
        
        randomizeDirection();
        
        hidden = false;
    }
    
    /**
     * Steps the target.
     */
    public void step()
    {
        wallBounce();
        
        myX += dx;
        myY += dy;
    }
    
    /**
     * Returns the factor by which this target increases the 
     * score when destroyed.
     */
    public int bonus()
    {
        return 0;
    }
    
    /**
     * Sets whether or not the target is hidden to the specified value.
     */
    public void setHidden(boolean newVal)
    {
        hidden = newVal;
    }
    
    /**
     * Returns whether or not the target is hidden.
     */
    public boolean isHidden()
    {
        return hidden;
    }

    /**
     * Returns the shape representation of the target.
     */
    public Shape shape()
    {
        return new Ellipse2D.Double(myX, myY, myWidth, myHeight);
    }
    
    /**
     * Randomizes this target's directional movement.
     */
    public void randomizeDirection()
    {
        Random rand = new Random();
        dx = rand.nextInt(myMax) + 1;
        dy = rand.nextInt(myMax) + 1;
    }
    
    /**
     * Randomizes this target's location.
     */
    public void randomizeLocation()
    {
        Random rand = new Random();
        myX = rand.nextInt((int)(canvas.width() - WIDTH)) + 1;
        myY = rand.nextInt((int)(canvas.height() - HEIGHT)) + 1;
    }
    
    /**
     * Raises the maximum speed of the target, potentially making the 
     * target faster.
     */
    public void raiseMax()
    {
        myMax++;
    }
    
    /**
     * Adjusts for target bouncing off walls.
     */
    protected void wallBounce()
    {
        if (myY <= 0) //top
            dy = Math.abs(dy);
        else if (myY + myHeight >= canvas.height()) //bottom
            dy = -Math.abs(dy);
            
        if (myX <= 0) //left
            dx = Math.abs(dx);
        else if (myX + myWidth >= canvas.width()) //right
            dx = -Math.abs(dx);
    }
    
    /**
     * Paints the target onto the container if it is visible.
     */
    public void paintComponent(Graphics2D g2)
    {
        if (!hidden)
            super.paintComponent(g2);
    }
}