/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.sprite.model;

/**
 *
 * @author Administrator
 */
public class Region implements Cloneable {

    public int x = 0;
    public int y = 0;
    public int width = 0;
    public int height = 0;
    public int index = 0;

    @Override
    public Object clone() throws CloneNotSupportedException {
        Region clone = (Region) super.clone();
        return clone;
    }
}
