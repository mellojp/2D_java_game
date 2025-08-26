import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame w = new JFrame("Game Window");
        Game g = new Game();

        w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        w.setSize(400,400);
        w.setResizable(false);

        w.add(g);

        w.setVisible(true);

    }
}
