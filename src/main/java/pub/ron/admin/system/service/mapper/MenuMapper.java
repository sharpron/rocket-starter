package pub.ron.admin.system.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pub.ron.admin.system.domain.Menu;
import pub.ron.admin.system.dto.MenuDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapper {

  MenuDto mapDto(Menu menu);
}
