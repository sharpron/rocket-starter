package pub.ron.admin.system.rest;

import pub.ron.admin.system.body.CreateUserBody;
import pub.ron.admin.system.body.ModifyUserBody;
import pub.ron.admin.system.dto.ForceModifyPassDto;
import pub.ron.admin.system.dto.ModifyPassDto;
import pub.ron.admin.system.dto.UserQuery;
import pub.ron.admin.system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.ron.admin.system.service.mapper.UserMapper;

/**
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
  public ResponseEntity<?> findByPage(Pageable pageable, UserQuery userQuery) {
    return ResponseEntity.ok(
        userService.findByPage(pageable, userQuery).map(userMapper::mapUserDto)
    );
  }

  @PostMapping
  @Operation(tags = "创建用户")
  @RequiresPermissions("user:create")
  public ResponseEntity<?> create(
      @Valid @RequestBody CreateUserBody createUser) {
    userService.create(userMapper.mapUser(createUser));
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .build();
  }

  @PutMapping
  @Operation(tags = "修改用户")
  @RequiresPermissions("user:modify")
  public ResponseEntity<?> modify(
      @RequestBody @Valid ModifyUserBody modifyUserBody) {
    userService.update(userMapper.mapUser(modifyUserBody));
    return ResponseEntity.ok().build();
  }

  @PutMapping("/me/passwords")
  @Operation(tags = "用户修改自己的密码")
  public ResponseEntity<?> modifySelfPass(
      @RequestBody @Valid ModifyPassDto modifyPassDto) {
    userService.modifyPass(modifyPassDto);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/passwords")
  @Operation(tags = "管理员修改用户的密码")
  @RequiresPermissions("user:password:modify")
  public ResponseEntity<?> modifyPass(
      @RequestBody @Valid ForceModifyPassDto modifyPassDto) {
    userService.forceModifyPass(
        modifyPassDto.getUsername(),
        modifyPassDto.getNewPass());
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  @Operation(tags = "管理员删除用户")
  @RequiresPermissions("user:remove")
  public ResponseEntity<?> remove(
      @PathVariable Long id) {
    userService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
