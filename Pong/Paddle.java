package Pong;

//import
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import GameComponents.Direction;
import GameComponents.Shape2D;
import GameComponents.Steppable;

/**
 * A paddle used for the pong game. A paddle is represented 
 * by a white rectangle.
 */
public class Paddle extends Shape2D implements Runnable, Steppable
{
    //constants
    private static final Color DEFAULT_COLOR = Color.white; //color of paddle
    private static final double GAP = 15; //gap from wall
    public static final double WIDTH = 40; //width of paddle
    public static final double HEIGHT = 100; //height of paddle
    private static final double SPEED = 10; //increment by which paddle moves
    
    private GameComponents.Canvas canvas; //canvas paddle is on
    private double dy; //direction of movement in y direction only
    
    private double initX, initY; //initial (x,y) coordinates of paddle
    private boolean isActive; //game is currently active
    private boolean isOver; //game is over
    
    /**
     * Creates a Paddle at the center of the specified canvas on the 
     * specified Direction side.
     */
    public Paddle(Direction side, GameComponents.Canvas acanvas)
    {
        super(0, 0, WIDTH, HEIGHT, DEFAULT_COLOR); //coordinates calculated later
        canvas = acanvas;
        
        if (side.equals(Direction.WEST))
            myX = GAP;
        else if (side.equals(Direction.EAST))
            myX = canvas.width() - GAP - WIDTH;

        myY = (canvas.height() - HEIGHT)/2;
        
        initX = myX;
        initY = myY;

        dy = 0;
        
//         Thread thread = new Thread(this);
//         thread.start();
    }
    
    /**
     * Runs the paddle (separate thread).
     */
    public void run()
    {
        while (!isOver)
        {
            while(isActive)
            {
                
            }
        }
    }
    
    /**
     * Steps the paddle in the direction of movement.
     */
    public void step()
    {
        //still in bounds
        if ((myY + dy >= 0) && (myY + HEIGHT + dy) <= canvas.height())
            myY += dy;
        else //hitting top or bottom
            brake();
    }
    
    /**
     * Moves the paddle up by one increment unit.
     */
    public void moveUp()
    {
        dy = -SPEED;
    }
    
    /**
     * Moves the paddle down by one increment unit.
     */
    public void moveDown()
    {
        dy = SPEED;
    }
    
    /**
     * Stops the paddles movement.
     */
    public void brake()
    {
        dy = 0;
    }
    
    /**
     * Returns the left bound of the paddle.
     */
    public double leftBound()
    {
        return myX;
    }
    
    /**
     * Returns the right bound of the paddle.
     */
    public double rightBound()
    {
        return myX + WIDTH;
    }
    
    /**
     * Returns the speed of the paddle.
     */
    public double speed()
    {
        return dy;
    }
    
    /**
     * Returns the x coordinate of the paddle.
     */
    public double x()
    {
        return myX;
    }
    
    /**
     * Returns the y coordinate of the paddle.
     */
    public double y()
    {
        return myY;
    }
    
    /**
     * Returns the width of the paddle.
     */
    public double width()
    {
        return WIDTH;
    }
    
    /**
     * Returns the height of the paddle.
     */
    public double height()
    {
        return HEIGHT;
    }
    
    /**
     * Sets the game's activity to be considered the specified value.
     */
    public void setActive(boolean newVal)
    {
        isActive = newVal;
    }
    
    /**
     * Sets the game's completedness to be the specified value.
     */
    public void setOver(boolean newVal)
    {
        isOver = newVal;
    }
    
    /**
     * Returns the shape representation of the Paddle.
     */
    public Shape shape()
    {
        return new Rectangle2D.Double(myX, myY, WIDTH, HEIGHT);
    }
    
    /**
     * Returns whether the specifed y coordinate is between the ends 
     * of the paddle.
     */
    public boolean encompasses(double y)
    {
        return (myY < y) && (myY + HEIGHT > y);
    }
    
    /**
     * Resets the paddle's location to be its initial one and 
     * stops its movement.
     */
    public void reset()
    {
        myX = initX;
        myY = initY;
        dy = 0;
    }
}