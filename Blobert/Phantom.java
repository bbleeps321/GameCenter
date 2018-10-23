package Blobert;

//import 

/**
 * A Phantom is just like a Ghost except it makes any bloberts 
 * who hit it lose two lives instead of one. A Phantom is 
 * represented by a medium sea green rectangle.
 */
public class Phantom extends Ghost
{
    //constants
    private static final String IMAGE = "blobert/phantom.gif"; //phantom image
    
    /**
     * Creates a Phantom at a random location inside the dimensions of the 
     * environment canvas.
     */
    public Phantom(GameComponents.Canvas acanvas)
    {
        super(acanvas);
        myImage = sprite.getSprite(IMAGE);
    }
    
    /**
     * Returns how many lives any blobert who is hit by this ghost 
     * will lose.
     */
    public int damageEffect()
    {
        return 2;
    }
}