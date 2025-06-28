package BricksBreaker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel {

    public static final double ASPECT_RATIO = 0.8;
    public static int SCREEN_WIDTH = 480, SCREEN_HEIGHT = 600;

    public static int numShots = 50;
    public static int numRows = 12;
    public static int bricksPerRow = 10;

    private final ShotSet shotset;
    private final BrickSet brickset;
    private GameState state;

    private final Point[] routeLine;
    private final int numRouteLineDots = 20;
    private final int routeLineDotSize = 6;

    private enum GameState {
        CHOOSE_DIRECTION,
        PLAY,
        COLLECT,
        MOVE_DOWN,
        ADD_NEW_ROW,
        WIN,
        GAMEOVER
    }

    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(0x222222));
        this.setLayout(null);

        MouseTracker mouseTracker = new MouseTracker();
        this.addMouseMotionListener(mouseTracker);
        this.addMouseListener(mouseTracker);

        shotset = new ShotSet(numShots);
        brickset = new BrickSet("./resources/bricks.txt");

        state = GameState.CHOOSE_DIRECTION;

        routeLine = new Point[numRouteLineDots];
        for (int i = 0; i < numRouteLineDots; i++) {
            routeLine[i] = new Point();
        }

        Timer timer = new Timer(10, e -> {
            update();
            repaint();
        });
        timer.start();
    }

    public void update() {
        switch (state) {
            case CHOOSE_DIRECTION:
                break;
            case PLAY:
                boolean finish = shotset.calcNextPosition();
                if (finish) {
                    if (brickset.allDestroyed()) {
                        state = GameState.WIN;
                    } else {
                        state = GameState.MOVE_DOWN;
                        brickset.down1row();
                    }
                }
                shotset.attack(brickset);
                shotset.update();
                break;
            case COLLECT:
                break;
            case MOVE_DOWN:
                boolean finished = brickset.moveDown();
                if (finished) {
                    state = GameState.CHOOSE_DIRECTION;
                }
                break;
            case ADD_NEW_ROW:
                break;
            case WIN:
                break;
            case GAMEOVER:
                break;
            default:
                break;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        switch (state) {
            case CHOOSE_DIRECTION:
                // draw route line
                g2.setColor(Color.ORANGE);
                Point start = shotset.getStartPosition();
                g2.fillOval(start.x, start.y, Shot.SIZE, Shot.SIZE);
                for (Point dot : routeLine) {
                    g2.fillOval(dot.x, dot.y, routeLineDotSize, routeLineDotSize);
                }
                // draw bricks
                brickset.paint(g2);
                break;
            case PLAY:
                shotset.paint(g2);
                brickset.paint(g2);
                break;
            case COLLECT:
                break;
            case MOVE_DOWN:
                g2.setColor(Color.ORANGE);
                start = shotset.getStartPosition();
                g2.fillOval(start.x, start.y, Shot.SIZE, Shot.SIZE);
                brickset.paint(g2);
                break;
            case ADD_NEW_ROW:
                break;
            case WIN:
                g2.setColor(Color.orange);
                g2.setFont(new Font("Arial", 0, (int) (SCREEN_HEIGHT * 0.4)));
                g2.drawString("Win", SCREEN_WIDTH / 2 - 128, (int) (SCREEN_HEIGHT * 0.7));
                break;
            case GAMEOVER:
                g2.setColor(Color.orange);
                g2.setFont(new Font("Arial", 0, (int) (SCREEN_HEIGHT * 0.2)));
                g2.drawString("Game Over", 8, (int) (SCREEN_HEIGHT * 0.6));
                break;
            default:
                break;
        }
    }

    public void calcSize() {
        int brickSizeIncludeMargin = (SCREEN_WIDTH - Brick.margin) / bricksPerRow;
        int brickWidth = brickSizeIncludeMargin - Brick.margin;
        int usedWidth = brickSizeIncludeMargin * bricksPerRow - Brick.margin;
        int startX = (SCREEN_WIDTH - usedWidth) / 2;
        int startY = 20;
        this.brickset.relocate(startY, startX, brickWidth);

        // for (int r = 0; r < numRows; r++) {
        //     for (int c = 0; c < bricksPerRow; c++) {
        //         this.bricks[r][c].relocate(
        //                 startX + c * brickSizeIncludeMargin,
        //                 startY + r * brickSizeIncludeMargin,
        //                 brickWidth
        //         );
        //     }
        // }
    }

    public void updateRouteLine(int mouseX, int mouseY) {
        final double gap = 20.0;
        Point batteryPos = shotset.getStartPosition();
        int x = batteryPos.x + Shot.SIZE / 2;
        int y = batteryPos.y + Shot.SIZE / 2;
        double angle = shotset.getCursorAngle();
        int dx = (int) (gap * Math.cos(angle));
        int dy = (int) (gap * Math.sin(angle) * -1.0);

        int r = routeLineDotSize / 2;
        x -= r;
        y -= r;

        for (int i = 0; i < numRouteLineDots; i++) {
            x += dx;
            y += dy;
            if (x < 0) {
                dx = -dx;
                x = -x;
            } else if (x + routeLineDotSize >= SCREEN_WIDTH) {
                dx = -dx;
                x = SCREEN_WIDTH - ((x + routeLineDotSize) - SCREEN_WIDTH) - routeLineDotSize;
            }
            routeLine[i].x = x;
            routeLine[i].y = y;
        }
    }

    private class MouseTracker implements MouseMotionListener, MouseListener {

        public MouseTracker() {
        }

        // MouseMotionListener
        @Override
        public void mouseMoved(MouseEvent e) {
            int x = e.getX(), y = e.getY();
            shotset.mouseMoved(x, y);
            updateRouteLine(x, y);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int x = e.getX(), y = e.getY();
            shotset.mouseMoved(x, y);
            updateRouteLine(x, y);
        }

        // MouseListener
        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (state == GameState.CHOOSE_DIRECTION) {
                state = GameState.PLAY;
                shotset.shoot();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
