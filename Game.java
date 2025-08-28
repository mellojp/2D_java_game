import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Controllers.EntityManager;
import Controllers.InputManager;
import Entities.Enemy;

import java.awt.Font;
import java.awt.Color;

/**
 * Classe principal do jogo, responsável por inicializar a janela,
 * gerenciar o loop principal do jogo e coordenar os diferentes componentes
 * como o InputManager e o EntityManager.
 */
public class Game extends JPanel implements Runnable {

    public final int SCREEN_WIDTH = 1280;
    public final int SCREEN_HEIGHT = 720;
    public final int BORDER_SIZE = 200;

    private Thread gameThread;
    private BufferedImage background;

    private InputManager inputManager;
    private EntityManager entityManager;

    private GameState currentState;
    public enum GameState {
        START_MENU, END_MENU, PAUSE_MENU, PLAYING;
    }

    /**
     * Construtor da classe Game.
     * Configura a janela e inicializa os gerenciadores do jogo.
     */
    public Game() {

        this.currentState = GameState.START_MENU;

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        inputManager = new InputManager();
        entityManager = new EntityManager(SCREEN_WIDTH, SCREEN_HEIGHT, BORDER_SIZE);

        this.addKeyListener(inputManager);
        this.addMouseListener(inputManager);
        this.addMouseMotionListener(inputManager);
        this.setFocusable(true);

        try {
            background = ImageIO.read(getClass().getResource("/sprites/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inicia o loop do jogo em uma nova thread.
     */
    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Reseta o jogo para seu estado inicial.
     */
    private void resetGame() {
        entityManager.reset();
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
                update(); // -> atualiza a lógica do jogo
                repaint(); // -> redesenha a tela
                delta--;
            }
        }
    }

    /**
     * Atualiza o estado do jogo, delegando para os respectivos gerenciadores.
     * Verifica as condições de fim de jogo.
     */
    private void update() {
        inputManager.update(); // Sempre atualiza o input

        switch (currentState) {
            case START_MENU:
                // Se o jogador clicar, o jogo começa
                if (inputManager.isMouseLeftPressed()) {
                    currentState = GameState.PLAYING;
                }
                break;

            case PLAYING:
                // Lógica principal do jogo
                entityManager.update(inputManager);

                // Verifica a condição de "Game Over"
                for (Enemy e : entityManager.getEnemies()) {
                    if (entityManager.getPlayer().isColliding(e, 0)) {
                        currentState = GameState.END_MENU; // Muda para o menu de fim de jogo
                        break;
                    }
                }
                
                // Lógica para pausar (adicionar 'pause' ao InputManager se não existir)
                if (inputManager.isPauseToggled()) { 
                    currentState = GameState.PAUSE_MENU;
                }
                break;

            case PAUSE_MENU:
                // Se o jogador apertar a tecla de pausa novamente, o jogo volta
                if (inputManager.isPauseToggled()) {
                    currentState = GameState.PLAYING;
                }
                break;

            case END_MENU:
                // Se o jogador apertar 'R' no menu de fim de jogo, o jogo reseta
                if (inputManager.reset) {
                    resetGame();
                    currentState = GameState.PLAYING;
                }
                break;
        }
    }

    /**
     * Renderiza a tela com base no GameState atual.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Desenha o fundo em todos os estados
        if (background != null) {
            g.drawImage(background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        }

        switch (currentState) {
            case START_MENU:
                // Desenha a tela do menu inicial
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 50));
                g.drawString("Clique para Começar", SCREEN_WIDTH / 2 - 250, SCREEN_HEIGHT / 2);
                break;

            case PLAYING:
                // Desenha as entidades do jogo
                entityManager.render(g);
                break;

            case PAUSE_MENU:
                // Desenha a tela de pausa por cima do jogo
                entityManager.render(g); // Mostra o jogo "congelado"
                g.setColor(new Color(0, 0, 0, 150)); // Fundo semitransparente
                g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 50));
                g.drawString("PAUSADO", SCREEN_WIDTH / 2 - 120, SCREEN_HEIGHT / 2);
                break;

            case END_MENU:
                // Desenha a tela de fim de jogo
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 50));
                g.drawString("FIM DE JOGO", SCREEN_WIDTH / 2 - 180, SCREEN_HEIGHT / 2);
                g.setFont(new Font("Arial", Font.PLAIN, 20));
                g.drawString("Pressione 'R' para reiniciar", SCREEN_WIDTH / 2 - 120, SCREEN_HEIGHT / 2 + 50);
                break;
        }
    }
}