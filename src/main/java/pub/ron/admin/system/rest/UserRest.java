package pub.ron.admin.system.rest;

import io.swagger.v3.oas.annotations.Operation;
import java.util.Set;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pub.ron.admin.common.validator.Create;
import pub.ron.admin.common.validator.Update;
import pub.ron.admin.logging.Log;
import pub.ron.admin.system.body.UserBody;
import pub.ron.admin.system.dto.ForceModifyPassDto;
import pub.ron.admin.system.dto.ModifyPassDto;
import pub.ron.admin.system.dto.UserQuery;
import pub.ron.admin.system.service.UserService;
import pub.ron.admin.system.service.mapper.UserMapper;

/**
 * user rest api.
 *
 * @author ron 2020/11/18
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRest {

  private final UserService userService;

  private final UserMapper userMapper;

  @GetMapping
  @Operation(tags = "分页查询用户")
  @RequiresPermissions("user:query")
  @Log("查询用户")
  public ResponseEntity<?> findByPage(Pageable pageable, UserQuery userQuery) {
    return ResponseEntity.ok(
        userService.findByPage(pageable, userQuery).map(userMapper::mapUserDto));
  }

  @PostMapping
  @Operation(tags = "创建用户")
  @RequiresPermissions("user:create")
  @Log("创建用户")
  public ResponseEntity<?> create(@RequestBody @Validated(Create.class) UserBody userBody) {
    userService.create(userMapper.mapUser(userBody));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping
  @Operation(tags = "修改用户")
  @RequiresPermissions("user:modify")
  @Log("修改用户")
  public ResponseEntity<?> modify(@RequestBody @Validated(Update.class) UserBody userBody) {
    userService.update(userMapper.mapUser(userBody));
    return ResponseEntity.ok().build();
  }

  @PutMapping("/me/passwords")
  @Operation(tags = "用户修改自己的密码")
  @Log("修改自己密码")
  public ResponseEntity<?> modifySelfPass(@RequestBody @Valid ModifyPassDto modifyPassDto) {
    userService.modifyPass(modifyPassDto);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/passwords")
  @Operation(tags = "管理员修改用户的密码")
  @RequiresPermissions("user:password:modify")
  @Log("修改用户密码")
  public ResponseEntity<?> modifyPass(@RequestBody @Valid ForceModifyPassDto modifyPassDto) {
    userService.forceModifyPass(modifyPassDto.getUsername(), modifyPassDto.getNewPass());
    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  @Operation(tags = "管理员删除用户")
  @RequiresPermissions("user:remove")
  @Log("删除用户")
  public ResponseEntity<?> remove(@RequestParam Set<Long> ids) {
    userService.deleteByIds(ids);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
