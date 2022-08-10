package pub.ron.admin.system.service.mapper;

import java.util.ArrayList;
import org.springframework.stereotype.Component;
import pub.ron.admin.system.body.MenuBody;
import pub.ron.admin.system.domain.Menu;
import pub.ron.admin.system.dto.MenuDto;
import pub.ron.admin.system.dto.MenuSmallDto;

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
    return menuDto;
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
    menuSmallDto.setTitle(menu.getTitle());
    menuSmallDto.setChildren(new ArrayList<>());
    return menuSmallDto;
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
