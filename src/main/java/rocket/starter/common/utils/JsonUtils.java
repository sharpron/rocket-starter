package rocket.starter.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 * json utilities.
 *
 * @author herong 2021/2/9
 */
@SuppressWarnings("unused")
public class JsonUtils {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private JsonUtils() {
  }

  /**
   * 将对象转化为json string.
   *
   * @param o 对象
   * @return json string
   */
  public static String toString(Object o) {
    try {
      return OBJECT_MAPPER.writeValueAsString(o);
    } catch (JsonProcessingException e) {
      throw new AssertionError(e);
    }
  }

  /**
   * 将json bytes 转换为对象.
   *
   * @param bytes json bytes
   * @return 对象
   */
  public static <T> T fromBytes(byte[] bytes, Class<T> clazz) {
    try {
      return OBJECT_MAPPER.readValue(bytes, clazz);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
