package rocket.starter.system.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import rocket.starter.system.dto.StorageResult;
import rocket.starter.system.service.FileService;

/**
 * 文件上传和下载.
 *
 * @author ron 2022/8/6
 */
@RestController
@RequestMapping("/api/files")
@Tag(name = "文件管理")
@RequiredArgsConstructor
@Slf4j
public class FileRest {

  private final FileService fileService;


  /**
   * 上传文件.
   *
   * @param file 文件
   * @return 上传结果
   */
  @Operation(summary = "上传文件")
  @PostMapping
  public ResponseEntity<StorageResult> uploadFile(@RequestParam("file") MultipartFile file) {
    StorageResult storage = fileService.storage(file);
    return ResponseEntity.ok(storage);
  }

  /**
   * 下载文件.
   *
   * @param path    路径
   * @param request 请求
   * @return 文件
   */
  @Operation(summary = "下载文件")
  @GetMapping
  public ResponseEntity<Resource> downloadFile(@RequestParam String path,
                                               HttpServletRequest request) {
    // Load file as Resource
    Resource resource = fileService.getResource(path);


    // Try to determine file's content type
    String contentType = null;
    try {
      contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
    } catch (IOException ex) {
      log.info("Could not determine file type.");
    }

    // Fallback to the default content type if type could not be determined
    if (contentType == null) {
      contentType = "application/octet-stream";
    }

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
  }
}
