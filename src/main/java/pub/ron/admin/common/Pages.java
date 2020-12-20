package pub.ron.admin.common;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * @author ron 2020/12/12
 */
public class Pages {

  private Pages() {

  }

  /**
   * 分页数据转换
   *
   * @param page    原始分页
   * @param mapping 分页数据转换
   * @param <I>     输入的类型
   * @param <O>     输出的类型
   * @return 转换的数据
   */
  public static <I, O> Page<O> map(
      Page<I> page, Function<I, O> mapping) {
    final List<O> result = page.getContent().stream()
        .map(mapping)
        .collect(Collectors.toList());
    return new PageImpl<>(
        result,
        page.getPageable(),
        page.getTotalElements()
    );
  }
}
