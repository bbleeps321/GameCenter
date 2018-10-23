/**
 * NOTE: Created by some programmer from the website:
 *       http://www.planetalia.com/cursos/
 *       All I did was modify it.
 */

package SpaceInvaders;

//import
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import GameComponents.SpriteCache;

public class Actor 
{
    //constants
    //directory of Space Invaders resources
    protected static final String DIRECTORY = "SpaceInvaders/"; 
    
    protected int x,y;
    protected int width, height;
    protected String[] spriteNames;
    protected int currentFrame;
    protected int frameSpeed;
    protected int t;
    protected Stage stage;
    protected SpriteCache spriteCache;
    protected boolean markedForRemoval;
    protected boolean active;
    
    public Actor(Stage stage)
    {
        this.stage = stage;
        spriteCache = stage.getSpriteCache();
        currentFrame = 0;
        frameSpeed = 1;
        t = 0;
        active = true;
    }
    
    public void remove()
    {
        markedForRemoval = true;
    }
    
    public boolean isMarkedForRemoval()
    {
        return markedForRemoval;
    }
    
    public void paint(Graphics2D g)
    {
        g.drawImage(spriteCache.getSprite(spriteNames[currentFrame]), 
                            x, y, stage);
    }
    
    public int getX() 
    {
        return x;
    }
    
    public void setX(int i)
    {
        x = i;
    }
        
    public int getY()
    {
        return y;
    }
    
    public void setY(int i)
    {
        y = i;
    }
    
    public int getFrameSpeed()
    {
        return frameSpeed;
    }
    
    public void setFrameSpeed(int i)
    {
        frameSpeed = i;
    }
    
    public void setSpriteNames(String[] names)
    { 
        spriteNames = names;
        height = 0;
        width = 0;
        for (int i = 0; i < names.length; i++)
        {
            BufferedImage image = spriteCache.getSprite(spriteNames[i]);
            height = Math.max(height, image.getHeight());
            width = Math.max(width, image.getWidth());
        }
    }           
    
    public int getHeight()
    {
        return height;
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public void setHeight(int i)
    {
        height = i;
    }
    
    public void setWidth(int i)
    {
        width = i;
    }

    public void act()
    {
        if (!active)
            return;
            
        t++;
        if (t % frameSpeed == 0)
        {
            t=0;
            currentFrame = (currentFrame + 1) % spriteNames.length;
        }
    }
    
    public Rectangle getBounds()
    {
        return new Rectangle(x, y, width, height);
    }
    
    public void collision(Actor a)
    {
        
    }
    
    public void setActive(boolean newVal)
    {
        active = newVal;
    }
}