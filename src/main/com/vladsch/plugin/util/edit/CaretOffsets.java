package com.vladsch.plugin.util.edit;

public class CaretOffsets {
    public final int pos;
    public final int start;
    public final int end;

    public CaretOffsets(final int pos, final int start, final int end) {
        this.pos = pos;
        this.start = start;
        this.end = end;
    }
}
