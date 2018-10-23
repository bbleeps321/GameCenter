package Asteroids;

//import
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.util.ArrayList;

import GameComponents.Shape2D;
import GameComponents.Steppable;

/**
 * Ship controlled by a human. Represented by a triangle 
 * pointing in the direction it is facing.
 */
public class Ship extends Shape2D implements Steppable
{
    //constants
    private static final String IMAGE = "asteroids/nave.gif";
    private static final Color COLOR = Color.white; //ship's color
    private static final double WIDTH = 25; //width of ship
    private static final double HEIGHT = 30; //height of ship
    private static final double ACCEL = 1.5; //rate at which ship accelerates
    private static final double ROTATE_UNIT = Math.PI/12; //unit ship rotates by
    private static final double ANGLE_CORRECTION = Math.PI/2; //for movement
    private static final double EPSILON = 1E-5; //range of error for doubles
    
    private double myAngle; //angle at which ship is tilted in radians
    private double mySpd; //speed of ship
    private double initX, initY; //initial coordinates of ship
    private double dx, dy; //movement in x and y directions
    private boolean hasActed; //if ship has acted once since being created
    private ArrayList<Shot> shots; //list of all active shots that have been fired
    private GameComponents.Canvas canvas; //canvas ship is on
    
    /**
     * Creates a ship at the specified coordinates in the canvas.
     */
    public Ship(double x, double y, GameComponents.Canvas acanvas)
    {
        super(x, y, WIDTH, HEIGHT, IMAGE);
        initX = x;
        initY = y;
        canvas = acanvas;
        myAngle = 0; //no angle initially
        mySpd = 0;
        dx = 0;
        dy = 0;
        shots = new ArrayList<Shot>();
        hasActed = false;
    }
    
    /**
     * Steps the ship and all shots it has fired.
     */
    public void step()
    {
        shift(); //shift as needed

        myX += dx;
        myY += dy;
        
        for (Shot s : shots)
            s.step();
            
        removeDeadShots();
    }
    
    /**
     * Returns the directional values of the ship based on 
     * the speed and angle.
     */
    private double[] direction()
    {
        double angle = myAngle - ANGLE_CORRECTION; //real angle measure
        double xDir = mySpd*Math.cos(angle);
        double yDir = mySpd*Math.sin(angle);
        
        return new double[] {xDir,yDir};
    }
    
    /**
     * Shifts the ship to appear on the opposite side of the 
     * screen if it is completely off screen.
     */
    private void shift()
    {
        if (myX + myWidth < 0) //off the left
            myX = canvas.width(); //move to be just off the right
        else if (myX > canvas.width()) //off the right
            myX = -myWidth; //move to be just off the left
        
        if (myY + myHeight < 0) //off the top
            myY = canvas.height(); //move to be just off bottom
        else if (myY > canvas.height()) //off the bottom
            myY = -myHeight; //move to be just off top
    }
    
    /**
     * Moves the ship forward.
     */
    public void thrust()
    {
        mySpd += 1.5;
        double[] dir = direction();
        dx = dir[0];
        dy = dir[1];
        hasActed = true;
    }
    
    /**
     * Moves the ship backwards.
     */
    public void reverse()
    {
        mySpd -= 1.5;
        double[] dir = direction();
        dx = dir[0];
        dy = dir[1];
        hasActed = true;
    }
    
    /**
     * Rotates the ship to the left.
     */
    public void turnLeft()
    {
        myAngle -= ROTATE_UNIT;
        
        //correct for coterminal angles
        if(myAngle < 0)
            myAngle += 2*Math.PI;
            
        hasActed = true;
    }
    
    /**
     * Rotates the ship to the right.
     */
    public void turnRight()
    {
        myAngle += ROTATE_UNIT;
        
        //correct for coterminal angles
        if(myAngle > 2*Math.PI)
            myAngle -= 2*Math.PI;
            
        hasActed = true;
    }
    
    /**
     * Stops the ship's movement.
     */
    public void brake()
    {
        dx = 0;
        dy = 0;
        mySpd = 0;
        
        hasActed = true;
    }
    
    /**
     * Fires the weapon with the specified name.
     */
    public void fire(String shotName)
    {
        double[] tip = tip();
        double x = tip[0];
        double y = tip[1];
        shots.add(Shot.createInstanceOf(shotName, x, y, myAngle, canvas));
        hasActed = true;
    }
    
    /**
     * Returns the cost of firing the weapon with the specified name. 
     * Does NOT fire the weapon
     */
    public int fireCost(String shotName)
    {
        Shot s = Shot.createInstanceOf(shotName);

        return s.cost();
    }
    
    /**
     * Returns the number of shots that have been fired and are active.
     */
    public int numShots()
    {
        return shots.size();
    }
    
    /**
     * Returns the shot at the specified shot index
     */
    public Shot shotAt(int index)
    {
        return shots.get(index);
    }
    
    /**
     * Resets the ship to its initial state.
     */
    public void reset()
    {
        myX = initX;
        myY = initY;
        mySpd = 0;
        dx = 0;
        dy = 0;
        myAngle = 0;
        shots.clear();
        hasActed = false;
    }
    
    /**
     * Returns whether or not the ship has acted once.
     */
    public boolean acted()
    {
        return hasActed;
    }
    
    /**
     * Returns the shape representation of the ship.
     */
    public Shape shape()
    {
        Polygon p = new Polygon();
        p.addPoint((int)((myX + (myX + myWidth))/2), (int)myY); //top point
        p.addPoint((int)myX, (int)(myY + myHeight)); //left base
        p.addPoint((int)(myX + myWidth), (int)(myY + myHeight)); //right base
        return p;
    }
    
    /**
     * Returns the tip coordinates of this ship.
     */
    private double[] tip()
    {
        double x = (myX + (myX + myWidth))/2;
        double y = myY;
        return new double[] {x,y};
    }
    
    /**
     * Returns the center coordinates of this ship.
     */
    private double[] center()
    {
        double x = (myX + (myX + myWidth))/2;
        double y = (myY + (myY + myHeight))/2;
        return new double[] {x,y};
    }
    
    /**
     * Removes the "dead" shots.
     */
    private void removeDeadShots()
    {
        for (int i = 0; i < shots.size(); i++)
        {
            if (shots.get(i).isHidden())
            {
                shots.remove(i);
                i--;
            }
        }
    }
    
    /**
     * Paints the ship and its shots onto the canvas.
     */
    public void paintComponent(Graphics2D g2)
    {
        double[] center = center();
        
        //rotate ship's direction
        g2.rotate(myAngle, center[0], center[1]);
        
        //paint ship
        super.paintComponent(g2);
        
        //rotate back to original orientation
        g2.rotate(-myAngle, center[0], center[1]);
        
        //paint shots
        for (Shot s : shots)
            s.paintComponent(g2);
    }
}