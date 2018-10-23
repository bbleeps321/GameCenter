package GameComponents;

//import
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

/**
 * Abstract class for all games to be used in the Game Center.
 * Displays a new frame for the game. Handles the updating of  
 * the UI when the protected method startTimer has been invoked. 
 * Handles window events (game loses window focus). Forces sub-
 * classes to have numerous methods for compatibility with Game
 * Center.
 */
public abstract class Game extends JPanel implements ActionListener, 
                                                     WindowListener,
                                                     KeyListener,
                                                     MouseInputListener
{
    //constants
    protected static final int DELAY = 33; //delay between timer events
    protected static final int GAP = 5; //gap between components
    
    //caches for accessing sound and images
    protected static final SoundCache sound = new SoundCache();
    protected static final SpriteCache sprite = new SpriteCache();
    
    private static final int TOP_SCORES = 10; //number of scores in file
    
    private javax.swing.Timer timer; //timer for scheduling of updating UI
    private WindowListener winListener; //to store window listener for later
    private boolean firstDisplay; //if is first time screen is being displayed
    private boolean fullScreen;	// if game should be displayed as full screen
    
    protected JFrame myFrame; //the initial game window
    protected JButton startButton; //standard start button for game
    protected int myWidth, myHeight; //dimensions of game frame
    
    protected String myDirectory; //directory of files
    
    private ScoreDialog scoreDialog; //dialog for submitting scores
    
    private boolean scoreSubmitted; //is a score has already been submitted once
    protected boolean gamePaused; //if the game is paurse
        
    /**
     * Creates a game GUI panel using the specified layout manager.
     */
    protected Game(int width, int height, String directory)
    {
        super(new BorderLayout());
        myWidth = width;
        myHeight = height;
        myDirectory = directory;
        fullScreen = false;
        
        //create timer that drives animations
        timer = new javax.swing.Timer(DELAY, this);
        timer.setInitialDelay(DELAY);
        
        startButton = new JButton("Start");
        startButton.addActionListener(this);
        
        firstDisplay = true;
        
        scoreDialog = new ScoreDialog((JFrame)getTopLevelAncestor(), 
                                    scoreFileName(), name());
                                    
        scoreSubmitted = false;
    }
    
    /**
     * Creates a game GUI panel using the specified layout manager.
     */
    protected Game(int width, int height, String directory, boolean useFullScreen)
    {
        super(new BorderLayout());
        myWidth = width;
        myHeight = height;
        myDirectory = directory;
        fullScreen = useFullScreen;
        
        //create timer that drives animations
        timer = new javax.swing.Timer(DELAY, this);
        timer.setInitialDelay(DELAY);
        
        startButton = new JButton("Start");
        startButton.addActionListener(this);
        
        firstDisplay = true;
        
        scoreDialog = new ScoreDialog((JFrame)getTopLevelAncestor(), 
                                    scoreFileName(), name());
                                    
        scoreSubmitted = false;
    }
    
    /**
     * Creates an instance of a game with the class name. 
     * Returns null if the class or correct constructor cannot be found.
     */
    public static Game createInstanceOf(String className)
    {
        try
        {
            //find constructor requiring no parameters
            Class classToCreate = Class.forName(className);
            Class[] params = {};
            Constructor constructor = classToCreate.getConstructor(params);
            
            //create instance of object using found constructor
            Object[] argList = {};
            return (Game)constructor.newInstance(argList);
        }
        catch (Exception e)
        {
            System.out.println("Game" + e);
            return null;
        }
    }
    
    /**
     * Creates and displays a frame containing the specified panel. 
     * Sets the value of myFrame (the main frame) to the new one.
     */
    public void createAndShowFrame(JPanel panel)
    {
        //create frame
        myFrame = new JFrame(name());
        myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        myFrame.setResizable(false);

        myFrame.setContentPane(panel);
        myFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("icon.gif"));
        
        //display frame
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
        boolean isFullScreen = device.isFullScreenSupported();
        
        if (fullScreen && isFullScreen)
        {
            // Full-screen mode
            device.setFullScreenWindow(myFrame);
            myFrame.validate();
            myFrame.setUndecorated(true);
            myFrame.setResizable(false);
        }
        else
        {
            // Windowed mode
        	myFrame.pack();
            myFrame.setVisible(true);
            myFrame.setLocationRelativeTo(null); //center of screen
        }
        
        
        myFrame.addWindowListener(this);
        if (winListener != null)
            myFrame.addWindowListener(winListener);
        
        //add listeners if is first time being shown
        if (firstDisplay)
        {
            addKeyListener(this);
            addMouseListener(this);
            addMouseMotionListener(this);
            addKeyListener(new KeyListener()
            {
                public void keyPressed(KeyEvent e)
                {
                    if (e.getKeyCode() == KeyEvent.VK_F1)
                        if (gamePaused)
                            startGame();
                        else
                            pauseGame();
                    else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    	System.exit(0); // quit game
                }
                public void keyReleased(KeyEvent e) {}
                public void keyTyped(KeyEvent e) {}
            });
        }
            
        firstDisplay = false;
        
        this.setFocusable(true);
        this.requestFocusInWindow();
    }
    
    /**
     * Starts the timer for updating the UI.
     */
    public void startTimer()
    {
        timer.start();
    }
    
    /**
     * Stops the timer for updating the UI.
     */
    public void stopTimer()
    {
        timer.stop();
    }
    
    /**
     * Required by abstract class Game. Continues the game functions.
     */
    public void startGame()
    {
        startTimer();
        updateUI();
        gamePaused = false;
    }
    
    /**
     * Required by abstract class Game. Pauses the game functions.
     */
    public void pauseGame()
    {
        stopTimer();
        updateUI();
        gamePaused = true;
    }
    
    /**
     * Plays the specified sound file.
     */
    protected void playSound(String file)
    {
        sound.playSound(myDirectory + file);
    }
    
    /**
     * Loops the specified sound file.
     */
    protected void loopSound(String file)
    {
        sound.loopSound(myDirectory + file);
    }
    
    /**
     * Returns a sprite image from the specified file.
     */
    public BufferedImage getSprite(String file)
    {
        return sprite.getSprite(myDirectory + file);
    }
    
    /**
     * Adds the specified action listener to the starting button.
     */
    public void addActionListener(ActionListener listener)
    {
        startButton.addActionListener(listener);
    }
    
    /**
     * Adds the specified window listener to the starting button. 
     * If the window has not been created yet, will store the 
     * listener and add it to the frame later.
     */
    public void addWindowListener(WindowListener listener)
    {
        if (myFrame != null)
            myFrame.addWindowListener(listener);
        else
            winListener = listener;
    }
    
    /**
     * Required by ActionListener interface. Fired when 
     * an ActionEvent is invoked. Handles the updating 
     * of the UI and performance of the start button.
     * 
     * Note: If overridden, this method should be called
     *       first so UI will continue to be updated.
     *       Can also be overridden for states that need 
     *       to be checked periodically.
     */
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == startButton)
        {
            //hide cursor
            BufferedImage cursor = sprite.createCompatible(10, 10, 
                                                        Transparency.BITMASK);
            Toolkit t = Toolkit.getDefaultToolkit();
            Cursor c = t.createCustomCursor(cursor, new Point(5,5) ,"null");
            setCursor(c);
            
            initializeGameplayComponents(); //create gameplay components
            
            setPreferredSize(new Dimension(myWidth, myHeight));
            createAndShowFrame(this);
            
            scoreSubmitted = false;
        }
        else
        {
            runProcess(); //do game process
        }
        updateUI();
    }
    
    /**
     * Required by WindowListener. Fired when the containing 
     * window is activated.
     */
    public void windowActivated(WindowEvent e)
    {
        startGame();
    }
    
    /**
     * Required by WindowListener. Fired when the containing 
     * window is deactivated.
     */
    public void windowDeactivated(WindowEvent e)
    {
        pauseGame();
    }
    
    /**
     * Returns a panel of options for the game. Default: 
     * returns a panel containing only a start button
     */
    public JPanel getOptionsPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        
        panel.add(startButton, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Returns the String url of the text file containing help information.
     */
    public String helpFileName()
    {
        return myDirectory + "help.txt";
    }
    
    /**
     * Returns the String url of the text file containing score information.
     */
    public String scoreFileName()
    {
        return myDirectory + "score.txt";
    }
    
    /**
     * Displays a dialog for entering a highscore.
     */
    protected void submitScore(int score)
    {
        if (!scoreSubmitted) //not already submitted once
        {
            scoreDialog.setScore(score); //set score to add
            scoreDialog.setVisible(true);
            scoreSubmitted = true;
        }
    }
    
    //abstract methods
    /**Initializes the game (components, vairables, etc.).**/
    protected abstract void initializeGameplayComponents();
    
    /**Runs a game process once (hit detection, condition met, etc.).**/
    protected abstract void runProcess();
    
    /**Returns the name of the game.**/
    public abstract String name();
    
    /**Returns the description of the game.**/
    public abstract String description();
    
    /**Returns whether this game can be scored.**/
    public abstract boolean isScorable();

    /**Required by WindowListener but likely will not be used.**/
    public void windowClosed(WindowEvent e) { pauseGame(); }
    public void windowIconified(WindowEvent e) { pauseGame(); }
    public void windowClosing(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}
    
    /**Required by KeyListener, usage depends on game.**/
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    
    /**Required by MouseInputListener, usage depends on game.**/
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
}