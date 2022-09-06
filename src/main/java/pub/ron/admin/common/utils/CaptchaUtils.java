package pub.ron.admin.common.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Random;
import java.util.UUID;
import javax.imageio.ImageIO;

/**
 * This is a simple captcha class, use it to generate a random string and then to create an image of
 * it.
 *
 * @author ron
 */
public class CaptchaUtils {

  private CaptchaUtils() {
  }

  /**
   * Generates a random alphanumeric string of eight characters.
   *
   * @return random alphanumeric string of eight characters.
   */
  public static String generateText() {
    return UUID.randomUUID().toString().substring(0, 4);
  }

  /**
   * Generates a PNG image of text 180 pixels wide, 40 pixels high with white background.
   *
   * @param text expects string size eight (8) characters.
   * @return byte array that is a PNG image generated with text displayed.
   */
  public static byte[] generateImage(String text) {
    final int w = 100;
    final int h = 30;
    BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    Graphics2D g = image.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
        RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    g.setColor(Color.white);
    g.fillRect(0, 0, w, h);
    g.setFont(new Font("Serif", Font.PLAIN, 26));
    g.setColor(Color.blue);
    int start = 10;
    byte[] bytes = text.getBytes();

    Random random = new Random();
    for (int i = 0; i < bytes.length; i++) {
      g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
      g.drawString(new String(new byte[] {bytes[i]}), start + (i * 20),
          (int) (Math.random() * 10 + 20));
    }
    g.setColor(Color.white);
    g.dispose();
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    try {
      ImageIO.write(image, "png", bout);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return bout.toByteArray();
  }
}