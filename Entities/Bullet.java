package Entities;

public class Bullet extends Entity {
    
    private double dx;
    private double dy;
    
    public Bullet(int x, int y, int targetX, int targetY) {
        super(x, y, 10,"/sprites/bullet.png");
        this.width = 5;
        this.height = 5;

        this.angle = Math.atan2(targetY - y, targetX - x);
        this.dx = Math.cos(this.angle);
        this.dy = Math.sin(this.angle);
    }

    public void tick() {
        x_axis += dx * speed;
        y_axis += dy * speed;
        this.area.x = this.x_axis;
        this.area.y = this.y_axis;
    }

}
