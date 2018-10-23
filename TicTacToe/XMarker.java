package TicTacToe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JComponent;

public class XMarker extends JComponent
{
    private static final double SIZE = 80;
    
    private Line2D.Double[] mark;
    
    /**Precondition: none
     * 
     * Post-Condition: creates two lines representing an X at the specified coordinates.
     */
    public XMarker(double left, double top)
    {
        mark = new Line2D.Double[2];
        
        double startX = left + 10;
        double startY = top + 10;
        double endX = startX + SIZE;
        double endY = startY + SIZE;
        mark[0] = new Line2D.Double(startX, startY, endX, endY);
        
        startX = left + 10 + SIZE;
        startY = top + 10;
        endX = left + 10;
        endY = top + 10 + SIZE;
        
        mark[1] = new Line2D.Double(startX, startY, endX, endY);
    }
    
    /**Precondition: none
     * 
     * Post-Condition: paints the x onto the canvas.
     */
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.red);
        
        for (int i = 0; i < mark.length; i++)
        {
            g2.draw(mark[i]);
        }
    }

}