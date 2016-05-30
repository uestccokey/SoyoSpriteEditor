/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.sprite.io;

import com.soyostar.editor.info.SoftInformation;
import com.soyostar.editor.sprite.model.Animation;
import com.soyostar.editor.sprite.model.Clip;
import com.soyostar.editor.sprite.model.Frame;
import com.soyostar.editor.sprite.model.Picture;
import com.soyostar.editor.sprite.model.Region;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 *
 * @author Administrator
 */
public class DefaultAnimationXMLWriter implements IAnimationWriter {

    public void writeProperties(HashMap<String, String> properties, Element element) {
        if (!properties.isEmpty()) {
            Iterator iter1 = properties.entrySet().iterator();
            Element propertysElement = element.addElement("properties");
            while (iter1.hasNext()) {
                Element propertyElement = propertysElement.addElement("property");
                java.util.Map.Entry entry = (java.util.Map.Entry) iter1.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                propertyElement.addAttribute("name", key.toString());
                propertyElement.addAttribute("value", val.toString());
            }
        }
    }

    public void writeAnimation(Animation ani, String filename) throws Exception {
        Document doc = DocumentHelper.createDocument();
        Element aniElement = doc.addElement("animation");
        aniElement.addAttribute("version", SoftInformation.majorVersion + "." + SoftInformation.minorVersion);
        aniElement.addAttribute("name", "" + ani.getName());
        aniElement.addAttribute("gid", "" + ani.getIndex());
        writeProperties(ani.getProperties(), aniElement);
        ArrayList<Picture> pictures = ani.getPictures();
        int pictureN = pictures.size();
        int clipN = 0;
        for (int i = 0; i < pictureN; i++) {
            Picture picture = ani.getPictures().get(i);
            Element pictureElement = aniElement.addElement("picture");
            pictureElement.addAttribute("file", picture.getSourceImageFile());
            pictureElement.addAttribute("gid", ""+picture.getGid());
            for (int j = 0; j < picture.getTiles().size(); j++) {
                Clip clip = picture.getTile(j);
                clip.setGid(clipN++);
                Element clipElement = pictureElement.addElement("clip");
                clipElement.addAttribute("gid", "" + clip.getGid());
                clipElement.addAttribute("x", "" + clip.getSourcePoint().x);
                clipElement.addAttribute("y", "" + clip.getSourcePoint().y);
                clipElement.addAttribute("width", "" + clip.getW());
                clipElement.addAttribute("height", "" + clip.getH());
            }
        }
        int frameN = ani.getFrames().size();
        for (int i = 0; i < frameN; i++) {
            Frame frame = ani.getFrame(i);
            Element frameElement = aniElement.addElement("frame");
            frameElement.addAttribute("name", frame.getName());
            frameElement.addAttribute("delay", "" + frame.getDelay());
            for (int j = 0; j < frame.getTiles().size(); j++) {
                Clip clip = frame.getTile(j);
                Element clipElement = frameElement.addElement("clip");
                clipElement.addAttribute("gid", "" + clip.getOriginal().getGid());
                clipElement.addAttribute("x", "" + clip.getFramePoint().x);
                clipElement.addAttribute("y", "" + clip.getFramePoint().y);
                clipElement.addAttribute("alpha", "" + clip.getTransparency());
                clipElement.addAttribute("mirror", "" + clip.isMirror());
                clipElement.addAttribute("rotate", "" + clip.getRotation());
                clipElement.addAttribute("zoom", "" + clip.getZoom());
            }
            for (int j = 0; j < frame.getRegionsCount(); j++) {
                Region region = frame.getRegion(j);
                Element regionElement = frameElement.addElement("region");
                regionElement.addAttribute("gid", "" + region.index);
                regionElement.addAttribute("x", "" + region.x);
                regionElement.addAttribute("y", "" + region.y);
                regionElement.addAttribute("width", "" + region.width);
                regionElement.addAttribute("height", "" + region.height);
            }
            writeProperties(frame.getProperties(), frameElement);
        }

        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter xmlw = new XMLWriter(new FileWriter(filename), format);
        xmlw.write(doc);
        xmlw.close();
    }
}
