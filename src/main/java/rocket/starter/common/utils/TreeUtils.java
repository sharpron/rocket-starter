package rocket.starter.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 树形结构数据生成工具.
 *
 * @author ron 2022/8/7
 */
public class TreeUtils {

  private TreeUtils() {
  }

  /**
   * 定义树结点.
   *
   * @param <T> 节点类型
   */
  public interface Node<T> {

    /**
     * 该节点是否是另一个节点的子节点.
     *
     * @param parent parent
     * @return true or false
     */
    boolean childrenOf(T parent);

    /**
     * 添加子节点.
     *
     * @param child child
     */
    void addChildren(T child);
  }


  /**
   * 树节点处理器.
   *
   * @param <T> 节点处理
   */
  public interface NodeHandler<T> {

    /**
     * 后一个节点是否时前一个节点的子节点.
     *
     * @param parent parent
     * @param child  child
     * @return true or false
     */
    boolean childrenOf(T parent, T child);

    /**
     * 向父节点添加子节点.
     *
     * @param parent parent
     * @param child  child
     */
    void addChildren(T parent, T child);
  }


  /**
   * 构建树结构数据.
   *
   * @param inputs 源数据
   * @param <T>    数据类型
   * @return 树结构数据
   */
  public static <T extends Node<T>> List<T> buildTree(List<T> inputs) {
    return buildTree(inputs, new NodeHandler<>() {
      @Override
      public boolean childrenOf(T parent, T child) {
        return child.childrenOf(parent);
      }

      @Override
      public void addChildren(T parent, T child) {
        parent.addChildren(child);
      }
    });
  }

  /**
   * 构建树结构数据.
   *
   * @param inputs 源数据
   * @param <T>    数据类型
   * @return 树结构数据
   */
  public static <T> List<T> buildTree(List<T> inputs, NodeHandler<T> nodeHandler) {
    List<T> used = new ArrayList<>();
    for (T input : inputs) {
      for (T i : inputs) {
        if (nodeHandler.childrenOf(input, i)) {
          nodeHandler.addChildren(input, i);
          used.add(i);
        }
      }
    }

    return inputs.stream()
        .filter(e -> !used.contains(e))
        .collect(Collectors.toList());
  }
}
