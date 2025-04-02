import javax.swing.*;
import java.awt.*;

public class Pipe {
    private int x, y;
    private int initialY; // Store initial Y position
    public int width, height; // Changed from private to public for direct access
    private boolean isTop;
    private boolean counted;
    private float speed;
    private Image image;
    private float oscillationFactor = 0; // For vertical movement
    private float oscillationSpeed = 0.03f; // How fast it oscillates
    private int oscillationAmplitude = 15; // How far it moves up/down
    private boolean verticalMovement; // Whether this pipe has vertical movement

    public Pipe(int x, int y, int width, int height, boolean isTop, float speed, boolean verticalMovement) {
        this.x = x;
        this.y = y;
        this.initialY = y;
        this.width = width;
        this.height = height;
        this.isTop = isTop;
        this.counted = false;
        this.speed = speed;
        this.verticalMovement = verticalMovement;

        // Randomize starting oscillation position to prevent all pipes moving in sync
        this.oscillationFactor = (float) (Math.random() * Math.PI * 2);

        // Load the appropriate pipe image
        if (isTop) {
            this.image = new ImageIcon("d:/Uni/HK2 Năm 3/Java/BTTH2/images/toppipe.png").getImage();
        } else {
            this.image = new ImageIcon("d:/Uni/HK2 Năm 3/Java/BTTH2/images/bottompipe.png").getImage();
        }
    }

    public void move() {
        // Horizontal movement
        x -= speed;

        // Vertical oscillation if enabled for this pipe
        if (verticalMovement) {
            oscillationFactor += oscillationSpeed;

            // Calculate vertical offset using sine function for smooth oscillation
            int verticalOffset = (int) (Math.sin(oscillationFactor) * oscillationAmplitude);

            // Apply vertical offset to current position
            y = initialY + verticalOffset;
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(image, x, y, width, height, null);
    }

    public boolean intersects(Bird bird) {
        return getBounds().intersects(bird.getBounds());
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getWidth() {
        return width;
    }

    public boolean isTop() {
        return isTop;
    }

    public boolean isCounted() {
        return counted;
    }

    public void setCounted(boolean counted) {
        this.counted = counted;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setVerticalMovement(boolean verticalMovement) {
        this.verticalMovement = verticalMovement;
    }

    public void setOscillationParameters(float speed, int amplitude) {
        this.oscillationSpeed = speed;
        this.oscillationAmplitude = amplitude;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        this.initialY = y;
    }

    public int getHeight() {
        return height;
    }
}
