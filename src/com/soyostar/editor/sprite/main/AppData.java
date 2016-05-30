/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.sprite.main;

import com.soyostar.editor.project.Project;

/**
 *
 * @author 全局数据
 */
public class AppData {

    private static AppData data;
    /**
     *
     */
    private Project project;

    /**
     *
     * @return
     */
    public Project getCurProject() {
        return project;
    }

    /**
     *
     * @param p
     */
    public void setCurProject(Project p) {
        this.project = p;
    }

    private AppData() {
    }
//    private SpriteEditorFrame mf = null;
//
//    /**
//     *
//     * @param mf
//     */
//    public void setMainFrame(SpriteEditorFrame mf) {
//        this.mf = mf;
//    }
//
//    /**
//     *
//     * @return
//     */
//    public SpriteEditorFrame getMainFrame() {
//        return mf;
//    }

    /**
     *
     * @return
     */
    public synchronized static AppData getInstance() {
        if (data == null) {
            data = new AppData();
        }
        return data;
    }
    private int currentAnimationIndex = -1;

    /**
     *
     * @return
     */
    public int getCurrentAnimationIndex() {
        return currentAnimationIndex;
    }

    /**
     *
     * @param currentAnimationIndex
     */
    public void setCurrentAnimationIndex(int currentAnimationIndex) {
        this.currentAnimationIndex = currentAnimationIndex;
    }
}
