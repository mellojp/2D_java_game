package Entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import Sprites.Spritesheet;

import java.awt.Color;  

public class Enemy extends Entity{
    
    public BufferedImage[] front,back;

    double hp = 3.0;
    double maxHp = 3.0;

    /**
     * Construtor da classe Enemy.
     * @param x A posição inicial do inimigo no eixo X.
     * @param y A posição inicial do inimigo no eixo Y.
     */
    public Enemy(int x, int y, Spritesheet sheet){
        super(x,y,3,sheet);
        front = spritesheet.getFrames("enemy_front");
        back = spritesheet.getFrames("enemy_back");
        currAnim = back;
    }

@Override
public void render(Graphics g) {
    if (currAnim == null || currAnim.length == 0) return;

    Graphics2D g2d = (Graphics2D) g;
    AffineTransform old = g2d.getTransform();

    double centerX = x_axis + width / 2.0;
    double centerY = y_axis + height / 2.0;

    
    double finalAngle = angle - Math.PI / 2;

 
    if (currAnim == back) {
        finalAngle += Math.PI;
    }

    
    g2d.rotate(finalAngle, centerX, centerY);

    //Desenha o sprite
    g2d.drawImage(currAnim[curSprite], x_axis, y_axis, width, height, null);

    // Restaura as transformações originais
    g2d.setTransform(old);

    // --- Barra de Vida do Inimigo ---
    
    // Fundo da barra
    g.setColor(Color.BLACK);
    g.fillRect(x_axis, y_axis - 10, width, 5);
    
    // Vida atual
    g.setColor(Color.RED);
    double healthRatio = this.hp / this.maxHp;
    g.fillRect(x_axis, y_axis - 10, (int)(width * healthRatio), 5);
}

    /**
     * Atualiza a posição do inimigo para seguir o jogador.
     * @param p O jogador a ser seguido.
     */
    public void tick(Player p){


            
        
        int dx = p.getX_axis() - this.x_axis;
        int dy = p.getY_axis() - this.y_axis;
        
        double rawAngle = Math.atan2(dy, dx);
        double maxRotation = 3 * Math.PI / 4;
        double minRotation = Math.PI / 4;
        if(rawAngle > 0){
            if(rawAngle>maxRotation){
                rawAngle = maxRotation;
            }else if(rawAngle < minRotation){
                rawAngle = minRotation;
            }
        }else{
            if(rawAngle < -maxRotation){
                rawAngle = - maxRotation;
            }else if(rawAngle > -minRotation){
                rawAngle = - minRotation;
            }
        }
        

        this.angle = rawAngle;



        double dist = Math.hypot(dx, dy);
        double velocityX = (dx / dist);
        double velocityY = (dy / dist);

        if(dist > 1){
            this.x_axis += velocityX * speed;
            this.y_axis += velocityY * speed;
        }
        this.area.x = this.x_axis;
        this.area.y = this.y_axis;

        if (dy > 0) {
        updateAnim(front);
        } else {
        updateAnim(back);
        }
        
        AnimCycle(currAnim, 15);
    }

    public void onHit(){
        this.hp = hp - 1;

    }

    public boolean checkDeath(){
        return hp <= 0.0;
    }

}
