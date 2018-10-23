package FiveInARow;

//import
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.JFrame;

import GameComponents.Direction;
import GameComponents.Game;
import GameComponents.Location;

/**
 * Controls the five-in-a-row game's processes.
 */
public class FiveInARowControl extends Game
{
    // constants
    private static final int        WIDTH = 550;                    // width of game frame.
    private static final int        HEIGHT = 550;                   // height of game frame.
    /** make row/col count selectable? **/
    private static final int        ROWS = 15;                      // number of rows in the board.
    private static final int        COLS = 15;                      // number of columns in the board.
    private static final int        SIZE = 50;			          	// size of each grid (one side)
    private static final String     DIRECTORY = "FiveInARow/";      // directory of game files.
    private static final int        SIDE_COUNT = 2;                 // number of sides per game.
    private static final int		TARGET_COUNT = 5;				// target number of pieces to align.
    private static final Color      COLOR[] = {Color.white,         // colors of each side.
                                               Color.black};
    
    private Side                m_side[];                 	// the pieces of each side in the game.
    private Board               m_board;               		// the board game is played on.
    private boolean             m_over;                 	// if the game is over.
    private int                 m_currentSide;              // whose turn it is (index of side).
    private int                 m_turnCount;              	// total number of turns elapsed.
    private int					m_winIndex;					// index of winning side, -1 if no winner
    private Ellipse2D.Double    m_cursor;                 	// the ellipse representing the cursor.
    
    /**
     * Creates the game control.
     */
    public FiveInARowControl()
    {
        super(WIDTH, HEIGHT, DIRECTORY);
    }
    
    /**
     * Initializes the game play components.
     */
    protected void initializeGameplayComponents()
    {
        // initialize board environment
        m_board = new Board(ROWS, COLS, SIZE);
        
        // initialize sides
        m_side = new Side[SIDE_COUNT];
        for (int i = 0; i < SIDE_COUNT; i++)
            m_side[i] = new Side((i == 0) ? "White" : "Black", COLOR[i], m_board.env());
        
        // initialize other variables
        m_over = false;
        m_currentSide = 0;
        m_turnCount = 0;
        m_winIndex = -1;
        m_cursor = new Ellipse2D.Double(0, 0, SIZE, SIZE);
    }
    
    /**
     * Handles processes for when the mouse is clicked.
     * Places a piece at the mouse location, if possible.
     */
    public void mouseClicked(MouseEvent e)
    {
        // return if the game is already over
        if (m_over) return;
        
        Location mouseLoc = m_board.env().toLocation(e.getX(), e.getY());
        
        // return if location is not empty
        if (!m_board.env().isEmpty(mouseLoc)) return;
        
        // create new piece at the location in the board
        Piece p = m_side[m_currentSide].addPiece(mouseLoc);
        
        // check if the game is over
        if (isWinningPiece(p))
        {
        	m_winIndex = m_currentSide;
            m_over = true;        // end the game
        }
        else
        {
            nextTurn();         // prepare for next turn
        }
    }
    
    /**
     * Handles processes for when the mouse is moved.
     * Moves the cursor to the new mouse position.
     */
    public void mouseMoved(MouseEvent e)
    {
        // set coordinates so that actual mouse is at center of cursor ellipse.
    	Location mouseLoc = m_board.env().toLocation(e.getX(), e.getY());
        m_cursor.x = m_board.env().doubleX(mouseLoc.x());
        m_cursor.y = m_board.env().doubleY(mouseLoc.y());
    }
    
    /**
     * Returns the name of the game.
     */
    public String name()
    {
        return "Five-In-A-Row";
    }
    
    /**
     * Returns the description of the game.
     */
    public String description()
    {
        return "Try to get five pieces in a row while blocking your " + 
                    "opponent from doing the same!";
    }
    
    /**
     * Returns whether or not this game can be scored.
     */
    public boolean isScorable()
    {
        return false;
    }
    
    /**
     * Prepares for the next turn.
     */
    private void nextTurn()
    {
        m_turnCount++;
        m_currentSide = m_turnCount%2;
    }
    
    /**
     * Returns whether or not the target piece is a winning move.
     */
    private boolean isWinningPiece(Piece targ)
    {
    	// get list of pieces adjacent to the target.
    	ArrayList<Location> nbrs = m_board.env().getNeighbors8(targ.loc());
    	ArrayList<Piece> pieces = new ArrayList<Piece>();
    	for (Location loc : nbrs)
    		if (!m_board.env().isEmpty(loc))
    			pieces.add((Piece)m_board.env().objectAt(loc));
    	
    	for (Piece p : pieces)
    		// if number of pieces on same side in a row is equal to or exceeds the target count
    		if (consecutiveCount(p, m_board.env().getDirection(targ.loc(), p.loc())) >= TARGET_COUNT)
    			return true;
    	
        return false;
    }
    
    /**
     * Returns the number of pieces in the specified direction and its opposite 
     * that are consecutive and of the same side.
     */
    private int consecutiveCount(Piece targ, Direction dir)
    {
    	int count = 1; // target piece counts as one.
    	
    	Location targLoc = targ.loc();
    	Location next = m_board.env().neighborOf(targLoc, dir);
    	Piece nextPiece = (Piece)m_board.env().objectAt(next);
    	while(nextPiece != null && targ.side().equals(nextPiece.side()))
    	{
    		count++;
    		next = m_board.env().neighborOf(next, dir);
        	nextPiece = (Piece)m_board.env().objectAt(next);
    	}
    	
    	// for opposite direction
    	dir = dir.reverse();
    	next = m_board.env().neighborOf(targLoc, dir);
    	nextPiece = (Piece)m_board.env().objectAt(next);
    	while(nextPiece != null && targ.side().equals(nextPiece.side()))
    	{
    		count++;
    		next = m_board.env().neighborOf(next, dir);
        	nextPiece = (Piece)m_board.env().objectAt(next);
    	}
    	
    	return count;
    }
    
    /**
     * Runs the game process. (Not needed).
     */
    protected void runProcess() {}
    
    /**
     * Paints the game onto the container.
     */
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        
        // paint board
        m_board.paintBoard(g2);
        
        // paint each piece
        for (int i = 0; i < m_side.length; i++)
            for (int j = 0; j < m_side[i].pieceCount(); j++)
                m_side[i].piece(j).paintComponent(g2);
        
        // paint cursor at mouse location
        g2.setColor(COLOR[m_currentSide]);
        g2.fill(m_cursor);
        
        if (m_over) //game over
        {
            g2.setColor(Color.red);
            g2.setFont(new Font("", Font.BOLD, 40));
            
            g2.drawString(m_side[m_winIndex].name() + " wins!", WIDTH/2 - 100, HEIGHT/2 - 10);
        }
    }
    
    /**
     * Runs the game.
     */
    public static void main(String[] args)
    {
        Game gui = new FiveInARowControl();
        JFrame frame = new JFrame(gui.name());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(150, 120));
        
        frame.setContentPane(gui.getOptionsPanel());
        
        frame.pack();
        frame.setVisible(true);
    }
}