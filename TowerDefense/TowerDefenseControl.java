package TowerDefense;

//import
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import GameComponents.BoundedEnv;
import GameComponents.Game;
import GameComponents.Location;

/**
 * Controls the Tower Defense game and the components and 
 * functions associated with it (creeps, towers, score, etc.). 
 * The stage is read in through a file of specific parameters.
 */
public class TowerDefenseControl extends Game
{
    //constants
    private static final String DIRECTORY = "TowerDefense/"; //directory
    private static final String FIELD_DIRECTORY = DIRECTORY + "Fields/";
    private static final String FILE_EXTENSION = ".txt";
    
    //symbols for interpreting field text files
    private static final String START_SYMBOL = "S";
    private static final String END_SYMBOL = "E";
    private static final String PATH_SYMBOL = "P";
    private static final String BLOCKED_SYMBOL = "X";
    private static final String OPEN_SYMBOL = "O";
    
    private static final int WIDTH_WHOLE = 700; //width of game frame
    private static final int WIDTH = 500; //width of game canvas
    private static final int HEIGHT = 500; //height of game frame
    private static final int NUM_PER_WAVE = 5; //number of creeps per wave
    private static final int INIT_LIVES = 10; //initial number of lives
    private static final int INIT_GOLD = 100; //initial amount of gold
    
    private static final Integer[] DELAY_CHOICES = {20, 15, 10, 5}; //time delay
    private static final String[] CREEPS = {"Creep", //types of creeps
                                            "Fast",
                                            "Tank"};
    private static final String[] TOWERS = {"Tower", //types of towers
                                            "Sniper",
                                            "Rapid"}; 
                                                   
    //for options panel
    private JComboBox fieldCombo; //combo for selecting fields
    private JComboBox delayCombo; //combo for selecting delay between waves
    
    private BoundedEnv theEnv; //environment game takes place in
    private ArrayList<Creep> creeps; //list of all creeps on the field
    private ArrayList<Tower> myTowers; //list of all towers place by player
    private ArrayList<Location> path; //list of locations making up creep path
                                      //first and last are start and end
                                      //locations in between not in order
    private Tower selectedTower; //the tower currently selected (null if none)
    private CommandBar myBar; //command/unit view bar
    
    private double waveGap; //standard time gap between each wave in ms
    private int waveCount; //counter for how many timer events since last wave
    private int creepsToSend; //how many creeps to send remaining in this wave
    private String creepType; //the creep type in the current wave
    private boolean started; //if the waves have started
    
    private int waveNum; //number of total waves
    private int myScore; //score of player
    private int myGold; //gold owned by player
    private int lives; //number of lives remaining
    private boolean over; //if the game is over

    /**
     * Creates the game.
     */
    public TowerDefenseControl()
    {
        super(WIDTH_WHOLE, HEIGHT, DIRECTORY);
    }
    
    /**
     * Fired when a mouse clicked event is invokes. Places the tower 
     * at the mouse's location if able.
     */
    public void mouseClicked(MouseEvent e)
    {
        //find cell mouse is in
        int locX = theEnv.cellX(e.getX());
        int locY = theEnv.cellY(e.getY());
        Location mouseLoc = new Location(locX, locY);
        
        if (myBar.mouseClicked(e)) //event was over command bar
            return;
        
        //select the tower/creep if the one is there
        if (!theEnv.isEmpty(mouseLoc)) //location has something
        {
            if (theEnv.objectAt(mouseLoc) instanceof Tower) //is tower
            {
                selectedTower = (Tower)theEnv.objectAt(mouseLoc);
                myBar.setUnit(selectedTower);
            }
        }
        else if (path.contains(mouseLoc)) //location is on the path
        {
            return;
        }
        else //empty, try to place tower
        {
            String selected = myBar.getSelected();
            if (selected != null) //type selected
            {
                int cost = myBar.selectedCost();
                if (myGold - cost >= 0) //can afford it
                {
                    myTowers.add(Tower.createInstanceOf(selected, theEnv, 
                                        mouseLoc, creeps));
                    myGold -= cost;
                }
            }
        }
    }
    
    /**
     * Fired when a key pressed event is invoked. Send the next 
     * wave if the 'N' button is pressed
     */
    public void keyPressed(KeyEvent e)
    {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_N)
        {
            sendNextWave();
            started = true;
        }
        else if (keyCode == KeyEvent.VK_U)
        {
            if (selectedTower != null) //tower selected
            {
                int cost = selectedTower.cost();
                if (myGold - cost >= 0 ) //can afford it
                {
                    selectedTower.upgrade();
                    myGold -= cost;
                }
            }
        }
    }
    
    /**
     * Initializes the game components.
     */
    protected void initializeGameplayComponents()
    {
        //initializes the environment and path
        readFieldFile();
                                     
        //create list of creeps (empty)
        creeps = new ArrayList<Creep>();

        waveGap = ((Integer)delayCombo.getSelectedItem())*1000; //to ms
        
        //create empty list of player's towers
        myTowers = new ArrayList<Tower>();
        
        //create command bar
        double barWidth = WIDTH_WHOLE - WIDTH;
        myBar = new CommandBar(TOWERS, WIDTH_WHOLE - barWidth, barWidth, HEIGHT);
        
        waveCount = 0;
        waveNum = 0;
        myGold = INIT_GOLD;
        started = false;
        lives = INIT_LIVES;
        
        //set cursor
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    /**
     * Runs the game process.
     */
    protected void runProcess()
    {
        if (started)
        {
            //check if time to send next wave
            if (waveCount*DELAY >= waveGap)
            {
                sendNextWave();
            }
            else
            {
                waveCount++;
            }

            //step all creeps
            for (Creep c : creeps)
                c.step();
                
            //step all towers
            for (Tower t : myTowers)
                t.step();
            
            //check if need to send another creep
            Location start = path.get(0); //first in path is always start
            if (creepsToSend > 0 && theEnv.isEmpty(start))
            {
                Creep c = Creep.createInstanceOf(creepType, theEnv, 
                                                start, path, 1);
                c.setHP(c.baseHP()*(1 + waveNum));
                creeps.add(c);
                
                creepsToSend--;
            }
            
            //remove old creeps
            removeOldCreeps();
            
            //end game if all lives are gone
            if (lives <= 0)
                over = true;
        }
    }
    
    /**
     * Creates and sends the next wave.
     */
    private void sendNextWave()
    {
        if (waveCount*DELAY < waveGap) //wave sent early
            myScore += waveNum*2;
            
        waveNum++;
        Random rand = new Random();
        int index = rand.nextInt(CREEPS.length);
        creepType = CREEPS[index];
        creepsToSend = NUM_PER_WAVE; //set number left to send
        
        //create the first in wave
        Location start = path.get(0); //first in path is always start
        
        Creep c = Creep.createInstanceOf(creepType, theEnv, start, path, 1);
        c.setHP(c.baseHP()*(waveNum));
        creeps.add(c);
        creepsToSend--;
        
        waveCount = 0; //reset
    }
    
    /**
     * Removes the old creeps (one's that are no longer in the 
     * environment) from the list of creeps.
     */
    private void removeOldCreeps()
    {
        for (int i = 0; i < creeps.size(); i++)
        {
            if (!theEnv.contains(creeps.get(i)))
            {
                //subtract a life if the creep reached the end
                if (creeps.get(i).loc().equals(path.get(path.size() - 1)))
                {
                    lives--;
                }
                else //creep was killed
                {
                    myScore += waveNum + creeps.get(i).cost()*2;
                    myGold += creeps.get(i).cost();
                }
                
                creeps.remove(i);

                i--;
            }
        }
    }
    
    /**
     * Returns a panel of options for the game. Contains 
     * a combobox for difficulty setting and one for time 
     * between each wave.
     */
    public JPanel getOptionsPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        
        Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
        
        try
        {
            Scanner in = new Scanner(new FileInputStream(FIELD_DIRECTORY + 
                                                            "fields.txt"));
            String[] fieldList = readWholeFile(in);
            
            //initialize components
            fieldCombo = new JComboBox(fieldList);
            fieldCombo.setSelectedIndex(0);
            fieldCombo.setEditable(false);
            fieldCombo.setBorder(BorderFactory.createTitledBorder(emptyBorder, 
                                                         "Field Select"));
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e);
        }
        finally
        {
            //initialize components
            delayCombo = new JComboBox(DELAY_CHOICES);
            delayCombo.setSelectedIndex(0);
            delayCombo.setEditable(false);
            delayCombo.setBorder(BorderFactory.createTitledBorder(emptyBorder, 
                                                 "Wave Interval (sec)"));
            
            panel.add(fieldCombo, BorderLayout.NORTH);
            panel.add(delayCombo, BorderLayout.CENTER);
            panel.add(startButton, BorderLayout.SOUTH);
            
            return panel;
        }
    }
    
    /**
     * Returns the name of the game.
     */
    public String name()
    {
        return "Tower Defense";
    }
    
    /**
     * Returns the description of the game.
     */
    public String description()
    {
        return "Prevent the creeps from crossing to the other side!";
    }
    
    /**
     * Returns whether or not this game can be scored.
     */
    public boolean isScorable()
    {
        return true;
    }
    
    /**
     * Reads the field file.
     */
    private void readFieldFile()
    {
        try
        {
            String selectedFile = (String)fieldCombo.getSelectedItem();
            Scanner in = new Scanner(new FileInputStream(FIELD_DIRECTORY + 
                                                        selectedFile + 
                                                        FILE_EXTENSION));
            
            //read dimensions
            int width = nextIntInFile(in);
            int height = nextIntInFile(in);
            
            //initialize environment
            theEnv = new BoundedEnv(width, height, WIDTH/width, HEIGHT/height);
            
            //read path
            path = new ArrayList<Location>();
            
            String[][] field = readWholeFileToken(in);
            Location end = null;
            for (int i = 0; i < field.length; i++)
            {
                for (int j = 0; j < field[i].length; j++)
                {
                    if (field[i][j].equals(PATH_SYMBOL))
                        path.add(new Location(i,j));
                    else if (field[i][j].equals(START_SYMBOL))
                        path.add(0, new Location(i,j)); //start at beginning
                    else if (field[i][j].equals(END_SYMBOL))
                        end = new Location(i,j);
                }
            }
            path.add(end); //place end loc at end of list
        }
        catch (IOException e) {}
    }
    
    /**
     * Returns the next integer in the file with the specified scanner.
     */
    private int nextIntInFile(Scanner in)
    {
        while (!in.hasNextInt())
        {
            in.next();
        }
        return in.nextInt();
    }
    
    /**
     * Reads the file using the specified scanner and returns 
     * each seperate token in a string[][].
     */
    private String[][] readWholeFileToken(Scanner in)
    {
        String[][] tokens = new String[theEnv.height()][theEnv.width()];
        
        for (int i = 0; i < tokens.length; i++)
            for (int j = 0; j < tokens[i].length; j++)
                tokens[j][i] = in.next();
        
        return tokens;
    }
    
    /**
     * Reads the whole file, returning each line in a string as 
     * part of an array.
     */
    private String[] readWholeFile(Scanner in)
    {
        ArrayList<String> linesList = new ArrayList<String>();
        while (in.hasNextLine())
            linesList.add(in.nextLine());
            
        //copy to array
        String[] lines = new String[linesList.size()];
        for (int i = 0; i < lines.length; i++)
            lines[i] = linesList.get(i);
            
        return lines;
    }
    
    /**
     * Paints the game onto the container.
     */
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        
        //paint background white
        g2.setColor(Color.white);
        g2.fill(g2.getClipBounds());
        
        //paint score displays
        g2.setColor(Color.red);
        g2.drawString("Wave: " + waveNum, GAP, 3*GAP); //wave
        g2.drawString("Gold: " + myGold, WIDTH/3 - 15, 3*GAP); //gold
        g2.drawString("Score: " + myScore, 2*WIDTH/3 - 15, 3*GAP); //score
        g2.drawString("Lives: " + lives, WIDTH - 60, 3*GAP); //lives
            
        //paint player's towers
        for (Tower t : myTowers)
            t.paintComponent(g2);
            
        //paint path
        g2.setColor(Color.lightGray);
        for (Location loc : path)
        {
            double x = theEnv.doubleX(loc.x());
            double y = theEnv.doubleY(loc.y());
            Rectangle2D.Double r = new Rectangle2D.Double(x, y, 
                                    theEnv.cellWidth(), theEnv.cellHeight());
            g2.fill(r);
        }
            
        //paint creeps
        for (Creep c : creeps)
            c.paintComponent(g2);
            
        //paint command bar
        myBar.paintComponent(g2);
        
        if (over) //game over
        {
            g2.setColor(Color.red);
            g2.setFont(new Font("", Font.BOLD, 40));
            g2.drawString("Game Over", WIDTH/2 - 100, HEIGHT/2 - 10);
            pauseGame(); //stop game actions
            submitScore(myScore);
        }
    }
    
    /**
     * Runs the game.
     */
    public static void main(String[] args)
    {
        Game gui = new TowerDefenseControl();
        JFrame frame = new JFrame(gui.name());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(150, 155));
        
        frame.setContentPane(gui.getOptionsPanel());
        
        frame.pack();
        frame.setVisible(true);
    }
}