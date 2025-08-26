import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.event.KeyEvent;


public class Game extends JPanel implements Runnable, KeyListener{

    private BufferedImage background;
    private Player player;
    private ArrayList<Enemy> enemies;

    public final int SCREEN_WIDTH = 400;
    public final int SCREEN_HEIGHT = 400;
    private Thread gameThread;
    
    /**
     * Construtor da classe Game.
     * Inicializa as entidades do jogo, como o jogador e inimigos.
     * Configura a janela do jogo.
     */
    public Game(){

        this.addKeyListener(this);
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

        //lógica de adicionar inimigos temporária para testes
        Enemy e = new Enemy(50, 50);
        enemies.add(e);

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
    }

    /**
     * Método obrigatório para implementar a interface KeyListener.
     * Não será utilizado neste jogo.
     * @param e O evento de tecla.
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Ouve os eventos de tecla pressionada no teclado e atualiza o estado do jogador para controlar o movimento.
     * @param e O evento de tecla pressionada.
     */
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

    /**
     * Ouve os eventos de tecla solta no teclado e atualiza o estado do jogador, interrompendo o movimento.
     * @param e O evento de tecla solta.
     */
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
     * Atualiza o estado de todas as entidades do jogo, como a posição do jogador e dos inimigos.
     * Verifica se algum inimigo alcançou o jogador.
     */
    public void update(){
        player.tick(SCREEN_WIDTH,SCREEN_HEIGHT);
        for (Enemy e : enemies) {
            e.tick(player);
            if (player.isColliding(e)) {
                gameThread = null; 
                break;
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
        enemies.add(e);

        if (gameThread == null) {
            startGame();
        }
    }
}
