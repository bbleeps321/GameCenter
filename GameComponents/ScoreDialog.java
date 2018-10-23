package GameComponents;

//import
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Components for displaying and taking in a user name and score.
 */
public class ScoreDialog extends JDialog
{
    private JTextField userIn; //field for entering user name
    private JButton okButton, cancelButton; //buttons of submittting and canceling
    private String myFile; //file components reads from/writes to
    private String game; //name of game this belongs to
    private int scoreToSubmit; //the score that will be submitted next
    
    /**
     * Creates a score dialog.
     */
    public ScoreDialog(Frame parent, String fileName, String gameName)
    {
        super (parent, "Enter Score", true);
        
        myFile = fileName;
        game = gameName;
        
        //create contentpane
        JPanel contentPane = createContentPane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        setContentPane(contentPane);
        setVisible(false);
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        setSize(200,115);
        setResizable(false);
        setLocationRelativeTo(null);
    }
    
    /**
     * Creates the content pane to be displayed.
     */
    private JPanel createContentPane()
    {
        JPanel contentPane = new JPanel(new BorderLayout());
        
        //initialize components
        userIn = new JTextField();
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        
        //add listeners
        okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String name = userIn.getText();
                if (name.equals("")) //no name
                    name = "Anonymous";
                    
                submitScore(scoreToSubmit, name);
                
                setVisible(false);
                
                userIn.setText(""); //clear text field
            }
        });
        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                dispose();
                userIn.setText(""); //clear text field
            }
        });
        
        //create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1,2));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        //add to content pane
        contentPane.add(userIn, BorderLayout.NORTH);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        
        return contentPane;
    }
    
    /**
     * Sets the score to be sumbitted next to the specified value.
     */
    public void setScore(int score)
    {
        scoreToSubmit = score;
    }
    
    /**
     * Submits the specified score as a high score into the file.
     */
    private void submitScore(int score, String name)
    {
        ArrayList<Integer> scores = new ArrayList<Integer>();
        ArrayList<String> names = new ArrayList<String>();
        try //try reading file
        {
            Scanner in = new Scanner(new File(myFile));
            
            //read entire file's scores
            in.nextLine(); //skip first two lines
//             in.nextLine();
            if (in.hasNextLine())
            {
                while (in.hasNextLine())
                {
                    String line = in.nextLine();
                    if (!line.equals(""))
                    {
                        scores.add(parseScore(line));
                        names.add(parseName(line));
                    }
                }
                //check if score is greater than any score in file, if is, insert
                for (int i = 0; i < scores.size(); i++)
                {
                    if (score >= scores.get(i))
                    {
                        scores.add(i, score); //insert
                        names.add(i, name); //insert
                        break;
                    }
                    else if (i == scores.size() - 1) //last
                    {
                        scores.add(score); //add to end
                        names.add(name); //add to end
                    }
                }
            }
            else
            {
                scores.add(score);
                names.add(name);
            }
        }
        catch (FileNotFoundException e) //no file
        {
            scores.add(score);
            names.add(name);
            System.out.println(e);
        }
        finally //write to file
        {
            try
            {
                PrintStream out = new PrintStream(myFile);
                
                //write title
                out.println(game + " High Scores");
                
                //write scores
                for (int i = 0; i < scores.size(); i++)
                {
                    out.print("\n"); //newline
                    out.print((i+1) + ". " + scores.get(i) + " " + names.get(i));
                }
            }
            catch (FileNotFoundException e)
            {
                System.out.println(e);
            }
        }
    }
    
    /**
     * Parses the score from the specified string.
     */
    private int parseScore(String line)
    {
        String[] parts = line.split(" ");
        return Integer.parseInt(parts[1].trim());
    }
    
    /**
     * Parses the name from the specified string.
     */
    private String parseName(String line)
    {
        String[] parts = line.split(" ");
        String name = "";
        for (int i = 2; i < parts.length; i++) //add rest as name
            name += parts[i];
        return name;
    }
}