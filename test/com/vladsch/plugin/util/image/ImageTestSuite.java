/*
 *
 */
package com.vladsch.plugin.util.image;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        PointTest.class,
        RectangleTest.class,
        BorderTransformTest.class,
        CropTransformTest.class,
        ScaleTransformTest.class,
        TransformerTest.class,
        SimpleSelectableShapeTest.class,
        FancyBorderSelectableShapeTest.class,
        DrawShapesTransformerTest.class,
        RubberBandSelectedShapeTest.class,
        TransparencyTransformTest.class,
})
public class ImageTestSuite {
}
