package pub.ron.admin.common.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import pub.ron.admin.common.AppException;
import pub.ron.admin.system.domain.Dept;
import pub.ron.admin.system.domain.Role;
import pub.ron.admin.system.security.SubjectUtils;
import pub.ron.admin.system.security.principal.UserPrincipal;

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
      return (root, query, criteriaBuilder) -> null;
    }
    return (root, query, criteriaBuilder) -> {
      final Class<?> aClass = o.getClass();
      return Stream.of(aClass.getDeclaredFields())
          .peek(e -> e.setAccessible(true))
          .filter(e -> e.isAnnotationPresent(Where.class))
          .map(
              e -> {
                final Object fieldVal;
                try {
                  fieldVal = e.get(o);
                } catch (IllegalAccessException ex) {
                  throw new AssertionError(ex);
                }
                if (fieldVal == null) {
                  return null;
                }
                final Where where = e.getAnnotation(Where.class);
                final String rootName = where.root().isEmpty() ? e.getName() : where.root();

                switch (where.type()) {
                  case like:
                    return criteriaBuilder.like(
                        WhereBuilder.getPath(rootName, root), "%" + fieldVal + "%");
                  case eq:
                    return criteriaBuilder.equal(
                        WhereBuilder.getPath(rootName, root), fieldVal);
                  case lt:
                    return criteriaBuilder.lt(
                        WhereBuilder.getPath(rootName, root), asNumber(fieldVal));
                  case gt:
                    return criteriaBuilder.gt(
                        WhereBuilder.getPath(rootName, root), asNumber(fieldVal));
                  case le:
                    return criteriaBuilder.le(
                        WhereBuilder.getPath(rootName, root), asNumber(fieldVal));
                  case ge:
                    return criteriaBuilder.ge(
                        WhereBuilder.getPath(rootName, root), asNumber(fieldVal));
                  case between:
                    final List<Long> numbers = asBetween(fieldVal);
                    return criteriaBuilder.between(
                        WhereBuilder.getPath(rootName, root), numbers.get(0), numbers.get(1));
                  case betweenTime:
                    final List<Long> timestamps = asBetween(fieldVal);
                    return criteriaBuilder.between(
                        WhereBuilder.getPath(rootName, root),
                        new Date(timestamps.get(0)),
                        new Date(timestamps.get(1)));
                  default:
                    throw new AssertionError();
                }
              })
          .filter(Objects::nonNull)
          .reduce(criteriaBuilder::and)
          .orElse(null);
    };
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
    final Specification<T> specification = buildSpec(o);
    final UserPrincipal userPrincipal = SubjectUtils.currentUser();
    return (root, query, builder) -> {
      final Join<Role, Dept> deptJoin = root.join("dept");
      final Predicate deptPredicate =
          builder.or(
              builder.equal(deptJoin.get("id"), userPrincipal.getDeptId()),
              builder.like(deptJoin.get("path"), userPrincipal.getDeptPath() + "%"));
      final Predicate predicate = specification.toPredicate(root, query, builder);
      if (predicate == null) {
        return deptPredicate;
      }
      return builder.and(deptPredicate, predicate);
    };
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
   * type "between" need <span>List&lt;Long&gt; size=2</span>.
   *
   * <p>supports array or Collection
   *
   * @param value value
   * @return <span>List&lt;Long&gt; size=2</span>
   */
  @SuppressWarnings("unchecked")
  private static List<Long> asBetween(Object value) {
    if (value instanceof Long[]) {
      return Arrays.asList((Long[]) value);
    }
    if (value instanceof Collection) {
      return new ArrayList<>((Collection<Long>) value);
    }
    throw new AppException(String.format("只支持Collection<Long>或者Long[]类型: %s", value));
  }
}
