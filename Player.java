public class Player extends Entity {
    private int mov_speed;
    public boolean left, right, down, up;

    public Player() {
        super();
        this.x_axis = 50;
        this.y_axis = 50;
        this.mov_speed = 5;
    }
}
