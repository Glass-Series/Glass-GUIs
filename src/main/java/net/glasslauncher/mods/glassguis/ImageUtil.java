package net.glasslauncher.mods.glassguis;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;

public class ImageUtil {
    private static final DrawContext DRAW_CONTEXT = new DrawContext();
    private static final Cache<String, Dimension> IMAGE_SIZE_CACHE = Caffeine.newBuilder().build();
    protected static final Cache<String, Image> IMAGE_CACHE = Caffeine.newBuilder().build();

    /**
     * A modified version of <a href="https://stackoverflow.com/a/12164026">SO 12164026</a>
     * Gets image dimensions for given file
     * @param imgFile image file
     * @return dimensions of image
     * @throws IOException if the file is not a known image
     */
    public static Dimension getImageDimension(File imgFile) throws IOException {
        int pos = imgFile.getName().lastIndexOf(".");
        if (pos == -1) {
            throw new IOException("No extension for file: " + imgFile.getAbsolutePath());
        }

        String suffix = imgFile.getName().substring(pos + 1);
        Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
        if (!iter.hasNext()) {
            throw new IOException("Not a known image file: " + imgFile.getAbsolutePath());
        }
        ImageReader reader = iter.next();

        try {
            ImageInputStream stream = new FileImageInputStream(imgFile);
            reader.setInput(stream);
            int width = reader.getWidth(reader.getMinIndex());
            int height = reader.getHeight(reader.getMinIndex());
            reader.dispose();
            return new Dimension(width, height);
        } catch (IOException e) {
            reader.dispose();
            throw e;
        }
    }

    /**
     * Used for caching information about images. You should only instantiate this class once, ideally, though I have used a hacky solution to get a guesstimate of the image size.
     * NOTE: There seems to be a limit of 32x32 due to limitations, but frankly, if you need an image larger than this, you shouldn't be putting it in a tooltip.
     */
    public static class Image {
        public final String image;
        @Getter
        private final int width;
        @Getter
        private final int height;

        public Image(String image) {
            this.image = image;
            Dimension dimension = IMAGE_SIZE_CACHE.get(image, key -> {
                try {
                    //noinspection DataFlowIssue
                    return getImageDimension(Paths.get(this.getClass().getResource(key).toURI()).toFile());

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            width = dimension.width;
            height = dimension.height;
        }

        public static Image of(String image) {
            return IMAGE_CACHE.get(image, Image::new);
        }

        /**
         * Made as its own method so people could in theory render literally anything they want.
         */
        public void draw(int x, int y, int maxWidth, int maxHeight, boolean shouldFill) {
            int width = this.width;
            int height = this.height;

            if ((shouldFill && width < maxWidth && height < maxHeight) || width > maxWidth || height > maxHeight) {
                float imgAspect = ((float) width) / height;
                float renderAspect = ((float) maxWidth) / maxHeight;
                if (renderAspect == 1 && imgAspect == 1) {
                    width = maxWidth;
                    height = maxHeight;
                }
                else {
                    double scaleX = (double) maxWidth / width;
                    double scaleY = (double) maxHeight / height;

                    double fittingAspect = Math.min(scaleX, scaleY);

                    width = (int) (width * fittingAspect);
                    height = (int) (height * fittingAspect);
                }
            }

            GL11.glDisable(2896);
            GL11.glDisable(2912);
            RenderHelper.bindTexture(image);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderHelper.drawTexture(x, y, width, height);
        }
    }

    public record Dimension(int width, int height) {}
}
