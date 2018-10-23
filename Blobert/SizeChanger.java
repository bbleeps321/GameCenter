package Blobert;

//import

/**
 * The blobert object that is controlled in the Blobert game. 
 * A SizeChanger can grow and shrink on command.
 */
public class SizeChanger extends Blobert
{
    //constants
    //colors when still or moving
    private static final double GROWTH = 1.5; //factor at which blob grows
    private static final int MAX_SIZE = 300; //maximum size of blob
    private static final int MIN_SIZE = 5; //minimum size of blob 
    
    public SizeChanger(double x, double y, GameComponents.Canvas acanvas)
    {
        super(x, y, acanvas);
    }
    
    /**
     * First ability of blobert. Dilates the blob up to the 
     * maximum size.
     */
    public void act1()
    {
        super.act1();
        mySize *= GROWTH;
        if (mySize > MAX_SIZE)
            mySize = MAX_SIZE;
            
        myWidth = mySize;
        myHeight = mySize;
    }
    
    /**
     * Second ability of blobert. Dilates the blob down to the 
     * minimum size.
     */
    public void act2()
    {
        super.act2();
        mySize *= 1/GROWTH;
        if (mySize < MIN_SIZE)
            mySize = MIN_SIZE;
            
        myWidth = mySize;
        myHeight = mySize;
    }
    
    /**
     * Resets the properties of the blobert to 
     * what they were initially.
     */
    public void resetBlobert()
    {
        super.resetBlobert();
        mySize = DEFAULT_SIZE;
        myWidth = mySize;
        myHeight = mySize;
    }
}
