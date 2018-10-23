package GameComponents;

//import
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * A Shape2D object that implements the Locatable interface, 
 * allowing it to be placed into an environment.
 */
public abstract class Shape2DLocatable extends Shape2D implements Locatable
{
    protected BoundedEnv theEnv; //environment object is in
    protected Location myLoc; //location of object
    
    /**
     * Creates a Shape2DLocatable at the specified location in the 
     * given environment.
     */
    public Shape2DLocatable(BoundedEnv env, Location loc)
    {
        super(0, 0, 0, 0); //values calculated later
        
        theEnv = env;
        myLoc = loc;
        
        //confirm that parameters are not null
        if (theEnv != null && myLoc != null)
        {
            //calculate coordinates
            myX = theEnv.doubleX(myLoc.x());
            myY = theEnv.doubleY(myLoc.y());
            
            //calculate dimensions
            myWidth = theEnv.cellWidth();
            myHeight = theEnv.cellHeight();
            
            theEnv.add(this);
        }
    }
    
    /**
     * Creates a Shape2DLocatable at the specified location in the 
     * given environment with the given color
     */
    public Shape2DLocatable(BoundedEnv env, Location loc, Color col)
    {
        super(0, 0, 0, 0, col); //values calculated later
        
        theEnv = env;
        myLoc = loc;
        
        //confirm that parameters are not null
        if (theEnv != null && myLoc != null)
        {
            //calculate coordinates
            myX = theEnv.doubleX(myLoc.x());
            myY = theEnv.doubleY(myLoc.y());
            
            //calculate dimensions
            myWidth = theEnv.cellWidth();
            myHeight = theEnv.cellHeight();
            
            theEnv.add(this);
        }
    }
    
    /**
     * Creates a Shape2DLocatable at the specified location in the 
     * given environment with the given image name
     */
    public Shape2DLocatable(BoundedEnv env, Location loc, String imageName)
    {
        super(0, 0, 0, 0, imageName); //values calculated later
        
        theEnv = env;
        myLoc = loc;
        
        //confirm that parameters are not null
        if (theEnv != null && myLoc != null)
        {
            //calculate coordinates
            myX = theEnv.doubleX(myLoc.x());
            myY = theEnv.doubleY(myLoc.y());
            
            //calculate dimensions
            myWidth = theEnv.cellWidth();
            myHeight = theEnv.cellHeight();
            
            theEnv.add(this);
        }
    }
    
    /**
     * Returns the location of the object.
     */
    public Location loc()
    {
        return myLoc;
    }
    
    /**
     * Changes the location of the object and updates its 
     * location in the environment.
     */
    public void changeLocation(Location loc)
    {
        Location oldLoc = loc();
        myLoc = loc;
        myX = theEnv.doubleX(loc.x());
        myY = theEnv.doubleY(loc.y());
        theEnv.recordMove(this, oldLoc);
    }
    
    /**
     * Paints the object onto the container only if it is still 
     * in the environment.
     */
    public void paintComponent(Graphics2D g2)
    {
        if (theEnv.contains(this))
            super.paintComponent(g2);
    }
}