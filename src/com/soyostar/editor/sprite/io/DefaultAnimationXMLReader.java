/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.sprite.io;

import com.soyostar.editor.sprite.main.AppData;
import com.soyostar.editor.sprite.model.Animation;
import com.soyostar.editor.sprite.model.Clip;
import com.soyostar.editor.sprite.model.Frame;
import com.soyostar.editor.sprite.model.Picture;
import com.soyostar.editor.sprite.model.Region;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *
 * @author Administrator
 */
public class DefaultAnimationXMLReader implements IAnimationReader {

    public void readProperties(Element node, HashMap<String, String> properties) {
        if (node.getName().equalsIgnoreCase("property")) {
            properties.put(node.attributeValue("name"), node.attributeValue("value"));
        } else if (node.getName().equalsIgnoreCase("properties")) {
            for (Iterator i = node.elementIterator("property"); i.hasNext();) {
                Element element = (Element) i.next();
                readProperties(element, properties);
            }
        }
    }

    public Animation readAnimation(String aniFile) throws Exception {
        Animation ani = new Animation();
        SAXReader sax = new SAXReader();
        try {
            Document document = sax.read(new File(aniFile));
            // 得到根元素
            Element root = document.getRootElement();
            if (root.attributeValue("name") != null) {
                ani.setName(root.attributeValue("name"));
            }
            if (root.attributeValue("gid") != null) {
                ani.setIndex(Integer.parseInt(root.attributeValue("gid")));
            }
            if (root.element("properties") != null) {
                readProperties(root.element("properties"), ani.getProperties());
            }
            ArrayList<Clip> clips = new ArrayList<Clip>();
            for (Iterator i = root.elementIterator("picture"); i.hasNext();) {
                Element pictureElement = (Element) i.next();
                Picture picture = new Picture();
                picture.setSourceImageFile(pictureElement.attributeValue("file"));
                picture.setGid(Integer.parseInt(pictureElement.attributeValue("gid")));
                for (Iterator j = pictureElement.elementIterator("clip"); j.hasNext();) {
                    Clip clip = new Clip();
                    Element tileElement = (Element) j.next();
                    clip.setPicture(picture);
                    clip.setGid(Integer.parseInt(tileElement.attributeValue("gid")));
                    clip.setSourcePoint(new Point(Integer.parseInt(tileElement.attributeValue("x")), Integer.parseInt(tileElement.attributeValue("y"))));
                    clip.setW(Integer.parseInt(tileElement.attributeValue("width")));
                    clip.setH(Integer.parseInt(tileElement.attributeValue("height")));
                    picture.addTile(clip);
                    clips.add(clip);
                }
            }
            for (Iterator i = root.elementIterator("frame"); i.hasNext();) {
                Element frameElement = (Element) i.next();
                Frame frame = new Frame();
                frame.setName(frameElement.attributeValue("name"));
                frame.setDelay(Integer.parseInt(frameElement.attributeValue("delay")));
                frame.setAnimation(ani);
                for (Iterator j = frameElement.elementIterator("clip"); j.hasNext();) {
                    Element tileElement = (Element) j.next();
                    Clip clip = (Clip) clips.get(Integer.parseInt(tileElement.attributeValue("gid"))).clone();
                    clip.setFramePoint(new Point(Integer.parseInt(tileElement.attributeValue("x")), Integer.parseInt(tileElement.attributeValue("y"))));
                    clip.setMirror(Boolean.parseBoolean(tileElement.attributeValue("mirror")));
                    clip.setRotation(Integer.parseInt(tileElement.attributeValue("rotate")));
                    clip.setTransparency(Integer.parseInt(tileElement.attributeValue("alpha")));
                    clip.setZoom(Float.parseFloat(tileElement.attributeValue("zoom")));
                    frame.addTile(clip);
                }
                for (Iterator j = frameElement.elementIterator("region"); j.hasNext();) {
                    Element regionElement = (Element) j.next();
                    Region region = new Region();
                    region.index = Integer.parseInt(regionElement.attributeValue("id"));
                    region.x = Integer.parseInt(regionElement.attributeValue("x"));
                    region.y = Integer.parseInt(regionElement.attributeValue("y"));
                    region.width = (Integer.parseInt(regionElement.attributeValue("width")));
                    region.height = (Integer.parseInt(regionElement.attributeValue("height")));
                    frame.addRegion(region);
                }
                ani.addFrame(frame);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ani;
    }
}
