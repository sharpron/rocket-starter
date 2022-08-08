package pub.ron.admin.system.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pub.ron.admin.system.body.MenuBody;
import pub.ron.admin.system.domain.Menu;
import pub.ron.admin.system.dto.MenuDto;
import pub.ron.admin.system.dto.MenuSmallDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapper {

  MenuDto mapDto(Menu menu);

  MenuSmallDto mapSmallDto(Menu menu);

  Menu mapMenu(MenuBody menuBody);
}
