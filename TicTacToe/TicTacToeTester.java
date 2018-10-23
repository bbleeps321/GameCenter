package TicTacToe;

import javax.swing.JFrame;

public class TicTacToeTester
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Tic-Tac-Toe");

        final int FRAME_WIDTH = 500;
        final int FRAME_HEIGHT = 380;
        
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setVisible(true);
        frame.setResizable(false);

        GameControl game = new GameControl(frame);
        
    } // END MAIN
    
} // END CLASS