package rocket.starter.system.service.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import rocket.starter.system.body.DeptBody;
import rocket.starter.system.domain.Dept;
import rocket.starter.system.dto.DeptDto;

/**
 * 部门映射.
 *
 * @author ron 2022/8/12
 */
@Component
public class DeptMapper {

  /**
   * 转换到dto.
   *
   * @param dept dept
   * @return dto
   */
  public DeptDto mapDto(Dept dept) {
    DeptDto deptDto = new DeptDto();
    deptDto.setId(dept.getId());
    deptDto.setName(dept.getName());
    deptDto.setParentId(dept.getParent() == null ? null : dept.getParent().getId());
    deptDto.setDisabled(dept.getDisabled());
    deptDto.setOrderNo(dept.getOrderNo());
    deptDto.setChildren(new ArrayList<>());
    deptDto.setId(dept.getId());
    deptDto.setCreateBy(dept.getCreateBy());
    deptDto.setCreateTime(dept.getCreateTime());
    deptDto.setModifyBy(dept.getModifyBy());
    deptDto.setModifyTime(dept.getModifyTime());
    return deptDto;
  }

  public List<DeptDto> mapDto(List<Dept> dept) {
    return dept.stream().map(this::mapDto).collect(Collectors.toList());
  }


  /**
   * 转换到dept.
   *
   * @param deptBody deptBody
   * @return dept
   */
  public Dept mapDept(DeptBody deptBody) {
    Dept dept = new Dept();
    dept.setName(deptBody.getName());
    dept.setOrderNo(deptBody.getOrderNo());
    dept.setDisabled(deptBody.getDisabled());
    dept.setParent(deptBody.getId() == null ? null :
        new Dept(deptBody.getId()));
    dept.setPath(deptBody.getPath());
    dept.setId(deptBody.getId());
    return dept;
  }

}
