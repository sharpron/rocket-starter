package pub.ron.admin.system.service;

import java.util.List;
import pub.ron.admin.common.BaseService;
import pub.ron.admin.system.domain.Dict;
import pub.ron.admin.system.domain.DictItem;

/**
 * dict service.
 *
 * @author ron 2022/8/9
 */
public interface DictItemService extends BaseService<DictItem> {

  /**
   * 通过字典引用名称查询字典项.
   *
   * @param dictName 字典名称
   * @return 字典项
   */
  List<DictItem> findByDictName(String dictName);
}
