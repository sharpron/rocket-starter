package rocket.starter.system.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import rocket.starter.system.dto.StorageResult;

/**
 * 文件存储服务.
 *
 * @author ron 2022/8/6
 */
public interface FileService {

  /**
   * 存储文件.
   *
   * @param file 文件
   */
  StorageResult storage(MultipartFile file);

  /**
   * 获取资源.
   *
   * @param path 路径
   * @return 文件资源
   */
  Resource getResource(String path);

  /**
   * 删除文件.
   *
   * @param path 路径
   */
  void delete(String path);
}
