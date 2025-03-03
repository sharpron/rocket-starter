package rocket.starter.system.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rocket.starter.system.dto.StorageResult;
import rocket.starter.system.service.FileService;

/**
 * 本地文件处理.
 *
 * @author ron 2022/8/6
 */
@Service
public class LocalFileService implements FileService {

  private final Path uploadPath;

  @Autowired
  public LocalFileService(@Value("${upload-directory}") String uploadDirectory) {
    this.uploadPath = Paths.get(uploadDirectory).toAbsolutePath().normalize();
  }

  @Override
  public StorageResult storage(MultipartFile file) {
    try {
      String rawFileName = file.getOriginalFilename();
      InputStream inputStream = file.getInputStream();
      String relativePath = generateRelativePath();
      Path path = ensurePathExists(relativePath);

      String newFileName = generateNewFileName(rawFileName);
      Path filePath = path.resolve(newFileName);
      Files.copy(inputStream, filePath);

      return new StorageResult(rawFileName, relativePath + newFileName);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 获取完整路径.
   *
   * @param relativePath relativePath
   * @return 完整路径
   */
  private Path getFullPath(String relativePath) {
    Path targetPath = uploadPath.resolve(relativePath).normalize();

    // 确保路径在上传路径内
    if (!targetPath.startsWith(uploadPath)) {
      throw new SecurityException("非法访问: " + relativePath);
    }
    return targetPath;
  }

  private Path ensurePathExists(String relativePath) {
    Path path = getFullPath(relativePath);
    if (!Files.exists(path)) {
      try {
        return Files.createDirectories(path);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return path;
  }

  private static String generateNewFileName(String fileName) {
    String extension = FilenameUtils.getExtension(fileName);
    String uuid = UUID.randomUUID().toString();
    return uuid + '.' + extension;
  }

  private static String generateRelativePath() {
    LocalDate now = LocalDate.now();
    return String.format("%d/%d/%d/", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
  }

  @Override
  public Resource getResource(String path) {
    return new FileSystemResource(getFullPath(path));
  }

  @Override
  public void delete(String path) {
    try {
      Files.delete(getFullPath(path));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
