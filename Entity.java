import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Entity {

    protected int x_axis;
    protected int y_axis;
    protected int width;  
    protected int height; 
    protected Rectangle area; 

    protected int speed;
    public BufferedImage sprite;

    /**
     * Construtor da classe Entity.
     * @param x A posição inicial no eixo X.
     * @param y A posição inicial no eixo Y.
     * @param spd A velocidade da entidade.
     */
    public Entity(int x, int y, int spd, String sprite_path){
        this.x_axis = x;
        this.y_axis = y;
        this.speed = spd;
        this.width = 32;
        this.height = 32;
        this.area = new Rectangle(this.x_axis, this.y_axis, this.width, this.height);
        try {
            sprite = ImageIO.read(getClass().getResource(sprite_path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getX_axis(){
        return this.x_axis;
    }

    public int getY_axis(){
        return this.y_axis;
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
    public boolean isColliding(Entity other) {
        return this.area.intersects(other.area);
    }

    /**
     * Renderiza o sprite da entidade na tela.
     * @param g O contexto gráfico a ser usado para a renderização.
     */
    public void render(Graphics g) {
         if (sprite != null) {
            g.drawImage(sprite, x_axis, y_axis, width, height, null);
        }
    }
}