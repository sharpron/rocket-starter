package pub.ron.admin.system.dto;

import lombok.Data;
import pub.ron.admin.common.validator.Password;

/**
 * modify pass dto.
 *
 * @author ron 2020/11/18
 */
@Data
public class ModifyPassDto {

  @Password private String oldPass;

  @Password private String newPass;
}
