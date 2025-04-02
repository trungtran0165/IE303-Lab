import javax.swing.*;
import java.awt.*;

public class Bird {
    private int x, y;
    private int width, height;
    private double velocity;
    private double gravity;
    private Image image;

    public Bird(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 40;
        this.height = 30;
        this.velocity = 0;
        this.gravity = 0.5;

        // Load bird image
        this.image = new ImageIcon("d:/Uni/HK2 Năm 3/Java/BTTH2/images/flappybird.png").getImage();
    }

    public void move() {
        velocity += gravity;
        y += (int) velocity;
    }

    public void jump() {
        velocity = -8.0;
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(image, x, y, width, height, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
