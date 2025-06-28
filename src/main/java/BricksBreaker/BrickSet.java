package BricksBreaker;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BrickSet {

    private Brick[] bricks;
    private boolean[] destroyed;
    private int numBricks, numRemainBricks;
    private int numRows;
    private int numInitialRows;
    private int brickSize, needtoMoveDown;

    public BrickSet(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            if ((line = reader.readLine()) != null) {
                String[] nums = line.split(" ");
                assert nums.length == 3 :
                        "First line: <number of bricks> <number of rows> <number of initial rows>";
                numBricks = Integer.parseInt(nums[0]);
                numRemainBricks = numBricks;
                numRows = Integer.parseInt(nums[1]);
                numInitialRows = Integer.parseInt(nums[2]);
                assert numInitialRows < numRows :
                        "Try to display " + numInitialRows + " rows, only " + numRows + " rows.";
                bricks = new Brick[numBricks];
                destroyed = new boolean[numBricks];
            }

            int b = 0;
            for (int r = 0; r < numRows; r++) {
                line = reader.readLine();
                String[] nums = line.split(" ");
                assert nums.length == GamePanel.bricksPerRow : "Error in " + filename;
                int n = nums.length;
                for (int c = 0; c < n; c++) {
                    int val = Integer.parseInt(nums[c]);
                    if (val > 0) {
                        bricks[b] = new Brick(numInitialRows - r - 1, c, val);
                        b++;
                    }
                }
            }
            assert b == numBricks : "Number of bricks does not match";
        } catch (FileNotFoundException e) {
            System.err.println(filename + " not found");
        } catch (IOException e) {
            System.err.println(filename + " readLine");
        }

        this.brickSize = 0;
        this.needtoMoveDown = 0;
    }

    public void relocate(int top, int left, int brickSize) {
        this.brickSize = brickSize;
        int brickSizeIncludeMargin = brickSize + Brick.margin;
        for (Brick brick : bricks) {
            brick.relocate(
                    left + brickSizeIncludeMargin * brick.getCol(),
                    top + brickSizeIncludeMargin * brick.getRow(),
                    brickSize
            );
        }
    }

    public void down1row() {
        for (Brick brick : bricks) {
            brick.down1row();
        }
        needtoMoveDown = brickSize;
    }

    public boolean moveDown() {
        for (Brick brick : bricks) {
            brick.moveDown();
        }
        needtoMoveDown--;
        return needtoMoveDown == 0;
    }

    public void paint(Graphics2D g) {
        for (int i = 0; i < numBricks; i++) {
            if (!destroyed[i]) {
                bricks[i].paint(g);
            }
        }
    }

    public boolean setDestroyed(int idx) {
        destroyed[idx] = true;
        numRemainBricks--;
        return numRemainBricks == 0;
    }

    public int getSize() {
        return bricks.length;
    }

    public Brick getBrick(int idx) {
        return bricks[idx];
    }

    public boolean isDestroyed(int idx) {
        return destroyed[idx];
    }

    public boolean allDestroyed() {
        return numRemainBricks <= 0;
    }
}
