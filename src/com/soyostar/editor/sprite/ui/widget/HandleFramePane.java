/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.sprite.ui.widget;

import com.soyostar.editor.sprite.model.Animation;
import com.soyostar.editor.sprite.model.Frame;
import com.soyostar.editor.sprite.model.Clip;
import com.soyostar.editor.sprite.main.AppData;
import com.soyostar.editor.util.TileTransferable;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.Scrollable;

/**
 *
 * @author Administrator
 */
public class HandleFramePane extends JPanel implements DropTargetListener, Runnable, Scrollable, MouseListener, MouseMotionListener, KeyListener {

    /**
     *
     */
    public static final int FRAME_PANEL_W = 512;
    /**
     *
     */
    public static final int FRAME_PANEL_H = 512;
    /**
     *
     */
    public static final int ZOOM_NORMALSIZE = 5;
    private float zoom = 1.0f;                    //正常缩放级别为1
    private int zoomLevel = ZOOM_NORMALSIZE;      //初始话为正常缩放级别
    private static float[] zoomLevels = {
        0.0625f, 0.125f, 0.25f, 0.5f, 0.75f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f
    };
    private int selectedTileIndex = -1;
    private boolean isPlay = false;//播放标志
    private boolean isLoop = true;//循环播放标志
    private boolean isShowOutLine = false;//显示外框
    private AppData data = AppData.getInstance();

    /**
     *
     */
    public HandleFramePane() {
        new Thread(this).start();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
    }
    private Frame curFrame;//当前正在处理的Frame

    /**
     *
     * @return
     */
    public Frame getCurFrame() {
        return curFrame;
    }

    /**
     *
     * @param curFrame
     */
    public void setCurFrame(Frame curFrame) {
        this.curFrame = curFrame;
        repaint();
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

    public void dragEnter(DropTargetDragEvent dtde) {
    }

    public void dragOver(DropTargetDragEvent dtde) {
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    public void dragExit(DropTargetEvent dte) {
    }

    public void drop(DropTargetDropEvent event) {
        if (curFrame != null) {
            try {
                Transferable transferable = event.getTransferable();
                Object o = transferable.getTransferData(
                        TileTransferable.TILE_FLAVOR);
                if (o != null) {
                    if (o instanceof Clip) {
                        Clip t = (Clip) o;
                        Point p = event.getLocation();
                        t.setFramePoint(new Point(p.x - t.getW() / 2, p.y - t.getH() / 2));
                        t.setMirror(false);
                        t.setRotation(0);
                        t.setTransparency(0);
                        t.setRenderType(0);
                        t.setZoom(1.0f);
                        curFrame.addTile(t);
                        setSelectedTileIndex(curFrame.getTiles().size() - 1);
                    }
                }
                event.dropComplete(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public Clip getSelectedTile() {
        if (curFrame == null) {
            return null;
        }
        return curFrame.getTile(selectedTileIndex);
    }

    /**
     *
     * @return
     */
    public int getSelectedTileIndex() {
        return selectedTileIndex;
    }

    /**
     *
     * @param selectedTileIndex
     */
    public void setSelectedTileIndex(int selectedTileIndex) {
        this.selectedTileIndex = selectedTileIndex;
        repaint();
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
     * @param zoomLevel
     */
    public void setZoomLevel(int zoomLevel) {
        if (zoomLevel >= 0 && zoomLevel < zoomLevels.length) {
            this.zoomLevel = zoomLevel;
            setZoom(zoomLevels[zoomLevel]);
        }
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
    public void setZoom(float zoom) {
        if (zoom > 0) {
            this.zoom = zoom;
            setSize(getPreferredSize());
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((int) (FRAME_PANEL_W * zoom), (int) (FRAME_PANEL_H * zoom));
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

        int tw = (int) (FRAME_PANEL_W * zoom);
        int th = (int) (FRAME_PANEL_H * zoom);

        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 0, tw, th);

        g2d.setColor(Color.BLACK);
        g2d.drawLine(0, th >> 1, tw, th >> 1);
        g2d.drawLine(tw >> 1, 0, tw >> 1, th);
        if (curFrame != null) {
            curFrame.paint(g, zoom);
            if (this.isShowOutLine) {
                for (int i = 0; i < curFrame.getTiles().size(); i++) {
                    g.setColor(Color.BLUE);
                    Clip clip = curFrame.getTile(i);
                    int x = 0, y = 0, w = 0, h = 0;
                    switch (clip.getRotation()) {
                        case 0:
                        case 180:
                            x = (int) (clip.getFramePoint().x * zoom);
                            y = (int) (clip.getFramePoint().y * zoom);
                            w = (int) (clip.getW() * zoom);
                            h = (int) (clip.getH() * zoom);
                            break;
                        case 90:
                        case 270:
                            x = (int) (clip.getFramePoint().x * zoom) - ((int) (clip.getH() * zoom) / 2 - (int) (clip.getW() * zoom) / 2);
                            y = (int) (clip.getFramePoint().y * zoom) + ((int) (clip.getH() * zoom) / 2 - (int) (clip.getW() * zoom) / 2);
                            w = (int) (clip.getH() * zoom);
                            h = (int) (clip.getW() * zoom);
                            break;
                    }
                    g.drawRect(x, y, w, h);
                }
            }
            if (selectedTileIndex >= 0 && selectedTileIndex <= curFrame.getTiles().size() - 1) {
                g.setColor(Color.RED);
                Clip clip = curFrame.getTile(selectedTileIndex);
                int x = 0, y = 0, w = 0, h = 0;
                switch (clip.getRotation()) {
                    case 0:
                    case 180:
                        x = (int) (clip.getFramePoint().x * zoom);
                        y = (int) (clip.getFramePoint().y * zoom);
                        w = (int) (clip.getW() * zoom);
                        h = (int) (clip.getH() * zoom);
                        break;
                    case 90:
                    case 270:
                        x = (int) (clip.getFramePoint().x * zoom) - ((int) (clip.getH() * zoom) / 2 - (int) (clip.getW() * zoom) / 2);
                        y = (int) (clip.getFramePoint().y * zoom) + ((int) (clip.getH() * zoom) / 2 - (int) (clip.getW() * zoom) / 2);
                        w = (int) (clip.getH() * zoom);
                        h = (int) (clip.getW() * zoom);
                        break;
                }
                g.drawRect(x, y, w, h);
            }
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setStroke(new BasicStroke(2.0F, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER));
            g2d.drawOval(16, 16, 32, 32);
            g2d.setFont(new Font("Serif", Font.BOLD, 16));
            g2d.drawString("" + curFrame.getID(), 29 - curFrame.getID() / 10 * 4, 37);
        }
    }

    /**
     *
     * @return
     */
    public boolean isIsLoop() {
        return isLoop;
    }

    /**
     *
     * @param isLoop
     */
    public void setIsLoop(boolean isLoop) {
        this.isLoop = isLoop;
    }

    /**
     *
     * @return
     */
    public boolean isIsPlay() {
        return isPlay;
    }

    /**
     *
     * @param isPlay
     */
    public void setIsPlay(boolean isPlay) {
        this.isPlay = isPlay;
    }

    public boolean isIsShowOutLine() {
        return isShowOutLine;
    }

    public void setIsShowOutLine(boolean isShowOutLine) {
        this.isShowOutLine = isShowOutLine;
    }

    /**
     *
     * @param loop
     */
    public void playOrPause(boolean loop) {
        isLoop = loop;
        if (isPlay) {
            isPlay = false;
        } else {
            isPlay = true;
            ani = data.getCurProject().getAnimation(data.getCurrentAnimationIndex());
        }
        selectedTileIndex = -1;
    }
    private Animation ani;

    public void run() {
        int index = 0;
        while (true) {
            if (isPlay && ani != null && ani.getFrame(index) != null) {
                Frame f = ani.getFrame(index);
                setCurFrame(f);
                index++;
                if (index >= ani.getFrames().size()) {
                    index = 0;
                    if (!isLoop) {
                        isPlay = false;
                    }
                }
                try {
                    Thread.sleep(f.getDelay());
                } catch (InterruptedException ex) {
                }
            } else {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                }
            }
        }
    }
    private Cursor moveCursor = new Cursor(Cursor.MOVE_CURSOR);
    private Point moveOrgine = new Point();
    private Point moveTraget = new Point();

    public void removeSelectedTile() {
        if (curFrame.getTile(selectedTileIndex) != null) {
            curFrame.removeTile(selectedTileIndex);
            selectedTileIndex = -1;
            repaint();
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {

        if (curFrame != null) {
            int cx = (int) (e.getX() / zoom);
            int cy = (int) (e.getY() / zoom);
            for (int i = 0, n = curFrame.getTiles().size(); i < n; i++) {
                Clip t = curFrame.getTiles().get(i);
                if (t.frameContains(cx, cy)) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        selectedTileIndex = i;
                        this.moveOrgine.setLocation(e.getX(), e.getY());
                        this.moveTraget.setLocation(e.getX(), e.getY());
                        setCursor(this.moveCursor);
//                    } else if (e.getButton() == MouseEvent.BUTTON3) {
//                        curFrame.removeTile(i);
                    }
                    repaint();
                    return;
                }
            }
        }
        selectedTileIndex = -1;
        repaint();
    }

    public void mouseReleased(MouseEvent e) {
        setCursor(Cursor.getDefaultCursor());
    }

    public void mouseEntered(MouseEvent e) {
        this.requestFocus();
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        if (selectedTileIndex < 0 || selectedTileIndex > curFrame.getTiles().size() - 1) {
            return;
        }
        Clip tile = curFrame.getTiles().get(selectedTileIndex);
        this.moveTraget.setLocation(e.getX(), e.getY());
        int mx = this.moveTraget.x - this.moveOrgine.x;
        int my = this.moveTraget.y - this.moveOrgine.y;
        if (Math.abs(mx) >= zoom) {
            int xx = tile.getFramePoint().x + (int) (mx / zoom);
            tile.getFramePoint().x = xx;
            this.moveOrgine.x = this.moveTraget.x;
        }
        if (Math.abs(my) >= zoom) {
            int yy = tile.getFramePoint().y + (int) (my / zoom);
            tile.getFramePoint().y = yy;
            this.moveOrgine.y = this.moveTraget.y;
        }
        repaint();
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (selectedTileIndex < 0 || selectedTileIndex > curFrame.getTiles().size() - 1) {
            return;
        }
        Clip tile = curFrame.getTiles().get(selectedTileIndex);
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                tile.getFramePoint().y--;
                break;
            case KeyEvent.VK_DOWN:
                tile.getFramePoint().y++;
                break;
            case KeyEvent.VK_LEFT:
                tile.getFramePoint().x--;
                break;
            case KeyEvent.VK_RIGHT:
                tile.getFramePoint().x++;
                break;
        }
        repaint();
    }

    public void keyReleased(KeyEvent e) {
    }
}
