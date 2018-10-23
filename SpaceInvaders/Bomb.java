/**
 * NOTE: Created by some programmer from the website:
 *       http://www.planetalia.com/cursos/
 *       All I did was modify it.
 */

package SpaceInvaders;

public class Bomb extends Actor 
{
    //constants
    public static final int UP_LEFT = 0;
    public static final int UP = 1;
    public static final int UP_RIGHT = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;
    public static final int DOWN_LEFT = 5;
    public static final int DOWN = 6;
    public static final int DOWN_RIGHT = 7;
    
    protected static final int BOMB_SPEED = 5; 
    protected int vx;
    protected int vy;
    
    public Bomb(Stage stage, int heading, int x, int y)
    {
        super(stage);
        this.x = x;
        this.y = y;
        String sprite = "";
        switch (heading) 
        {
            case UP_LEFT : 
            {
                vx = -BOMB_SPEED;
                vy = -BOMB_SPEED;
                sprite = DIRECTORY + "bombUL.gif";
                break;
            }
            case UP : 
            {
                vx = 0; 
                vy = -BOMB_SPEED; 
                sprite = DIRECTORY + "bombU.gif";
                break;
            }
            case UP_RIGHT :
            {
                vx = BOMB_SPEED;
                vy = -BOMB_SPEED;
                sprite=DIRECTORY + "bombUR.gif";
                break;
            }
            case LEFT : 
            {
                vx = -BOMB_SPEED; 
                vy = 0; 
                sprite = DIRECTORY + "bombL.gif";
                break;
            }
            case RIGHT : 
            {
                vx = BOMB_SPEED; 
                vy = 0; 
                sprite = DIRECTORY + "bombR.gif";
                break;
            }
            case DOWN_LEFT : 
            {
                vx = -BOMB_SPEED; 
                vy = BOMB_SPEED; 
                sprite = DIRECTORY + "bombDL.gif";
                break;
            }
            case DOWN : 
            {
                vx = 0; 
                vy = BOMB_SPEED; 
                sprite = DIRECTORY + "bombD.gif";
                break;
            }
            case DOWN_RIGHT : 
            {
                vx = BOMB_SPEED; 
                vy = BOMB_SPEED; 
                sprite = DIRECTORY + "bombDR.gif";
                break;
            }
        }   
        setSpriteNames(new String[] {sprite});
    }
    
    public void act()
    {
        if (!active)
            return;
            
        super.act();
        y += vy;
        x += vx;
        if ((y < 0) || (y > Stage.HEIGHT) || (x < 0) || (x > Stage.WIDTH))
            remove();
    }
}