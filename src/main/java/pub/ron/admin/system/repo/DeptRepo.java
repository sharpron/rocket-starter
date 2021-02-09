package pub.ron.admin.system.repo;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pub.ron.admin.common.BaseRepo;
import pub.ron.admin.system.domain.Dept;

/**
 * @author ron 2020/11/19
 */
@Repository
public interface DeptRepo extends BaseRepo<Dept> {

  /**
   * 通过路径查询子部门
   *
   * @param path 路径
   * @return 结果
   */
  @Query("from Dept where path like :#{#path + '%'} order by orderNo")
  List<Dept> findByPath(String path);

  /**
   * 获取路径
   *
   * @param id id
   * @return 路径
   */
  @Query("select path from Dept where id=?1")
  String getPath(Long id);

}
