package pub.ron.admin.system.service.impl;

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
import pub.ron.admin.system.dto.StorageResult;
import pub.ron.admin.system.service.FileService;

/**
 * 本地文件处理.
 *
 * @author ron 2022/8/6
 */
@Service
public class LocalFileService implements FileService {


  private final String uploadDirectory;

  @Autowired
  public LocalFileService(@Value("${upload-directory}") String uploadDirectory) {
    this.uploadDirectory = uploadDirectory;
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

  private Path ensurePathExists(String relativePath) {
    Path path = Paths.get(uploadDirectory, relativePath);
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
    return new FileSystemResource(Paths.get(uploadDirectory, path));
  }

  @Override
  public void delete(String path) {
    try {
      Files.delete(Paths.get(uploadDirectory, path));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) throws IOException {
    Files.createDirectory(Paths.get("./files/"));
  }
}
