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
public interface IAnimationReader {
    /**
     *
     * @param aniFile
     * @throws Exception
     */
    public Animation readAnimation(String aniFile) throws Exception;
}
