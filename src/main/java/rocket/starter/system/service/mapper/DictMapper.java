package rocket.starter.system.service.mapper;

import org.springframework.stereotype.Component;
import rocket.starter.system.body.DictBody;
import rocket.starter.system.domain.Dict;


/**
 * 字典映射.
 *
 * @author ron 2022/8/12
 */
@Component
public class DictMapper {

  /**
   * 转换到字典.
   *
   * @param dictBody dictBody
   * @return 字典
   */
  public Dict mapDict(DictBody dictBody) {
    Dict dict = new Dict();
    dict.setName(dictBody.getName());
    dict.setDescription(dictBody.getDescription());
    dict.setId(dictBody.getId());
    return dict;
  }
}
