package rocket.starter.property.repo;

import org.springframework.stereotype.Repository;
import rocket.starter.common.BaseRepo;
import rocket.starter.property.domain.Property;

/**
 * 属性操作.
 *
 * @author ron 2021/10/21
 */
@Repository
public interface PropertyRepo extends BaseRepo<Property> {

  /**
   * 通过key查询属性.
   *
   * @param key key
   * @return 属性
   */
  Property findByReferenceKey(String key);
}
