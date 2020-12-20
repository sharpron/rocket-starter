package pub.ron.admin.system.service.mapper;

import pub.ron.admin.system.body.DeptBody;
import pub.ron.admin.system.domain.Dept;
import pub.ron.admin.system.dto.DeptDto;
import pub.ron.admin.system.dto.DeptNode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DeptMapper {

  @Mapping(target = "children", expression = "java(new java.util.LinkedList())")
  DeptNode mapNode(Dept dept);

  @Mapping(target = "children", expression = "java(new java.util.LinkedList())")
  DeptDto mapDto(Dept dept);

  @Mapping(target = "parent.id", source = "parentId")
  Dept mapDept(DeptBody deptBody);
}
