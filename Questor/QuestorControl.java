package Questor;

// import
import GameComponents.Game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;

/**
 * Controller for the Questor game.
 */
public class QuestorControl extends Game
{
    //constants
    private static final String DIRECTORY = "questor/"; //where game resources are
    private static final int WIDTH = 500; //width of screen
    private static final int HEIGHT = 500; //height of screen
    
    /**
     * Initializes the game.
     */
    public QuestorControl()
    {
        super(WIDTH, HEIGHT, DIRECTORY);
    }
    
    /**
     * Required by KeyListener. Handles keyboard input 
     * for controlling the hero.
     */
    public void keyPressed(KeyEvent e)
    {
        
    }
    
    /**
     * Initializes the gameplay components.
     */
    protected void initializeGameplayComponents()
    {
        
    }
    
    /**
     * Paints the game to the container.
     */
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        
        // paint background
        g2.setColor(Color.black);
        g2.fill(g2.getClipBounds());
    }
    
    /**
     * Description of game.
     */
    public String description()
    {
        return "Embark on a quest through a fantastical world while battling enemies!";
    }
    
    /**
     * Name of game.
     */
    public String name()
    {
        return "Questor";
    }
    
    /**
     * Game is not scorable.
     */
    public boolean isScorable()
    {
        return false;
    }
    
    /**Runs the game process. Not needed for this game.**/
    protected void runProcess() {}
    
    /**
     * Runs the game.
     */
    public static void main(String[] args)
    {
        Game gui = new QuestorControl();
        JFrame frame = new JFrame(gui.name());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(150, 120));
        
        frame.setContentPane(gui.getOptionsPanel());
        
        frame.pack();
        frame.setVisible(true);
    }
}