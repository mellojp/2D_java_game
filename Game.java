import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Dimension;


public class Game extends JPanel implements Runnable{

    private BufferedImage background;
    
    public Game(){
        try {
            background = ImageIO.read(getClass().getResource("/sprites/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
            background = null;
        }
        this.setPreferredSize(new Dimension(400, 400));
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
    }

    public void run(){
        
    }
}
