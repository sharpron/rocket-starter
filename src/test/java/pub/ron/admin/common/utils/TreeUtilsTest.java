package pub.ron.admin.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pub.ron.admin.common.utils.TreeUtils.Node;

/**
 * TreeUtilsTest.
 *
 * @author ron 2022/8/26
 */
public class TreeUtilsTest {

  @Value
  @RequiredArgsConstructor
  static class Menu implements Node<Menu> {

    Long id;
    Long parentId;
    String title;
    List<Menu> children;

    public Menu(Long id, Long parentId, String title) {
      this(id, parentId, title, new ArrayList<>());
    }

    @Override
    public boolean childrenOf(Menu menu) {
      return Objects.equals(parentId, menu.getId());
    }

    @Override
    public void addChildren(Menu menu) {
      children.add(menu);
    }
  }

  private static List<Menu> mockData() {
    Menu system = new Menu(1L, null, "系统管理");
    Menu menu = new Menu(10L, 1L, "菜单管理");
    Menu log = new Menu(11L, 1L, "日志管理");
    Menu menuADD = new Menu(100L, 10L, "菜单新增");
    return Arrays.asList(system, menu, log, menuADD);
  }

  private static List<Menu> expectedData() {
    return List.of(new Menu(1L, null, "系统管理", Arrays.asList(
        new Menu(10L, 1L, "菜单管理", List.of(new Menu(100L, 10L, "菜单新增"))),
        new Menu(11L, 1L, "日志管理")
    )));
  }

  @Test
  public void test() {
    List<Menu> menus = TreeUtils.buildTree(mockData());
    Assertions.assertEquals(expectedData(), menus);
  }

  @Test
  public void testShuffle() {
    List<Menu> menus = mockData();
    Collections.shuffle(menus);
    Assertions.assertEquals(expectedData(), TreeUtils.buildTree(menus));
  }
}
