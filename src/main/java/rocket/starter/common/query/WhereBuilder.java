package rocket.starter.common.query;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ReflectionUtils;
import rocket.starter.common.AppException;
import rocket.starter.common.BaseEntity;
import rocket.starter.system.domain.Dept;
import rocket.starter.system.domain.Role;
import rocket.starter.system.security.Principal;
import rocket.starter.system.security.SubjectUtils;

/**
 * 条件构建器.
 *
 * @author ron 2020/11/19
 */
public class WhereBuilder {

  /**
   * Prevent external instantiation.
   */
  private WhereBuilder() {
  }

  /**
   * 构建条件.
   *
   * @param o   对象
   * @param <T> 对象的范性
   * @return 条件
   */
  public static <T> Specification<T> buildSpec(Object o) {
    if (o == null) {
      return null;
    }

    return (root, query, criteriaBuilder) -> {
      final Class<?> aClass = o.getClass();
      return Stream.of(aClass.getDeclaredFields())
          .filter(e -> e.isAnnotationPresent(Where.class))
          .map(e -> buildPredicate(e, o, criteriaBuilder, root))
          .filter(Objects::nonNull)
          .reduce(criteriaBuilder::and)
          .orElse(null);
    };
  }

  private static <T> Predicate buildPredicate(Field e, Object targetObject,
      CriteriaBuilder criteriaBuilder, Root<T> root) {
    final Where where = e.getAnnotation(Where.class);
    final String rootName = where.root().isEmpty() ? e.getName() : where.root();
    e.setAccessible(true);
    Object fieldVal = ReflectionUtils.getField(e, targetObject);
    if (fieldVal == null) {
      return where.ignoreNull() ? null :
          criteriaBuilder.isNull(WhereBuilder.getPath(rootName, root));
    }

    switch (where.type()) {
      case like:
        return criteriaBuilder.like(WhereBuilder.getPath(rootName, root), "%" + fieldVal + "%");
      case right_like:
        return criteriaBuilder.like(WhereBuilder.getPath(rootName, root), fieldVal + "%");
      case left_like:
        return criteriaBuilder.like(WhereBuilder.getPath(rootName, root), "%" + fieldVal);
      case eq:
        return criteriaBuilder.equal(WhereBuilder.getPath(rootName, root), fieldVal);
      case lt:
        return criteriaBuilder.lt(WhereBuilder.getPath(rootName, root), asNumber(fieldVal));
      case gt:
        return criteriaBuilder.gt(WhereBuilder.getPath(rootName, root), asNumber(fieldVal));
      case le:
        return criteriaBuilder.le(WhereBuilder.getPath(rootName, root), asNumber(fieldVal));
      case ge:
        return criteriaBuilder.ge(WhereBuilder.getPath(rootName, root), asNumber(fieldVal));
      case between:
        return rangePredicate(fieldVal, criteriaBuilder, rootName, root, value -> value);
      case betweenTime:
        ZoneId zoneId = ZoneId.systemDefault();
        return rangePredicate(fieldVal, criteriaBuilder, rootName, root, value ->
            LocalDateTime.ofInstant(Instant.ofEpochMilli(value), zoneId)
        );
      case in:
        return criteriaBuilder.in(WhereBuilder.getPath(rootName, root)).value(fieldVal);
      default:
        throw new AssertionError();
    }
  }

  /**
   * 范围查询条件.
   *
   * @param fieldVal        值 [start, end]
   * @param criteriaBuilder criteriaBuilder
   * @param rootName        rootName
   * @param root            root
   * @param valueConverter  valueConverter
   * @param <T>             type
   * @param <Y>             comparable value type.
   * @return 条件
   */
  private static <T, Y extends Comparable<Y>> Predicate rangePredicate(
      Object fieldVal,
      CriteriaBuilder criteriaBuilder,
      String rootName,
      Root<T> root,
      Function<Long, Y> valueConverter) {

    final List<Long> numbers = asList(fieldVal);
    if (numbers.size() != 2) {
      throw new AppException("between only two values are supported.");
    }
    Long min = numbers.get(0);
    Long max = numbers.get(1);
    if (min != null && max != null) {
      return criteriaBuilder.between(WhereBuilder.getPath(rootName, root),
          valueConverter.apply(min), valueConverter.apply(max));
    }

    if (min != null) {
      return criteriaBuilder.greaterThanOrEqualTo(
          WhereBuilder.getPath(rootName, root), valueConverter.apply(min));
    }
    if (max != null) {
      return criteriaBuilder.lessThanOrEqualTo(
          WhereBuilder.getPath(rootName, root), valueConverter.apply(max));
    }
    throw new AppException("between at least one value that is not null is required.");
  }

  /**
   * Analysis of path.
   *
   * @param rootName root name
   * @param root     root
   * @param <Y>      type of root
   * @param <T>      type of entity
   * @return Path
   */
  @SuppressWarnings("unchecked")
  private static <Y, T> Path<Y> getPath(String rootName, Root<T> root) {
    final String[] paths = rootName.split("\\.");
    Path<?> path = root;
    for (String pathString : paths) {
      path = path.get(pathString);
    }
    return (Path<Y>) path;
  }

  /**
   * Convert parameters to numeric types.
   *
   * @param o   对象
   * @param <T> entity type
   * @return Specification
   */
  public static <T> Specification<T> buildSpecWithDept(Object o) {
    final Principal principal = SubjectUtils.currentUser();
    final Specification<T> deptSpecification = (root, query, builder) -> {
      final Join<Role, Dept> deptJoin = root.join("dept");
      return builder.or(
          builder.equal(deptJoin.get(BaseEntity.ID), principal.getDeptId()),
          builder.like(deptJoin.get("path"), principal.getDeptPath() + "%"));
    };
    return deptSpecification.and(buildSpec(o));
  }

  /**
   * Convert parameters to numeric types.
   *
   * @param value value
   * @return number type
   */
  private static Number asNumber(Object value) {
    if (value instanceof Number) {
      return (Number) value;
    }
    throw new AppException(String.format("只支持Number类型: %s", value));
  }

  /**
   * Convert object to list.
   *
   * <p>supports array or Collection
   *
   * @param value value
   * @return <span>List&lt;Long&gt; size=2</span>
   */
  @SuppressWarnings("unchecked")
  private static List<Long> asList(Object value) {
    if (value instanceof Long[]) {
      return Arrays.asList((Long[]) value);
    }
    if (value instanceof Collection) {
      return new ArrayList<>((Collection<Long>) value);
    }
    throw new AppException(String.format("只支持Collection<Long>或者Long[]类型: %s", value));
  }

}
