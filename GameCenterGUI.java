//import
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import java.util.Arrays;

import GameComponents.Game;

/**
 * Controls the graphics user interface for the GameCenter program. 
 * User interface for selecting and accessing the different games 
 * (found in GameComponents). GUI for each game should be supplied 
 * in the actual game object.
 */
public class GameCenterGUI extends JPanel implements ActionListener, 
                                                     WindowListener
{
    //constants
    private static final int WIDTH = 250;   // width of frame.
    private static final int HEIGHT = 300;  // height of frame.
    
    //classes of games (their controllers)
    private static final String[] GAME_CLASSES =
    {
        "Blobert.BlobertControl",
        "Worm.WormControl",
        "Pong.PongControl",
        "SpaceInvaders.Invaders",
        "Critter.CritterControl",
        "Firewall.FirewallControl",
        "Asteroids.AsteroidsControl",
        "TowerDefense.TowerDefenseControl",
        "Tetris.TetrisControl",
        "TicTacToe.TicTacToeControl",
        "Gateway.GatewayControl",
        "TypeMaster.TypeMasterControl",
        "Hunter.HunterControl",
        "Avoider.AvoiderControl",
//         "FiveInARow.FiveInARowControl"
    };
    
    private SoundComponent sound; //sound control for playing audio files
    private HelpComponent help; //help frame for how to play games
    private ScoreComponent score; //frame for high scores of games
    private JTabbedPane tabbedPane; //pane containing options for each game
    private JMenuItem bgmItem; //item for viewing BGM options
    private JMenuItem helpItem; //item for viewing help for games
    private JMenuItem scoreItem; //item for viewing high scores
    private JMenuItem closeItem; // item for closing frame.
    private Game[] myGames; //list of all games
    
    /**
     * Creates a content pane containing the GUI components.
     */
    public Container createContentPane()
    {
        Arrays.sort(GAME_CLASSES);
        JPanel contentPane = new JPanel(new BorderLayout());
        
        tabbedPane = new JTabbedPane();
        
        sound = new SoundComponent((JFrame)getTopLevelAncestor());
        Game[] games = new Game[GAME_CLASSES.length];
        for (int i = 0; i < GAME_CLASSES.length; i++)
        {
            String gameClass = GAME_CLASSES[i]; 
            
            games[i] = Game.createInstanceOf(gameClass);
            JPanel optionsPanel = games[i].getOptionsPanel();
            games[i].addWindowListener(this);
            tabbedPane.add(games[i].name(), optionsPanel);
            tabbedPane.setToolTipTextAt(i, games[i].description());
        }
        help = new HelpComponent(games);
        score = new ScoreComponent(games);
        myGames = games;
        
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        return contentPane;
    }
    
    /**
     * Creates a JMenuBar fo the GUI.
     */
    public JMenuBar createMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        
        //create items
        bgmItem = new JMenuItem("Sound");
        bgmItem.addActionListener(this);
        
        helpItem = new JMenuItem("Help");
        helpItem.addActionListener(this);
        
        scoreItem = new JMenuItem("Scores");
        scoreItem.addActionListener(this);
        
        closeItem = new JMenuItem("Close");
        closeItem.addActionListener(this);
        
        menuBar.add(bgmItem);
        menuBar.add(helpItem);
        menuBar.add(scoreItem);
        menuBar.add(closeItem);
        
        return menuBar;
    }
    
    /**
     * Required by ActionListener interface. Fired when an 
     * ActionEvent is invoked. Specifically, fired when a 
     * menu item is pressed.
     */
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if (source == bgmItem)
            sound.setVisible(true);
        else if (source == helpItem)
            help.setVisible(true);
        else if (source == scoreItem)
            score.setVisible(true);
        else if (source == closeItem)
            System.exit(0);
    }
    
    /**
     * Required by WindowListener. Fired when the containing 
     * window is activated.
     */
    public void windowActivated(WindowEvent e)
    {
        sound.startSong();
    }
    
    /**
     * Required by WindowListener. Fired when the containing 
     * window is deactivated.
     */
    public void windowDeactivated(WindowEvent e)
    {
        sound.stopSong();
        //update scores
        int index = tabbedPane.getSelectedIndex();
        score.update(myGames[index]);
    }
    
    /**Required by WindowListener.**/
    public void windowClosed(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowClosing(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}
    
    /**
     * Start the GameCenter program.
     * The String[] args parameter is not needed to run this.
     */
    public static void main(String[] args)
    {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                createAndShowGUI(); 
            }
        });
    }
    
    /**
     * Create the GUI and show it.  For thread safety, this 
     * method should be invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI()
    {
        //Create and set up the window.
        JFrame frame = new JFrame("Game Center");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("icon.gif"));

        //Create and set up the content pane.
        GameCenterGUI exe = new GameCenterGUI();
        frame.setJMenuBar(exe.createMenuBar());
        frame.setContentPane(exe.createContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null); //center of screen
        
    }
}