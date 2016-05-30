/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.project;

import com.soyostar.editor.sprite.listener.ProjectChangeListener;
import com.soyostar.editor.sprite.listener.ProjectChangedEvent;
import com.soyostar.editor.sprite.model.Animation;
import com.soyostar.editor.sprite.model.Clip;
import com.soyostar.editor.sprite.model.Picture;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Administrator
 */
public class Project {

    private String name = "";//项目标题名
    private String path = "";//路径名
    private String softVersion = "";//项目对应的软件版本
    private final List projectChangeListeners = new LinkedList();
    //FIXME应该加入一个AnimationManager类来管理animation,picture和clip
    private HashMap<Integer, Animation> anis = new HashMap<Integer, Animation>();
    private ArrayList<Picture> pictures = new ArrayList<Picture>();
//    private ArrayList<Clip> clips = new ArrayList<Clip>();

//    /**
//     *
//     * @return
//     */
//    public ArrayList<Clip> getClips() {
//        return clips;
//    }
//
//    /**
//     *
//     * @param clips
//     */
//    public void setClips(ArrayList<Clip> clips) {
//        this.clips = clips;
//    }
//
//    /**
//     *
//     * @param clip
//     */
//    public void addClip(Clip clip) {
//        clips.add(clip);
//    }
//
//    /**
//     *
//     * @param id
//     */
//    public void removeClip(int id) {
//        clips.remove(id);
//    }

    /**
     *
     * @return
     */
    public ArrayList<Picture> getPictures() {
        return pictures;
    }

    /**
     *
     * @param pic
     */
    public void addPicture(Picture pic) {
        pictures.add(pic);
        firePictureAdded(pic);
    }

    /**
     *
     * @param index
     */
    public void removePicture(int index) {
        pictures.remove(index);
        firePictureRemoved(index);
    }

    /**
     *
     * @param index
     * @return
     */
    public Picture getPicture(int index) {
        if (index < 0 || index > pictures.size() - 1) {
            return null;
        }
        return pictures.get(index);
    }

    /**
     *
     * @return
     */
    public HashMap<Integer, Animation> getAnimations() {
        return anis;
    }

    /**
     * 
     * @param ani
     */
    public void addAnimation(Animation ani) {
        int id = getAnimationMaxIndex() + 1;
        anis.put(id, ani);
        ani.setIndex(id);
        fireAnimationAdded(ani);
    }

    /**
     * 
     * @param ani
     * @param id
     */
    public void addAnimation(Animation ani, int id) {
        anis.put(id, ani);
        fireAnimationAdded(ani);
    }

    /**
     * 
     */
    public void removeAllAnimation() {
        anis.clear();
    }

    /**
     * 
     * @param index
     */
    public void removeAnimation(int index) {
        anis.remove(index);
        fireAnimationRemoved(index);
    }

    /**
     * 
     * @param id
     * @return
     */
    public Animation getAnimation(int id) {
        return anis.get(id);
    }

    /**
     * 
     * @return
     */
    public int getAnimationCounts() {
        return anis.size();
    }

    /**
     * 
     * @return
     */
    public int getAnimationMaxIndex() {
        int max = -1;
        Set<Integer> aniset = anis.keySet();
        Iterator it = aniset.iterator();
        while (it.hasNext()) {
            Integer in = (Integer) it.next();
            if (in > max) {
                max = in;
            }
        }
        return max;
    }

    /**
     * 
     * @param listener
     */
    public void addProjectChangeListener(ProjectChangeListener listener) {
        projectChangeListeners.add(listener);
    }

    /**
     * Removes a change listener.
     * @param listener the listener to remove
     */
    public void removeProjectChangeListener(ProjectChangeListener listener) {
        projectChangeListeners.remove(listener);
    }

    /**
     * Notifies all registered map change listeners about a change.
     */
    protected void fireProjectChanged() {
        Iterator iterator = projectChangeListeners.iterator();
        ProjectChangedEvent event = null;

        while (iterator.hasNext()) {
            if (event == null) {
                event = new ProjectChangedEvent(this);
            }
            ((ProjectChangeListener) iterator.next()).projectChanged(event);
        }
    }

    /**
     * Notifies all registered map change listeners about a change.
     * @param index
     */
    protected void firePictureRemoved(int index) {
        Iterator iterator = projectChangeListeners.iterator();
        ProjectChangedEvent event = null;

        while (iterator.hasNext()) {
            if (event == null) {
                event = new ProjectChangedEvent(this);
            }
            ((ProjectChangeListener) iterator.next()).pictureRemoved(event, index);
        }
    }

    /**
     * Notifies all registered map change listeners about a change.
     * @param pic
     */
    protected void firePictureAdded(Picture pic) {
        Iterator iterator = projectChangeListeners.iterator();
        ProjectChangedEvent event = null;

        while (iterator.hasNext()) {
            if (event == null) {
                event = new ProjectChangedEvent(this);
            }
            ((ProjectChangeListener) iterator.next()).pictureAdded(event, pic);
        }
    }

    /**
     * Notifies all registered map change listeners about a change.
     * @param index 
     */
    protected void fireAnimationRemoved(int index) {
        Iterator iterator = projectChangeListeners.iterator();
        ProjectChangedEvent event = null;

        while (iterator.hasNext()) {
            if (event == null) {
                event = new ProjectChangedEvent(this);
            }
            ((ProjectChangeListener) iterator.next()).animationRemoved(event, index);
        }
    }

    /**
     * Notifies all registered map change listeners about a change.
     * @param ani
     */
    protected void fireAnimationAdded(Animation ani) {
        Iterator iterator = projectChangeListeners.iterator();
        ProjectChangedEvent event = null;

        while (iterator.hasNext()) {
            if (event == null) {
                event = new ProjectChangedEvent(this);
            }
            ((ProjectChangeListener) iterator.next()).animationAdded(event, ani);
        }
    }

    /**
     *
     * @return
     */
    public String getSoftVersion() {
        return softVersion;
    }

    /**
     *
     * @param softVersion
     */
    public void setSoftVersion(String softVersion) {
        this.softVersion = softVersion;
        fireProjectChanged();
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
        fireProjectChanged();
    }

    /**
     *
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     *
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
        fireProjectChanged();
    }

    /**
     *
     */
    public static final class Filter extends FileFilter {

        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            if (f.getName().equals("Project.xml")) {
                return true;
            }
            return false;
        }

        public String getDescription() {
            return "工程文件 (Project.xml)";
        }
    }
}
