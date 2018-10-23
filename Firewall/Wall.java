package Firewall;

//import
import java.awt.Graphics2D;
import java.util.ArrayList;

import GameComponents.BoundedEnv;
import GameComponents.Location;
import GameComponents.Steppable;

/**
 * A Wall is a grouping of bricks. It controls each brick's 
 * stepping functions and provided methods for accessing them.
 * Over time, the speed at which the bricks fall increases.
 */
public class Wall implements Steppable
{
    //constants
    private static final int DELAY = 60; //initial delay for moving bricks
    
    private BoundedEnv theEnv; //environment of all bricks
    private ArrayList<ArrayList<Brick>> wall; //list of all bricks
                                              //lowest index is closest to bottom
    private int stepCount; //counts steps for delay
    private int lostBricks; //number of bricks that have been lost
    private int myDelay; //current delay for moving bricks

    /**
     * Creates a wall with the specified number of columns in 
     * the given environment but no rows
     */
    public Wall(BoundedEnv env, int cols)
    {
        theEnv = env;
        wall = new ArrayList<ArrayList<Brick>>();
        for (int i = 0; i < cols; i++)
            wall.add(new ArrayList<Brick>());
            
        stepCount = 0;
        lostBricks = 0;
        myDelay = DELAY;
    }
    
    /**
     * Steps the wall and all the bricks in it while 
     * adding a new row at the top.
     */
    public void step()
    {
        if (stepCount >= myDelay)
        {
            //step all old bricks
            for (ArrayList<Brick> a : wall)
                for (Brick b : a)
                    b.step();
                    
            //add a new row at the top
            for (int i = 0; i < wall.size(); i++)
                wall.get(i).add(new Brick(theEnv, new Location(i,0), 1));
                
            stepCount = 0;
            myDelay--;
        }
        else
        {
            stepCount++;
        }
        removeDeadBricks();
    }
    
    /**
     * Returns whether or not the bottom-most bricks have reached 
     * the bottom of the environment.
     */
    public boolean reachedBottom()
    {
        int bottomY = theEnv.height() - 1;
        for (ArrayList<Brick> a : wall)
            if (a.size() > 0) //something in column)
                if (a.get(0).loc().y() == bottomY) //lowest brick in row at borrom
                    return true;
        return false;
    }
    
    /**
     * Removes the bricks that are no longer in the environment. 
     * Also increments the number of bricks that have been lost.
     */
    private void removeDeadBricks()
    {
        for (int i = 0; i < wall.size(); i++)
        {
            ArrayList<Brick> list = wall.get(i);
            for (int j = 0; j < list.size(); j++)
            {
                if (!theEnv.contains(list.get(j)))
                {
                    list.remove(j);
                    j--;
                    lostBricks++;
                }
            }
        }
    }
    
    /**
     * Returns the number of bricks that have been lost.
     */
    public int numBricksLost()
    {
        return lostBricks;
    }
    
    /**
     * Paints the wall onto the container.
     */
    public void paintComponent(Graphics2D g2)
    {
        for (ArrayList<Brick> a : wall)
            for (Brick b : a)
                b.paintComponent(g2);
    }
}