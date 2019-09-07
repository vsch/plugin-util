package com.vladsch.plugin.util.image;

public enum ShapeType {
    RECTANGLE(false, false),
    SQUARE(false, true),
    OVAL(true, false),
    CIRCLE(true, true);

    public final boolean isOval;
    public final boolean isConstrained;

    ShapeType(final boolean isOval, final boolean isConstrained) {
        this.isOval = isOval;
        this.isConstrained = isConstrained;
    }
}
