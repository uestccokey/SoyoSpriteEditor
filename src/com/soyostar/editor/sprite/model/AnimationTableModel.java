/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.sprite.model;

import com.soyostar.editor.sprite.model.Animation;
import com.soyostar.editor.sprite.main.AppData;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrator
 */
public class AnimationTableModel extends AbstractTableModel {

    private static final String COLUMN_NAME[] = {
        "ID", "动画名"
    };
    private static final Class COLUMN_CLASS[] = {
        Integer.class, String.class
    };

    /**
     *
     */
    public AnimationTableModel() {
        updateData();
    }

    /**
     *
     */
    public void updateData() {
        anis.clear();
        java.util.Iterator it = data.getCurProject().getAnimations().entrySet().iterator();

        while (it.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
            anis.add((Animation) entry.getValue());
        }
    }
    private ArrayList<Animation> anis = new ArrayList<Animation>();

    @Override
    public String getColumnName(int c) {
        return COLUMN_NAME[c];
    }

    @Override
    public Class<?> getColumnClass(int c) {
        return COLUMN_CLASS[c];
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAME.length;
    }
    private AppData data = AppData.getInstance();

    public Object getValueAt(int rowIndex, int columnIndex) {

        Animation ani = anis.get(rowIndex);
        if (ani != null) {
            switch (columnIndex) {
                case 0:
                    return ani.getIndex();//FIX ME rowIndex和Ani的id不一样，id可能会有间隔的
                case 1:
                    return ani.getName();
            }
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if (col == 1) {
            return true;
        }
        return false;
    }

    @Override
    public void setValueAt(Object v, int r, int c) {
        Animation ani = anis.get(r);

        if (ani != null) {
            switch (c) {
                case 1:
                    anis.get(r).setName(v.toString());
                    break;
            }
        }

        this.fireTableCellUpdated(r, c);
    }

    @Override
    public int getRowCount() {

        return data.getCurProject().getAnimationCounts();
    }
}
