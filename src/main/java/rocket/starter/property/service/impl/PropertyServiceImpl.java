package rocket.starter.property.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import rocket.starter.common.AbstractService;
import rocket.starter.common.BaseRepo;
import rocket.starter.property.domain.Property;
import rocket.starter.property.repo.PropertyRepo;
import rocket.starter.property.service.PropertyService;

/**
 * 属性操作.
 *
 * @author ron 2021/10/21
 */
@Component
@CacheConfig(cacheNames = "property")
@RequiredArgsConstructor
public class PropertyServiceImpl extends AbstractService<Property>
    implements PropertyService {

  private final PropertyRepo repository;
  private final ApplicationContext applicationContext;


  @Override
  @Cacheable(key = "#key")
  public String getPropertyValue(String key) {
    Property property = repository.findByReferenceKey(key);
    return property.getValue();
  }

  @Override
  protected BaseRepo<Property> getBaseRepo() {
    return repository;
  }

  /**
   * 更新属性内容.
   * 这里通过key去清除相关缓存时注意，key属性不能被更新.
   *
   * @param property 属性
   */
  @Override
  public void afterUpdate(Property property) {
    applicationContext.getBean(PropertyServiceImpl.class).clearCache(property);
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

}
