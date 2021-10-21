package pub.ron.admin.property.dto;

import lombok.Data;
import pub.ron.admin.common.query.Where;
import pub.ron.admin.property.domain.ValueType;

/**
 * 属性查询条件.
 *
 * @author ron 2021/10/21
 */
@Data
public class PropertyCriteria {

  @Where(type = Where.Type.like)
  private String key;

  @Where(type = Where.Type.like)
  private String value;

  @Where
  private ValueType valueType;
}
