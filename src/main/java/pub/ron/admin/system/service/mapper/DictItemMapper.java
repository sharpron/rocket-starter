package pub.ron.admin.system.service.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pub.ron.admin.system.body.DictItemBody;
import pub.ron.admin.system.domain.DictItem;
import pub.ron.admin.system.dto.DictItemSmallDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictItemMapper {

  DictItem mapDict(DictItemBody dictItemBody);

  List<DictItemSmallDto> mapSmalls(List<DictItem> dictItems);
}
