package Pong;

//import
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import GameComponents.Shape2D;
import GameComponents.Steppable;

/**
 * The ball in the pong game. Represented by a white 
 * ellipse.
 */
public class Ball extends Shape2D implements Steppable
{
    //constants
    private static final Color DEFAULT_COLOR = Color.white; //color of ball
    private static final double SIZE = 25; //size of ball (length and width)
    
    private double dx, dy; //directional movement of ball
    private double initX, initY; //initial coordinates of ball
    private GameComponents.Canvas canvas; //canvas ball is on
    
    /**
     * Creates a ball at the specified coordinates with no 
     * directional movement.
     */
    public Ball(double x, double y, GameComponents.Canvas acanvas)
    {
        super(x, y, SIZE, SIZE);
        initX = myX;
        initY = myY;
        dx = 0;
        dy = 0;
        canvas = acanvas;
    }
    
    /**
     * Steps the ball, moving it in the set direction.
     */
    public void step()
    {
        if (inBounds()) //in bounds
        {
            wallBounce(); //correct for bouncing off top and bottom walls
            
            myX += dx;
            myY += dy;
        }
    }
    
    /**
     * Sets the direction of the ball's movement to the specified values.
     */
    public void setDirection(double aDx, double aDy)
    {
        dx = aDx;
        dy = aDy;
    }
    
    /**
     * Bounces the ball off a paddle with the specified 
     * paddle directional movement.
     */
    public void paddleBounce(double paddleDy)
    {
        //change direction
        dx = -(dx + .2); //speeds up slightly at each paddle bounce
        dy += .5*paddleDy;
        
        //move
        myX += dx;
        myY += dy;
    }
    
    /**
     * Corrects for bouncing off the top and bottom walls.
     */
    private void wallBounce()
    {
        if (myY <= 0) //top
            dy = Math.abs(dy);
        else if (myY + myHeight >= canvas.height()) //bottom
            dy = -Math.abs(dy);
    }
    
    /**
     * Returns the left bound of the ball.
     */
    public double leftBound()
    {
        return myX;
    }
    
    /**
     * Returns the right bound of the ball.
     */
    public double rightBound()
    {
        return myX + SIZE;
    }
    
    /**
     * Returns whether or not the ball is in bounds.
     */
    public boolean inBounds()
    {
        return (myX >= 0) && (myX <= canvas.width());
    }
    
    /**
     * Returns whether or not the ball is moving.
     */
    public boolean isMoving()
    {
        return (dx != 0) && (dy != 0);
    }
    
    /**
     * Resets the ball to its initial coordinates and stops 
     * it movement.
     */
    public void reset()
    {
        myX = initX;
        myY = initY;
        dx = 0;
        dy = 0;
    }
    
    /**
     * Returns the shape representation of the ball.
     */
    public Shape shape()
    {
        return new Ellipse2D.Double(myX, myY, SIZE, SIZE);
    }
}