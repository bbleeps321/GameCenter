package TowerDefense;

//import
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import GameComponents.BoundedEnv;
import GameComponents.Location;
import GameComponents.Shape2DLocatable;
import GameComponents.Steppable;

/**
 * A tower is any stationary object inside the Tower Defense 
 * game. The basic tower has a simple gun and a range of 2 cells.
 */
public class Tower extends Shape2DLocatable implements Steppable, Unit
{
    //constants
    protected static final Color COLOR = Color.gray; //default color of tower
    private static final String IMAGE = "towerdefense/turret_"; //need add level
    protected static final String EXTENSION = ".gif"; //image typw
    protected static final int MAX_LEVEL = 5; //max upgrade level
    protected static final int RANGE = 3; //default range of tower's gun
    protected static final int DAMAGE = 4; //default damage done by gun
    protected static final int DELAY = 16; //default delay for stepping
    private static final int EXPLOSION_SIZE = 10; //size of explosion
    
    protected ArrayList<Creep> myTargs; //list of creeps to be shot at
    protected int myRng; //range of tower's gun
    protected int myDelay; //delay for stepping
    protected int myDmg; //damage done by gun
    protected int myLevel; //the level of upgrade of the tower
    protected int stepCount; //count of how many times since last act
    protected double myAngle; //angle of rotation of tower in radians
    
    //for firing
    protected Ellipse2D.Double explosion; //shape shown when hitting creep
    protected boolean firing; //if the tower is currently firing
        
    /**
     * Creates a dummy tower with no real purpose other than 
     * to provide information regarding its properties.
     */
    public Tower()
    {
        super(null, null);
        myLevel = 0;
    }
    
    /**
     * Creates a tower at the specified location in the given 
     * environment. Should be overridden by all subclasses.
     */
    public Tower(BoundedEnv env, Location loc, ArrayList<Creep> targList)
    {
        super(env, loc);

        myTargs = targList;
        myRng = RANGE;
        myDmg = DAMAGE;
        myDelay = DELAY;
        myAngle = 0;
        myLevel = 1;
        
        //set up image
        myImage = sprite.getSprite(IMAGE + myLevel + EXTENSION);
        
        //initialize explosion (location set later)
        explosion = new Ellipse2D.Double(0, 0, EXPLOSION_SIZE, EXPLOSION_SIZE);
        firing = false;
        
        stepCount = 0;
    }
    
    /**
     * Creates a tower at the specified location in the given 
     * environment with the specified parameters.
     */
    protected Tower(BoundedEnv env, Location loc, ArrayList<Creep> targList, 
                                    String image, int rng, int dmg, int delay)
    {
        super(env, loc);

        myTargs = targList;
        myRng = rng;
        myDmg = dmg;
        myDelay = delay;
        myAngle = 0;
        myLevel = 1;
        
        //set up image
        myImage = sprite.getSprite(image + myLevel + EXTENSION);
        
        //initialize explosion (location set later)
        explosion = new Ellipse2D.Double(0, 0, EXPLOSION_SIZE, EXPLOSION_SIZE);
        firing = false;
        
        stepCount = 0;
    }
    
    /**
     * Creates an instance of a tower with the given class name. 
     * Returns null if the class or correct constructor cannot be found.
     */
    public static Tower createInstanceOf(String className, BoundedEnv env, 
                                       Location loc, ArrayList<Creep> targ)
    {
        try
        {
            //find constructor requiring environment, location, arraylist
            Class classToCreate = Class.forName("TowerDefense."+className);
            Class[] params = {env.getClass(), loc.getClass(), targ.getClass()};
            Constructor constructor = classToCreate.getConstructor(params);
            
            //create instance of object using found constructor
            Object[] argList = {env, loc, targ};
            return (Tower)constructor.newInstance(argList);
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }
    
    /**
     * Steps the tower. Will attack any creeps that get into 
     * its range.
     */
    public void step()
    {
        if (stepCount >= myDelay)
        {
            for (Creep c : myTargs) //check each creep
            {
                if (theEnv.distance(myLoc, c.loc()) <= myRng) //is in range
                {
                    //angle correction needed
                    myAngle = theEnv.angleRad(myLoc, c.loc());
                    
                    fire(c); //attack
                }
            }

            stepCount = 0;
        }
        else
        {
            stepCount++;
        }
    }
    
    /**
     * Fires the tower on the specified creep target.
     */
    protected void fire(Creep c)
    {
        c.hurt(myDmg); //damage creep

        //set location of explosion
        explosion.x = theEnv.doubleX(c.loc().x());
        explosion.y = theEnv.doubleY(c.loc().y());
        
        firing = true;
    }
    
    /**
     * Upgrades the Tower's damage and/or range, based on level.
     */
    public void upgrade()
    {
        if (myLevel < MAX_LEVEL)
        {
            myLevel++;
            if (myLevel == 2)
            {
                myDmg *= 1.25;
            }
            else if (myLevel == 3)
            {
                myDmg *= 1.5;
            }
            else if (myLevel == 4)
            {
                myDmg *= 1.5;
            }
            else if (myLevel == 5)
            {
                myDmg *= 2;
                myRng *= 2;
            }
            //set up image
            myImage = sprite.getSprite(IMAGE + myLevel + EXTENSION);
        }
    }
    
    /**
     * Returns the shape representation of the tower.
     */
    public Shape shape()
    {
        return new Rectangle2D.Double(myX, myY, myWidth, myHeight);
    }
    
    /**
     * Returns the name of the tower.
     */
    public String name()
    {
        return "Tower " + myLevel;
    }
    
    /**
     * Returns the description of the tower.
     */
    public String description()
    {
        return "Basic tower";
    }
    
    /**
     * Returns the image representation of the tower.
     */
    public Image image()
    {
        return myImage;
    }
    
    /**
     * Returns the cost of building/upgrading the tower.
     */
    public int cost()
    {
        if (myLevel == 0) //not built
            return 10;
        else if (myLevel == 1)
            return 20;
        else if (myLevel == 2)
            return 40;
        else if (myLevel == 3)
            return 70;
        else if (myLevel == 4)
            return 130;
        else
            return 0; //cannot be upgraded
    }
    
    /**
     * Returns whether or not this tower is a dummy.
     */
    public boolean isDummy()
    {
        return theEnv == null; //env will be null if it is a dummy
    }
    
    /**
     * Paints the tower onto the container if it is in the 
     * environment.
     */
    public void paintComponent(Graphics2D g2)
    {
        if (!theEnv.contains(this))
            return;
            
        double centerX = myX + myWidth/2;
        double centerY = myY + myHeight/2;
        g2.rotate(myAngle, centerX, centerY);
        super.paintComponent(g2);
        g2.rotate(-myAngle, centerX, centerY);
        
        if (firing)
        {
            g2.setColor(Color.red);
            g2.fill(explosion);
            firing = false;
        }
    }
    
    /**
     * Returns a string representation of the tower.
     */
    public String toString()
    {
        return this.getClass().getName() + ": " + myLoc;
    }
}