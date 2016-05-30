/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.sprite.model;

import com.soyostar.editor.sprite.model.Frame;
import com.soyostar.editor.sprite.main.AppData;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Administrator
 */
public class FrameTableModel extends AbstractTableModel {

    private static final String COLUMN_NAME[] = {
        "ID", "帧名", "播放时间(ms)"
    };
    private static final Class COLUMN_CLASS[] = {
        Integer.class, String.class, Integer.class
    };

    /**
     *
     */
    public FrameTableModel() {
    }

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
        Frame frame = data.getCurProject().getAnimation(data.getCurrentAnimationIndex()).getFrame(rowIndex);
        if (frame != null) {
            switch (columnIndex) {
                case 0:
                    return rowIndex;
                case 1:
                    return frame.getName();
                case 2:
                    return frame.getDelay();
            }
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if (col == 0) {
            return false;
        }
        return true;
    }

    @Override
    public void setValueAt(Object v, int r, int c) {
        Frame frame = data.getCurProject().getAnimation(data.getCurrentAnimationIndex()).getFrame(r);

        if (frame != null) {
            switch (c) {
                case 1:
                    frame.setName(v.toString());
                    break;
                case 2:
                    frame.setDelay(Integer.parseInt(v.toString()));
                    break;
            }
        }
        this.fireTableCellUpdated(r, c);
    }

    @Override
    public int getRowCount() {
        if (data.getCurProject().getAnimation(data.getCurrentAnimationIndex()) == null) {
            return 0;
        }
        return data.getCurProject().getAnimation(data.getCurrentAnimationIndex()).getFrames().size();
    }
}
