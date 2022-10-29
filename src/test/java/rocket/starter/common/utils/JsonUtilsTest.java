package rocket.starter.common.utils;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author ron 2022/10/24
 */
public class JsonUtilsTest {

  @Data
  static class TestObject {
    private String a;
    private int b;
  }

  @Test
  public void testToString() {
    TestObject testObject = new TestObject();
    testObject.setA("a");
    testObject.setB(1);
    String result = JsonUtils.toString(testObject);
    Assertions.assertEquals("{\"a\":\"a\",\"b\":1}", result);
  }

  @Test
  public void testFromBytes() {
    String testJson = "{\"a\":\"a\",\"b\":1}";
    TestObject testObject = JsonUtils.fromBytes(testJson.getBytes(), TestObject.class);
    Assertions.assertEquals("a", testObject.getA());
    Assertions.assertEquals(1, testObject.getB());
  }
}
