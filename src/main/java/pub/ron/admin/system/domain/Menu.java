package pub.ron.admin.system.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pub.ron.admin.common.BaseEntity;

/**
 * menu.
 *
 * @author ron 2020/12/14
 */
@Getter
@Setter
@Entity
@Table(name = "sys_menu")
public class Menu extends BaseEntity {

  /**
   * menu title.
   */
  @NotBlank
  private String title;

  /**
   * order no.
   */
  @NotNull
  private Integer orderNo;

  /**
   * menu type.
   */
  @NotNull
  private MenuType type;

  /**
   * type == ALL.
   */
  private String icon;

  /**
   * type == MENU or type == LINK.
   */
  private String path;


  private Boolean cacheable;

  /**
   * default is true for type == BUTTON.
   * others is false.
   */
  private boolean hide;

  /**
   * Use everything but type equals CATEGORY.
   */
  private String perm;

  /**
   * parent id.
   */
  private Long parentId;
}
