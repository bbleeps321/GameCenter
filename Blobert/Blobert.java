package Blobert;

//import
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import GameComponents.Shape2D;
import GameComponents.Steppable;

/**
 * The blobert object that is controlled in the Blobert game.
 */
public class Blobert extends Shape2D implements Steppable
{
    //constants
    protected static final double SPD = 2.5; //increment by which speed changes
    public static final int DEFAULT_SIZE = 20; //initial size of blob
    private static final int DELAY = 33; //delay of thread in milliseconds
    
    private GameComponents.Canvas canvas; //environment containing blobert
    protected double dx, dy; //direction of movement in the x and y directions
    protected double mySize; //size of blobert (width and height)
    protected ArrayList<Ghost> theGrave; //graveyard of all ghosts
    private boolean acted; //whether or not the blobert has acted
    
    //for resetting blobert
    private double initX, initY; //initial coordinates
    
    /**
     * Creates a Blobert at the specified coordinates.
     */
    public Blobert(double x, double y, GameComponents.Canvas acanvas)
    {
        super(x, y, DEFAULT_SIZE, DEFAULT_SIZE);
        
        initX = x;
        initY = y;
        
        //start at default size
        mySize = DEFAULT_SIZE;
        
        canvas = acanvas;
        theGrave = null;
        
        //start with no movement
        dx = 0;
        dy = 0;

        acted = false;
    }
    
    /**
     * Creates a Blobert at the specified coordinates.
     */
    public Blobert(double x, double y, GameComponents.Canvas acanvas, 
                        ArrayList<Ghost> graveyard)
    {
        super(x, y, DEFAULT_SIZE, DEFAULT_SIZE);
        
        initX = x;
        initY = y;
        
        //start at default size
        mySize = DEFAULT_SIZE;
        
        canvas = acanvas;
        theGrave = graveyard;
        
        //start with no movement
        dx = 0;
        dy = 0;

        acted = false;
    }
    
    /**
     * Creates an instance of a blobert with the class name. 
     * Returns null if the class or correct constructor cannot be found.
     */
    public static Blobert createInstanceOf(String className, double x, double y,
                                               GameComponents.Canvas acanvas)
    {
        try
        {
            //find constructor requiring types of arguments
            Class classToCreate = Class.forName("Blobert." + className);
            Class[] params = {Double.TYPE, Double.TYPE, acanvas.getClass()};
            Constructor constructor = classToCreate.getConstructor(params);
            
            //create instance of object using found constructor
            Object[] args = {x, y, acanvas};
            return (Blobert)constructor.newInstance(args);
        }
        catch (Exception e)
        {
            System.out.println(e + "here");
            return null;
        }
    }
    
    /**
     * Creates an instance of a blobert with the class name. 
     * Returns null if the class or correct constructor cannot be found.
     */
    public static Blobert createInstanceOf(String className, double x, double y,
                                            GameComponents.Canvas acanvas,
                                            ArrayList<Ghost> graveyard)
    {
        try
        {
            //find constructor requiring types of arguments
            Class classToCreate = Class.forName("Blobert." + className);
            Class[] params = {Double.TYPE, Double.TYPE, acanvas.getClass(),
                                    graveyard.getClass()};
            Constructor constructor = classToCreate.getConstructor(params);
            
            //create instance of object using found constructor
            Object[] args = {x, y, acanvas, graveyard};
            return (Blobert)constructor.newInstance(args);
        }
        catch (Exception e)
        {
            //try to create using constructor without graveyard parameter
            return createInstanceOf(className, x, y, acanvas);
        }
    }
    
    /**
     * Steps the blobert.
     */
    public void step()
    {
        wallBounce(); //adjust for hitting wall
        
        myX += dx; //move
        myY += dy;
    }
    
    /**
     * Accelerates the blob's movement in the upwards direction.
     */
    public void moveUp()
    {
        dy -= SPD;
        acted = true;
    }
    
    /**
     * Accelerates the blob's movement in the downwards direction.
     */
    public void moveDown()
    {
        dy += SPD;
        acted = true;
    }
    
    /**
     * Accelerates the blob's movement in the left direction.
     */
    public void moveLeft()
    {
        dx -= SPD;
        acted = true;
    }
    
    /**
     * Accelerates the blob's movement in the right direction.
     */
    public void moveRight()
    {
        dx += SPD;
        acted = true;
    }
    
    /**
     * Resets blob's speed to zero in both the x and y directions.
     */
    public void brake()
    {
        dx = 0;
        dy = 0;
    }
    
    /**
     * Uses abilities of blobert. When extended in a subclass 
     * it should be called first so the acted variable is 
     * correctly set.
     */
    public void act1()
    {
        acted = true;
    }
    public void act2()
    {
        acted = true;
    }
    
    /**
     * Returns the cost of using the blobert's abilities.
     */
    public int act1Cost()
    {
        return 0;
    }
    public int act2Cost()
    {
        return 0;
    }
    
    /**
     * Resets the properties of the blobert to what they were 
     * when it was first initialized.
     */
    public void resetBlobert()
    {
        myX = initX;
        myY = initY;
        brake(); //stop movement;
        acted = false;
    }
    
    /**
     * Returns whether or not the blobert has acted or not.
     */
    public boolean hasActed()
    {
        return acted;
    }
    
    /**
     * Returns whether or not the blobert is moving.
     */
    public boolean isMoving()
    {
        return (dx != 0) || (dy != 0);
    }
    
    /**
     * Returns the shape of the Blobert.
     */
    public Shape shape()
    {
        return new Ellipse2D.Double(myX, myY, myWidth, myHeight);
    }
    
    /**
     * Adjusts for blob hitting border of screen.
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
     * Returns the preferred color of the blobert.
     */
    protected Color preferredColor()
    {
        if (isMoving())
            return Color.yellow;
        else
            return new Color(255, 215, 0);
    }
    
    /**
     * Paints the blobert onto the container.
     */
    public void paintComponent(Graphics2D g2)
    {
        g2.setColor(preferredColor());
        g2.fill(shape());
    }
}
