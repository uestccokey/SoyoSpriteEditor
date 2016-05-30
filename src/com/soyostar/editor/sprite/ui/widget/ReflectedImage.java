/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.sprite.ui.widget;

import com.soyostar.editor.util.UIUtil;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * 
 * @author Administrator
 */
@SuppressWarnings("serial")
public class ReflectedImage extends JPanel {

    private BufferedImage img;

    /**
     * 
     */
    public ReflectedImage() {
        img = loadImage("apple.png");
        img = createReflectedImage(img);
        setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        g2d.drawImage(img, 30, 30, null);
    }

    // 因为Swing的back-buffer是不透明的，所以要自己创建一个透明图片来处理反射
    /**
     * 
     * @param img
     * @return
     */
    public static BufferedImage createReflectedImage(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage reflect = new BufferedImage(w, h * 2, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = reflect.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        // 绘制原图像
        g2.drawImage(img, 0, 0, null);

        // 绘制镜面图像
        g2.translate(0, h + 1);
        g2.drawImage(UIUtil.drawWaveRenderer(img, true, 0.8, 0, 0, w, h, 0, 0), 0, 0, w, (int) (h / 1.5), null);

        // 绘制透明的渐变
        g2.setPaint(new GradientPaint(0, 0, new Color(1.0f, 1.0f, 1.0f, 0.9f), 0, (int) (h / 2),
            new Color(1.0f, 1.0f, 1.0f, 0.0f)));
        // 关键就在DstIn，只使用源图像的透明度，目标图像不透明的地方继续不透明:
        // Ar = Ad * As
        // Cr = Cd * As
        g2.setComposite(AlphaComposite.DstIn);
        g2.fillRect(0, 0, w, h);

        g2.dispose();

        return reflect;
    }

    // 读取图片
    /**
     * 
     * @param path
     * @return
     */
    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void createGuiAndShow() {
        JFrame frame = new JFrame("Reflected Image");
        frame.getContentPane().add(new ReflectedImage());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 520);
        frame.setLocationRelativeTo(null);
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                createGuiAndShow();
            }
        });
    }
}
