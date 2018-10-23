//import
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import GameComponents.SoundCache;

/**
 * Controls a sound cache and a list of song files that can be accessed. 
 * Controls components placed in a modal dialog to dictate the selection 
 * of music and whether or not to play it.
 */
public class SoundComponent extends JDialog implements ActionListener
{
    //constants
    private static final String[] EXTENSION = {".mid", //music file types
                                               ".wav", 
                                               ".au"};
    private static final int GAP = 20; //border gap and gap between components
    private static final String DIRECTORY = "songs/"; //directory of music
    
    private static final String SONG_FILE = "res/songs/songs.rtf";

    private SoundCache cache; //cache for accesing songs
    private String[] songFiles; //list of songs to select from as files
    private String[] songNames; //list of songs to select from as names
    private String current; //current BGM

    private JComboBox musicCombo; //combobox for selecting songs
    private JCheckBox onBox; //whether or not the BGM should play
    private JCheckBox randBox; //whether or not the BGM should be random
    private JButton okButton; //button for disposing of dialog
    
    /**
     * Create the GUI.
     */
    public SoundComponent(Frame parent)
    {
        super(parent, "Sound Controls", true);
        setSize(200,150);
        
        songFiles = readFile();
        songNames = new String[songFiles.length];
        //remove all file extensions from each song
        for (int i = 0; i < songFiles.length; i++)
        {
            for (int j = 0; j < EXTENSION.length; j++)
            {
                String s = songFiles[i].replaceFirst(EXTENSION[j], "");
                if (!s.equals(songFiles[i])) //extension successfully removed
                {
                    songNames[i] = s;
                    break;
                }
            }
        }

        //initialize components
        musicCombo = new JComboBox(songNames);
        musicCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        musicCombo.setEnabled(false);
        
        onBox = new JCheckBox("BGM On");
//         onBox.setSelected(true);
        onBox.setSelected(false);
        
        randBox = new JCheckBox("Random");
        randBox.setSelected(true);
        randBox.addActionListener(this);
        
        okButton = new JButton("OK");
        okButton.addActionListener(this);
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.X_AXIS));
        checkBoxPanel.add(onBox);
        checkBoxPanel.add(Box.createRigidArea(new Dimension(0,GAP)));
        checkBoxPanel.add(randBox);
        
        JPanel thePanel = new JPanel();
        thePanel.setLayout(new BoxLayout(thePanel, BoxLayout.Y_AXIS));
        thePanel.add(musicCombo);
        thePanel.add(Box.createRigidArea(new Dimension(0,5)));
        thePanel.add(checkBoxPanel);
        thePanel.add(Box.createRigidArea(new Dimension(0,5)));
        thePanel.add(okButton);
        thePanel.setOpaque(true);
        thePanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        
        setContentPane(thePanel);
        setResizable(false);
        
        musicCombo.addActionListener(this);
        
        cache = new SoundCache();
        
        setFocusable(true);
        requestFocusInWindow();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        current = songFiles[musicCombo.getSelectedIndex()];
        
        setLocationRelativeTo(null); //center of screen
    }
    
    /**
     * Fired when an action event occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if (source == musicCombo)
            current = songFiles[musicCombo.getSelectedIndex()];
        else if (source == okButton)
            setVisible(false);
        else if (source == randBox)
            musicCombo.setEnabled(!randBox.isSelected());
    }

    /**
     * Loops the selected BGM if enabled.
     */
    public void startSong()
    {
        if (randBox.isSelected())
        {
            Random rand = new Random();
            int index = rand.nextInt(songFiles.length);
            current = songFiles[index];
        }
        
        if (onBox.isSelected())
            cache.loopSong(DIRECTORY + current);
    }
    
    /**
     * Stops the selected BGM.
     */
    public void stopSong()
    {
        if (onBox.isSelected())
            cache.stopSong();
    }
    
    /**
     * Returns the sound cache being used by this component.
     */
    public SoundCache getSoundCache()
    {
        return cache;
    }
    
    /**
     * Reads a specific text file and returns a string[] of the lines in it.
     */
    private String[] readFile()
    {
        try
        {
            Scanner in = new Scanner(new FileInputStream(new File(SONG_FILE)));

            ArrayList<String> temp = new ArrayList<String>();
            while (in.hasNextLine())
            {
                String s = in.nextLine();
                if ((s.trim()).length() > 0) // if not an empty line
                {
                    temp.add(s);
                }
            }
            String[] lines = new String[temp.size()];
            
            return temp.toArray(lines);
        }
        catch (FileNotFoundException e) {return null;}
    }
} // END CLASS
