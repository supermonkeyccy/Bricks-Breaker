package BricksBreaker;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

public class ShotSet {

    private static final double MIN_ANGLE = Math.toRadians(5.0);
    private static final double MAX_ANGLE = Math.toRadians(175.0);

    private final List<Shot> shots;
    private final List<ShotState> state;
    private int numShots;

    private final Point startPos;
    private static final double SPEED = 10.0;
    private double angle;
    private int vx, vy;

    private final Timer timer;
    private int fireIdx;

    private int numCollectedBalls;

    private enum ShotState {
        READY, PLAY, BACK, STOP
    }

    public ShotSet(int numShots) {
        this.numShots = numShots;
        this.shots = new ArrayList<>(numShots);
        this.state = new ArrayList<>(numShots);
        this.startPos = new Point(
                (GamePanel.SCREEN_WIDTH - Shot.SIZE) / 2,
                GamePanel.SCREEN_HEIGHT - Shot.SIZE - 2
        );
        for (int i = 0; i < numShots; i++) {
            this.shots.add(new Shot(startPos.x, startPos.y));
            this.state.add(ShotState.READY);
        }

        this.fireIdx = 0;
        this.numCollectedBalls = 0;
        this.timer = new Timer(50, e -> {
            fire();
        });
    }

    public void addNewShot() {
        numShots++;
        shots.add(new Shot(startPos.x, startPos.y));
    }

    public boolean calcNextPosition() {
        for (int i = 0; i < numShots; i++) {
            Shot shot = shots.get(i);
            ShotState st = state.get(i);

            // ready -> play : see private void fire()
            // stop : do nothing, wait for others to come back
            if (st == ShotState.PLAY) { // play (-> back)
                if (shot.y + Shot.SIZE >= GamePanel.SCREEN_HEIGHT) {
                    if (numCollectedBalls == 0) {
                        startPos.x = shot.x;
                        shot.y = shot.nextY = startPos.y;
                        shot.setVelocity(0, 0);
                        numCollectedBalls = 1;
                        state.set(i, ShotState.STOP);
                    } else {
                        shot.y = shot.nextY = startPos.y;
                        shot.setVelocity((int) (SPEED * Math.signum(startPos.x - shot.x)), 0);
                        state.set(i, ShotState.BACK);
                    }
                } else {
                    shot.move();
                }
            } else if (st == ShotState.BACK) { // back (-> stop)
                if ((startPos.x - shot.x) * shot.getVx() <= 0.0) {
                    shot.x = shot.nextX = startPos.x;
                    shot.setVelocity(0, 0);
                    state.set(i, ShotState.STOP);
                    numCollectedBalls++;
                } else {
                    shot.move();
                }
            }
        }
        return numCollectedBalls == numShots;
    }

    public void attack(BrickSet bs) {
        int n = bs.getSize();
        for (Shot shot : shots) {
            for (int b = 0; b < n; b++) {
                if (bs.isDestroyed(b)) {
                    continue;
                }
                // Brick brick = bs.getBrick(b);
                boolean destroy = shot.attack(bs.getBrick(b));
                if (destroy) {
                    bs.setDestroyed(b);
                }
            }
        }
    }

    public void update() {
        for (Shot shot : shots) {
            shot.update();
        }
    }

    public void paint(Graphics2D g) {
        for (Shot shot : shots) {
            shot.paint(g);
        }
    }

    public void mouseMoved(int mouseX, int mouseY) {
        int x = startPos.x + Shot.SIZE / 2;
        int y = startPos.y + Shot.SIZE / 2;
        angle = Math.atan2(y - mouseY, mouseX - x);
        if (angle < -Math.PI / 2 || angle > MAX_ANGLE) {
            angle = MAX_ANGLE;
        } else if (angle < MIN_ANGLE) {
            angle = MIN_ANGLE;
        }
    }

    public void shoot() {
        vx = (int) (SPEED * Math.cos(angle));
        vy = (int) (-SPEED * Math.sin(angle));
        for (int i = 0; i < numShots; i++) {
            state.set(i, ShotState.READY);
        }
        numCollectedBalls = 0;
        timer.start();
        fire();
    }

    private void fire() {
        assert state.get(fireIdx) == ShotState.READY;
        shots.get(fireIdx).setVelocity(vx, vy);
        state.set(fireIdx, ShotState.PLAY);
        fireIdx++;
        if (fireIdx == numShots) {
            fireIdx = 0;
            timer.stop();
        }
    }

    public Point getStartPosition() {
        return startPos;
    }

    public double getCursorAngle() {
        return angle;
    }
}
