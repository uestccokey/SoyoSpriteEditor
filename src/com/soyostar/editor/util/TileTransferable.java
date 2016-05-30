/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.util;

import com.soyostar.editor.sprite.model.Clip;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 *
 * @author Administrator
 */
public class TileTransferable implements Transferable {

    /**
     *
     */
    public static final DataFlavor TILE_FLAVOR = new DataFlavor(Clip.class, "Tile");
    private static final DataFlavor FLAVOR[] = {
        TILE_FLAVOR
    };
    private Clip data;

    /**
     *
     * @return
     */
    public Clip getTile() {
        return data;
    }

    /**
     *
     * @param tile
     */
    public TileTransferable(Clip tile) {
        this.data = tile;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return FLAVOR;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return false;
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor.equals(TILE_FLAVOR)) {
            return this.data;
        }
        return null;
    }
}
