package Blobert3D;

//import
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Constructor;
import java.util.Random;

import GameComponents.Shape2D;
import GameComponents.Steppable;

/**
 * A ghost is represented by a rectangle. A ghost is initialized 
 * with a random location and direction of movement. It will bounce
 * around in the environment. If a blobert touches it, the blobert 
 * will die.
 */
public class Ghost extends Shape2D implements Steppable
{
    //constants
    private static final String IMAGE = "Blobert/Ghost.gif";
    private static final int DEFAULT_SIZE = 20; //default width and height
    protected static final int MAX_SPEED = 15; //max speed of ghost
    
    protected double dx, dy; //direction of movement in x and y directions
    protected double mySize; //size of ghost (width and height)
    private boolean hidden; //whether or not the ghost is hidden
    
    private GameComponents.Canvas canvas; //canvas ghost is on
    
    /**
     * Creates a Ghost at a random location inside the dimensions of 
     * the environment width and height.
     */
    public Ghost(GameComponents.Canvas acanvas)
    {
        super(0, 0, 0, 0, IMAGE); //values will be reset later
        canvas = acanvas;
        
        //start at random location
        Random rand = new Random();
        myX = rand.nextInt((int)canvas.width() - DEFAULT_SIZE);
        myY = rand.nextInt((int)canvas.height() - DEFAULT_SIZE);
        
        //start at default size
        myWidth = DEFAULT_SIZE;
        myHeight = DEFAULT_SIZE;
        
        //start with random movement
        dx = rand.nextInt(MAX_SPEED) + 1;
        dy = rand.nextInt(MAX_SPEED) + 1;
        
        hidden = false; //initially visible
    }

    /**
     * Creates an instance of a Ghost with the class name. 
     * Returns null if the class or correct constructor cannot be found.
     */
    public static Ghost createInstanceOf(String className,
                                            GameComponents.Canvas acanvas)
    {
        try
        {
            //find constructor requiring canvas
            Class classToCreate = Class.forName("Blobert."+className);
            Class[] params = {acanvas.getClass()};
            Constructor constructor = classToCreate.getConstructor(params);
            
            //create instance of object using found constructor
            Object[] argList = {acanvas};
            return (Ghost)constructor.newInstance(argList);
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }
    
    /**
     * Steps this ghost's movement.
     */
    public void step()
    {
        wallBounce(); //adjust for hitting wall
                
        myX += dx; //move
        myY += dy;
    }
    
    /**
     * Adjusts for Ghost hitting border of screen
     * by changing it direction of movement.
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
     * Returns the shape representing the Ghost.
     */
    public Shape shape()
    {
        return new Rectangle2D.Double(myX, myY, myWidth, myHeight);
    }
    
    /**
     * Reverses the directional movement of the ghost.
     */
    public void reverse()
    {
        dx = -dx;
        dy = -dy;
    }
    
    /**
     * Resets the location and directional movement of this ghost.
     */
    public void reset()
    {
        Random rand = new Random();
        
        //location
        myX = rand.nextInt((int)canvas.width() - DEFAULT_SIZE);
        myY = rand.nextInt((int)canvas.height() - DEFAULT_SIZE);
        
        //direction
        dx = rand.nextInt(MAX_SPEED) + 1;
        dy = rand.nextInt(MAX_SPEED) + 1;
    }
    
    /**
     * Stops the ghost's movement.
     */
    public void stopMovement()
    {
        dx = 0;
        dy = 0;
    }
    
    /**
     * Returns how many lives any blobert who is hit by this ghost 
     * will lose.
     */
    public int damageEffect()
    {
        return 1;
    }
    
    /**
     * Hides the ghost from the container.
     */
    public void hideGhost()
    {
        hidden = true;
    }
    
    /**
     * Shows the ghost in the container.
     */
    public void showGhost()
    {
        hidden = false;
    }
    
    /**
     * Returns whether or not the ghost is hidden.
     */
    public boolean isHidden()
    {
        return hidden;
    }
    
    /**
     * Paints the Ghost onto its container.
     */
    public void paintComponent(Graphics2D g2)
    {
        if (!hidden)
        {
            super.paintComponent(g2);
        }
    }
}