package rocket.starter.system.service.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import rocket.starter.system.body.MenuBody;
import rocket.starter.system.domain.Menu;
import rocket.starter.system.dto.MenuDto;
import rocket.starter.system.dto.MenuSmallDto;

/**
 * 菜单映射.
 *
 * @author ron 2022/8/12
 */
@Component
public class MenuMapper {

  /**
   * 转换成dto.
   *
   * @param menu menu
   * @return dto
   */
  public MenuDto mapDto(Menu menu) {
    MenuDto menuDto = new MenuDto();
    menuDto.setTitle(menu.getTitle());
    menuDto.setOrderNo(menu.getOrderNo());
    menuDto.setParentId(menu.getParentId());
    menuDto.setType(menu.getType());
    menuDto.setPath(menu.getPath());
    menuDto.setCacheable(menu.getCacheable());
    menuDto.setIcon(menu.getIcon());
    menuDto.setHide(menu.isHide());
    menuDto.setPerm(menu.getPerm());
    menuDto.setChildren(new ArrayList<>());
    menuDto.setId(menu.getId());
    menuDto.setCreateBy(menu.getCreateBy());
    menuDto.setCreateTime(menu.getCreateTime());
    menuDto.setModifyBy(menu.getModifyBy());
    menuDto.setModifyTime(menu.getModifyTime());
    menuDto.setChildren(new ArrayList<>());
    return menuDto;
  }

  public List<MenuDto> mapDto(List<Menu> menus) {
    return menus.stream().map(this::mapDto).collect(Collectors.toList());
  }

  /**
   * 转换成small.
   *
   * @param menu menu
   * @return small
   */
  public MenuSmallDto mapSmallDto(Menu menu) {
    MenuSmallDto menuSmallDto = new MenuSmallDto();
    menuSmallDto.setId(menu.getId());
    menuSmallDto.setParentId(menu.getParentId());
    menuSmallDto.setTitle(menu.getTitle());
    menuSmallDto.setChildren(new ArrayList<>());
    return menuSmallDto;
  }

  public List<MenuSmallDto> mapSmallDto(List<Menu> menu) {
    return menu.stream().map(this::mapSmallDto).collect(Collectors.toList());
  }

  /**
   * 转换成实体.
   *
   * @param menuBody menuBody
   * @return 实体
   */
  public Menu mapMenu(MenuBody menuBody) {
    Menu menu = new Menu();
    menu.setTitle(menuBody.getTitle());
    menu.setOrderNo(menuBody.getOrderNo());
    menu.setType(menuBody.getType());
    menu.setIcon(menuBody.getIcon());
    menu.setPath(menuBody.getPath());
    menu.setCacheable(menuBody.getCacheable());
    menu.setHide(menuBody.isHide());
    menu.setPerm(menuBody.getPerm());
    menu.setParentId(menuBody.getParentId());
    menu.setId(menuBody.getId());
    return menu;
  }
}
