/**
 * NOTE: Created by some programmer from the website:
 *       http://www.planetalia.com/cursos/
 *       All I did was modify it.
 */

package SpaceInvaders;

//import
import java.awt.image.ImageObserver;

import GameComponents.SoundCache;
import GameComponents.SpriteCache;

public interface Stage extends ImageObserver
{
    //cosntants
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int PLAY_HEIGHT = 500; 
    public static final int SPEED = 10;
    
    public SpriteCache getSpriteCache();
    
    public SoundCache getSoundCache();
    
    public void addActor(Actor a);
    
    public Player getPlayer();
    
    public void gameOver();
}