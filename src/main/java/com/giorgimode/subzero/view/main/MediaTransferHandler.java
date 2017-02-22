package com.giorgimode.subzero.view.main;

import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.net.URL;
import java.util.List;

import javax.swing.TransferHandler;

@SuppressWarnings("serial")
abstract class MediaTransferHandler extends TransferHandler {

    private final DataFlavor uriListFlavor;

    private final DataFlavor javaUrlFlavor;

    private final DataFlavor javaFileListFlavor;

    MediaTransferHandler() {
        uriListFlavor = newDataFlavor("text/uri-list;class=java.lang.String");
        javaUrlFlavor = newDataFlavor("application/x-java-url;class=java.net.URL");
        javaFileListFlavor = DataFlavor.javaFileListFlavor;
    }

    private DataFlavor newDataFlavor(String mimeType) {
        try {
            return new DataFlavor(mimeType);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public final boolean canImport(TransferSupport support) {
        return getDataFlavor(support) != null;
    }

    @Override
    public final boolean importData(TransferSupport support) {
        DataFlavor flavor = getDataFlavor(support);
        if (flavor != null) {
            try {
                Object transferData = support.getTransferable().getTransferData(flavor);
                if (transferData instanceof String) {
                    String value = (String)transferData;
                    String[] uris = value.split("\\r\\n");
                    if (uris.length > 0) {
                        onMediaDropped(uris);
                    }
                    return true;
                }
                else if (transferData instanceof URL) {
                    URL value = (URL)transferData;
                    String uri = value.toExternalForm();
                    onMediaDropped(new String[] {uri});
                }
                else if (transferData instanceof List) {
                    List<?> value = (List<?>)transferData;
                    if (value.size() > 0) {
                        // Play the first MRL that was dropped (the others are discarded)
                        File file = (File)value.get(0);
                        String uri = file.getAbsolutePath();
                        onMediaDropped(new String[] {uri});
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private DataFlavor getDataFlavor(TransferSupport support) {
        if (support.isDataFlavorSupported(uriListFlavor)) {
            return uriListFlavor;
        }
        if (support.isDataFlavorSupported(javaUrlFlavor)) {
            return javaUrlFlavor;
        }
        if (support.isDataFlavorSupported(javaFileListFlavor)) {
            return javaFileListFlavor;
        }
        return null;
    }

    protected abstract void onMediaDropped(String[] uris);
}
