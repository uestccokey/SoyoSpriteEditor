/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.sprite.ui.widget;

import com.soyostar.editor.sprite.model.Picture;
import com.soyostar.editor.sprite.main.AppData;
import com.soyostar.editor.sprite.main.SpriteEditorPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

/**
 *
 * @author Administrator
 */
public class ChoosePicturePane extends JPanel implements MouseListener, MouseMotionListener {

    private Dimension viewSize = new Dimension(128, 128);
    private static final int titleSize = 16;
    private int selectedIndex = -1;
    private Picture selectedPicture = null;
    private SpriteEditorPanel sed;
    private static final int WS = 16;
    private static final int HS = 16;

    /**
     *
     * @return
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     *
     * @return
     */
    public Picture getSelectedPicture() {
        return selectedPicture;
    }

    /**
     *
     * @param sed
     */
    public ChoosePicturePane(SpriteEditorPanel sed) {
        this.sed = sed;
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        if (AppData.getInstance().getCurProject() == null) {
            return size;
        }
        int len = AppData.getInstance().getCurProject().getPictures().size();
        return new Dimension(Math.max(size.width, this.viewSize.width + 2 * WS),
                Math.max(size.height, len * (this.viewSize.height + HS + this.titleSize) + HS));
    }

    /**
     *
     * @param sel
     */
    public void setSelectedIndex(int sel) {
        selectedIndex = sel;
        selectedPicture = AppData.getInstance().getCurProject().getPicture(sel);
        this.updateUI();
    }

    /**
     * 
     * @param x
     * @param y
     */
    public void setSelectedPicture(int x, int y) {
        int len = AppData.getInstance().getCurProject().getPictures().size();
        if (len <= 0) {
            return;
        }
        for (int i = 0; i < len; i++) {
            if (x > WS && y > HS + i * (this.viewSize.height + titleSize + HS) && y < HS + i * (this.viewSize.height + titleSize + HS) + this.viewSize.height + titleSize && x < WS + this.viewSize.width) {
                this.setSelectedIndex(i);
                return;
            }
        }
        this.setSelectedIndex(-1);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        if (AppData.getInstance().getCurProject() != null) {
            for (int i = 0; i < AppData.getInstance().getCurProject().getPictures().size(); i++) {
                Picture pic = AppData.getInstance().getCurProject().getPicture(i);
                g.setColor(Color.WHITE);
                g.fillRect(WS, HS + titleSize + i * titleSize + i * viewSize.height + HS * i, viewSize.width - 1, viewSize.height - 1);
                g.setColor(Color.LIGHT_GRAY);
                for (int y = HS / 8 + titleSize / 8 + i * (titleSize + viewSize.height) / 8; y < HS / 8 + titleSize / 8 + i * (titleSize + viewSize.height) / 8 + viewSize.height / 8; y++) {
                    for (int x = WS / 8; x < (WS + viewSize.width) / 8; x++) {
                        if ((y + x) % 2 == 1) {
                            g.fillRect(x * 8, y * 8 + 16 * i, 8, 8);
                        }
                    }
                }
                pic.paintPreview(g,
                        WS + 1, i * (this.viewSize.height + this.titleSize + HS) + HS + titleSize + 1,
                        this.viewSize.width - 2, this.viewSize.height - 2);
                Color labelColor = Color.BLACK;
                if (this.selectedIndex == i) {
                    labelColor = Color.BLUE;
                }
                for (int j = 0; j < this.titleSize; j++) {
                    g.setColor(new Color(labelColor.getRed(), labelColor.getGreen(), labelColor.getBlue(), 127 + j * 8));
                    g.drawLine(WS, j + HS + i * (this.viewSize.height + titleSize + HS), WS + this.viewSize.width, i * (this.viewSize.height + titleSize + HS) + j + HS);
                }
                if (this.selectedIndex == i) {
                    g.setColor(Color.BLUE);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.draw3DRect(WS, HS + titleSize + i * (this.viewSize.height + HS + titleSize), this.viewSize.width, this.viewSize.height, true);
                g.setColor(Color.WHITE);
                g.drawString(pic.getSourceImageFile(), WS + 2, i * (this.viewSize.height + titleSize + HS) + HS + titleSize - 2);

            }
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        setSelectedPicture(e.getX(), e.getY());
        sed.handlePictureScrollPane.setViewportView(sed.getHandlePicturePanes().get(selectedPicture));
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }
}
