/**
 * NOTE: Created by some programmer from the website:
 *       http://www.planetalia.com/cursos/
 *       All I did was modify it.
 */

package SpaceInvaders;

//import
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import GameComponents.Game;
import GameComponents.SoundCache;
import GameComponents.SpriteCache;

public class Invaders extends Game implements Stage, KeyListener
{
    //constants
    //directory of resources
    private static final String DIRECTORY = "SpaceInvaders/"; 
    private static final String[] SHIP_CHOICES = {"Normal",
                                                  "Speeder",
                                                  "Bomber"};
    
    private long usedTime;
    
    private SoundCache soundCache;
    private ArrayList<Actor> actors; 
    private Player player;
    private BufferedImage background, backgroundTile;
    private int backgroundY;
    
    private boolean gameEnded = false;
    private boolean active;
    
    private JComboBox shipSelect; //combo for selecting a ship
    
    public Invaders()
    {
        super(Stage.WIDTH, Stage.HEIGHT, DIRECTORY);
    }
    
    public void gameOver()
    {
        gameEnded = true;
    }
    
    public void initWorld()
    {
        actors = new ArrayList<Actor>();
        for (int i = 0; i < 10; i++)
        {
            Monster m = new Monster(this);
            m.setX((int)(Math.random() * Stage.WIDTH));
            m.setY(i * 20);
            m.setVx((int)(Math.random() * 20 - 10));
            
            actors.add(m);
        }
        
        String shipName = (String)shipSelect.getSelectedItem();
        if (shipName.equals("Normal"))
            shipName = "Player";
        player = Player.createInstanceOf(shipName, this);
        player.setX(Stage.WIDTH/2);
        player.setY(Stage.PLAY_HEIGHT - 2*player.getHeight());
        
        backgroundTile = getSprite("oceano.gif");
        background = sprite.createCompatible(Stage.WIDTH,  
                             Stage.HEIGHT + backgroundTile.getHeight(),
                             Transparency.OPAQUE);
                             
        Graphics2D g = (Graphics2D)background.getGraphics();
        g.setPaint(new TexturePaint(backgroundTile, 
                                    new Rectangle(0, 0, 
                                    backgroundTile.getWidth(), 
                                    backgroundTile.getHeight())));
        g.fillRect(0, 0, background.getWidth(), background.getHeight());
        backgroundY = backgroundTile.getHeight();
    
    }
    
    public void addActor(Actor a)
    {
        actors.add(a);
    }   
    
    public Player getPlayer()
    {
        return player;
    }
    
    public void updateWorld()
    {
        int i = 0;
        while (i < actors.size())
        {
            Actor m = actors.get(i);
            if (m.isMarkedForRemoval())
            {
                actors.remove(i);
            }
            else 
            {
                m.act();
                i++;
            }
        }
        player.act();
    }
    
    public void checkCollisions()
    {
        Rectangle playerBounds = player.getBounds();
        for (int i = 0; i < actors.size(); i++)
        {
            Actor a1 = (Actor)actors.get(i);
            Rectangle r1 = a1.getBounds();
            if (r1.intersects(playerBounds))
            {
                player.collision(a1);
                a1.collision(player);
            }
            for (int j = i + 1; j < actors.size(); j++)
            {
                Actor a2 = (Actor)actors.get(j);
                Rectangle r2 = a2.getBounds();
                if (r1.intersects(r2))
                {
                    a1.collision(a2);
                    a2.collision(a1);
                }
            }
        }
    }
    
    public void paintShields(Graphics2D g)
    {
        g.setPaint(Color.red);
        g.fillRect(280, Stage.PLAY_HEIGHT, player.MAX_SHIELDS, 30);
        
        g.setPaint(Color.blue);
        g.fillRect(280 + player.MAX_SHIELDS - player.getShields(),
                    Stage.PLAY_HEIGHT, player.getShields(), 30);
                    
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setPaint(Color.green);
        g.drawString("Shields", 170, Stage.PLAY_HEIGHT + 20);
    }
    
    public void paintScore(Graphics2D g)
    {
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setPaint(Color.green);
        g.drawString("Score:", 20, Stage.PLAY_HEIGHT + 20);
        
        g.setPaint(Color.red);
        g.drawString(player.getScore() + "", 100, Stage.PLAY_HEIGHT + 20);
    }
    
    public void paintAmmo(Graphics2D g)
    {
        int xBase = 280 + player.MAX_SHIELDS + 10;
        for (int i = 0; i < player.getClusterBombs(); i++)
        {
            BufferedImage bomb = getSprite("bombUL.gif");
            g.drawImage(bomb, xBase + i*bomb.getWidth(), Stage.PLAY_HEIGHT, this);
        }
    }
    
    public void paintfps(Graphics2D g)
    {
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.setColor(Color.white);
        if (usedTime > 0)
            g.drawString(String.valueOf(1000/usedTime) + " fps",
                            Stage.WIDTH - 50, Stage.PLAY_HEIGHT);
        else
            g.drawString("--- fps", Stage.WIDTH - 50, Stage.PLAY_HEIGHT);
    }
    
    
    public void paintStatus(Graphics2D g)
    {
        paintScore(g);
        paintShields(g);
        paintAmmo(g);
        paintfps(g);  
    }
    
    public void paintWorld(Graphics2D g) 
    {
        g.drawImage(background, 0, 0, Stage.WIDTH, Stage.HEIGHT, 0, backgroundY,
                     Stage.WIDTH, backgroundY + Stage.HEIGHT, this);
                     
        for (int i = 0; i < actors.size(); i++)
        {
            Actor m = actors.get(i);
            m.paint(g);
        }
        player.paint(g);

        paintStatus(g);
    }
    
    public void paintGameOver(Graphics2D g)
    {
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("GAME OVER", Stage.WIDTH/2 - 50, Stage.HEIGHT/2);
    }
    
    public SpriteCache getSpriteCache()
    {
        return sprite;
    }
    
    public SoundCache getSoundCache()
    {
        return sound;
    }
    
    public void keyPressed(KeyEvent e)
    {
        player.keyPressed(e);
    }
    
    public void keyReleased(KeyEvent e)
    {
        player.keyReleased(e);
    }
    
    public void keyTyped(KeyEvent e) {}
    
    /************************************************************************/
    /********************************Made by me******************************/
    /************************************************************************/
    
    /**
     * Returns the panel containing the option components.
     */
    public JPanel getOptionsPanel()
    {
        //create components for dialog 
        shipSelect = new JComboBox(SHIP_CHOICES);
        shipSelect.setSelectedIndex(0);
        Border emptyBorder = BorderFactory.createEmptyBorder(GAP,GAP,GAP,GAP);
        shipSelect.setBorder(BorderFactory.createTitledBorder(emptyBorder, 
                                "Choose Your Ship "));
        
        //place in panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(150,60));
        panel.add(shipSelect, BorderLayout.CENTER);
        panel.add(startButton, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Initializes the gameplay components.
     */
    protected void initializeGameplayComponents()
    {
        setBounds(0, 0, Stage.WIDTH, Stage.HEIGHT);
        
        setIgnoreRepaint(true);
        
        active = true;
        initWorld();
        gameEnded = false;
    }
    
    /**
     * Runs the game process.
     */
    protected void runProcess()
    {
        if (!gameEnded) //game not over
        {
            long startTime = System.currentTimeMillis();
            backgroundY--;
            if (backgroundY < 0)
                backgroundY = backgroundTile.getHeight();
                
            updateWorld();
            checkCollisions();
            
            usedTime = System.currentTimeMillis() - startTime;
            
            if (player.getShields() <= 0)
            {
                gameOver();
            }
        }
    }
    
    /**
     * Required by abstract class Game. Continues the game functions.
     */
    public void startGame()
    {
        startTimer();
        setActive(true);
    }
    
    /**
     * Required by abstract class Game. Pauses the game functions.
     */
    public void pauseGame()
    {
        stopTimer();
        setActive(false);
    }
    
    /**
     * Required by abstract class Game. Returns the name of the game.
     */
    public String name()
    {
        return "Space Invaders";
    }
    
    /**
     * Required by abstract class Game. Returns the description of the game.
     */
    public String description()
    {
        return "Defend the earth from the space invaders!";
    }
    
    public void setActive(boolean newVal)
    {
        active = newVal;
        player.setActive(active);
        for (Actor actor : actors)
            actor.setActive(active);
    }
    
    /**
     * Returns whether or not this game can be scored.
     */
    public boolean isScorable()
    {
        return true;
    }
    
    /**
     * Paints the game onto the container.
     */
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        
        paintWorld(g2);
        
        for (Actor actor : actors)
            actor.paint(g2);
            
        //paint player last so it is on top
        player.paint(g2);
        
        //check if should paint game over
        if (gameEnded)
        {
            paintGameOver(g2);
            submitScore(player.getScore());
        }
    }
    
    /**
     * Runs the game.
     */
    public static void main(String[] args)
    {
        Game gui = new Invaders();
        JFrame frame = new JFrame(gui.name());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(150, 120));
        
        frame.setContentPane(gui.getOptionsPanel());
        
        frame.pack();
        frame.setVisible(true);
    }
}