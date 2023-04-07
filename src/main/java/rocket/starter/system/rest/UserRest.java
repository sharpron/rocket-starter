package rocket.starter.system.rest;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.core.io.Resource;
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
import rocket.starter.common.utils.ExcelUtils;
import rocket.starter.common.validator.Create;
import rocket.starter.common.validator.Update;
import rocket.starter.logging.Log;
import rocket.starter.system.body.UserBaseBody;
import rocket.starter.system.body.UserBody;
import rocket.starter.system.dto.ForceModifyPassDto;
import rocket.starter.system.dto.ModifyPassDto;
import rocket.starter.system.dto.UserDto;
import rocket.starter.system.dto.UserQuery;
import rocket.starter.system.security.UserLocker;
import rocket.starter.system.service.UserService;
import rocket.starter.system.service.mapper.UserMapper;

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

  private final UserLocker userLocker;

  /**
   * 分页查询用户.
   *
   * @param pageable  分页参数
   * @param userQuery 查询条件
   * @return 数据
   */
  @GetMapping
  @Operation(tags = "分页查询用户")
  @RequiresPermissions("user:query")
  @Log("查询用户")
  public ResponseEntity<?> findByPage(Pageable pageable, UserQuery userQuery) {
    return ResponseEntity.ok(
        userService.findByPage(pageable, userQuery).map(user -> {
          UserDto userDto = userMapper.mapUserDto(user);
          userDto.setLocked(userLocker.isLocked(userDto.getUsername()));
          return userDto;
        }));
  }

  /**
   * 下载excel格式的数据.
   *
   * @return 资源
   */
  @GetMapping("excels")
  @RequiresPermissions("user:query")
  @Log("用户导出")
  public ResponseEntity<Resource> getAsExcel(UserQuery userQuery) {
    List<String[]> data = userService.findAll(userQuery).stream()
        .map(e -> new String[]{
            e.getUsername(), e.getNickname(), e.getMobile(),
            e.getEmail(), ExcelUtils.formatValue(userLocker.isLocked(e.getUsername())),
            ExcelUtils.formatValue(e.getDisabled()), e.getDept().getName()})
        .collect(Collectors.toList());
    Resource resource = ExcelUtils.getExcelResource(
        new String[]{"用户名", "昵称", "手机号", "邮箱", "是否锁定", "是否禁用", "所在部门"}, data);
    return ExcelUtils.buildResponse(resource);
  }

  @PostMapping
  @Operation(tags = "创建用户")
  @RequiresPermissions("user:create")
  @Log("创建用户")
  public ResponseEntity<?> create(
      @RequestBody @Validated({Default.class, Create.class}) UserBody userBody) {
    userService.create(userMapper.mapUser(userBody));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping
  @Operation(tags = "修改用户")
  @RequiresPermissions("user:modify")
  @Log("修改用户")
  public ResponseEntity<?> modify(
      @RequestBody @Validated({Default.class, Update.class}) UserBody userBody) {
    userService.update(userMapper.mapUser(userBody));
    return ResponseEntity.ok().build();
  }

  @PutMapping("/me")
  @Operation(tags = "用户修改自己信息")
  @Log("用户修改自己信息")
  public ResponseEntity<?> modifySelfInfo(@RequestBody @Validated UserBaseBody userBaseBody) {
    userService.modifyUserBase(userBaseBody);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/me/passwords")
  @Operation(tags = "用户修改自己的密码")
  @Log("修改自己密码")
  public ResponseEntity<?> modifySelfPass(@RequestBody @Validated ModifyPassDto modifyPassDto) {
    userService.modifyPass(modifyPassDto);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/passwords")
  @Operation(tags = "管理员修改用户的密码")
  @RequiresPermissions("user:password:modify")
  @Log("修改用户密码")
  public ResponseEntity<?> modifyPass(@RequestBody @Validated ForceModifyPassDto modifyPassDto) {
    userService.forceModifyPass(modifyPassDto.getUserId(), modifyPassDto.getNewPass());
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/locked")
  @Operation(tags = "管理员解锁指定用户")
  @RequiresPermissions("user:locked")
  @Log("管理员解锁指定用户")
  public ResponseEntity<?> unlock(@RequestParam String username) {
    userLocker.unlock(username);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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
