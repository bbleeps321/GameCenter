/**
 * NOTE: Created by some programmer from the website:
 *       http://www.planetalia.com/cursos/
 *       All I did was modify it.
 */

package SpaceInvaders;

//import
import java.awt.event.KeyEvent;
import java.lang.reflect.Constructor;

public class Player extends Actor 
{
    //constants
    public static final int MAX_SHIELDS = 200;
    public static final int MAX_BOMBS = 5;
    protected static final int PLAYER_SPEED = 4;
    
    protected int vx;
    protected int vy;
    private boolean up,down,left,right;
    private int clusterBombs; 
    private int score;
    private int shields;
    private int mySpeed;
    
    
    public Player (Stage stage)
    {
        super(stage);
        setSpriteNames(new String[] {DIRECTORY + "nave.gif"});
        clusterBombs = MAX_BOMBS;
        shields = MAX_SHIELDS;
        score = 0;
        mySpeed = PLAYER_SPEED;
    }
    
    /**
     * Made by me.
     * Allows for subclasses to specify an amount of cluster bombs, 
     * and speed
     */
    protected Player(Stage stage, int bombNum, int speed)
    {
        super(stage);
        setSpriteNames(new String[] {DIRECTORY + "nave.gif"});
        clusterBombs = bombNum;
        shields = MAX_SHIELDS;
        score = 0;
        mySpeed = speed;
    }
    
    /**
     * Made by me.
     * Creates an instance of a blobert with the class name. 
     * Returns null if the class or correct constructor cannot be found.
     */
    public static Player createInstanceOf(String className, Stage stage)
    {
        try
        {
            //find constructor requiring Stage
            Class classToCreate = Class.forName("SpaceInvaders."+className);
            Class[] params = {Class.forName("SpaceInvaders.Stage")};
            Constructor constructor = classToCreate.getConstructor(params);
            
            //create instance of object using found constructor
            Object[] argList = {stage};
            return (Player)constructor.newInstance(argList);
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }
    
    public void act()
    {
        if (!active)
            return;
            
        super.act();
        x += vx;
        y += vy;
        if (x < 0) 
            x = 0;
        if (x > Stage.WIDTH - getWidth())
            x = Stage.WIDTH - getWidth();
        if (y < 0 )
            y = 0;
        if (y > Stage.PLAY_HEIGHT - getHeight())
            y = Stage.PLAY_HEIGHT - getHeight();
    }

    public int getVx()
    {
        return vx;
    }
    
    public void setVx(int i)
    {
        vx = i;
    }
    
    public int getVy()
    {
        return vy;
    }
    
    public void setVy(int i)
    {
        vy = i;
    }
    
    protected void updateSpeed()
    {
        vx=0;
        vy=0;
        if (down)
            vy = mySpeed;
        if (up)
            vy = -mySpeed;
        if (left)
            vx = -mySpeed;
        if (right)
            vx = mySpeed;
    }
  
    public void keyReleased(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_DOWN : 
            {
                down = false;
                break;
            }
            case KeyEvent.VK_UP : 
            {
                up = false; 
                break;
            }
            case KeyEvent.VK_LEFT : 
            {
                left = false; 
                break; 
            }
            case KeyEvent.VK_RIGHT : 
            {
                right = false;
                break;
            }
        }
        updateSpeed();
    }
    
    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_DOWN : 
            {
                down = true;
                break;
            }
            case KeyEvent.VK_UP : 
            {
                up = true; 
                break;
            }
            case KeyEvent.VK_LEFT : 
            {
                left = true; 
                break; 
            }
            case KeyEvent.VK_RIGHT : 
            {
                right = true;
                break;
            }
            case KeyEvent.VK_SPACE : 
            {
                fire(); 
                break;
            }
            case KeyEvent.VK_A : 
            {
                fireCluster(); 
                break;
            }
        }
        updateSpeed();
    }
  
    public void fire()
    {
        Bullet b = new Bullet(stage);
        b.setX(x);
        b.setY(y - b.getHeight());
        stage.addActor(b);
        stage.getSoundCache().playSound(DIRECTORY + "missile.wav");
    }
    
    public void fireCluster()
    {
        if (clusterBombs == 0)
            return;
        
        clusterBombs--;
        stage.addActor(new Bomb(stage, Bomb.UP_LEFT, x, y));
        stage.addActor(new Bomb(stage, Bomb.UP, x, y));
        stage.addActor(new Bomb(stage, Bomb.UP_RIGHT, x, y));
        stage.addActor(new Bomb(stage, Bomb.LEFT, x, y));
        stage.addActor(new Bomb(stage, Bomb.RIGHT, x, y));
        stage.addActor(new Bomb(stage, Bomb.DOWN_LEFT, x, y));
        stage.addActor(new Bomb(stage, Bomb.DOWN, x, y));
        stage.addActor(new Bomb(stage, Bomb.DOWN_RIGHT, x, y));
    }
  
    public int getScore()
    {
        return score;
    }
    
    public void setScore(int i)
    {
        score = i;
    }
    
    public void addScore(int i)
    {
        score += i;
    }

    public int getShields()
    {
        return shields;
    }
    
    public void setShields(int i)
    {
        shields = i;
    }
    
    public void addShields(int i)
    {
        shields += i;
        if (shields > MAX_SHIELDS)
            shields = MAX_SHIELDS;
    }
    
    public void collision(Actor a)
    {
        if (a instanceof Monster)
        {
            a.remove();
            addScore(40);
            addShields(-40);
        }
        
        if (a instanceof Laser )
        {
            a.remove();
            addShields(-10);
        }
        
        if (getShields() < 0)
            stage.gameOver();
    }   

    public int getClusterBombs()
    {
        return clusterBombs;
    }
    
    public void setClusterBombs(int i)
    {
        clusterBombs = i;
    }
}