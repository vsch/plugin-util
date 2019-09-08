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
        SimpleShapeTest.class,
        BorderedShapeTest.class,
        DrawTransformerTest.class,
        RubberBandShapeTest.class,
        TransparencyTransformTest.class,
        ImageIssueTest.class,
})
public class ImageTestSuite {
}
