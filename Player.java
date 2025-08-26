import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Entity {

    public boolean up, down, left, right;

    /**
     * Construtor da classe Player.
     * @param x A posição inicial do jogador no eixo X.
     * @param y A posição inicial do jogador no eixo Y.
     */
    public Player(int x, int y) {
        super(x,y,4);
         try {
            sprite = ImageIO.read(getClass().getResource("/sprites/player.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Atualiza a posição do jogador com base nas teclas pressionadas,
     * respeitando os limites da tela.
     * @param max_width A largura máxima da tela.
     * @param max_height A altura máxima da tela.
     */
    public void tick(int max_width, int max_height) {
        if (up) {
            if (y_axis - speed > 0) y_axis -= speed;
        }
        if (down) {
            if (y_axis + height + speed < max_height) y_axis += speed;
        }
        if (left) {
            if (x_axis - speed > 0) x_axis -= speed;

        }
        if (right) {
            if (x_axis + width + speed < max_width) x_axis += speed;
        }

        this.area.x = this.x_axis;
        this.area.y = this.y_axis;
    }

}
