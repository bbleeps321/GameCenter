package TypeMaster;

//import
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import GameComponents.Shape2D;

/**
 * Circle that moves from the top of the screen to the bottom. Contains 
 * a letter that must be types in order to be popped.
 */
public class Bubble extends Shape2D
{
    //constants
    private static final int FALL_UNIT = 8; //unit bubble falls by
    private static final int SIZE = 30; //default size of bubble
    private static final Color COLOR = Color.black; //color of bubble & text
    private static final char[] LETTERS = //possible letter choices
    {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 
     'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
     
    private char myChar; //the letter contained by this bubble
    private boolean popped; //if the bubble is popped
    
    /**
     * Creates a bubble at the specified coordinates with a 
     * random character assigned to it.
     */
    public Bubble(double x, double y)
    {
        super(x, y, SIZE, SIZE);
        
        //randomly select a letter
        Random rand = new Random();
        int index = rand.nextInt(LETTERS.length);
        myChar = LETTERS[index];
        
        //not initially popped
        popped = false;
    }
    
    /**
     * Steps the bubble, making it fall by one unit.
     */
    public void step()
    {
        myY += FALL_UNIT;
    }

    /**
     * Pops the bubble.
     */
    public void pop()
    {
        popped = true;
    }
    
    /**
     * Returns the character contained by this bubble.
     */
    public char getChar()
    {
        return myChar;
    }
    
    /**
     * Returns the shape representation of this bubble.
     */
    public Shape shape()
    {
        return new Ellipse2D.Double(myX, myY, myWidth, myHeight);
    }
    
    /**
     * Paints the bubble onto the container.
     */
    public void paintComponent(Graphics2D g2)
    {
        if (!popped) //only draw if not popped
        {
            g2.setColor(COLOR); //set color
            g2.draw(shape()); //outline bubble
            
            //draw char
            g2.setFont(new Font("", Font.BOLD, SIZE - 10));
            g2.drawString(Character.toString(myChar), 
                            (float)myX + 10, (float)myY + 20);
        }
    }
}