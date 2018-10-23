package Avoider;

//import
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import GameComponents.Shape2D;

/**
 * Player-controlled object that is avoiding the chasers.
 */
public class Avoider extends Shape2D
{
    //constants
    protected static final int SIZE = 20; //default size of avoider
    protected static final Color COLOR = Color.green; //default color of avoider
    private static final int MAX_SPEED = 10; //max speed of avoider
    
    protected GameComponents.Canvas canvas; //canvas avoider is on
    protected Avoider myTarget; //what avoider is chasing
    protected double dx, dy; //movement in x and y direction
    
    /**
     * Creates a Avoider object at the specified (x,y) coordinates on 
     * the given canvas with the specified Avoider to chase.
     */
    public Avoider(double x, double y, GameComponents.Canvas acanvas)
    {
        super(x, y, SIZE, SIZE, COLOR);
        canvas = acanvas;
    }
    
    /**
     * Moves the center of the avoider to the given coordinates.
     */
    public void moveTo(double x, double y)
    {
        myX = x + SIZE/2;
        myY = y + SIZE/2;
    }
    
    /**
     * Returns the shape representation of the avoider.
     */
    public Shape shape()
    {
        return new Ellipse2D.Double(myX, myY, myWidth, myHeight);
    }
}