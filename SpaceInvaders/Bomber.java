package SpaceInvaders;

/**
 * Made by me.
 * A player-controlled ship that is slower but with a 
 * greater number of bombs and shields
 */
public class Bomber extends Player
{
    //constants
    public static final int MAX_BOMBS = 10;
    public static final double SHIELD_BUFFER = .75; //damage multiplier when hit
    protected static final int PLAYER_SPEED = 1;
    
    /**
     * Creates a Speeder on the specified stage.
     */
    public Bomber(Stage stage)
    {
        super(stage, MAX_BOMBS, PLAYER_SPEED);
    }
    
    /**
     * Increases the shield by the specified amount.
     */
    public void addShields(int i)
    {
        i *= SHIELD_BUFFER;
        super.addShields(i);
    }
}