/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.sprite.ui.widget;

import com.soyostar.editor.sprite.model.Picture;
import com.soyostar.editor.sprite.model.Clip;
import com.soyostar.editor.util.TileTransferable;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Scrollable;

/**
 *
 * @author Administrator
 */
public class ShowPicturePane extends JPanel implements Scrollable, MouseListener, DragGestureListener {

    private Picture pic;

    /**
     *
     * @param sed
     * @param pic
     */
    public ShowPicturePane(Picture pic) {
        this.pic = pic;
        this.addMouseListener(this);
    }

    @Override
    public Dimension getPreferredSize() {
        if (pic == null) {
            return new Dimension(0, 0);
        } else {
            int w = pic.getSourceImage().getWidth();
            int h = pic.getSourceImage().getHeight();
            return new Dimension(w, h);
        }
    }

    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 16;
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return (visibleRect.height / 16) * 16;
    }

    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.clearRect(0, 0, this.getWidth(), this.getHeight());
        if (pic == null) {
            return;
        }
        Dimension preSize = this.getPreferredSize();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, preSize.width, preSize.height);
        BufferedImage img = pic.getSourceImage();
        g2d.drawImage(img, 0, 0, preSize.width, preSize.height, 0, 0, img.getWidth(), img.getHeight(), null);
        for (int j = 0; j < pic.getTiles().size(); j++) {
            g2d.setColor(Color.GREEN);
            Clip tile = pic.getTiles().get(j);
            g.draw3DRect(tile.getSourcePoint().x, tile.getSourcePoint().y,
                    tile.getW(), tile.getH(), true);
        }
        if (selectedIndex != -1) {
            g2d.setColor(Color.RED);
            Clip tile = pic.getTile(selectedIndex);
            if (tile != null) {

                g.draw3DRect(tile.getSourcePoint().x, tile.getSourcePoint().y,
                        tile.getW(), tile.getH(), false);
                g2d.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_ATOP, 0.2f));
                g.fillRect(tile.getSourcePoint().x, tile.getSourcePoint().y,
                        tile.getW(), tile.getH());
            }
        }
    }
    private int selectedIndex = -1;

    public void dragGestureRecognized(DragGestureEvent event) {
        if (selectedIndex != -1) {
            Clip t;
            try {
                t = (Clip) pic.getTile(selectedIndex).clone();
                if (t != null) {
                    Transferable transferable = new TileTransferable(t);
                    event.startDrag(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR), transferable);
                }
            } catch (CloneNotSupportedException ex) {
                ex.printStackTrace();
            }

        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {

        for (int i = 0, n = pic.getTiles().size(); i < n; i++) {
            Clip tile = pic.getTile(i);
            if (tile.pictureContains(e.getX(), e.getY())) {
                selectedIndex = i;
                repaint();
                return;
            }
        }
        selectedIndex = -1;
        repaint();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
