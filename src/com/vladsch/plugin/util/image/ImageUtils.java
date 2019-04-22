package com.vladsch.plugin.util.image;

import com.vladsch.plugin.util.FileIOKt;
import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;

import static com.vladsch.flexmark.util.Utils.minLimit;

public class ImageUtils {
    public static Color TRANSPARENT = new Color(0, 0, 0, 0);

    public static Image getImageFromClipboard() {
        Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        return getImageFromTransferable(transferable);
    }

    public static Image getImageFromTransferable(final Transferable transferable) {
        try {
            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                return (Image) transferable.getTransferData(DataFlavor.imageFlavor);
            } else {
                return null;
            }
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException();
        }

        return null;
    }

    public static BufferedImage scaleImage(BufferedImage sourceImage, int newWidth, int newHeight, int opType) {
        if (sourceImage == null) {
            return null;
        }

        if (newWidth == 0 || newHeight == 0) {
            return null;
        }

        AffineTransform at = AffineTransform.getScaleInstance(
                (double) newWidth / sourceImage.getWidth(null),
                (double) newHeight / sourceImage.getHeight(null)
        );

        //  http://nickyguides.digital-digest.com/bilinear-vs-bicubic.htm
        AffineTransformOp op = new AffineTransformOp(at, opType != 0 ? opType : AffineTransformOp.TYPE_BILINEAR);
        return op.filter(sourceImage, null);
    }

    public static BufferedImage toBufferedImage(Image src) {
        if (src == null) {
            return null;
        } else if (src instanceof BufferedImage) {
            return (BufferedImage) src;
        }

        int w = src.getWidth(null);
        int h = src.getHeight(null);
        if (w < 0 || h < 0) {
            return null;
        }

        int type = BufferedImage.TYPE_INT_ARGB;  // other options
        BufferedImage dest = new BufferedImage(w, h, type);
        //BufferedImage dest = UIUtil.createImage(w, h, type);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(src, 0, 0, null);
        //UIUtil.drawImage(g2, src, 0, 0, null);
        g2.dispose();

        return dest;
    }

    public static void save(BufferedImage image, File file, String format) {
        try {
            ImageIO.write(image, format, file);  // ignore returned boolean
        } catch (Throwable e) {
            System.out.println("Write error for " + file.getPath() + ": " + e.getMessage());
        }
    }

    /**
     * @param cachedImageFile file
     *
     * @return Could be {@code null} if the image could not be read from the file (because of whatever strange reason).
     */
    public static BufferedImage loadImageFromFile(File cachedImageFile) {
        if (cachedImageFile == null || !cachedImageFile.isFile()) {
            return null;
        }

        if (".svg".equals(FileIOKt.getDotExtension(cachedImageFile))) {
            return loadSvgImageFromFile(cachedImageFile);
        } else {
            try {
                // related to http://bugs.java.com/bugdatabase/view_bug.do;jsessionid=dc84943191e06dffffffffdf200f5210dd319?bug_id=6967419
                for (int i = 0; i < 3; i++) {
                    BufferedImage read = null;
                    try {
                        read = ImageIO.read(cachedImageFile);
                    } catch (IndexOutOfBoundsException e) {
                        System.err.print("*");
                        System.err.println("could not read" + cachedImageFile);
                        continue;
                    }

                    if (i > 0) System.err.println();

                    return read;
                }
            } catch (Throwable e) {
                //System.err.println("deleting " + cachedImageFile);
                //cachedImageFile.delete();
                return null;
            }
        }
        return null;
    }

    public static String base64Encode(BufferedImage image) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, "PNG", bos);
            byte[] imageBytes = bos.toByteArray();
            // diagnostic/2553 on windows its \r\n
            imageString = Base64.getEncoder().encodeToString(imageBytes).replace("\r", "").replace("\n", "");
            //imageString = javax.xml.bind.DatatypeConverter.printBase64Binary(imageBytes);
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "data:image/png;base64," + imageString;
    }

    public static String base64Encode(File file) {
        if (file == null || !file.isFile()) {
            return null;
        }

        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] imageBytes = new byte[(int) file.length()];
            if (fileInputStreamReader.read(imageBytes) != -1) {
                // diagnostic/2553 on windows its \r\n
                return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes).replace("\r", "").replace("\n", "");
                //return "data:image/png;base64," + javax.xml.bind.DatatypeConverter.printBase64Binary(imageBytes);
            }
            return null;
        } catch (Throwable e) {
            return null;
        }
    }

    public static BufferedImage base64Decode(File file) {
        if (file == null || !file.isFile()) {
            return null;
        }

        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            if (fileInputStreamReader.read(bytes) != -1) {
                String encoded = new String(bytes, StandardCharsets.UTF_8);
                int pos = encoded.indexOf(',');
                if (pos >= 0) {
                    String encodedImage = encoded.substring(pos + 1);
                    byte[] imageBytes = Base64.getDecoder().decode(encodedImage);

                    ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
                    final BufferedImage bufferedImage = ImageIO.read(bis);
                    bis.close();
                    return bufferedImage;
                }
            }
            return null;
        } catch (Throwable e) {
            return null;
        }
    }

    private static final Pattern BASE64_ENCODING_PATTERN = Pattern.compile("^data:image/[a-z0-9_-]+;base64,", Pattern.CASE_INSENSITIVE);

    public static boolean isEncodedImage(String encoded) {
        return encoded != null && encoded.startsWith("data:image/") && BASE64_ENCODING_PATTERN.matcher(encoded).find();
    }

    public static boolean isPossiblyEncodedImage(String encoded) {
        return encoded != null && encoded.startsWith("data:image/");
    }

    public static BufferedImage base64Decode(String encoded) {
        if (encoded == null || encoded.isEmpty()) {
            return null;
        }

        try {
            int pos = encoded.indexOf(',');
            if (pos >= 0) {
                String encodedImage = encoded.substring(pos + 1);
                byte[] imageBytes = Base64.getDecoder().decode(encodedImage);

                ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
                final BufferedImage bufferedImage = ImageIO.read(bis);
                bis.close();
                return bufferedImage;
            }
            return null;
        } catch (Throwable e) {
            return null;
        }
    }

    public static BufferedImage loadSvgImageFromURL(String imageURL) {
        return loadSvgImageFromURL(imageURL, 1.0f, false);
    }

    public static BufferedImage loadSvgImageFromURL(String imageURL, final float scale) {
        return loadSvgImageFromURL(imageURL, scale, false);
    }

    public static BufferedImage loadSvgImageFromURL(String imageURL, final float scale, boolean logImageProcessing) {
        BufferedImage image = loadSvgImageFromURL(imageURL, null, logImageProcessing);
        if (image != null && scale != 0f) {
            image = loadSvgImageFromURL(imageURL, new Point((int) (image.getWidth() * scale), (int) (image.getHeight() * scale)), logImageProcessing);
        }
        return image;
    }

    public static BufferedImage loadSvgImageFromURLSized(String imageURL, final float sizeX, float sizeY, boolean logImageProcessing) {
        return loadSvgImage(new TranscoderInput(imageURL), sizeX, sizeY, logImageProcessing);
    }

    public static BufferedImage loadSvgImageFromStream(InputStream svgInputStream, final float sizeX, float sizeY, boolean logImageProcessing) {
        return loadSvgImage(new TranscoderInput(svgInputStream), sizeX, sizeY, logImageProcessing);
    }

    public static BufferedImage loadSvgImageFromURL(String imageURL, final Point size, boolean logImageProcessing) {
        return loadSvgImage(new TranscoderInput(imageURL), size, logImageProcessing);
    }

    public static BufferedImage loadSvgImageFromStream(InputStream svgInputStream, final Point size, boolean logImageProcessing) {
        return loadSvgImage(new TranscoderInput(svgInputStream), size, logImageProcessing);
    }

    public static BufferedImage loadSvgImageFromStream(InputStream svgInputStream, final @Nullable Float sizeX, final @Nullable Float sizeY, boolean logImageProcessing) {
        return loadSvgImage(new TranscoderInput(svgInputStream), sizeX, sizeY, logImageProcessing);
    }

    private static BufferedImage loadSvgImage(@NotNull TranscoderInput input, @Nullable final Point size, boolean logImageProcessing) {
        if (size == null) {
            return loadSvgImage(input, null, null, logImageProcessing);
        } else {
            return loadSvgImage(input, (float) size.x, (float) size.y, logImageProcessing);
        }
    }

    private static BufferedImage loadSvgImage(TranscoderInput input, final @Nullable Float sizeX, final @Nullable Float sizeY, boolean logImageProcessing) {
        BufferedImage image;

        try {
            PNGTranscoder t = new PNGTranscoder();

            if (sizeX != null && sizeX != 0f || sizeY != null && sizeY != 0f) {
                t.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, sizeX == null ? 1.0f : sizeX);
                t.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, sizeY == null ? 1.0f : sizeY);
            }

            // Create the transcoder output.
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(ostream);

            // Save the image.
            t.transcode(input, output);

            // Flush and close the stream.
            ostream.flush();
            ostream.close();

            byte[] imageBytes = ostream.toByteArray();

            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            image = ImageIO.read(bis);
            bis.close();
        } catch (IOException | TranscoderException e) {
            if (logImageProcessing) {
                e.printStackTrace();
            }
            image = null;
        } catch (Throwable t) {
            if (logImageProcessing) {
                t.printStackTrace();
            }
            image = null;
        }
        return image;
    }

    public static BufferedImage loadSvgImageFromFile(File imageFile) {
        return loadSvgImageFromFile(imageFile, false);
    }

    public static BufferedImage loadSvgImageFromFile(File imageFile, boolean logImageProcessing) {
        return loadSvgImageFromURL("file://" + imageFile.getPath(), 1.0f, logImageProcessing);
    }

    public static BufferedImage loadImageFromURL(String imageURL) {
        return loadImageFromURL(imageURL, false);
    }

    public static BufferedImage loadImageFromURL(String imageURL, boolean logImageProcessing) {
        if (imageURL != null) {
            try {
                Image image = ImageIO.read(new URL(imageURL));
                return toBufferedImage(image);
            } catch (IOException e) {
                if (logImageProcessing) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * http://stackoverflow.com/questions/7603400/how-to-make-a-rounded-corner-image-in-java
     */
    public static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius, int borderWidth) {
        if ((float) cornerRadius == 0) return image;

        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        //BufferedImage output = UIUtil.createImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();

        // This is what we want, but it only does hard-clipping, i.e. aliasing
        // g2.setClip(new RoundRectangle2D ...)

        // so instead fake soft-clipping by first drawing the desired clip shape
        // in fully opaque white with antialiasing enabled...
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);

        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, (float) cornerRadius, (float) cornerRadius));

        // ... then compositing the image on top,
        // using the white shape from above as alpha source
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);

        g2.dispose();
        //output.setRGB(3, 3, 123);
        return output;
    }

    public static BufferedImage addBorder(BufferedImage image, Color borderColor, int borderWidth, int cornerRadius) {
        int w = image.getWidth() + borderWidth * 2;
        int h = image.getHeight() + borderWidth * 2;
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        //BufferedImage output = UIUtil.createImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();
        g2.setColor(borderColor);
        g2.drawImage(image, borderWidth, borderWidth, image.getWidth(), image.getHeight(), null);
        //UIUtil.drawImage(g2, image, 0, 0, null);
        g2.setStroke(new BasicStroke(borderWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, borderWidth));
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        int width = w - borderWidth - 1;
        int height = h - borderWidth - 1;
        int halfBorder = borderWidth / 2;
        if (cornerRadius > 0) {
            int adjustedRadius = cornerRadius + borderWidth;
            g2.drawRoundRect(halfBorder, halfBorder, width, height, adjustedRadius, adjustedRadius);
        } else {
            g2.drawRect(halfBorder, halfBorder, width, height);
        }
        g2.dispose();
        //output.setRGB(3, 3, 123);
        return output;
    }

    public static BufferedImage drawRectangle(BufferedImage image, int x, int y, int w, int h, Color borderColor, int borderWidth, int cornerRadius) {

        return drawRectangle(image, x, y, w, h, borderColor, borderWidth, cornerRadius, null, 0.0f);
    }

    public static BufferedImage drawRectangle(BufferedImage image, int x, int y, int w, int h, Color borderColor, int borderWidth, int cornerRadius, float[] dash, float dashPhase) {
        BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        //BufferedImage output = UIUtil.createImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();
        boolean invert = borderColor == null;

        if (invert) {
            // invert
            final int rgb = image.getRGB(x + w / 2, y + h / 2);
            borderColor = Color.getColor("", ~(rgb & 0xFFFFFF));
        }

        g2.drawImage(image, 0, 0, null);
        //UIUtil.drawImage(g2, image, 0, 0, null);
        if (dash != null) {
            g2.setStroke(new BasicStroke(borderWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, borderWidth, dash, dashPhase));
        } else {
            g2.setStroke(new BasicStroke(borderWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, borderWidth));
        }

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        if (invert) {
            //g2.setXORMode(Color.WHITE);
        }

        g2.setColor(borderColor);
        if (cornerRadius > 0) {
            g2.drawRoundRect(x, y, w, h, cornerRadius, cornerRadius);
        } else {
            g2.drawRect(x, y, w, h);
        }
        g2.dispose();
        //output.setRGB(3, 3, 123);
        return output;
    }

    public static BufferedImage overlayImage(BufferedImage imageBack, Image imageFore, int x, int y) {
        BufferedImage output = new BufferedImage(imageBack.getWidth(), imageBack.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();
        g2.drawImage(imageBack, 0, 0, null);
        g2.drawImage(imageFore, x, y, null);
        g2.dispose();
        return output;
    }

    public static BufferedImage drawOval(BufferedImage image, int x, int y, int w, int h, Color borderColor, int borderWidth, float[] dash, float dashPhase) {
        BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        //BufferedImage output = UIUtil.createImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();
        boolean invert = borderColor == null;

        if (invert) {
            // invert
            final int rgb = image.getRGB(x + w / 2, y + h / 2);
            borderColor = Color.getColor("", ~(rgb & 0xFFFFFF));
        }

        g2.drawImage(image, 0, 0, null);
        //UIUtil.drawImage(g2, image, 0, 0, null);
        if (dash != null) {
            g2.setStroke(new BasicStroke(borderWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, borderWidth, dash, dashPhase));
        } else {
            g2.setStroke(new BasicStroke(borderWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, borderWidth));
        }

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        if (invert) {
            //g2.setXORMode(Color.WHITE);
        }

        g2.setColor(borderColor);
        g2.drawOval(x, y, w, h);
        g2.dispose();
        return output;
    }

    public static BufferedImage drawHighlightRectangle(
            BufferedImage image,
            int x, int y, int w, int h,
            Color borderColor, int borderWidth, int cornerRadius,
            Color innerFillColor
    ) {
        //BufferedImage output = UIUtil.createImage(w, h, BufferedImage.TYPE_INT_ARGB);
        //noinspection UndesirableClassUsage
        BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int imgW = image.getWidth();
        int imgH = image.getHeight();

        Graphics2D g2 = output.createGraphics();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        boolean innerFilled = innerFillColor.getAlpha() != 0;

        g2.drawImage(image, 0, 0, null);

        if (cornerRadius > 0) {
            if (innerFilled) {
                g2.setColor(innerFillColor);
                g2.fillRoundRect(x, y, w, h, cornerRadius, cornerRadius);
            }

            if (borderWidth > 0) {
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(borderWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, borderWidth));
                g2.drawRoundRect(x, y, w, h, cornerRadius, cornerRadius);
            }
        } else {
            if (innerFilled) {
                g2.setColor(innerFillColor);
                g2.fillRect(x, y, w, h);
            }

            if (borderWidth > 0) {
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(borderWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, borderWidth));
                g2.drawRect(x, y, w, h);
            }
        }

        g2.dispose();
        return output;
    }

    public static BufferedImage drawHighlightOval(
            BufferedImage image,
            int x, int y, int w, int h,
            Color borderColor, int borderWidth,
            Color innerFillColor
    ) {
        //BufferedImage output = UIUtil.createImage(w, h, BufferedImage.TYPE_INT_ARGB);
        //noinspection UndesirableClassUsage
        BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int imgW = image.getWidth();
        int imgH = image.getHeight();

        Graphics2D g2 = output.createGraphics();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        boolean innerFilled = innerFillColor.getAlpha() != 0;

        g2.drawImage(image, 0, 0, null);

        if (innerFilled) {
            g2.setColor(innerFillColor);
            g2.fillOval(x, y, w, h);
        }

        if (borderWidth > 0) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, borderWidth));
            g2.drawOval(x, y, w, h);
        }

        g2.dispose();
        return output;
    }

    public static BufferedImage punchOuterHighlightRectangle(
            BufferedImage image,
            BufferedImage outerImage,
            int x, int y, int w, int h,
            int borderWidth, int cornerRadius,
            Color outerFillColor,
            final int outerBorderWidth,
            final int outerCornerRadius,
            boolean applyToImage
    ) {
        //BufferedImage output = UIUtil.createImage(w, h, BufferedImage.TYPE_INT_ARGB);
        boolean outerFilled = outerFillColor.getAlpha() != 0;
        if (!outerFilled) {
            return outerImage;
        }

        //noinspection UndesirableClassUsage
        BufferedImage output = outerImage != null ? outerImage : new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int imgW = image.getWidth();
        int imgH = image.getHeight();

        Graphics2D g2 = output.createGraphics();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        if (outerImage == null) {
            // first one, we need to fill it
            g2.setColor(outerFillColor);
            if (outerCornerRadius > 0) {
                g2.fillRoundRect(outerBorderWidth, outerBorderWidth, imgW - 2 * outerBorderWidth, imgH - 2 * outerBorderWidth, outerCornerRadius, outerCornerRadius);
            } else {
                g2.fillRect(outerBorderWidth, outerBorderWidth, imgW - 2 * outerBorderWidth, imgH - 2 * outerBorderWidth);
            }
        }

        if (cornerRadius > 0) {
            g2.setColor(TRANSPARENT);
            g2.setComposite(AlphaComposite.Src);
            g2.fillRoundRect(minLimit(0, x - borderWidth / 2), minLimit(0, y - borderWidth / 2), w + borderWidth, h + borderWidth, cornerRadius + borderWidth, cornerRadius + borderWidth);
        } else {
            g2.setColor(TRANSPARENT);
            g2.setComposite(AlphaComposite.Src);
            g2.fillRect(minLimit(0, x - borderWidth / 2), minLimit(0, y - borderWidth / 2), w + borderWidth, h + borderWidth);
        }

        if (applyToImage) {
            // combine with image
            g2.setComposite(AlphaComposite.DstOver);
            g2.drawImage(image, 0, 0, null);
        }

        g2.dispose();
        return output;
    }

    public static BufferedImage punchOuterHighlightOval(
            BufferedImage image,
            BufferedImage outerImage,
            int x, int y, int w, int h,
            int borderWidth,
            Color outerFillColor,
            final int outerBorderWidth,
            final int outerCornerRadius,
            boolean applyToImage
    ) {
        //BufferedImage output = UIUtil.createImage(w, h, BufferedImage.TYPE_INT_ARGB);
        boolean outerFilled = outerFillColor.getAlpha() != 0;
        if (!outerFilled) {
            return outerImage;
        }

        //noinspection UndesirableClassUsage
        BufferedImage output = outerImage != null ? outerImage : new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int imgW = image.getWidth();
        int imgH = image.getHeight();

        Graphics2D g2 = output.createGraphics();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        if (outerImage == null) {
            // first one, we need to fill it
            g2.setColor(outerFillColor);
            if (outerCornerRadius > 0) {
                g2.fillRoundRect(outerBorderWidth, outerBorderWidth, imgW - 2 * outerBorderWidth, imgH - 2 * outerBorderWidth, outerCornerRadius, outerCornerRadius);
            } else {
                g2.fillRect(outerBorderWidth, outerBorderWidth, imgW - 2 * outerBorderWidth, imgH - 2 * outerBorderWidth);
            }
        }

        g2.setColor(TRANSPARENT);
        g2.setComposite(AlphaComposite.Src);
        g2.fillOval(minLimit(0, x - borderWidth / 2), minLimit(0, y - borderWidth / 2), w + borderWidth, h + borderWidth);

        if (applyToImage) {
            // combine with image
            g2.setComposite(AlphaComposite.DstOver);
            g2.drawImage(image, 0, 0, null);
        }

        g2.dispose();
        return output;
    }

    /**
     * http://stackoverflow.com/questions/2386064/how-do-i-crop-an-image-in-java
     */
    public static BufferedImage cropImage(BufferedImage image, int trimLeft, int trimRight, int trimTop, int trimBottom) {
        BufferedImage output = image.getSubimage(trimLeft, trimTop, image.getWidth() - trimLeft - trimRight, image.getHeight() - trimTop - trimBottom);
        return output;
    }

    /**
     * http://stackoverflow.com/questions/464825/converting-transparent-gif-png-to-jpeg-using-java
     */
    public static BufferedImage removeAlpha(BufferedImage image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        //BufferedImage bufferedImage = UIUtil.createImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();
        //Color.WHITE estes the background to white. You can use any other color
        g.drawImage(image, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), Color.WHITE, null);
        g.dispose();

        return bufferedImage;
    }

    /**
     * http://stackoverflow.com/questions/665406/how-to-make-a-color-transparent-in-a-bufferedimage-and-save-as-png
     */
    public static BufferedImage toTransparent(BufferedImage image, final Color color, final int tolerance) {
        //        ImageFilter filter = new RGBImageFilter() {
        //            public final int filterRGB(int x, int y, int rgb) {
        //                return (rgb << 8) & 0xFF000000;
        //            }
        //        };
        //
        //        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
        //        return toBufferedImage(Toolkit.getDefaultToolkit().createImage(ip));
        ImageFilter filter = new RGBImageFilter() {

            // the color we are looking for... Alpha bits are set to opaque
            public int markerRGB = color.getRGB() | 0xFF000000;
            int radius = tolerance * tolerance * 3;

            public final int filterRGB(int x, int y, int rgb) {
                if (tolerance == 0 && (rgb | 0xFF000000) == markerRGB) {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                } else {
                    if ((rgb & 0xFF000000) == 0xFF000000) {

                        int delta1 = ((rgb & 0xFF0000) >> 16) - ((markerRGB & 0xFF0000) >> 16);
                        int delta2 = ((rgb & 0x00FF00) >> 8) - ((markerRGB & 0x00FF00) >> 8);
                        int delta3 = ((rgb & 0x0000FF)) - ((markerRGB & 0x0000FF));

                        int radDiff = delta1 * delta1 + delta2 * delta2 + delta3 * delta3;
                        if (radDiff <= radius) {
                            // Mark the alpha bits as zero - transparent
                            return 0x00FFFFFF & rgb;
                        }
                    }
                }
                return rgb;
            }
        };

        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
        return toBufferedImage(Toolkit.getDefaultToolkit().createImage(ip));
    }

    public static byte[] getImageBytes(BufferedImage image) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, "PNG", bos);
            byte[] imageBytes = bos.toByteArray();

            bos.close();
            return imageBytes;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
