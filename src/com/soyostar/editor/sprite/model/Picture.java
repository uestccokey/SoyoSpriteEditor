/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.sprite.model;

import com.soyostar.editor.sprite.main.AppData;
import com.soyostar.editor.util.PaintParameter;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author Administrator
 */
public class Picture {

    private BufferedImage sourceImage;
    private String sourceImageFile = "";//源图片位置
    private ArrayList<Clip> tiles = new ArrayList<Clip>();//切割得到的图块集
    private int gid = 0;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    /**
     *
     * @return
     */
    public BufferedImage getSourceImage() {
        return sourceImage;
    }

    /**
     *
     * @param sourceImage
     */
    public void setSourceImage(BufferedImage sourceImage) {
        this.sourceImage = sourceImage;
    }

    /**
     *
     * @return
     */
    public String getSourceImageFile() {
        return sourceImageFile;
    }

    /**
     *
     * @param file
     * @throws IOException
     */
    public void setSourceImageFile(String file) throws IOException {
        this.sourceImageFile = file;
        Image image = ImageIO.read(new File(AppData.getInstance().getCurProject().getPath()
                + File.separator + "image" + File.separator + "animation" + File.separator + file));
        if (image == null) {
            throw new IOException("Failed to load " + file);
        }
        sourceImage = new BufferedImage(
                image.getWidth(null),
                image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        sourceImage.getGraphics().drawImage(image, 0, 0, null);
    }
    //参数 g-画笔 par-绘制参数 x,y-要绘制的位置 frameZoom-帧引起的缩放

    /**
     *
     * @param g
     * @param pai
     * @param x
     * @param y
     * @param frameZoom
     */
    public void paint(Graphics g, PaintParameter pai, int x, int y, float frameZoom) {
        Graphics2D g2d = (Graphics2D) g;
        BufferedImage bImage = sourceImage.getSubimage(pai.getRec().x, pai.getRec().y, pai.getRec().width, pai.getRec().height);
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(x * frameZoom, y * frameZoom);
        switch (pai.getRotate()) {
            case 0:
                break;
            case 90:
                affineTransform.translate((pai.getRec().width / 2 + pai.getRec().height / 2) * frameZoom, (pai.getRec().height / 2 - pai.getRec().width / 2) * frameZoom);
                affineTransform.rotate(Math.toRadians(90));
                break;
            case 180:
                affineTransform.translate(pai.getRec().width * frameZoom, pai.getRec().height * frameZoom);
                affineTransform.rotate(Math.toRadians(180));
                break;
            case 270:
                affineTransform.translate((pai.getRec().width / 2 - pai.getRec().height / 2) * frameZoom, (pai.getRec().width / 2 + pai.getRec().height / 2) * frameZoom);
                affineTransform.rotate(Math.toRadians(270));
                break;
        }

        if (pai.isMirror()) {
            affineTransform.scale(-frameZoom, frameZoom);
            affineTransform.translate(-pai.getRec().width, 0);
        } else {
            affineTransform.scale(frameZoom, frameZoom);
        }
        g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_ATOP, (255 - pai.getTransparency() * 1.0f) / 255));
        g2d.drawImage(bImage, affineTransform, null);
        g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_ATOP, 1.0f));
    }
    //按照原样进行缩放预览

    /**
     *
     * @param g
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public void paintPreview(Graphics g, int x, int y, int w, int h) {
        if (sourceImage.getWidth() == sourceImage.getHeight()) {
            g.drawImage(sourceImage.getScaledInstance(w, h, BufferedImage.SCALE_DEFAULT), x, y, null);
        } else {
            if (sourceImage.getWidth() > sourceImage.getHeight()) {
                double ratio = sourceImage.getWidth() / sourceImage.getHeight();
                g.drawImage(sourceImage.getScaledInstance(w, (int) (h / ratio), BufferedImage.SCALE_DEFAULT), x, y, null);
            } else {
                double ratio = sourceImage.getHeight() / sourceImage.getWidth();
                g.drawImage(sourceImage.getScaledInstance((int) (w / ratio), h, BufferedImage.SCALE_DEFAULT), x, y, null);
            }
        }
    }

    /**
     *
     * @return
     */
    public ArrayList<Clip> getTiles() {
        return tiles;
    }

    /**
     *
     * @param id
     * @return
     */
    public Clip getTile(int id) {
        if (id < 0 || id >= tiles.size()) {
//            System.out.println("id:" + id + " size:" + tiles.size());
            return null;
        }
        return tiles.get(id);
    }

    /**
     *
     * @param tiles
     */
    public void setTiles(ArrayList<Clip> tiles) {
        this.tiles = tiles;
    }

    /**
     *
     * @param tile
     */
    public void addTile(Clip tile) {
        tile.setPicture(this);
        tiles.add(tile);
//        data.getCurProject().addClip(tile);
    }
//    private AppData data = AppData.getInstance();

    /**
     *
     * @param id
     */
    public void removeTile(int id) {
        tiles.remove(id);
//        data.getCurProject().removeClip(id);
    }
}
