package main;

import entity.Player;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48 tile
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // FPS
    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyH);

    // Set player's default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);

        // If set to true, all the drawing from this component will be done in an off-screen
        // painting buffer. In short, enabling this can improve game's rendering performance.
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);
        this.setFocusable(true); // With this, GamePanel can be "focused" to receive key input.
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start(); // Calls the run() below
    }

//    /**
//     * Sleep method
//     */
//    @Override
//    public void run() {
//        double drawInterval = 1_000_000_000 / FPS; // 0.0166667 seconds
//        double nextDrawTime = System.nanoTime() + drawInterval; // The allocated time for a single loop is 0.0166667 seconds
//
//        while (gameThread != null) {
//            System.out.println("The game loop is running");
//
//            long currentTime = System.nanoTime(); // Returns the current value of the running JVM's high-res time source in nanoseconds
//            System.out.println("Current time: " + currentTime);
//
//            // 1. UPDATE: update information such as character position
//            update();
//
//            // 2. DRAW: draw the screen with the updated information
//            repaint(); // Calls the paintComponent(Graphics g) below
//
//            try {
//                double remainingTime = nextDrawTime - System.nanoTime();
//                remainingTime /= 1_000_000; // Convert nanoseconds to milliseconds
//
//                if (remainingTime < 0) {
//                    remainingTime = 0;
//                }
//
//                Thread.sleep((long) remainingTime);
//
//                nextDrawTime += drawInterval;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * Delta/Accumulator method
     */
    @Override
    public void run() {
        double drawInterval = 1_000_000_000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            // When 'delta' reaches draw interval, we update the screen
            // e.g., it becomes 0.0166667 (or more) by adding the elapsed time (currentTime - lastTime)
            // and we try to divide it by drawInterval, then surely the quotient will become greater than 1
            // (Basically, this will be true once the numerator > denominator)
            if (delta >= 1) {
                update();
                repaint();

                delta--;
                drawCount++;
            }

            if (timer >= 1_000_000_000) { // 1 second
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        player.update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Graphics2D class extends the Graphics class to provide
        // more sophisticated control over geometry, coordinate
        // transformations, color management, and text layout.
        Graphics2D g2 = (Graphics2D) g;

        player.draw(g2);

        // Dispose of this graphics context and release any system resources
        // that it is using.
        g2.dispose();
    }
}
