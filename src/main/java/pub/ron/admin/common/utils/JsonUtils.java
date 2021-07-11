package pub.ron.admin.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * json utilities.
 *
 * @author herong 2021/2/9
 */
public class JsonUtils {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private JsonUtils() {}

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
}
