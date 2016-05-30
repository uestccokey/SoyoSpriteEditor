/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.sprite.ui.widget;

import com.soyostar.editor.sprite.model.Picture;
import com.soyostar.editor.sprite.model.Clip;
import com.soyostar.editor.sprite.main.AppData;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Scrollable;

/**
 *
 * @author Administrator
 */
public class HandlePicturePane extends JPanel implements Scrollable,
        //        DragGestureListener,
        MouseListener, MouseMotionListener {

    /**
     *
     */
    public static final int ZOOM_NORMALSIZE = 5;
    /**
     *
     */
    protected double zoom = 1.0;                    //正常缩放级别为1
    /**
     *
     */
    protected int zoomLevel = ZOOM_NORMALSIZE;      //初始话为正常缩放级别
    /**
     *
     */
    protected static double[] zoomLevels = {
        0.0625, 0.125, 0.25, 0.5, 0.75, 1.0, 1.5, 2.0, 2.5, 3.0
    };
    private Picture pic;
//    private SpriteEditorDialog sed;

    /**
     *
     * @param sed
     * @param pic
     */
    public HandlePicturePane(Picture pic) {
        this.pic = pic;
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    /**
     *
     * @return
     */
    public boolean zoomIn() {
        if (zoomLevel < zoomLevels.length - 1) {
            setZoomLevel(zoomLevel + 1);
        }

        return zoomLevel < zoomLevels.length - 1;
    }

    /**
     *
     * @return
     */
    public boolean zoomOut() {
        if (zoomLevel > 0) {
            setZoomLevel(zoomLevel - 1);
        }

        return zoomLevel > 0;
    }

    /**
     *
     * @param zoom
     */
    public void setZoom(double zoom) {
        if (zoom > 0) {
            this.zoom = zoom;
            //revalidate();
            setSize(getPreferredSize());
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (pic == null) {
            return new Dimension(0, 0);
        } else {
            int w = (int) (pic.getSourceImage().getWidth() * this.zoom);
            int h = (int) (pic.getSourceImage().getHeight() * this.zoom);
            return new Dimension(w, h);
        }
    }
    private int selectedIndex = -1;

    /**
     *
     * @param index
     */
    public void setSelectedIndex(int index) {
        this.selectedIndex = index;
        this.updateUI();
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
            g.draw3DRect((int) (tile.getSourcePoint().x * zoom), (int) (tile.getSourcePoint().y * zoom),
                    (int) (tile.getW() * zoom), (int) (tile.getH() * zoom), true);
        }
        if (Math.abs((int) (createTraget.getX() - createOrgine.getX())) > 0
                && Math.abs((int) (createTraget.getY() - createOrgine.getY())) > 0) {
            g2d.setColor(Color.RED);
            g.draw3DRect((int) Math.min(createTraget.getX(), createOrgine.getX()), (int) Math.min(createTraget.getY(), createOrgine.getY()),
                    Math.abs((int) (createTraget.getX() - createOrgine.getX())), Math.abs((int) (createTraget.getY() - createOrgine.getY())), false);
            g2d.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_ATOP, 0.2f));
            g.fillRect((int) Math.min(createTraget.getX(), createOrgine.getX()), (int) Math.min(createTraget.getY(), createOrgine.getY()),
                    Math.abs((int) (createTraget.getX() - createOrgine.getX())), Math.abs((int) (createTraget.getY() - createOrgine.getY())));
        }

        if (selectedIndex != -1) {
            g2d.setColor(Color.RED);
            Clip tile = pic.getTile(selectedIndex);
            if (tile != null) {
                g.draw3DRect((int) (tile.getSourcePoint().x * zoom), (int) (tile.getSourcePoint().y * zoom),
                        (int) (tile.getW() * zoom), (int) (tile.getH() * zoom), false);
                g2d.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_ATOP, 0.2f));
                g.fillRect((int) (tile.getSourcePoint().x * zoom), (int) (tile.getSourcePoint().y * zoom),
                        (int) (tile.getW() * zoom), (int) (tile.getH() * zoom));
            }

        }
    }

    /**
     *
     * @param zoomLevel
     */
    public void setZoomLevel(int zoomLevel) {
        if (zoomLevel >= 0 && zoomLevel < zoomLevels.length) {
            this.zoomLevel = zoomLevel;
            setZoom(zoomLevels[zoomLevel]);
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
    private Point moveOrgine = new Point();
    private Point moveTraget = new Point();
    private Cursor moveCursor = new Cursor(Cursor.MOVE_CURSOR);
    private Point createOrgine = new Point();
    private Point createTraget = new Point();
    private Point resizeOrgine = new Point();
    private Point resizeTraget = new Point();

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        int cx = (int) (e.getX() / zoom);
        int cy = (int) (e.getY() / zoom);
        for (int i = 0, n = pic.getTiles().size(); i < n; i++) {
            Clip tile = pic.getTile(i);
            if (tile.pictureContains(cx, cy)) {
                selectedIndex = i;
                if (e.getButton() == MouseEvent.BUTTON1) {//左键按下显示移动鼠标
                    if (e.isShiftDown()) {//按住shift，可以调整图块大小
                        this.resizeOrgine.setLocation(e.getX(), e.getY());
                        this.resizeTraget.setLocation(e.getX(), e.getY());
                    } else {
                        moveOrgine.setLocation(e.getX(), e.getY());
                        moveTraget.setLocation(e.getX(), e.getY());
                    }
                    setCursor(this.moveCursor);
                } else if (e.getButton() == MouseEvent.BUTTON3) {//右键按下表示擦除
                    pic.removeTile(selectedIndex);
                    selectedIndex = -1;
                }
                repaint();
                return;
            }
        }
        selectedIndex = -1;
        this.createOrgine.setLocation(e.getX(), e.getY());
        this.createTraget.setLocation(e.getX(), e.getY());
        repaint();
    }
    private AppData data = AppData.getInstance();

    public void mouseReleased(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1) {
            int w = (int) (Math.abs(createTraget.getX() - createOrgine.getX()) / zoom);
            int h = (int) (Math.abs(createTraget.getY() - createOrgine.getY()) / zoom);
            if (w > 0 && h > 0) {
                Clip tile = new Clip();
                tile.setSourcePoint(new Point((int) Math.min(createTraget.getX() / zoom, createOrgine.getX() / zoom), (int) Math.min(createTraget.getY() / zoom, createOrgine.getY() / zoom)));
                tile.setW(w);
                tile.setH(h);
                pic.addTile(tile);
            }
            createTraget.x = createOrgine.x;
            createTraget.y = createOrgine.y;
        }
        setCursor(Cursor.getDefaultCursor());
        repaint();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == MouseEvent.BUTTON1_MASK) {

            if (selectedIndex == -1) {//没有选中任何图块时，负责创建图块
                int cx = e.getX();
                int cy = e.getY();
                if (cx < 0) {
                    cx = 0;
                }
                if (cy < 0) {
                    cy = 0;
                }
                if (cx > pic.getSourceImage().getWidth() * zoom) {
                    cx = (int) (pic.getSourceImage().getWidth() * zoom);
                }
                if (cy > pic.getSourceImage().getHeight() * zoom) {
                    cy = (int) (pic.getSourceImage().getHeight() * zoom);
                }
                createTraget.setLocation(cx, cy);
            } else {//选中某一图块时，负责移动图块
                Clip t = pic.getTile(selectedIndex);
                if (e.isShiftDown()) {
                    this.resizeTraget.setLocation(e.getX(), e.getY());
                    int mx = this.resizeTraget.x - this.resizeOrgine.x;
                    int my = this.resizeTraget.y - this.resizeOrgine.y;
                    if (Math.abs(mx) >= zoom) {
                        int ww = t.getW() + (int) (mx / zoom);
                        if (ww < 0) {
                            ww = 0;
                        }
                        if (ww > pic.getSourceImage().getWidth() - t.getSourcePoint().x) {
                            ww = pic.getSourceImage().getWidth() - t.getSourcePoint().x;
                        }
                        t.setW(ww);
                        this.resizeOrgine.x = this.resizeTraget.x;
                    }
                    if (Math.abs(my) >= zoom) {
                        int hh = t.getH() + (int) (my / zoom);
                        if (hh < 0) {
                            hh = 0;
                        }
                        if (hh > pic.getSourceImage().getHeight() - t.getSourcePoint().y) {
                            hh = pic.getSourceImage().getHeight() - t.getSourcePoint().y;
                        }
                        t.setH(hh);
                        this.resizeOrgine.y = this.resizeTraget.y;
                    }
                } else {

                    this.moveTraget.setLocation(e.getX(), e.getY());
                    int mx = this.moveTraget.x - this.moveOrgine.x;
                    int my = this.moveTraget.y - this.moveOrgine.y;
                    if (Math.abs(mx) >= zoom) {
                        int xx = t.getSourcePoint().x + (int) (mx / zoom);
                        if (xx < 0) {
                            xx = 0;
                        }
                        if (xx > pic.getSourceImage().getWidth() - t.getW()) {
                            xx = pic.getSourceImage().getWidth() - t.getW();
                        }
                        t.setSourcePoint(new Point(xx, t.getSourcePoint().y));
                        this.moveOrgine.x = this.moveTraget.x;
                    }
                    if (Math.abs(my) >= zoom) {
                        int yy = t.getSourcePoint().y + (int) (my / zoom);
                        if (yy < 0) {
                            yy = 0;
                        }
                        if (yy > pic.getSourceImage().getHeight() - t.getH()) {
                            yy = pic.getSourceImage().getHeight() - t.getH();
                        }
                        t.setSourcePoint(new Point(t.getSourcePoint().x, yy));
                        this.moveOrgine.y = this.moveTraget.y;
                    }
                }

            }
        }
        repaint();
    }

    public void mouseMoved(MouseEvent e) {
    }
}
