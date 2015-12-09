package auranapse.projectmobility;

/**
 * Created by Gabriel Wong on 09-Dec-15.
 */
public class GameObject
{
    int Vel_X = 0, Vel_Y = 0;
    int Pos_X = 0, Pos_Y = 0;

    int COL_X = 0, COL_Y = 0;
    int COLOFF_X = 0, COLOFF_Y = 0;

    public static boolean checkCollision(GameObject GO1, GameObject GO2)
    {
        int TLX = GO1.Pos_X + GO1.COL_X + GO1.COLOFF_X + GO2.COL_X + GO2.COLOFF_X;
        int TLY = GO1.Pos_Y + GO1.COL_Y + GO1.COLOFF_Y + GO2.COL_Y + GO2.COLOFF_Y;

        int BRX = GO1.Pos_X - GO1.COL_X + GO1.COLOFF_X - GO2.COL_X + GO2.COLOFF_X;
        int BRY = GO1.Pos_Y - GO1.COL_Y + GO1.COLOFF_Y - GO2.COL_Y + GO2.COLOFF_Y;


        if(GO2.Pos_X >= TLX || GO2.Pos_X <= BRX)
        {
            return false;
        }

        if(GO2.Pos_Y >= TLY || GO2.Pos_Y <= BRY)
        {
            return false;
        }

        return true;
    }
}
