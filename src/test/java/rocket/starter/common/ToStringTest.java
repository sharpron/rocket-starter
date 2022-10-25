package rocket.starter.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author ron 2022/10/25
 */
public class ToStringTest {

  @Getter
  @Setter
  @ToString
  static abstract class Base {

    private Long id;
  }


  @Getter
  @Setter
  @ToString(callSuper = true)
  static class MyClass extends Base {

    private String name;
  }

  @Test
  public void testToString() {
    MyClass myClass = new MyClass();
    myClass.setId(1L);
    myClass.setName("Name");

    Assertions.assertEquals(
        "ToStringTest.MyClass(super=ToStringTest.Base(id=1), name=Name)", myClass.toString());

  }
}
