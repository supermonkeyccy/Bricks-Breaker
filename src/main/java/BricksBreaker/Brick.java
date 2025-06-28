package BricksBreaker;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

public class Brick extends Rectangle {

    public static int borderThickness = 4;
    public static int margin = 6;

    private final int col;
    private int row;
    private int val;

    public enum Bonus {
        NONE, HORIZONTAL, VERTICAL, HORIZONTAL_VERTICAL, NINE_SQUARE_GRID
    }
    // private Bonus bonus;

    // public Brick(int val) {
    //     this.x = 0;
    //     this.y = 0;
    //     this.width = 0;
    //     this.height = 0;
    //     this.row = 0;
    //     this.col = 0;
    //     this.val = val;
    // }
    public Brick(int row, int col, int val) {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.row = row;
        this.col = col;
        this.val = val;
    }

    public void paint(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(borderThickness));
        g.setColor(new Color(0x8899dd));
        g.drawRoundRect(x, y, width, height, 10, 10);
        g.setFont(new Font("Arial", 0, (int) (this.height * 0.4)));
        g.drawString(Integer.toString(val), x + this.width / 2, y + (int) (this.height * 0.7));
    }

    public boolean shooted() {
        this.val--;
        return this.val == 0;
    }

    public void relocate(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.width = size;
        this.height = size;
    }

    public void down1row() {
        row++;
    }

    public void moveDown() {
        y++;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getVal() {
        return val;
    }

    public Rectangle getCollisionRectangle() {
        return new Rectangle(
                this.x - margin,
                this.y - margin,
                this.width + margin * 2,
                this.height + margin * 2
        );
    }
}
