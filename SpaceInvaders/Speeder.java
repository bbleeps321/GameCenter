package SpaceInvaders;

/**
 * Made by me.
 * A player-controlled ship that is faster but with a 
 * fewer number of bombs and shields
 */
public class Speeder extends Player
{
    //constants
    public static final int MAX_BOMBS = 3;
    public static final double SHIELD_BUFFER = 1.25; //damage multiplier when hit
    protected static final int PLAYER_SPEED = 10;
    
    /**
     * Creates a Speeder on the specified stage.
     */
    public Speeder(Stage stage)
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