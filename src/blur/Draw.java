package blur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Draw extends JLabel implements MouseListener, MouseMotionListener {

    int[][] coords = new int[1000][1000];
    ArrayList<Color> c = new ArrayList<>();
    BufferedImage image;

    public Draw() {

        c.add(new Color(150, 0, 255, 150));
        c.add(new Color(0, 255, 0, 150));
        c.add(new Color(0, 255, 200, 150));
        c.add(new Color(255, 0, 0, 150));
        c.add(new Color(200, 0, 0, 150));
        c.add(new Color(0, 0, 255, 150));
        c.add(new Color(0, 255, 255, 150));

        try {
            image = ImageIO.read(new File("test.jpg"));
        } catch (IOException ex) {
        }

        for (int i = 0; i < coords.length; i++) {
            for (int j = 0; j < coords[j].length; j++) {
                    coords[i][j] = image.getRGB(i, j);
            }
        }
    }

    protected void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        Graphics2D g2d = (Graphics2D) g1;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        g1.drawImage(image, 0, 0, this);
        Color color = blendall(c);

        g1.setColor(color);
        g1.fillRect(500, 500, 200, 200);

        //System.out.println(color + " a=" + color.getAlpha());
        repaint();
    }

    private Color blendall(ArrayList<Color> c) {
        ArrayList<Color> color = new ArrayList<>();

        while (true) {
            for (int i = 0; i < c.size() - 1; i++) {

                double totalAlpha = c.get(i).getAlpha() + c.get(i + 1).getAlpha();
                double weight0 = c.get(i).getAlpha() / totalAlpha;
                double weight1 = c.get(i + 1).getAlpha() / totalAlpha;

                double r = weight0 * c.get(i).getRed() + weight1 * c.get(i + 1).getRed();
                double g = weight0 * c.get(i).getGreen() + weight1 * c.get(i + 1).getGreen();
                double b = weight0 * c.get(i).getBlue() + weight1 * c.get(i + 1).getBlue();
                double a = Math.max(c.get(i).getAlpha(), c.get(i + 1).getAlpha());

                color.add(new Color((int) r, (int) g, (int) b, (int) a));
            }

            if (c.size() == 1) {
                return c.get(0);
            } else {
                c = color;
                color = new ArrayList<>();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent me
    ) {
    }

    @Override
    public void mousePressed(MouseEvent me
    ) {
    }

    @Override
    public void mouseReleased(MouseEvent me
    ) {
    }

    @Override
    public void mouseEntered(MouseEvent me
    ) {
    }

    @Override
    public void mouseExited(MouseEvent me
    ) {
    }

    @Override
    public void mouseDragged(MouseEvent me
    ) {
    }

    @Override
    public void mouseMoved(MouseEvent me
    ) {
    }
}
