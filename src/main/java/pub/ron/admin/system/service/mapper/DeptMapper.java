package pub.ron.admin.system.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pub.ron.admin.system.body.DeptBody;
import pub.ron.admin.system.domain.Dept;
import pub.ron.admin.system.dto.DeptDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeptMapper {

  @Mapping(target = "children", expression = "java(new java.util.LinkedList())")
  @Mapping(target = "parentId", source = "parent.id")
  DeptDto mapDto(Dept dept);

  @Mapping(target = "parent.id", source = "parentId")
  Dept mapDept(DeptBody deptBody);
}
