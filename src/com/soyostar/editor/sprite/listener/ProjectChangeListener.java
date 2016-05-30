/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.sprite.listener;

import com.soyostar.editor.sprite.model.Animation;
import com.soyostar.editor.sprite.model.Picture;
import java.util.EventListener;

/**
 *
 * @author Administrator
 */
public interface ProjectChangeListener extends EventListener {

    /**
     * 
     * @param e
     */
    public void projectChanged(ProjectChangedEvent e);

    /**
     *
     * @param e
     * @param ani
     */
    public void animationAdded(ProjectChangedEvent e, Animation ani);

    /**
     *
     * @param e
     * @param pic
     */
    public void pictureAdded(ProjectChangedEvent e, Picture pic);

    /**
     *
     * @param e
     * @param index
     */
    public void animationRemoved(ProjectChangedEvent e, int index);

    /**
     *
     * @param e
     * @param index
     */
    public void pictureRemoved(ProjectChangedEvent e, int index);
}
