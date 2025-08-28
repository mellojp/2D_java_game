package Entities;

import java.awt.Graphics; 
import java.awt.Color;  

public class Enemy extends Entity{
    
    double hp = 3.0;
    double maxHp = 3.0;

    /**
     * Construtor da classe Enemy.
     * @param x A posição inicial do inimigo no eixo X.
     * @param y A posição inicial do inimigo no eixo Y.
     */
    public Enemy(int x, int y){
        super(x,y,3,"/sprites/enemy.png");
    }

    @Override
    public void render(Graphics g) {
        super.render(g); // Isso desenha o sprite do inimigo

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
        this.angle = Math.atan2(dy, dx);

        double dist = Math.hypot(dx, dy);
        double velocityX = (dx / dist);
        double velocityY = (dy / dist);

        if(dist > 1){
            this.x_axis += velocityX * speed;
            this.y_axis += velocityY * speed;
        }
        this.area.x = this.x_axis;
        this.area.y = this.y_axis;
    }

    public void onHit(){
        this.hp = hp - 1;

    }

    public boolean checkDeath(){
        return hp <= 0.0;
    }

}
