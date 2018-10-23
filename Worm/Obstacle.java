package Worm;

//import
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import GameComponents.Shape2D;

/**
 * An obstacle in the Worm game, represented by a black 
 * rectangle.
 */
public class Obstacle extends Shape2D
{
    //constants
    private static final Color COLOR = Color.black; //color of obstacle
    
    /**
     * Creates an obstacle at the specified location.
     */
    public Obstacle(double x, double y, double size)
    {
        super(x, y, size, size, COLOR);
    }
    
    /**
     * Returns the shape representation of the obstacle.
     */
    public Shape shape()
    {
        return new Rectangle2D.Double(myX, myY, myWidth, myHeight);
    }
}