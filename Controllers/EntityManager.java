package Controllers;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import Entities.Bullet;
import Entities.Enemy;
import Entities.Player;

/**
 * Gerencia todas as entidades do jogo, como jogador, inimigos e projéteis.
 * É responsável por atualizar, renderizar e tratar as interações entre as entidades.
 */
public class EntityManager {

    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;
    private int screenWidth, screenHeight, borderSize;
    private int count = 1;

    /**
     * Construtor da classe EntityManager.
     * @param screenWidth A largura da tela do jogo.
     * @param screenHeight A altura da tela do jogo.
     * @param borderSize O tamanho da borda para o spawn de inimigos.
     */
    public EntityManager(int screenWidth, int screenHeight, int borderSize) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.borderSize = borderSize;

        int playerX = (screenWidth / 2) - 16;
        int playerY = (screenHeight / 2) - 16;
        player = new Player(playerX, playerY);
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();

        spawnEnemies(2 * count);
    }

    /**
     * Atualiza o estado de todas as entidades do jogo.
     * @param inputManager O gerenciador de entradas para obter as ações do jogador.
     */
    public void update(InputManager inputManager) {
        // Atualiza o estado do jogador com base no input
        player.up = inputManager.up;
        player.down = inputManager.down;
        player.left = inputManager.left;
        player.right = inputManager.right;

        // Atualiza o ângulo do jogador para mirar no mouse
        player.angle = Math.atan2(inputManager.getMouseY() - (player.getY_axis() + 16), inputManager.getMouseX() - (player.getX_axis() + 16));

        // Lógica de tiro
        if (inputManager.isMouseLeftPressed()) {
            long crTime = System.currentTimeMillis();
            if (crTime - player.lastShot >= player.shotcd) {
                bullets.add(new Bullet(player.getX_axis() + 16, player.getY_axis() + 16, inputManager.getMouseX(), inputManager.getMouseY()));
                player.lastShot = crTime;
            }
        }

        player.tick(screenWidth, screenHeight);

        // Atualiza inimigos e verifica colisão com o jogador
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.tick(player);
        }

        // Atualiza projéteis e verifica colisão com inimigos
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            b.tick();
            if (b.getX_axis() < 0 || b.getX_axis() > screenWidth || b.getY_axis() < 0 || b.getY_axis() > screenHeight) {
                bullets.remove(i);
                i--;
                continue;
            }
            for (int j = 0; j < enemies.size(); j++) {
                Enemy e = enemies.get(j);
                if (b.isColliding(e, 0)) {
                    e.onHit();
                    if (e.checkDeath()) {
                        enemies.remove(j);
                    }
                    bullets.remove(i);
                    i--;
                    break;
                }
            }
        }

        // Lógica de colisão entre inimigos para evitar sobreposição
        for (int j = 0; j < enemies.size(); j++) {
            Enemy e1 = enemies.get(j);
            for (int k = 0; k < enemies.size(); k++) {
                if (j == k) continue;
                Enemy e2 = enemies.get(k);
                if (e1.isColliding(e2, 10)) {
                    int dx = e1.getX_axis() - e2.getX_axis();
                    int dy = e1.getY_axis() - e2.getY_axis();

                    if (Math.abs(dx) > Math.abs(dy)) {
                        if (dx > 0) {
                            e1.setX_axis(e1.getX_axis() + 1);
                            e2.setX_axis(e2.getX_axis() - 1);
                        } else {
                            e1.setX_axis(e1.getX_axis() - 1);
                            e2.setX_axis(e2.getX_axis() + 1);
                        }
                    } else {
                        if (dy > 0) {
                            e1.setY_axis(e1.getY_axis() + 1);
                            e2.setY_axis(e2.getY_axis() - 1);
                        } else {
                            e1.setY_axis(e1.getY_axis() - 1);
                            e2.setY_axis(e2.getY_axis() + 1);
                        }
                    }
                }
            }
        }

        // Gera uma nova onda de inimigos se todos forem derrotados
        if (enemies.isEmpty()) {
            count++;
            spawnEnemies(2 * count);
        }
    }

    /**
     * Renderiza todas as entidades na tela.
     * @param g O contexto gráfico a ser usado para a renderização.
     */
    public void render(Graphics g) {
        player.render(g);
        for (Enemy e : enemies) {
            e.render(g);
        }
        for (Bullet b : bullets) {
            b.render(g);
        }
    }

    /**
     * Cria uma nova onda de inimigos em posições aleatórias fora da tela.
     * @param quantity A quantidade de inimigos a serem criados.
     */
    public void spawnEnemies(int quantity) {
        Random r = new Random();
        for (int i = 0; i < quantity; i++) {
            int x_spawn, y_spawn;
            boolean lateral = r.nextBoolean();
            if (lateral) {
                boolean side = r.nextBoolean();
                if (side) x_spawn = r.nextInt(-this.borderSize, 0);
                else x_spawn = r.nextInt(screenWidth, screenWidth + this.borderSize);
                y_spawn = r.nextInt(0, screenHeight);
            } else {
                boolean ceil = r.nextBoolean();
                if (ceil) y_spawn = r.nextInt(-this.borderSize, 0);
                else y_spawn = r.nextInt(screenHeight, screenHeight + this.borderSize);
                x_spawn = r.nextInt(0, screenWidth);
            }
            enemies.add(new Enemy(x_spawn, y_spawn));
        }
    }

    /**
     * Reseta o estado do jogo para o início.
     */
    public void reset() {
        int playerX = (screenWidth / 2) - 16;
        int playerY = (screenHeight / 2) - 16;
        player.setX_axis(playerX);
        player.setY_axis(playerY);

        enemies.clear();
        bullets.clear();

        count = 1;
        spawnEnemies(2 * count);
    }

    /**
     * Retorna a instância do jogador.
     * @return O objeto Player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Retorna a lista de inimigos.
     * @return A lista de inimigos.
     */
    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }
}