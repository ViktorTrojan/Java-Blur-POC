package components.button;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

class Animation {

    public Animator animator;
    public Point pressedPoint;
    public int targetSize;
    public float alpha = 0.5f, animatSize = 0;
    public Color effectColor;
    private JButton btn;

    public Animation(Point _pressedPoint, Color _effectColor, int _targetSize, JButton _btn) {
        btn = _btn;
        effectColor = _effectColor;
        targetSize = _targetSize;
        pressedPoint = _pressedPoint;
        animator = new Animator(700, target);
        animator.setAcceleration(0.3f);
        animator.setDeceleration(0.7f);
        animator.setResolution(0);

        animator.start();
    }

    TimingTarget target = new TimingTargetAdapter() {
        @Override
        public void timingEvent(float fraction) {
            if (fraction > 0.5f) {
                alpha = 1 - fraction;
            }
            animatSize = fraction * targetSize;
            btn.repaint();
        }
    };
}

public class Button extends JButton {

    public Color getEffectColor() {
        return effectColor;
    }

    public void setEffectColor(Color effectColor) {
        this.effectColor = effectColor;
    }

    public int getRounded() {
        return rounded;
    }

    public void setRounded(int rounded) {
        this.rounded = rounded;
    }

    public boolean isColorVariants() {
        return colorVariants;
    }

    public void setColorVariants(boolean colorVariants) {
        this.colorVariants = colorVariants;
    }

    public Color getOutlineHoverColor() {
        return outlineHoverColor;
    }

    public void setOutlineHoverColor(Color outlineHoverColor) {
        this.outlineHoverColor = outlineHoverColor;
    }

    public Color getHoverColor() {
        return hoverColor;
    }

    public void setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;
    }

    private boolean isHovered = false, colorVariants = false;
    private int rounded = 12;
    private Color effectColor = new Color(90, 125, 240), outlineHoverColor = new Color(137, 176, 212), hoverColor = new Color(247, 247, 247);
    private ArrayList<Animation> rippleAnimation;

    public Button() {
        rippleAnimation = new ArrayList<>();
        setContentAreaFilled(false);
        setBorder(new EmptyBorder(5, 0, 5, 0));
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                rippleAnimation.removeIf((t) -> { // remove elements
                    return t.alpha == 0;
                });
                Color c = effectColor;
                if (colorVariants) {
                    float[] hsv = new float[3];
                    Color.RGBtoHSB(effectColor.getRed(), effectColor.getGreen(), effectColor.getBlue(), hsv); // convert Color to hsb
                    c = new Color(Color.HSBtoRGB((hsv[0] + (float)Math.random() * 0.1f - 0.05f) % 1, hsv[1], hsv[2])); // randomize hue and convert back to RGB
                }

                rippleAnimation.add(new Animation(me.getPoint(), c, Math.max(getWidth(), getHeight()) * 2, Button.this));
            }

            @Override
            public void mouseExited(MouseEvent me) {
                isHovered = false;
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                isHovered = true;
            }
        });
    }

    void drawRippleEffect(Graphics2D g2) {
        for (Animation a : rippleAnimation) {
            g2.setColor(a.effectColor);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, a.alpha));
            g2.fillOval((int) (a.pressedPoint.x - a.animatSize / 2), (int) (a.pressedPoint.y - a.animatSize / 2), (int) a.animatSize, (int) a.animatSize);
        }
    }

    @Override
    protected void paintComponent(final Graphics g) {
        int width = getWidth();
        int height = getHeight();
        
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(isHovered ? hoverColor : getBackground());
        g2.fillRoundRect(0, 0, width, height, rounded, rounded);
        g2.setColor(isHovered ? outlineHoverColor : getBackground().darker());
        g2.drawRoundRect(0, 0, width - 1, height - 1, rounded, rounded);

        if(isEnabled()) drawRippleEffect(g2); // only draw Ripple Effect when object is Enabled!

        g2.dispose();
        g.drawImage(img, 0, 0, null);
        super.paintComponent(g);
    }
}
