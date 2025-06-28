package BricksBreaker;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class Shot extends Rectangle {

    public static final int SIZE = 12;

    public int nextX, nextY;
    private int Vx, Vy;
    private int nextVx, nextVy;

    public Shot(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = SIZE;
        this.height = SIZE;
        this.Vx = 0;
        this.Vy = 0;

        this.nextX = x;
        this.nextY = y;
        this.nextVx = 0;
        this.nextVy = 0;
    }

    public void move() {
        nextX = x + Vx;
        nextY = y + Vy;
        if (nextX < 0) {
            nextVx = -Vx;
            nextX = -nextX;
        } else if (nextX + SIZE >= GamePanel.SCREEN_WIDTH) {
            nextVx = -Vx;
            nextX = GamePanel.SCREEN_WIDTH - (nextX + SIZE - GamePanel.SCREEN_WIDTH) - SIZE;
        }
        if (nextY < 0) {
            nextVy = -Vy;
            nextY = -nextY;
        }
    }

    public boolean attack(Brick brick) {
        if (!this.intersects(brick.getCollisionRectangle())) {
            return false;
        }

        int top = brick.y;
        int bottom = top + brick.height;
        int left = brick.x;
        int right = left + brick.width;

        int shotCenterX = x + SIZE / 2;
        int shotCenterY = y + SIZE / 2;

        if (Vy > 0.0 && shotCenterY < top) {
            nextVy = -Vy;
            nextY = top - (y + SIZE - top) - SIZE;
            brick.shooted();
        } else if (Vy < 0.0 && shotCenterY >= bottom) {
            nextVy = -Vy;
            nextY = bottom + (bottom - y);
            brick.shooted();
        }

        if (Vx > 0.0 && shotCenterX < left) {
            nextVx = -Vx;
            nextX = left - (x + SIZE - left) - SIZE;
            brick.shooted();
        } else if (Vx < 0.0 && shotCenterX >= right) {
            nextVx = -Vx;
            nextX = right + (right - x);
            brick.shooted();
        }

        return brick.getVal() <= 0;
    }

    public void update() {
        x = nextX;
        y = nextY;
        Vx = nextVx;
        Vy = nextVy;
    }

    public void paint(Graphics2D g) {
        g.setColor(Color.ORANGE);
        g.fillOval(this.x, this.y, SIZE, SIZE);
    }

    public void setVelocity(int newVx, int newVy) {
        nextVx = newVx;
        nextVy = newVy;
    }

    public void setVx(int newVx) {
        nextVx = newVx;
    }

    public void setVy(int newVy) {
        nextVy = newVy;
    }

    public void setPosition(Point pos) {
        nextX = pos.x;
        nextY = pos.y;
    }

    public void setPosition(int newX, int newY) {
        nextX = newX;
        nextY = newY;
    }

    public int getVx() {
        return Vx;
    }

    public int getVy() {
        return Vy;
    }

    public Point getPosition() {
        return new Point(x, y);
    }
}
