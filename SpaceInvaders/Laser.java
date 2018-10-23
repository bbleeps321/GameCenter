/**
 * NOTE: Created by some programmer from the website:
 *       http://www.planetalia.com/cursos/
 *       All I did was modify it.
 */

package SpaceInvaders;

public class Laser extends Actor 
{
    //constants
    protected static final int BULLET_SPEED = 3;
    
    public Laser(Stage stage)
    {
        super(stage);
        setSpriteNames(new String[] {DIRECTORY + "disparo0.gif",
                                     DIRECTORY + "disparo1.gif",
                                     DIRECTORY + "disparo2.gif"});
        setFrameSpeed(10);
    }
    
    public void act()
    {
        if (!active)
            return;
            
        super.act();
        y += BULLET_SPEED;
        if (y > Stage.PLAY_HEIGHT)
            remove();
    }
}