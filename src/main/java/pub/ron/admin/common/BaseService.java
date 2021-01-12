package pub.ron.admin.common;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author ron 2021/1/1
 */
public interface BaseService<T extends BaseEntity> {

  /**
   * 创建实体
   *
   * @param t t
   */
  void create(T t);

  /**
   * 更新实体
   *
   * @param t t
   */
  void update(T t);

  /**
   * 通过id删除
   *
   * @param id id
   */
  void deleteById(Long id);

  /**
   * 分页查询
   *
   * @param pageable 分页参数
   * @param query    查询条件
   * @return 结果
   */
  Page<T> findByPage(Pageable pageable, Object query);

  /**
   * 查询所有
   *
   * @param query 查询条件
   * @return 结果
   */
  List<T> findAll(Object query);

  /**
   * 通过id进行查找
   *
   * @param id id
   * @return 结果
   */
  T findById(Long id);
}
