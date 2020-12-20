package pub.ron.admin.system.service.mapper;

import pub.ron.admin.system.domain.Menu;
import pub.ron.admin.system.dto.MenuDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MenuMapper {

  MenuDto mapDto(Menu menu);

}
