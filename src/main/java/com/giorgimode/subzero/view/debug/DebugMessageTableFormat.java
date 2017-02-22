package com.giorgimode.subzero.view.debug;

import ca.odell.glazedlists.gui.TableFormat;

final class DebugMessageTableFormat implements TableFormat<DebugMessage> {

    // FIXME resource bundle
    private static final String[] COLUMN_NAMES = {
        "Message"
    };

    private static final int COLUMN_MESSAGE = 0;

    DebugMessageTableFormat() {
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Object getColumnValue(DebugMessage message, int column) {
        Object result;
        switch(column) {
            case COLUMN_MESSAGE:
                result = message.getMessage();
                break;
            default:
                result = null;
                break;
        }
        return result;
    }
}
