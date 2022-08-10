package pub.ron.admin.system.service.mapper;

import org.springframework.stereotype.Component;
import pub.ron.admin.system.body.DictBody;
import pub.ron.admin.system.domain.Dict;


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
