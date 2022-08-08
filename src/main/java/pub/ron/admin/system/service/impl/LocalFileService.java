package pub.ron.admin.system.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
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
  public LocalFileService(@Value("upload-directory") String uploadDirectory) {
    this.uploadDirectory = uploadDirectory;
  }


  @Override
  public StorageResult storage(MultipartFile file) {
    try {
      InputStream inputStream = file.getInputStream();
      String path = generatePath(file.getName());
      Files.copy(inputStream, Paths.get(uploadDirectory, path));
      return new StorageResult(file.getName(), path);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static String generatePath(String fileName) {
    String uuid = UUID.randomUUID().toString();
    LocalDate now = LocalDate.now();

    String extension = FilenameUtils.getExtension(fileName);

    return now.getYear() + File.separator + now.getMonthValue() + File.separator
        + now.getDayOfMonth() + File.separator + uuid + '.' + extension;
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
}
