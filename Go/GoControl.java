package Go;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;

import GameComponents.Game;
import GameComponents.Location;

public class GoControl extends Game
{
	// constants
    private static final int        WIDTH = 570;                    // width of game frame.
    private static final int        HEIGHT = 570;                   // height of game frame.
    /** make row/col count selectable? **/
    private static final int        ROWS = 18;                      // number of rows in the board.
    private static final int        COLS = 18;                      // number of columns in the board.
    private static final int        SIZE = 30;			          	// size of each grid (one side)
    private static final String     DIRECTORY = "FiveInARow/";      // directory of game files.
    private static final int        SIDE_COUNT = 2;                 // number of sides per game.
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
	 * Creates the GoControl.
	 */
	public GoControl()
	{
		super(WIDTH, HEIGHT, DIRECTORY);
	}
	
	/**
	 * Initializes the game components for this game.
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
        
        // handle events following piece placement.
        handlePiece(p);
        
        // prepare for next turn
        nextTurn();
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
     * Prepares for the next turn.
     */
    private void nextTurn()
    {
        m_turnCount++;
        m_currentSide = m_turnCount%2;
    }
    
    /**
     * Handles any events that need to occur after the specified piece has just been placed.
     */
    private void handlePiece(Piece p)
    {
    	// TOOD: implement piece handling.
    }
    
    /**
     * Returns whether or not the specified location on the board is valid by the rules.
     */
    private boolean locationValid(Location loc)
    {
    	// return false if location not valid (within board bounds).
    	if (!m_board.env().isValid(loc))
    		return false;
    	
    	// return false if location not empty.
    	if (!m_board.env().isEmpty(loc))
    		return false;
    	
    	// return false if location would kill piece(s) in area.
//    	if (surrounded(m_board.env().))
    	
    	// TODO: return false if Ko rules apply.
    	
    	return true;		// return true for all other cases.
    }
    
    /**
     * Returns whether or not the piece and its same-side adjacent pieces are completely 
     * surrounded with no eyes (can be removed immediately).
     */
    private boolean surrounded(Piece p)
    {
    	return false;
    }
    
	/**
	 * Returns whether or not this game is scorable.
	 */
	public boolean isScorable()
	{
		return false;
	}

	/**
	 * Returns the name of this game.
	 */
	public String name()
	{
		return "Go";
	}
	
	/**
	 * Returns the description of this game.
	 */
	public String description()
	{
		return "Play the traditional far-eastern game of Go.";
	}

	/**
	 * Runs this game's processes (not needed).
	 */
	protected void runProcess()	{}
	
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
	 * Runs this game.
	 */
	public static void main(String[] args)
	{
		Game gui = new GoControl();
        JFrame frame = new JFrame(gui.name());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(150, 120));
        
        frame.setContentPane(gui.getOptionsPanel());
        
        frame.pack();
        frame.setVisible(true);

	}

}
