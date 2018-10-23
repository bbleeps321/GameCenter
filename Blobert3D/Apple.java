package Blobert3D;

//import
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import GameComponents.Shape2D;

/**
 * An Apple is represented by a colored, non-black, Ellipse2D 
 * shape. It is what the Bloberts are trying to collect.
 */
public class Apple extends Shape2D
{
    //constants
    private static final String IMAGE = "blobert/apple.gif"; //apple image
    public static final int SIZE = 10; //size of ellipse
    
    private boolean isCollected; //if apple has been collected already
    
    /**
     * Creates an apple at the specified (x,y) coordinates.
     */
    public Apple(double x, double y)
    {
        super(x, y, SIZE, SIZE, IMAGE);
        isCollected = false;
    }
    
    /**
     * Returns the shape representing the Apple.
     */
    public Shape shape()
    {
        return new Ellipse2D.Double(myX, myY, SIZE, SIZE);
    }
    
    /**
     * Returns whether or not the apple has been collected.
     */
    public boolean collected()
    {
        return isCollected;
    }
    
    /**
     * Sets whether or not the apple has been collected to 
     * the new value.
     */
    public void setCollected(boolean newVal)
    {
        isCollected = newVal;
    }
    
    /**
     * Paints this apple to the container it is in.
     */
    public void paintComponent(Graphics2D g2)
    {
        if (!isCollected)
        {
            super.paintComponent(g2);
        }
    }
}