import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class StickerGenerator {
    public void create(InputStream inputStream, String stickerText, Color stickerColor, String fileName) {
        try {
            // read the original image
            BufferedImage originalImage = ImageIO.read(inputStream);

            // create a new image in memory with transparency and new size
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            int bottomMarginToIncrease = 200;
            int newImageHeight = height + bottomMarginToIncrease;
            BufferedImage newImage = new BufferedImage(width, newImageHeight, BufferedImage.TRANSLUCENT);

            // copy original image to new image in memory
            Graphics2D graphics = (Graphics2D) newImage.getGraphics();
            graphics.drawImage(originalImage, 0, 0, null);

            // set font
            Font font = new Font(Font.SANS_SERIF, Font.BOLD, 128);
            graphics.setColor(stickerColor);
            graphics.setFont(font);

            // write a phrase in the new image
            FontMetrics fontMetrics = graphics.getFontMetrics();
            Rectangle2D stringBounds = fontMetrics.getStringBounds(stickerText, graphics);
            int stickerTextWidth = (int) stringBounds.getWidth();
            int stickerTextHeight = (int) stringBounds.getHeight();

            int stickerTextXPosition = (width / 2) - (stickerTextWidth / 2);
            int stickerTextYPosition = newImageHeight - (bottomMarginToIncrease / 2) + (stickerTextHeight / 3);
            graphics.drawString(stickerText, stickerTextXPosition, stickerTextYPosition);

            // draw stroke on text
            FontRenderContext fontRenderContext = graphics.getFontRenderContext();
            TextLayout textLayout = new TextLayout(stickerText, font, fontRenderContext);

            Shape outline = textLayout.getOutline(null);
            AffineTransform transform = graphics.getTransform();
            transform.translate(stickerTextXPosition, stickerTextYPosition);
            graphics.setTransform(transform);

            BasicStroke outlineStroke = new BasicStroke(stickerTextWidth * 0.01F);
            graphics.setStroke(outlineStroke);

            graphics.setColor(Color.BLACK);
            graphics.draw(outline);
            graphics.setClip(outline);

            // creates the sticker directory if it doesn't exist
            File stickerDirectory = new File("generated-stickers");
            if(!stickerDirectory.exists()) {
                stickerDirectory.mkdir();
            }

            // write the new image in a file
            ImageIO.write(newImage, "png", new File(stickerDirectory + "/" + fileName + ".png"));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
