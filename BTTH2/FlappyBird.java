import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    private final int WIDTH = 360;
    private final int HEIGHT = 640;
    private final int PIPE_GAP = 200;
    private final int PIPE_WIDTH = 80;
    private final int PIPE_DELAY = 100; // Frames between new pipes
    private final int MIN_PIPE_HEIGHT = 80;
    private final int MAX_PIPE_HEIGHT = 350;
    private final float MIN_PIPE_SPEED = 2.5f;
    private final float MAX_PIPE_SPEED = 4.5f;
    private int pipeSpawnVariance = 20; // Variance in spawn timing

    private final float OSCILLATION_CHANCE = 0.6f; // 60% chance for pipes to oscillate
    private final float MIN_OSCILLATION_SPEED = 0.02f;
    private final float MAX_OSCILLATION_SPEED = 0.05f;
    private final int MIN_OSCILLATION_AMPLITUDE = 5;
    private final int MAX_OSCILLATION_AMPLITUDE = 25;

    private final int INITIAL_PIPE_DELAY = 100; // Initial frames between new pipes
    private final int MIN_PIPE_DELAY = 55;      // Minimum pipe delay (faster spawn)
    private int currentPipeDelay;               // Current pipe delay value that changes with score

    private Bird bird;
    private ArrayList<Pipe> pipes;
    private Timer timer;
    private Image background;
    private boolean gameOver;
    private boolean gameStarted;
    private int score;
    private int pipeCounter;
    private Random random;

    public FlappyBird() {
        JFrame frame = new JFrame("Flappy Bird");
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(this);
        frame.addKeyListener(this);
        frame.setVisible(true);

        // Load images
        background = new ImageIcon("d:/Uni/HK2 Năm 3/Java/BTTH2/images/flappybirdbg.png").getImage();

        // Initialize game objects
        bird = new Bird(WIDTH / 3, HEIGHT / 2);
        pipes = new ArrayList<>();
        random = new Random();

        // Game state
        gameOver = false;
        gameStarted = false;
        score = 0;
        pipeCounter = 0;
        currentPipeDelay = INITIAL_PIPE_DELAY;

        // Start game loop
        timer = new Timer(20, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw background
        g2d.drawImage(background, 0, 0, WIDTH, HEIGHT, null);

        // Draw pipes
        for (Pipe pipe : pipes) {
            pipe.draw(g2d);
        }

        // Draw bird
        bird.draw(g2d);

        // Draw score
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        g2d.drawString("Score: " + score, 10, 30);

        // Draw game over or start message
        if (gameOver) {
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 40));
            g2d.drawString("Game Over", WIDTH / 2 - 110, HEIGHT / 2);
            g2d.setFont(new Font("Arial", Font.PLAIN, 20));
            g2d.drawString("Press R to restart", WIDTH / 2 - 80, HEIGHT / 2 + 40);
        } else if (!gameStarted) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString("Press SPACE to start", WIDTH / 2 - 100, HEIGHT / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameStarted && !gameOver) {
            // Move bird
            bird.move();

            // Check for pipe creation with variable timing
            pipeCounter++;
            int spawnThreshold = currentPipeDelay + random.nextInt(pipeSpawnVariance) - pipeSpawnVariance / 2;

            if (pipeCounter >= spawnThreshold) {
                pipeCounter = 0;
                createPipe();
            }

            // Move pipes and check for score
            ArrayList<Pipe> pipesToRemove = new ArrayList<>();

            // Group pipes by pairs to maintain gap alignment
            for (int i = 0; i < pipes.size(); i += 2) {
                if (i + 1 < pipes.size()) { // Ensure we have a pair
                    Pipe topPipe = pipes.get(i);
                    Pipe bottomPipe = pipes.get(i + 1);

                    // Move pipes
                    topPipe.move();
                    bottomPipe.move();

                    // Maintain gap between top and bottom pipes
                    int currentGap = bottomPipe.getY() - (topPipe.getY() + topPipe.getHeight());
                    if (currentGap < PIPE_GAP) {
                        // Adjust bottom pipe position if needed
                        bottomPipe.setY(topPipe.getY() + topPipe.getHeight() + PIPE_GAP);
                    }

                    // Remove pipes that are off screen
                    if (topPipe.getX() + topPipe.getWidth() < 0) {
                        pipesToRemove.add(topPipe);
                        pipesToRemove.add(bottomPipe);
                    }

                    // Check for score (only count top pipes)
                    if (topPipe.isTop() && !topPipe.isCounted() && topPipe.getX() + topPipe.getWidth() < bird.getX()) {
                        topPipe.setCounted(true);
                        score++;
                    }

                    // Check for collision
                    if (topPipe.intersects(bird) || bottomPipe.intersects(bird)) {
                        gameOver = true;
                    }
                }
            }

            // Remove pipes that are off screen
            pipes.removeAll(pipesToRemove);

            // Check if bird hits the ground or ceiling
            if (bird.getY() <= 0 || bird.getY() + bird.getHeight() >= HEIGHT) {
                gameOver = true;
            }
        }

        repaint();
    }

    private void createPipe() {
        // Create pipes with random heights within appropriate bounds
        int minGap = PIPE_GAP - (score / 5 * 10); // Gap decreases slightly as score increases
        if (minGap < 150)
            minGap = 150; // Minimum gap size

        // More natural random pipe heights
        int pipeHeight = MIN_PIPE_HEIGHT + random.nextInt(MAX_PIPE_HEIGHT - MIN_PIPE_HEIGHT);

        // Ensure pipe doesn't go too close to top or bottom
        if (pipeHeight < MIN_PIPE_HEIGHT)
            pipeHeight = MIN_PIPE_HEIGHT;
        if (pipeHeight > HEIGHT - minGap - MIN_PIPE_HEIGHT) {
            pipeHeight = HEIGHT - minGap - MIN_PIPE_HEIGHT;
        }

        // Random speed factor for each pipe set
        float pipeSpeed = MIN_PIPE_SPEED + random.nextFloat() * (MAX_PIPE_SPEED - MIN_PIPE_SPEED);

        // Increase speed slightly as score increases
        pipeSpeed += Math.min(score / 10 * 0.2f, 2.0f);

        // Determine if this pipe pair will have vertical movement
        boolean hasVerticalMovement = random.nextFloat() < OSCILLATION_CHANCE;

        // Random oscillation parameters if vertical movement is enabled
        float oscillationSpeed = MIN_OSCILLATION_SPEED
                + random.nextFloat() * (MAX_OSCILLATION_SPEED - MIN_OSCILLATION_SPEED);
        int oscillationAmplitude = MIN_OSCILLATION_AMPLITUDE
                + random.nextInt(MAX_OSCILLATION_AMPLITUDE - MIN_OSCILLATION_AMPLITUDE);

        // Create the top pipe
        Pipe topPipe = new Pipe(WIDTH, 0, PIPE_WIDTH, pipeHeight, true, pipeSpeed, hasVerticalMovement);

        // Create the bottom pipe
        Pipe bottomPipe = new Pipe(WIDTH, pipeHeight + minGap, PIPE_WIDTH, HEIGHT - pipeHeight - minGap, false,
                pipeSpeed, hasVerticalMovement);

        // Set oscillation parameters if vertical movement is enabled
        if (hasVerticalMovement) {
            topPipe.setOscillationParameters(oscillationSpeed, oscillationAmplitude);
            bottomPipe.setOscillationParameters(oscillationSpeed, oscillationAmplitude);
        }

        pipes.add(topPipe);
        pipes.add(bottomPipe);
        
        // Decrease pipe spawn delay as score increases (pipes get closer together)
        updatePipeDelay();
    }
    
    private void updatePipeDelay() {
        // Decrease delay as score increases (every 5 points)
        int delayReduction = score / 5 * 5;
        currentPipeDelay = INITIAL_PIPE_DELAY - delayReduction;
        
        // Don't let delay go below minimum value
        if (currentPipeDelay < MIN_PIPE_DELAY) {
            currentPipeDelay = MIN_PIPE_DELAY;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if ((key == KeyEvent.VK_SPACE || key == KeyEvent.VK_ENTER) && !gameOver) {
            if (!gameStarted) {
                gameStarted = true;
            }
            bird.jump();
        }

        if (key == KeyEvent.VK_R && gameOver) {
            resetGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not needed
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not needed
    }

    private void resetGame() {
        bird = new Bird(WIDTH / 3, HEIGHT / 2);
        pipes.clear();
        gameOver = false;
        gameStarted = false;
        score = 0;
        pipeCounter = 0;
        currentPipeDelay = INITIAL_PIPE_DELAY; // Reset pipe delay to initial value
    }

    public static void main(String[] args) {
        new FlappyBird();
    }
}
