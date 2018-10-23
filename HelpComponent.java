//import
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import GameComponents.Game;

/**
 * Frame for displaying help information about games.
 */
public class HelpComponent extends JFrame
{
    //constants
    private static final int GAP = 20; //border gap and gap between components
    
    private JTabbedPane tabbedPane; //tabbed pane containing different text fields
    
    /**
     * Creates a HelpComponent reading text files from the 
     * array of Games.
     */
    public HelpComponent(Game[] games)
    {
        tabbedPane = new JTabbedPane();
        tabbedPane.setOpaque(true);
        
        for (int i = 0; i < games.length; i++)
        {
            //read the help file for this game
            ArrayList<String> text = readFile(games[i].helpFileName());
            
            //create panel for this tab
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
            
            
            
            //create text area with help text in it
            JTextArea textArea= new JTextArea();
            textArea.setEditable(false);
            for (String s : text)
                textArea.append(s + "\n");
                
            //add text area to panel
            JScrollPane scrollPane = new JScrollPane(textArea);
            panel.add(scrollPane, BorderLayout.CENTER);
            
            //add panel to tab
            tabbedPane.add(games[i].name(), panel);
        }
        
        setContentPane(tabbedPane);
        setSize(new Dimension(410,350));
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage("icon.gif"));
        
        setLocationRelativeTo(null); //center of screen
    }
    
    /**
     * Reads the specified String url, returning the entire 
     * text file as a String ArrayList.
     */
    private ArrayList<String> readFile(String file)
    {
        try
        {
            Scanner in = new Scanner(new FileInputStream(file));
            ArrayList<String> list = new ArrayList<String>();
            while (in.hasNextLine())
                list.add(in.nextLine());
            return list;
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e);
            return null;
        }
    }
}