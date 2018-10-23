/**
 * NOTE: Created by some programmer from the website:
 *       http://www.planetalia.com/cursos/
 *       All I did was modify it.
 */

package SpaceInvaders;

public class Bullet extends Actor 
{
    //constants
    protected static final int BULLET_SPEED = 10;
    
    public Bullet(Stage stage)
    {
        super(stage);
        setSpriteNames(new String[] {DIRECTORY + "misil.gif"});
    }
    
    public void act()
    {
        if (!active)
            return;
            
        super.act();
        y -= BULLET_SPEED;
        if (y < 0)
            remove();
    }
}