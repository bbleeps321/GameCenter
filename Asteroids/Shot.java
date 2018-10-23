package Asteroids;

//import
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.lang.reflect.Constructor;

import GameComponents.Shape2D;
import GameComponents.Steppable;

/**
 * A Shot is anything fired by a ship. It goes only in the direction 
 * it was initialized with and disappears when it leave the screen. 
 * Can destroy asteroids.
 */
public class Shot extends Shape2D implements Steppable
{
    //constants
    private static final int SIZE = 5; //size of shot
    private static final Color COLOR = Color.white; //color of shot
    private static final double ANGLE_CORRECTION = Math.PI/2; //for movement
    private static final double SPEED = 10; //speed of shot
    private static final int MOVES_PER_STEP = 2; //how many moves per step
    
    protected int movesPerStep; //moves in each step (affects speed)
    protected GameComponents.Canvas canvas; //canvas shot is on
    private double myAngle; //angle at which shot is moving
    private boolean hidden; //if shot is hidden from view
    
    /**
     * Creates a shot with nothing initialized. Should be used when 
     * using methods that do not require them to be (cost).
     */
    public Shot()
    {
        super(0, 0, 0, 0, COLOR);
    }
    
    /**
     * Creates a shot at the specified location with the initial 
     * directional facing.
     */
    public Shot(double x, double y, double ang, GameComponents.Canvas acanvas)
    {
        super(x, y, SIZE, SIZE, COLOR);
        canvas = acanvas;
        myAngle = ang;
            
        movesPerStep = MOVES_PER_STEP;
        hidden = false;
    }
    
    /**
     * Creates a shot at the specified location with the initial 
     * directional facing with the given color, size, and speed
     */
    protected Shot(double x, double y, double ang, GameComponents.Canvas acanvas,
                          Color col, double size, int mps)
    {
        super(x, y, size, size, col);
        canvas = acanvas;
        myAngle = ang;
            
        movesPerStep = mps;
        hidden = false;
    }
    
    /**
     * Creates an instance of a shot with the given class name. 
     * Returns null if the class or correct constructor cannot be found.
     */
    public static Shot createInstanceOf(String className, double x, double y,
                                                double dir,
                                                GameComponents.Canvas acanvas)
    {
        try
        {
            //find constructor requiring double, double, double, double, canvas
            Class classToCreate = Class.forName("Asteroids."+className);
            Class[] params = {Double.TYPE, Double.TYPE, Double.TYPE,
                                acanvas.getClass()};
            Constructor constructor = classToCreate.getConstructor(params);
            
            //create instance of object using found constructor
            Object[] argList = {x, y, dir, acanvas};
            return (Shot)constructor.newInstance(argList);
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }
    
    /**
     * Creates an instance of a shot with the given class name. 
     * Returns null if the class or correct constructor cannot be found.
     */
    public static Shot createInstanceOf(String className)
    {
        try
        {
            //find constructor requiring no parameter
            Class classToCreate = Class.forName("Asteroids."+className);
            Class[] params = {};
            Constructor constructor = classToCreate.getConstructor(params);
            
            //create instance of object using found constructor
            Object[] argList = {};
            return (Shot)constructor.newInstance(argList);
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }
    
    /**
     * Steps the shot. May be delayed, depending on outside 
     * factors. Moves multiple times per step so shot is faster.
     */
    public void step()
    {
        for (int i = 0; i < movesPerStep; i++)
        {
            if (hidden)
                return;
                
            double[] dir = direction();
            myX += dir[0];
            myY += dir[1];
            
            selfRemove(); //remove self from view if out of bounds
        }
    }
    
    /**
     * Returns the directional values of the shot based on 
     * the speed and angle.
     */
    private double[] direction()
    {
        double angle = myAngle - ANGLE_CORRECTION; //real angle measure
        double xDir = SPEED*Math.cos(angle);
        double yDir = SPEED*Math.sin(angle);
        
        return new double[] {xDir,yDir};
    }
    
    /**
     * Hides itself if it is off the canvas boundary.
     */
    private void selfRemove()
    {
        if ((myX < 0) || (myX + myWidth > canvas.width()) ||
            (myY < 0) || (myY + myHeight > canvas.height()))
        {
            hidden = true;
        }
    }
    
    /**
     * Detonates the shot, hiding it from view.
     */
    public void detonate()
    {
        hidden = true;
    }
    
    /**
     * Returns the cost of the shot.
     */
    public int cost()
    {
        return 0;
    }
    
    /**
     * Returns the power of the shot (how much it damages an asteroid).
     */
    public int power()
    {
        return 1;
    }
    
    /**
     * Returns whether or not the shot is hidden from view.
     */
    public boolean isHidden()
    {
        return hidden;
    }
    
    /**
     * Returns the shape representation of the shot.
     */
    public Shape shape()
    {
        return new Ellipse2D.Double(myX, myY, myWidth, myHeight);
    }
    
    /**
     * Returns if the shot overlaps the given shape.
     */
    public boolean overlaps(Shape2D s)
    {
        if (hidden)
            return false;
        return super.overlaps(s);
    }
    
    /**
     * Paints the shot onto the container. 
     */
    public void paintComponent(Graphics2D g2)
    {
        if (hidden)
            return;
            
        g2.rotate(myAngle, myX, myY);
        super.paintComponent(g2);
        g2.rotate(-myAngle, myX, myY);
    }
}