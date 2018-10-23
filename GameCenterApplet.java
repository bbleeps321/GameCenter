//import
import javax.swing.JApplet;

/**
 * Applet version of GameCenter.
 */
public class GameCenterApplet extends JApplet
{
    /**
     * Initializes the applet.
     */
    public void init()
    {
        GameCenterGUI gui = new GameCenterGUI();
        setContentPane(gui.createContentPane());
        setJMenuBar(gui.createMenuBar());
    }
}