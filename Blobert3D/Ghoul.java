package Blobert3D;

//import 
import java.util.Random;

/**
 * A Ghoul is just like a Ghost except it has a 1/20 chance 
 * of changing direction without needing to bounce off a wall.
 */
public class Ghoul extends Ghost
{
    //constants
    private static final String IMAGE = "blobert/ghoul.gif"; //ghoul image
    private static final double TURN_CHANCE = .05; //chance of turning
    
    /**
     * Creates a Ghoul at a random location inside the dimensions of the 
     * environment canvas.
     */
    public Ghoul(GameComponents.Canvas acanvas)
    {
        super(acanvas);
        myImage = sprite.getSprite(IMAGE);
    }
    
    /**
     * Steps this ghost's movement by one unit.
     */
    public void step()
    {
        super.step(); //step like normal
        
        //randomly move if needed
        Random rand = new Random();
        if (rand.nextDouble() < TURN_CHANCE)
        {
            dx = rand.nextInt(MAX_SPEED);
            dy = rand.nextInt(MAX_SPEED);
        }
    }
}