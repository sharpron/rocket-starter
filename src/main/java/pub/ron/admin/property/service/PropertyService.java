package pub.ron.admin.property.service;


import pub.ron.admin.common.BaseService;
import pub.ron.admin.property.domain.Property;

/**
 * 属性操作.
 *
 * @author ron 2021/10/21
 */
public interface PropertyService extends BaseService<Property> {

  /**
   * 通过属性key获取值.
   *
   * @param key key
   * @return 值
   */
  String getPropertyValue(String key);
}
