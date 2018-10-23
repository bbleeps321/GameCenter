package Questor;

// import
import GameComponents.BoundedEnv;
import GameComponents.Location;

/**
 * Subclass of Unit that cannot be controlled.
 */
public class Enemy extends Unit
{
    public Enemy(int level, BoundedEnv env, Location loc)
    {
        super("Foe", generateStats(level), env, loc, "asdasd");
    }
   
    public static int[] generateStats(int level)
    {
        int hp = 15 + 2*level;
        int str = 4 + level;
        int def = 3 + level;
        int skl = 3 + level;
        int spd = 4 + level;
        return new int[] {hp, str, def, skl, spd};
    }
}
