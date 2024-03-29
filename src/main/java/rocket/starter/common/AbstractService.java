package rocket.starter.common;

import java.util.List;
import java.util.Set;
import javax.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import rocket.starter.common.query.WhereBuilder;

/**
 * 常用的操作.
 *
 * @author ron 2021/1/1
 */
@RequiredArgsConstructor
public abstract class AbstractService<T extends BaseEntity>
    implements BaseService<T> {

  protected abstract BaseRepo<T> getBaseRepo();

  /**
   * 将实体创建到数据库中.
   *
   * @param t t
   */
  @Override
  public void create(T t) {
    if (t.getId() != null) {
      throw new IllegalArgumentException("创建时不能指定id");
    }
    beforeCreate(t);
    getBaseRepo().save(t);
    afterCreate(t);
  }

  protected void beforeCreate(T t) {

  }

  protected void afterCreate(T t) {

  }

  /**
   * 更新操作.
   *
   * @param t t
   */
  @Override
  public void update(T t) {
    if (t.getId() == null) {
      throw new IllegalArgumentException("修改时必须指定id");
    }
    beforeUpdate(t);
    getBaseRepo().save(t);
    afterUpdate(t);
  }

  protected void beforeUpdate(T t) {

  }

  protected void afterUpdate(T t) {

  }


  /**
   * 通过id删除数据.
   *
   * @param id id
   */
  @Override
  public void deleteById(Long id) {
    getBaseRepo().findById(id).ifPresent(t -> {
      beforeDelete(t);
      getBaseRepo().delete(t);
      afterDelete(t);
    });
  }

  protected void beforeDelete(T t) {

  }

  protected void afterDelete(T t) {

  }

  @Override
  public void deleteByIds(Set<Long> ids) {
    BaseRepo<T> baseRepo = getBaseRepo();
    List<T> entities = baseRepo.findAllById(ids);
    for (T entity : entities) {
      beforeDelete(entity);
      baseRepo.delete(entity);
      afterDelete(entity);
    }
  }

  /**
   * 分页查询数据.
   *
   * @param pageable 分页参数
   * @param query    查询条件
   * @return 查询结果
   */
  @Override
  public final Page<T> findByPage(Pageable pageable, Object query) {
    return getBaseRepo().findAll(getSpecification(query), pageable);
  }

  @Override
  public final Page<T> findByPage(Long lastId, int size, Object criteria) {
    return getBaseRepo().findAll((Specification<T>) (root, query, criteriaBuilder) -> {
      Specification<T> specification = getSpecification(criteria);
      Predicate predicate = specification.toPredicate(root, query, criteriaBuilder);
      if (lastId == null) {
        return predicate;
      }

      Predicate pagePredicate = criteriaBuilder.gt(root.get(BaseEntity.ID), lastId);
      if (predicate == null) {
        return pagePredicate;
      }
      return criteriaBuilder.and(predicate, pagePredicate);
    }, PageRequest.of(0, size, Direction.DESC, BaseEntity.ID));
  }

  /**
   * 获取指定的条件.
   *
   * @param query 查询相关的数据
   * @return 条件
   */
  protected Specification<T> getSpecification(Object query) {
    return WhereBuilder.buildSpec(query);
  }

  /**
   * 查询所有复合条件的数据.
   *
   * @param query 查询条件
   * @return 所有复合条件的数据
   */
  @Override
  public final List<T> findAll(Object query) {
    return getBaseRepo().findAll(getSpecification(query));
  }

  @Override
  public List<T> findAll(Object query, Sort sort) {
    return getBaseRepo().findAll(getSpecification(query), sort);
  }

  /**
   * 通过主键获取数据.
   *
   * @param id id
   * @return 数据
   */
  @Override
  public final T findById(Long id) {
    return getBaseRepo().findById(id).orElseThrow(() -> new AppException("数据不存在id：" + id));
  }
}
