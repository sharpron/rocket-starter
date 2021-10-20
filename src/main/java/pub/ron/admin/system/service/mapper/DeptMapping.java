package pub.ron.admin.system.service.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.mapstruct.Mapping;

/**
 * dept mapping.
 *
 * @author ron 2020/11/19
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
@Mapping(source = "deptId", target = "dept.id")
public @interface DeptMapping {
}
