package Entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;


import Sprites.Spritesheet;

public class Entity {

    protected int x_axis;
    protected int y_axis;
    protected int width;  
    protected int height; 
    protected Rectangle area; 
    protected Spritesheet spritesheet;

    protected int speed;
    
    protected BufferedImage[] currAnim;
    protected int curSprite = 0;
    protected int curFrames = 0;
   
    public double angle = 0.0;
    boolean movement = false;

    /**
     * Construtor da classe Entity.
     * @param x A posição inicial no eixo X.
     * @param y A posição inicial no eixo Y.
     * @param spd A velocidade da entidade.
     * @param sprite_path O caminho para o sprite.
     */
    public Entity(int x, int y, int spd, Spritesheet sheet){
        this.x_axis = x;
        this.y_axis = y;
        this.speed = spd;
        this.spritesheet = sheet;

        if (sheet != null && sheet.getFrames("default") != null && sheet.getFrames("default").length > 0) {
        BufferedImage firstFrame = sheet.getFrames("default")[0];
        this.width = firstFrame.getWidth();
        this.height = firstFrame.getHeight();
        } else {
        this.width = 64;  
        this.height = 64;
        }
        
        // Inicializa a área de colisão com as dimensões corretas
        this.area = new Rectangle(this.x_axis, this.y_axis, this.width, this.height);
    }

    public int getX_axis(){
        return this.x_axis;
    }

    public int getY_axis(){
        return this.y_axis;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Define a posição da entidade no eixo X e atualiza sua área de colisão.
     * @param value O novo valor para a posição X.
     */
    public void setX_axis(int value){
        this.x_axis = value;
        this.area.x = this.x_axis;
    }

    /**
     * Define a posição da entidade no eixo Y e atualiza sua área de colisão.
     * @param value O novo valor para a posição Y.
     */
    public void setY_axis(int value){
        this.y_axis = value;
        this.area.y = this.y_axis;
    }

    /**
     * Verifica se esta entidade está colidindo com outra entidade.
     * @param other A outra entidade a ser verificada.
     * @return Verdadeiro se houver colisão, falso caso contrário.
     */
    public boolean isColliding(Entity other, int minDist) {
        Rectangle r1 = new Rectangle(this.x_axis - minDist/2, this.y_axis - minDist/2,
                                     this.width + minDist, this.height + minDist);
        Rectangle r2 = new Rectangle(other.x_axis - minDist/2, other.y_axis - minDist/2,
                                     other.width + minDist, other.height + minDist);
        return r1.intersects(r2);
    }

    protected void AnimCycle(BufferedImage[] anim, int targetFrame){
		if (anim == null || anim.length == 0){
            return;
        } 
        curFrames++;
		if(curFrames >= targetFrame) {
			curSprite++;
			curFrames = 0;
			if(curSprite == (anim.length)){
				curSprite = 0;
			}
		}
    }

   protected void updateAnim(BufferedImage[] newAnim) {
        if (currAnim != newAnim) {
            currAnim = newAnim;
            curSprite = 0;
            curFrames = 0;
        }
    }
    

    /**
     * Renderiza o sprite da entidade na tela.
     * @param g O contexto gráfico a ser usado para a renderização.
     */
   public void render(Graphics g) {
    if (currAnim == null || currAnim.length == 0) return;

    Graphics2D g2d = (Graphics2D) g;
    AffineTransform old = g2d.getTransform();

    double centerX = x_axis + width / 2.0;
    double centerY = y_axis + height / 2.0;

    // Corrige a orientação do sprite para entrar de cabeça pra cima
    double correctedAngle = angle - Math.PI / 2;

    
    if (correctedAngle < -Math.PI/2 || correctedAngle > Math.PI/2) {
        correctedAngle += Math.PI;
    }

    // Aplica rotação em torno do centro da entidade
    g2d.rotate(correctedAngle, centerX, centerY);

    // Desenha o sprite
    g2d.drawImage(currAnim[curSprite], x_axis, y_axis, width, height, null);

    // Restaura transformações originais
    g2d.setTransform(old);
    }
}