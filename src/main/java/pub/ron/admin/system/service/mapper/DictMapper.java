package pub.ron.admin.system.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pub.ron.admin.system.body.DictBody;
import pub.ron.admin.system.domain.Dict;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictMapper {

  Dict mapDept(DictBody dictBody);
}
