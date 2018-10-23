package Avoider;

//import
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Constructor;
import java.util.Random;

import GameComponents.Shape2D;
import GameComponents.Steppable;

/**
 * Generic object that "chases" an Avoider object. Basic Chaser 
 * only bounces around screen.
 */
public class Chaser extends Shape2D implements Steppable
{
    //constants
    protected static final int SIZE = 20; //default size of chaser
    protected static final Color COLOR = Color.red; //default color of chaser
    protected static final int MAX_SPEED = 15; //max speed of chaser
    protected static final int MIN_SPEED = 5; //min speed of chaser
    
    protected GameComponents.Canvas canvas; //canvas chaser is on
    protected Avoider myTarget; //what chaser is chasing
    protected double dx, dy; //movement in x and y direction
    protected int myMax, myMin; //bounds of speed of this chaser
    
    /**
     * Creates a Chaser object at the specified (x,y) coordinates on 
     * the given canvas with the specified Avoider to chase.
     */
    public Chaser(double x, double y, Avoider target, 
                            GameComponents.Canvas acanvas)
    {
        super(x, y, SIZE, SIZE, COLOR);
        myTarget = target;
        canvas = acanvas;
        
        myMax = MAX_SPEED;
        myMin = MIN_SPEED;
        
        randomizeDirection();
    }
    
    /**
     * Creates a Chaser object at the specified (x,y) coordinates on 
     * the given canvas with the specified Avoider to chase and speed range.
     */
    protected Chaser(double x, double y, Avoider target, 
                            GameComponents.Canvas acanvas, 
                            int min, int max, Color col)
    {
        super(x, y, SIZE, SIZE, col);
        myTarget = target;
        canvas = acanvas;
        
        myMax = max;
        myMin = min;
        
        randomizeDirection();
    }
    
    /**
     * Creates a new instance of a chaser with the given class name.
     */
    public static Chaser createInstanceOf(String className, double x, double y,
                               Avoider target, GameComponents.Canvas acanvas)
    {
        try
        {
            //find constructor requiring types of arguments
            Class classToCreate = Class.forName("Avoider." + className);
            Class[] params = {Double.TYPE, Double.TYPE, 
                                target.getClass(), acanvas.getClass()};
            Constructor constructor = classToCreate.getConstructor(params);
            
            //create instance of object using found constructor
            Object[] args = {x, y, target, acanvas};
            return (Chaser)constructor.newInstance(args);
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }
    
    /**
     * Steps the chaser.
     */
    public void step()
    {
        //adjust for chasing
        chase();
        
        //adjust for wall bounce
        wallBounce();
        
        myX += dx;
        myY += dy;
    }
    
    /**
     * Returns the shape representation of the chaser.
     */
    public Shape shape()
    {
        return new Rectangle2D.Double(myX, myY, myWidth, myHeight);
    }
    
    /**
     * Adjusts this chaser's directional movement to chase the target. 
     * Intended for use by subclasses, not this class.
     */
    protected void chase() {}
    
    /**
     * Randomizes the chaser's directional movement while still 
     * within the speed constraints.
     */
    public void randomizeDirection()
    {
        Random rand = new Random();
        dx = rand.nextInt(myMax) + myMin;
        dy = rand.nextInt(myMax) + myMin;
    }
    
    /**
     * Randomizes this chaser's location.
     */
    public void randomizeLocation()
    {
        Random rand = new Random();
        myX = rand.nextInt((int)(canvas.width() - WIDTH)) + 1;
        myY = rand.nextInt((int)(canvas.height() - HEIGHT)) + 1;
    }
    
    /**
     * Adjusts for chaser bouncing off walls.
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
}