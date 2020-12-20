package pub.ron.admin.system.repo;

import pub.ron.admin.system.domain.Dept;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author ron 2020/11/19
 */
@Repository
public interface DeptRepo extends JpaRepository<Dept, Long> {

  /**
   * 通过路径查询子部门
   *
   * @param path 路径
   * @return 结果
   */
  @Query("from Dept where path like :#{#path + '%'} order by orderNo")
  List<Dept> findByPath(String path);

  long countByParent_Id(Long parentId);

  @Query("select path from Dept where id=?1")
  String getPath(Long id);
}
