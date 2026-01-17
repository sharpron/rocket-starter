package rocket.starter.common.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import org.apache.commons.lang3.RandomStringUtils;

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
    return RandomStringUtils.random(4, true, true);
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

    for (int i = 0; i < bytes.length; i++) {
      g.setColor(randomColor());
      g.drawString(new String(new byte[] {bytes[i]}), start + (i * 20),
          (int) (Math.random() * 10 + 20));
    }
    g.setColor(Color.white);
    g.dispose();


    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    // 使用MemoryCacheImageOutputStream可提速20倍
    try (MemoryCacheImageOutputStream imageOs = new MemoryCacheImageOutputStream(bout)) {
      ImageIO.write(image, "JPEG", imageOs);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return bout.toByteArray();
  }

  private static Color randomColor() {
    Random r = new Random();
    return new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
  }
}