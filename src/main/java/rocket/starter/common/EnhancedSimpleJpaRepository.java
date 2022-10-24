package rocket.starter.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 改进分页查询慢的情况.
 *
 * @author ron 2022/8/25
 */
@SuppressWarnings({"CheckStyle", "unused"})
public class EnhancedSimpleJpaRepository<T, ID> extends SimpleJpaRepository<T, ID> {

  private final EntityManager entityManager;

  @Value
  public static class IdEntity {

    Long id;
  }

  public EnhancedSimpleJpaRepository(
      JpaEntityInformation<T, ?> entityInformation,
      EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityManager = entityManager;
  }

  public EnhancedSimpleJpaRepository(Class<T> domainClass, EntityManager em) {
    super(domainClass, em);
    this.entityManager = em;
  }

  /**
   * 针对ID进行排序性优化
   *
   * @param spec     spec
   * @param pageable pageable
   * @return query
   */
  @Override
  protected TypedQuery<T> getQuery(@Nullable Specification<T> spec, Pageable pageable) {
    if (enableOptimization(pageable)) {
      Sort sort = pageable.isPaged() ? pageable.getSort() : Sort.unsorted();
      Optional<Long> locateId = getLocateId(spec, this.getDomainClass(), pageable);
      // return null query if locate id is not present.
      return locateId.map(aLong -> this.getQuery(spec, this.getDomainClass(), sort, aLong))
          .orElse(null);
    }
    return super.getQuery(spec, pageable);
  }

  /**
   * 分页核心逻辑.
   *
   * @param query       query
   * @param domainClass domainClass
   * @param pageable    pageable
   * @param spec        spec
   * @param <S>         实体类型
   * @return 分页数据
   */
  @Override
  protected <S extends T> Page<S> readPage(@Nullable TypedQuery<S> query, Class<S> domainClass,
      Pageable pageable, Specification<S> spec) {
    // return empty page if query is null.
    if (query == null) {
      return Page.empty();
    }
    if (pageable.isPaged()) {
      // optimization point
      int firstResult = enableOptimization(pageable) ? 0 : (int) pageable.getOffset();
      query.setFirstResult(firstResult);
      query.setMaxResults(pageable.getPageSize());
    }

    return PageableExecutionUtils.getPage(query.getResultList(), pageable,
        () -> executeCountQuery(this.getCountQuery(spec, domainClass)));
  }

  private Optional<Order> orderById(Sort sort) {
    Optional<Order> firstOrder = sort.get().findFirst();
    return firstOrder.filter(e -> e.getProperty().equals(BaseEntity.ID));
  }

  private boolean enableOptimization(Pageable pageable) {
    if (pageable.isUnpaged()) {
      return false;
    }
    Sort sort = pageable.getSort();
    return orderById(sort).isPresent();
  }


  private <S extends T> TypedQuery<S> getQuery(@Nullable Specification<S> spec,
      Class<S> domainClass, Sort sort, Long id) {
    CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<S> query = builder.createQuery(domainClass);
    Root<S> root = query.from(domainClass);
    query.select(root);
    if (sort.isSorted()) {
      query.orderBy(QueryUtils.toOrders(sort, root, builder));
    }
    Order idOrder = orderById(sort).orElseThrow(AssertionError::new);

    List<Predicate> predicates = new ArrayList<>(2);

    if (id != null) {
      Predicate idPredicate = idOrder.getDirection() == Direction.DESC ?
          builder.le(root.get(BaseEntity.ID), id) :
          builder.ge(root.get(BaseEntity.ID), id);
      predicates.add(idPredicate);
    }

    if (spec != null) {
      Predicate predicate = spec.toPredicate(root, query, builder);
      if (predicate != null) {
        predicates.add(predicate);
      }
    }

    query.where(predicates.toArray(Predicate[]::new));
    return this.applyRepositoryMethodMetadata(this.entityManager.createQuery(query));
  }


  private <S extends T> Optional<Long> getLocateId(@Nullable Specification<S> spec,
      Class<S> domainClass, Pageable pageable) {
    CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> query = builder.createQuery(Long.class);
    Root<S> root = query.from(domainClass);
    query.select(root.get(BaseEntity.ID));
    Sort sort = pageable.isPaged() ? pageable.getSort() : Sort.unsorted();
    if (sort.isSorted()) {
      query.orderBy(QueryUtils.toOrders(sort, root, builder));
    }
    if (spec != null) {
      Predicate predicate = spec.toPredicate(root, query, builder);
      if (predicate != null) {
        query.where(predicate);
      }
    }

    return this.applyRepositoryMethodMetadata(this.entityManager.createQuery(query))
        .setFirstResult((int) pageable.getOffset())
        .setMaxResults(1)
        .getResultStream()
        .findFirst();
  }

  private <S> TypedQuery<S> applyRepositoryMethodMetadata(TypedQuery<S> query) {
    CrudMethodMetadata metadata = getRepositoryMethodMetadata();
    if (metadata == null) {
      return query;
    } else {
      LockModeType type = metadata.getLockModeType();
      TypedQuery<S> toReturn = type == null ? query : query.setLockMode(type);
      this.getQueryHints().withFetchGraphs(this.entityManager).forEach(query::setHint);
      return toReturn;
    }
  }

  private static long executeCountQuery(TypedQuery<Long> query) {
    Assert.notNull(query, "TypedQuery must not be null!");
    List<Long> totals = query.getResultList();
    long total = 0L;

    Long element;
    for (Iterator<Long> var4 = totals.iterator(); var4.hasNext();
        total += element == null ? 0L : element) {
      element = var4.next();
    }

    return total;
  }
}
