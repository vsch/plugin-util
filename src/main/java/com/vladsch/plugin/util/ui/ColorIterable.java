package com.vladsch.plugin.util.ui;

import com.vladsch.plugin.util.HelpersKt;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.Iterator;

public class ColorIterable implements Iterable<Color> {
    public static int GRADIENT_HUE_MIN = 0;
    public static int GRADIENT_HUE_MAX = 360;
    public static int GRADIENT_HUE_STEPS = 18;
    public static int GRADIENT_SATURATION_MIN = 30;
    public static int GRADIENT_SATURATION_MAX = 20;
    public static int GRADIENT_SATURATION_STEPS = 2;
    public static int GRADIENT_BRIGHTNESS_MIN = 100;
    public static int GRADIENT_BRIGHTNESS_MAX = 100;
    public static int GRADIENT_BRIGHTNESS_STEPS = 1;
    public static int DARK_GRADIENT_HUE_MIN = 0;
    public static int DARK_GRADIENT_HUE_MAX = 360;
    public static int DARK_GRADIENT_HUE_STEPS = 12;
    public static int DARK_GRADIENT_SATURATION_MIN = 80;
    public static int DARK_GRADIENT_SATURATION_MAX = 80;
    public static int DARK_GRADIENT_SATURATION_STEPS = 1;
    public static int DARK_GRADIENT_BRIGHTNESS_MIN = 40;
    public static int DARK_GRADIENT_BRIGHTNESS_MAX = 30;
    public static int DARK_GRADIENT_BRIGHTNESS_STEPS = 2;

    private final int myHueMinRaw;
    private final int myHueMaxRaw;
    private final int myHueSteps;
    private final int mySaturationMinRaw;
    private final int mySaturationMaxRaw;
    private final int mySaturationSteps;
    private final int myBrightnessMinRaw;
    private final int myBrightnessMaxRaw;
    private final int myBrightnessSteps;

    public ColorIterable(
            final int hueMinRaw,
            final int hueMaxRaw,
            final int hueSteps,
            final int saturationMinRaw,
            final int saturationMaxRaw,
            final int saturationSteps,
            final int brightnessMinRaw,
            final int brightnessMaxRaw,
            final int brightnessSteps
    ) {
        myHueMinRaw = hueMinRaw;
        myHueMaxRaw = hueMaxRaw;
        myHueSteps = HelpersKt.minLimit(1, HelpersKt.min(hueMaxRaw >= hueMinRaw ? hueMaxRaw - hueMinRaw : hueMinRaw - hueMaxRaw, hueSteps));
        mySaturationMinRaw = saturationMinRaw;
        mySaturationMaxRaw = saturationMaxRaw;
        mySaturationSteps = HelpersKt.minLimit(1, HelpersKt.min(saturationMaxRaw >= saturationMinRaw ? saturationMaxRaw - saturationMinRaw : saturationMinRaw - saturationMaxRaw, saturationSteps));
        myBrightnessMinRaw = brightnessMinRaw;
        myBrightnessMaxRaw = brightnessMaxRaw;
        myBrightnessSteps = HelpersKt.minLimit(1, HelpersKt.min(brightnessMaxRaw >= brightnessMinRaw ? brightnessMaxRaw - brightnessMinRaw : brightnessMinRaw - brightnessMaxRaw, brightnessSteps));
    }

    // use defaults
    public ColorIterable(boolean darkColors) {
        this(
                darkColors ? DARK_GRADIENT_HUE_MIN : GRADIENT_HUE_MIN,
                darkColors ? DARK_GRADIENT_HUE_MAX : GRADIENT_HUE_MAX,
                darkColors ? DARK_GRADIENT_HUE_STEPS : GRADIENT_HUE_STEPS,
                darkColors ? DARK_GRADIENT_SATURATION_MIN : GRADIENT_SATURATION_MIN,
                darkColors ? DARK_GRADIENT_SATURATION_MAX : GRADIENT_SATURATION_MAX,
                darkColors ? DARK_GRADIENT_SATURATION_STEPS : GRADIENT_SATURATION_STEPS,
                darkColors ? DARK_GRADIENT_BRIGHTNESS_MIN : GRADIENT_BRIGHTNESS_MIN,
                darkColors ? DARK_GRADIENT_BRIGHTNESS_MAX : GRADIENT_BRIGHTNESS_MAX,
                darkColors ? DARK_GRADIENT_BRIGHTNESS_STEPS : GRADIENT_BRIGHTNESS_STEPS
        );
    }

    public int getMaxIndex() {
        int maxIndex = myHueSteps * mySaturationSteps * myBrightnessSteps;
        return Math.min(maxIndex, 1024);
    }

    @NotNull
    @Override
    public ColorIterator iterator() {
        return new ColorIterator(
                myHueMinRaw,
                myHueMaxRaw,
                myHueSteps,
                mySaturationMinRaw,
                mySaturationMaxRaw,
                mySaturationSteps,
                myBrightnessMinRaw,
                myBrightnessMaxRaw,
                myBrightnessSteps
        );
    }

    public class ColorIterator implements Iterator<Color> {
        private final int myHueMinRaw;
        private final int myHueMaxRaw;
        private final int myHueSteps;
        private final int mySaturationMinRaw;
        private final int mySaturationMaxRaw;
        private final int mySaturationSteps;
        private final int myBrightnessMinRaw;
        private final int myBrightnessMaxRaw;
        private final int myBrightnessSteps;
        private final int myMaxIndex;
        private int myIndex;
        private int myNextIndex;

        public ColorIterator(
                final int hueMinRaw,
                final int hueMaxRaw,
                final int hueSteps,
                final int saturationMinRaw,
                final int saturationMaxRaw,
                final int saturationSteps,
                final int brightnessMinRaw,
                final int brightnessMaxRaw,
                final int brightnessSteps
        ) {
            myHueMinRaw = hueMinRaw;
            myHueMaxRaw = hueMaxRaw;
            myHueSteps = hueSteps;
            mySaturationMinRaw = saturationMinRaw;
            mySaturationMaxRaw = saturationMaxRaw;
            mySaturationSteps = saturationSteps;
            myBrightnessMinRaw = brightnessMinRaw;
            myBrightnessMaxRaw = brightnessMaxRaw;
            myBrightnessSteps = brightnessSteps;
            int maxIndex = hueSteps * saturationSteps * brightnessSteps;
            myMaxIndex = Math.min(maxIndex, 1024);
            myIndex = 0;
            myNextIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return myNextIndex < myMaxIndex;
        }

        @Override
        public Color next() {
            if (myNextIndex >= myMaxIndex) throw new IllegalStateException("No more colors");

            myIndex = myNextIndex;
            myNextIndex++;

            int i = myIndex;
            int hueIndex = (i % myHueSteps);
            i /= myHueSteps;
            int saturationIndex = (i % mySaturationSteps);
            i /= mySaturationSteps;
            int brightnessIndex = (i % myBrightnessSteps);

            float hue = (myHueMinRaw + ((myHueMaxRaw - myHueMinRaw) * hueIndex) / ((float) myHueSteps)) / 360.0f;
            float saturation = (mySaturationMinRaw + ((mySaturationMaxRaw - mySaturationMinRaw) * saturationIndex) / ((float) (mySaturationSteps > 1 ? mySaturationSteps - 1 : mySaturationSteps))) / 100.0f;
            float brightness = (myBrightnessMinRaw + ((myBrightnessMaxRaw - myBrightnessMinRaw) * brightnessIndex) / ((float) (myBrightnessSteps > 1 ? myBrightnessSteps - 1 : myBrightnessSteps))) / 100.0f;

            return Color.getHSBColor(hue, saturation, brightness);
        }

        public int getIndex() {
            return myIndex;
        }

        public int getMaxIndex() {
            return myMaxIndex;
        }

        public int getHueSteps() {
            return myHueSteps;
        }

        public int getSaturationSteps() {
            return mySaturationSteps;
        }

        public int getBrightnessSteps() {
            return myBrightnessSteps;
        }

        public int getHueIndex() {
            int i = myIndex;
            int hueIndex = (i % myHueSteps);
            i /= myHueSteps;
            int saturationIndex = (i % mySaturationSteps);
            i /= mySaturationSteps;
            int brightnessIndex = (i % myBrightnessSteps);
            return hueIndex;
        }

        public boolean isHueStart() {
            int i = myIndex;
            int hueIndex = (i % myHueSteps);
            i /= myHueSteps;
            int saturationIndex = (i % mySaturationSteps);
            i /= mySaturationSteps;
            int brightnessIndex = (i % myBrightnessSteps);
            return myIndex > 0 && hueIndex == 0;
        }

        public boolean isHueEnd() {
            int i = myIndex;
            int hueIndex = (i % myHueSteps);
            i /= myHueSteps;
            int saturationIndex = (i % mySaturationSteps);
            i /= mySaturationSteps;
            int brightnessIndex = (i % myBrightnessSteps);
            return myHueSteps > 1 && hueIndex == myHueSteps - 1;
        }

        public int getSaturationIndex() {
            int i = myIndex;
            int hueIndex = (i % myHueSteps);
            i /= myHueSteps;
            int saturationIndex = (i % mySaturationSteps);
            i /= mySaturationSteps;
            int brightnessIndex = (i % myBrightnessSteps);
            return saturationIndex;
        }

        public boolean isSaturationStart() {
            int i = myIndex;
            int hueIndex = (i % myHueSteps);
            i /= myHueSteps;
            int saturationIndex = (i % mySaturationSteps);
            i /= mySaturationSteps;
            int brightnessIndex = (i % myBrightnessSteps);
            return myIndex > 0 && hueIndex == 0 && saturationIndex == 0;
        }

        public boolean isSaturationEnd() {
            int i = myIndex;
            int hueIndex = (i % myHueSteps);
            i /= myHueSteps;
            int saturationIndex = (i % mySaturationSteps);
            i /= mySaturationSteps;
            int brightnessIndex = (i % myBrightnessSteps);
            return mySaturationSteps > 1 && hueIndex == 0 && saturationIndex == mySaturationSteps - 1;
        }

        public int getBrightnessIndex() {
            int i = myIndex;
            int hueIndex = (i % myHueSteps);
            i /= myHueSteps;
            int saturationIndex = (i % mySaturationSteps);
            i /= mySaturationSteps;
            int brightnessIndex = (i % myBrightnessSteps);
            return brightnessIndex;
        }

        public boolean isBrightnessStart() {
            int i = myIndex;
            int hueIndex = (i % myHueSteps);
            i /= myHueSteps;
            int saturationIndex = (i % mySaturationSteps);
            i /= mySaturationSteps;
            int brightnessIndex = (i % myBrightnessSteps);
            return myIndex > 0 && hueIndex == 0 && saturationIndex == 0 && brightnessIndex == 0;
        }

        public boolean isBrightnessEnd() {
            int i = myIndex;
            int hueIndex = (i % myHueSteps);
            i /= myHueSteps;
            int saturationIndex = (i % mySaturationSteps);
            i /= mySaturationSteps;
            int brightnessIndex = (i % myBrightnessSteps);
            return myBrightnessSteps > 1 && hueIndex == 0 && saturationIndex == 0 && brightnessIndex == myBrightnessSteps - 1;
        }
    }
}
