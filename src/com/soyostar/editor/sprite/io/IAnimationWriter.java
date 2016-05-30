/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.sprite.io;

import com.soyostar.editor.sprite.model.Animation;

/**
 *
 * @author Administrator
 */
public interface IAnimationWriter {

    /**
     * Saves a ani to a file.
     * @param filename the filename of the ani file
     * @throws Exception
     */
    public void writeAnimation(Animation ani, String filename) throws Exception;
}
