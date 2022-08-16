package pub.ron.admin.common.utils;

import java.util.ArrayList;
import java.util.List;
import lombok.Value;

/**
 * 树形结构数据生成工具.
 *
 * @author ron 2022/8/7
 */
public class TreeUtils {

  private TreeUtils() {

  }

  /**
   * 生成树状数据.
   *
   * @param inputs    输入
   * @param matcher   匹配器
   * @param converter 转换器
   * @param <I>       数据输入类型
   * @param <O>       数据输出类型
   * @return 结果
   */
  public static <I, O> List<O> genTree(List<I> inputs,
                                       Matcher<I> matcher,
                                       Converter<I, O> converter) {
    List<O> result = new ArrayList<>();
    genTree(inputs, null, matcher, converter, result);
    return result;
  }

  private static <I, O> void genTree(List<I> inputs,
                                     Object parentId,
                                     Matcher<I> matcher,
                                     Converter<I, O> converter,
                                     List<O> outputs) {
    for (I input : inputs) {
      if (matcher.match(input, parentId)) {
        Result<O> result = converter.convert(input);
        outputs.add(result.output);
        genTree(inputs, result.parentId, matcher, converter, result.children);
      }
    }
  }

  /**
   * 检查是否是某个对象的子对象.
   *
   * @param <I> 类型
   */
  public interface Matcher<I> {

    boolean match(I input, Object pid);
  }

  /**
   * 结果.
   *
   * @param <O> 结果类型.
   */
  @Value
  public static class Result<O> {

    O output;
    Object parentId;
    List<O> children;
  }

  /**
   * 转换器.
   *
   * @param <I> 输入类型
   * @param <O> 输出类型
   */
  public interface Converter<I, O> {

    /**
     * 转换结果.
     *
     * @param input 输入
     * @return 结果
     */
    Result<O> convert(I input);
  }


}
