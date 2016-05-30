/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.sprite.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author Administrator
 */
public class Animation {

    private int index;//动画的序号，动画的唯一性标识
    private String name;//可选，动画的名称
    private ArrayList<Frame> frames = new ArrayList<Frame>();//帧序列
    private HashMap<String, String> properties = new HashMap<String, String>();  //属性

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public void addProperty(String key, String value) {
        properties.put(key, value);
    }

    public void removeProperty(String key) {
        properties.remove(key);
    }

    public void removeAllProperty() {
        properties.clear();
    }

    public ArrayList<Picture> getPictures() {
        ArrayList<Picture> pictures = new ArrayList<Picture>();
        for (int i = 0; i < frames.size(); i++) {
            Frame frame = frames.get(i);
            for (int j = 0; j < frame.getTiles().size(); j++) {
                Clip clip = frame.getTile(j);
                if (!pictures.contains(clip.getPicture())) {
                    pictures.add(clip.getPicture());
                }
            }
        }
        return pictures;
    }

    /**
     *
     * @return
     */
    public ArrayList<Frame> getFrames() {
        return frames;
    }

    /**
     *
     * @param id
     * @return
     */
    public Frame getFrame(int id) {
        if (id < 0 || id > frames.size() - 1) {
            return null;
        }
        return frames.get(id);
    }

    /**
     *
     * @param index
     */
    public void swapFrameDown(int index) {
        if (index + 1 == frames.size()) {
            throw new RuntimeException(
                    "Can't swap up when already at the top.");
        }
        Frame f = getFrame(index + 1);
        frames.set(index + 1, getFrame(index));
        frames.set(index, f);
    }

    /**
     *
     * @param index
     */
    public void swapFrameUp(int index) {
        if (index - 1 < 0) {
            throw new RuntimeException(
                    "Can't swap down when already at the bottom.");
        }
        Frame hold = getFrame(index - 1);
        frames.set(index - 1, getFrame(index));
        frames.set(index, hold);
    }

    /**
     *
     * @param frame
     */
    public void addFrame(Frame frame) {
        frames.add(frame);
    }

    /**
     *
     * @param id
     */
    public void removeFrame(int id) {
        frames.remove(id);
    }

    /**
     *
     * @param frames
     */
    public void setFrames(ArrayList<Frame> frames) {
        this.frames = frames;
    }

    /**
     *
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     *
     * @param index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}
