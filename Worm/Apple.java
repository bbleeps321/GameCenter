package Worm;

//import
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import GameComponents.Shape2D;

/**
 * An apple in the Worm game, represented by a red 
 * ellipse.
 */
public class Apple extends Shape2D
{
    //constants
    private static final String IMAGE = "worm/apple.gif"; //apple image

    protected int myBonus; //factor of bonus when apple is eaten
    
    /**
     * Creates an apple at the specified location.
     */
    public Apple(double x, double y, double size)
    {
        super(x, y, size, size, IMAGE);
        myBonus = 1;
    }
    
    /**
     * Returns the shape representation of the apple.
     */
    public Shape shape()
    {
        return new Ellipse2D.Double(myX, myY, myWidth, myHeight);
    }
    
    /**
     * Returns the factor by which the score increment is multiplied 
     * when this apple is eaten.
     */
    public int bonus()
    {
        return myBonus;
    }
}