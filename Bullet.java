import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;


public class Bullet extends Entity {
    
    private double dx;
    private double dy;
    private double rad;
    
    public Bullet(int x, int y, int targetX, int targetY) {
        super(x, y, 10,"/sprites/bullet.png");
        this.width = 5;
        this.height = 5;

        rad = Math.atan2(targetY - y, targetX - x);
        this.dx = Math.cos(rad);
        this.dy = Math.sin(rad);
    }

    public void tick() {
        x_axis += dx * speed;
        y_axis += dy * speed;
        this.area.x = this.x_axis;
        this.area.y = this.y_axis;
    }

    /**
     * Renderiza o sprite da bala na tela, com a rotação correta para a direção em que foi disparada.
     * @param g O contexto gráfico a ser usado para a renderização.
     */
    @Override
    public void render(Graphics g) {
        if (sprite != null) {
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform old = g2d.getTransform();
            g2d.rotate(rad, x_axis + width / 2.0, y_axis + height / 2.0);
            g.drawImage(sprite, x_axis, y_axis, width, height, null);
            g2d.setTransform(old);
        }
    }
}
