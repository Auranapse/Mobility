package auranapse.projectmobility;

/**
 * Created by Gabriel Wong on 09-Dec-15.
 */
public class GameObject
{
    float Vel_X = 0, Vel_Y = 0;
    float Pos_X = 0, Pos_Y = 0;

    int COL_X = 0, COL_Y = 0;
    int COLOFF_X = 0, COLOFF_Y = 0;

    AABBBox box = new AABBBox();

    public static boolean checkCollision(GameObject GO1, GameObject GO2)
    {
        return GO1.box.IsOverlapping(GO2.box);
    }

    public void Update(final float delta_time)
    {

    }

    public void UpdateBox()
    {
        box.range_x_.start_ = Pos_X - COL_X + COLOFF_X;
        box.range_x_.end_ = Pos_X + COL_X + COLOFF_X;

        box.range_y_.start_ = Pos_Y - COL_Y + COLOFF_Y;
        box.range_y_.end_ = Pos_Y + COL_Y + COLOFF_Y;
    }
}
