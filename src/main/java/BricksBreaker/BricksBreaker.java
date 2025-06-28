package BricksBreaker;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

public class BricksBreaker {

    static GamePanel panel;

    public static void main(String[] args) {
        panel = new GamePanel();
        panel.calcSize();
        System.out.println("init");

        JFrame frame = new JFrame("Bricks Breaker");
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = frame.getWidth();
                int height = (int) (width / GamePanel.ASPECT_RATIO);
                if (height > frame.getHeight()) {
                    height = frame.getHeight();
                    width = (int) (height * GamePanel.ASPECT_RATIO);
                }
                panel.setPreferredSize(new Dimension(width, height));
                panel.calcSize();
            }
        });
    }
}
