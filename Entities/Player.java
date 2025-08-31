package Entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Sprites.Spritesheet;

public class Player extends Entity {

    public boolean up, down, left, right;
    public long lastShot = 0, shotcd= 300;
    
   
    public BufferedImage[]frontM,frontI,backM,backI;
    
    
    public double maxHp = 10.0;
    public double currentHp = 10.0;
    //public double range


    /**
     * Construtor da classe Player.
     * @param x A posição inicial do jogador no eixo X.
     * @param y A posição inicial do jogador no eixo Y.
     */
    public Player(int x, int y, Spritesheet sheet) {
        super(x,y,4,sheet);
        frontI = spritesheet.getFrames("player_Ifront");
        frontM = spritesheet.getFrames("player_Mfront");
        backI = spritesheet.getFrames("player_Iback");
        backM = spritesheet.getFrames("player_Mback");

        currAnim = frontI;
       
    }
    /**
     * Atualiza a posição do jogador com base nas teclas pressionadas, respeitando os limites da tela.
     * @param max_width A largura máxima da tela.
     * @param max_height A altura máxima da tela.
     */
    public void tick(int max_width, int max_height) {
        
        movement = false;
        if (up) {
            if (y_axis - speed > 0) y_axis -= speed;
            else y_axis -= y_axis;
            movement = true;
        }
        if (down) {
            if (y_axis + height + speed < max_height) y_axis += speed;
            else y_axis += max_height - (y_axis + height);
            movement = true;
        }
        if (left) {
            if (x_axis - speed > 0) x_axis -= speed;
            else x_axis -= x_axis;
            movement = true;
        }
        if (right) {
            if (x_axis + width + speed < max_width) x_axis += speed;
            else x_axis += max_width - (x_axis + width);
            movement = true;
        }
        this.area.x = this.x_axis;
        this.area.y = this.y_axis;

        BufferedImage[] newAnim;
        double angleOriginal = angle + Math.PI / 2;

        if (angleOriginal >= -Math.PI/2 && angleOriginal <= Math.PI/2) {
            newAnim = movement ? frontM : frontI;
        } else {
            newAnim = movement ? backM : backI;
        }

        updateAnim(newAnim);
        AnimCycle(currAnim, 15);
    
    }

    public void onHit(double damage) {
        this.currentHp -= damage;
    }

    public boolean checkDeath(){
        return currentHp <= 0.0;
    }

    
    public void render(Graphics g) {
    super.render(g);
}

}
