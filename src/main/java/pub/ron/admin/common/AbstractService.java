package pub.ron.admin.common;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pub.ron.admin.common.query.WhereBuilder;

/**
 * 常用的操作
 *
 * @author ron 2021/1/1
 */
@RequiredArgsConstructor
public class AbstractService<T extends BaseEntity, R extends BaseRepo<T>>
    implements BaseService<T> {

  /**
   * repository
   */
  protected final R repository;


  /**
   * 将实体创建到数据库中
   *
   * @param t t
   */
  @Override
  public void create(T t) {
    if (t.getId() != null) {
      throw new IllegalArgumentException("创建时不能指定id");
    }
    repository.save(t);
  }


  /**
   * 更新操作
   *
   * @param t t
   */
  @Override
  public void update(T t) {
    if (t.getId() == null) {
      throw new IllegalArgumentException("修改时必须指定id");
    }
    repository.save(t);
  }

  /**
   * 通过id删除数据
   *
   * @param id id
   */
  @Override
  public void deleteById(Long id) {
    final Optional<T> optional = repository.findById(id);
    if (optional.isEmpty()) {
      throw new IllegalArgumentException("数据不存在" + id);
    }
    repository.delete(optional.get());
  }

  /**
   * 分页查询数据
   *
   * @param pageable 分页参数
   * @param query    查询条件
   * @return 查询结果
   */
  @Override
  public final Page<T> findByPage(Pageable pageable, Object query) {
    return repository.findAll(getSpecification(query), pageable);
  }

  /**
   * 获取指定的条件
   *
   * @param query 查询相关的数据
   * @return 条件
   */
  protected Specification<T> getSpecification(Object query) {
    return WhereBuilder.buildSpec(query);
  }

  /**
   * 查询所有复合条件的数据
   *
   * @param query 查询条件
   * @return 所有复合条件的数据
   */
  @Override
  public final List<T> findAll(Object query) {
    return repository.findAll(getSpecification(query));
  }

  /**
   * 通过主键获取数据
   *
   * @param id id
   * @return 数据
   */
  @Override
  public final T findById(Long id) {
    return repository.findById(id).orElseThrow(
        () -> new AppException("数据不存在id：" + id)
    );
  }
}
