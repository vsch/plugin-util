/*
   original code taken from: https://coderanch.com/t/333565/java/BufferedImage-System-Clipboard
   modified for passing image in constructor.
 */

package com.vladsch.plugin.util.ui;

import org.jetbrains.annotations.NotNull;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ClipboardImage implements ClipboardOwner {
    public ClipboardImage(BufferedImage image) {
        TransferableImage trans = new TransferableImage(image);
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        c.setContents(trans, this);
    }

    public void lostOwnership(Clipboard clip, Transferable trans) {
        //System.out.println( "Lost Clipboard Ownership" );
    }

    private class TransferableImage implements Transferable {
        Image myImage;

        public TransferableImage(Image image) {
            myImage = image;
        }

        @NotNull
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (flavor.equals(DataFlavor.imageFlavor) && myImage != null) {
                return myImage;
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }

        public DataFlavor[] getTransferDataFlavors() {
            DataFlavor[] flavors = new DataFlavor[1];
            flavors[0] = DataFlavor.imageFlavor;
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            DataFlavor[] flavors = getTransferDataFlavors();
            for (int i = 0; i < flavors.length; i++) {
                if (flavor.equals(flavors[i])) {
                    return true;
                }
            }

            return false;
        }
    }
}
