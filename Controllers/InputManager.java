package Controllers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Gerencia todas as entradas do usuário, incluindo teclado e mouse.
 * Esta classe centraliza o tratamento de eventos para facilitar o acesso ao estado dos controles em outras partes do jogo.
 */
public class InputManager implements KeyListener, MouseListener, MouseMotionListener {

    private boolean[] keys = new boolean[256];
    public boolean up, down, left, right, reset;

    private boolean pausedLastFrame = false;
    private boolean pauseToggled = false;
    private boolean resetLastFrame = false;
    private boolean resetToggled = false;

    private boolean mouseLeftPressed;
    private int mouseX, mouseY;

    /**
     * Atualiza o estado das variáveis de controle com base nas teclas pressionadas.
     * Deve ser chamado a cada frame do jogo.
     */
    public void update() {
        up = keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP];
        down = keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN];
        left = keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT];
        right = keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT];
        reset = keys[KeyEvent.VK_R];

        boolean isPauseCurrentlyPressed = keys[KeyEvent.VK_P];
        if (isPauseCurrentlyPressed && !pausedLastFrame) pauseToggled = true; 
        else pauseToggled = false;
        pausedLastFrame = isPauseCurrentlyPressed;

        boolean isResetCurrentlyPressed = keys[KeyEvent.VK_R];
        if (isResetCurrentlyPressed && !resetLastFrame) resetToggled = true;
        else resetToggled = false;
        resetLastFrame = isResetCurrentlyPressed;
    }

    /**
     * @return Verdadeiro APENAS no quadro em que a tecla de pausa foi pressionada.
     */
    public boolean isPauseToggled() {
        return pauseToggled;
    }

    public boolean isResetToggled() {
        return resetToggled;
    }

    /**
     * Verifica se o botão esquerdo do mouse está pressionado.
     * @return Verdadeiro se o botão esquerdo do mouse estiver pressionado, falso caso contrário.
     */
    public boolean isMouseLeftPressed() {
        return mouseLeftPressed;
    }

    /**
     * Obtém a posição atual do cursor do mouse no eixo X.
     * @return A coordenada X do mouse.
     */
    public int getMouseX() {
        return mouseX;
    }

    /**
     * Obtém a posição atual do cursor do mouse no eixo Y.
     * @return A coordenada Y do mouse.
     */
    public int getMouseY() {
        return mouseY;
    }

    /**
     * Chamado quando uma tecla é pressionada.
     * @param e O evento de teclado.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() < 256) {
            keys[e.getKeyCode()] = true;
        }
    }

    /**
     * Chamado quando uma tecla é liberada.
     * @param e O evento de teclado.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() < 256) {
            keys[e.getKeyCode()] = false;
        }
    }

    /**
     * Chamado quando um botão do mouse é pressionado.
     * @param e O evento de mouse.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            mouseLeftPressed = true;
        }
    }

    /**
     * Chamado quando um botão do mouse é liberado.
     * @param e O evento de mouse.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            mouseLeftPressed = false;
        }
    }

    /**
     * Chamado quando o mouse é movido.
     * @param e O evento de mouse.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    /**
     * Chamado quando o mouse é arrastado (movido com um botão pressionado).
     * @param e O evento de mouse.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}