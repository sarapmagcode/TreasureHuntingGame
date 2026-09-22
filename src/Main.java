import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Treasure Hunting Game 2D");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        // Causes this window to be sized to fit the preferred size and
        // layout of its subcomponents (i.e., GamePanel)
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
