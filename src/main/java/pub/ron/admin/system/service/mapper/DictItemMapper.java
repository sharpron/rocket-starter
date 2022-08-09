package pub.ron.admin.system.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pub.ron.admin.system.body.DictItemBody;
import pub.ron.admin.system.domain.DictItem;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictItemMapper {

  DictItem mapDict(DictItemBody dictItemBody);
}
