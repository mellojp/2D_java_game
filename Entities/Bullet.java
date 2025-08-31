package Entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Sprites.Spritesheet;

public class Bullet extends Entity {
        
    private double dx;
    private double dy;
    private BufferedImage[] move;
    
    public Bullet(int x, int y, int targetX, int targetY, Spritesheet sheet) {
        super(x, y, 10,sheet);
        this.angle = Math.atan2(targetY - y, targetX - x);
        this.dx = Math.cos(this.angle);
        this.dy = Math.sin(this.angle);
        move = spritesheet.getFrames("bullet");
        this.width = 10;
        this.height = 10;
        currAnim = move;
       
        
    }

    public void tick() {
        x_axis += dx * speed;
        y_axis += dy * speed;
        this.area.x = this.x_axis;
        this.area.y = this.y_axis;
        updateAnim(move);
        AnimCycle(currAnim, 15);
    }

   @Override
    public void render(Graphics g) {
    g.drawImage(currAnim[curSprite], x_axis, y_axis, width, height, null);
    }
}
