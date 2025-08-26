
import java.util.ArrayList;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Dimension;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class Game extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener{

    private BufferedImage background;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;

    public final int SCREEN_WIDTH = 800;
    public final int SCREEN_HEIGHT = 800;
    private Thread gameThread;
    
    /**
     * Construtor da classe Game.
     * Inicializa as entidades do jogo, como o jogador e inimigos.
     * Configura a janela do jogo.
     */
    public Game(){

        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setFocusable(true);

        try {
            background = ImageIO.read(getClass().getResource("/sprites/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
            background = null;
        }
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_WIDTH));

        int playerX = (SCREEN_WIDTH / 2) - 16;
        int playerY = (SCREEN_HEIGHT / 2) - 16;
        player = new Player(playerX, playerY);
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();


        //lógica de adicionar inimigos temporária para testes
        Enemy e = new Enemy(50, 50);
        Enemy e2 = new Enemy(700, 700);
        enemies.add(e);
        enemies.add(e2);

    }

    /**
     * Renderiza todos os componentes visuais do jogo na tela, incluindo o plano de fundo, jogador e inimigos.
     * @param g O contexto gráfico a ser usado para a renderização.
     */
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
        player.render(g);
        for (Enemy e: enemies){
            e.render(g);
        }
        for(Bullet b: bullets){
            b.render(g);
        }
    }

    /**
     * Loop principal do jogo, implementado da interface Runnable.
     * Controla a taxa de atualização e renderização para manter um FPS constante.
     */
    @Override
    public void run() {
        double drawInterval = 1000000000.0 / 60; // -> 60 FPS
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update(); // -> atualiza a posição das entidades na tela
                repaint(); // -> redesenha a tela com as novas posições
                delta--;
            }
        }
    }

    /**
     * Atualiza o estado de todas as entidades do jogo, como a posição do jogador, dos inimigos e dos disparos.
     * Verifica se algum inimigo alcançou o jogador, caso sim, encerra o lopp.
     * Verifica se algum disparo alcançou um inimigo, caso sim, remove o inimigo e o disparo.
     */
    public void update(){
        player.tick(SCREEN_WIDTH,SCREEN_HEIGHT);
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.tick(player);
            if (player.isColliding(e)) {
                gameThread = null; 
                break;
            }
        }

        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            b.tick();
            if (b.getX_axis() < 0 || b.getX_axis() > SCREEN_WIDTH || b.getY_axis() < 0 || b.getY_axis() > SCREEN_HEIGHT) {
                bullets.remove(i);
                i--;
                continue;
            }
            for (int j = 0; j < enemies.size(); j++) {
                Enemy e = enemies.get(j);
                if (b.isColliding(e)) {
                    enemies.remove(j);
                    bullets.remove(i);
                    i--;
                    break;
                }
            }
        }
    }

    /*
     * Inicia o loop do jogo em uma nova thread.
     */
    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Reseta o jogo para seu estado inicial.
     * Reposiciona o jogador, recria os inimigos e reinicia o loop do jogo se necessário.
     */
    public void resetGame() {
        int playerX = (SCREEN_WIDTH / 2) - 16;
        int playerY = (SCREEN_HEIGHT / 2) - 16;
        player.setX_axis(playerX);
        player.setY_axis(playerY);

        enemies.clear();

        //lógica de adicionar inimigos temporária para testes
        Enemy e = new Enemy(50, 50);
        Enemy e2 = new Enemy(700, 700);
        enemies.add(e);
        enemies.add(e2);;
        
        bullets.clear();

        if (gameThread == null) {
            startGame();
        }
    }


    /*
     * Métodos para ouvir as ações do teclado e mouse
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            player.up = true;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            player.down = true;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            player.left = true;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            player.right = true;
        }
        if (code == KeyEvent.VK_R || code ==  KeyEvent.VK_SPACE) {
            resetGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            player.up = false;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            player.down = false;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            player.left = false;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            player.right = false;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            bullets.add(new Bullet(player.getX_axis() + 16, player.getY_axis() + 16, e.getX(), e.getY()));
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        player.angle = Math.atan2(e.getY() - (player.getY_axis() + 16), e.getX() - (player.getX_axis() + 16));
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}
}
