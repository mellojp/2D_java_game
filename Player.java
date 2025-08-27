import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Player extends Entity {

    public boolean up, down, left, right;
    public double angle = 0;
    public long lastShot = 0, shotcd= 200;

    /**
     * Construtor da classe Player.
     * @param x A posição inicial do jogador no eixo X.
     * @param y A posição inicial do jogador no eixo Y.
     */
    public Player(int x, int y) {
        super(x,y,4,"/sprites/player.png");
    }

    /**
     * Atualiza a posição do jogador com base nas teclas pressionadas, respeitando os limites da tela.
     * @param max_width A largura máxima da tela.
     * @param max_height A altura máxima da tela.
     */
    public void tick(int max_width, int max_height) {
        if (up) {
            if (y_axis - speed > 0) y_axis -= speed;
            else y_axis -= y_axis;
        }
        if (down) {
            if (y_axis + height + speed < max_height) y_axis += speed;
            else y_axis += max_height - (y_axis + height);
        }
        if (left) {
            if (x_axis - speed > 0) x_axis -= speed;
            else x_axis -= x_axis;
        }
        if (right) {
            if (x_axis + width + speed < max_width) x_axis += speed;
            else x_axis += max_width - (x_axis + width);
        }

        this.area.x = this.x_axis;
        this.area.y = this.y_axis;
    }

    /**
     * Renderiza o sprite do jogador na tela.
     * @param g O contexto gráfico a ser usado para a renderização.
     */
    @Override
    public void render(Graphics g) {
        if (sprite != null) {
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform old = g2d.getTransform();
            g2d.rotate(angle, x_axis + width / 2, y_axis + height / 2);
            g.drawImage(sprite, x_axis, y_axis, width, height, null);
            g2d.setTransform(old);
        }
    }

}
