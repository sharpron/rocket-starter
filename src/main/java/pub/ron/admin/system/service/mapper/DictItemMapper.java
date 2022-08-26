package pub.ron.admin.system.service.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import pub.ron.admin.system.body.DictItemBody;
import pub.ron.admin.system.domain.DictItem;
import pub.ron.admin.system.dto.DictItemSmallDto;

/**
 * 字典项映射.
 *
 * @author ron 2022/8/12
 */
@Component
public class DictItemMapper {

  /**
   * 转换到字典项.
   *
   * @param dictItemBody dictItemBody
   * @return 字典项
   */
  public DictItem mapDict(DictItemBody dictItemBody) {
    DictItem dictItem = new DictItem();
    dictItem.setName(dictItemBody.getName());
    dictItem.setValue(dictItemBody.getValue());
    dictItem.setDisabled(dictItemBody.getDisabled());
    dictItem.setDictId(dictItemBody.getDictId());
    dictItem.setOrderNo(dictItemBody.getOrderNo());
    dictItem.setId(dictItemBody.getId());
    return dictItem;
  }

  /**
   * 转换到small.
   *
   * @param dictItem dictItem
   * @return small
   */
  public DictItemSmallDto mapSmall(DictItem dictItem) {
    DictItemSmallDto dictItemSmallDto = new DictItemSmallDto();
    dictItemSmallDto.setId(dictItem.getId());
    dictItemSmallDto.setName(dictItem.getName());
    dictItemSmallDto.setDisabled(dictItem.getDisabled());
    return dictItemSmallDto;
  }

  /**
   * 转换到small.
   *
   * @param dictItems dictItems
   * @return small
   */
  public List<DictItemSmallDto> mapSmalls(List<DictItem> dictItems) {
    return dictItems.stream().map(this::mapSmall).collect(Collectors.toList());
  }

  /**
   * 通过id映射字典项.
   *
   * @param id id
   * @return 字典项
   */
  public DictItem mapWithId(Long id) {
    if (id == null) {
      return null;
    }
    return new DictItem(id);
  }
}
