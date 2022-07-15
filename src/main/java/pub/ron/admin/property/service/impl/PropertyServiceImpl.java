package pub.ron.admin.property.service.impl;

import java.util.Optional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import pub.ron.admin.common.AbstractService;
import pub.ron.admin.property.domain.Property;
import pub.ron.admin.property.repo.PropertyRepo;
import pub.ron.admin.property.service.PropertyService;

/**
 * 属性操作.
 *
 * @author ron 2021/10/21
 */
@Component
@CacheConfig(cacheNames = "property")
public class PropertyServiceImpl extends AbstractService<Property, PropertyRepo>
    implements PropertyService {

  public PropertyServiceImpl(PropertyRepo repository) {
    super(repository);
  }


  @Override
  @Cacheable(key = "#key")
  public String getPropertyValue(String key) {
    Property property = repository.findByKey(key);
    return property.getValue();
  }

  /**
   * 更新属性内容.
   * 这里通过key去清除相关缓存时注意，key属性不能被更新.
   *
   * @param property 属性
   */
  @Override
  @CacheEvict(key = "#property.key")
  public void update(Property property) {
    super.update(property);
  }

  /**
   * 清除缓存的用法.
   *
   * @param property property
   */
  @CacheEvict(key = "#property.key")
  public void clearCache(Property property) {
    // empty block
  }

  @Override
  public void deleteById(Long id) {
    final Optional<Property> optional = repository.findById(id);
    if (optional.isEmpty()) {
      throw new IllegalArgumentException("数据不存在" + id);
    }
    repository.delete(optional.get());
  }
}
