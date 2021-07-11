package pub.ron.admin.system.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
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

  private String title;

  private Integer orderNo;

  private String link;

  private String icon;

  private String path;

  private String component;

  private boolean hide;

  private String perm;

  private Long parentId;
}
