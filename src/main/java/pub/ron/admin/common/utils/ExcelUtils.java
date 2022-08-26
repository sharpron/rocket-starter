package pub.ron.admin.common.utils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

/**
 * excel 常用工具.
 *
 * @author ron 2022/8/6
 */
public class ExcelUtils {

  private static final String LINE_SEPARATOR = "\n";

  private ExcelUtils() {

  }

  /**
   * 构建响应.
   *
   * @param resource 资源
   * @return 结果
   */
  public static ResponseEntity<Resource> buildResponse(Resource resource) {
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType("application/octet-stream"))
        .body(resource);
  }

  /**
   * 构建响应.
   *
   * @param header      header
   * @param dataFetcher dataFetcher
   * @return 结果
   */
  public static ResponseEntity<StreamingResponseBody> buildResponse(
      String[] header, DataFetcher dataFetcher) {
    return ResponseEntity.ok()
        .body(buildResponseBody(header, dataFetcher));
  }

  /**
   * 创建excel数据.
   *
   * @param header 表头
   * @param data   数据
   * @return excel
   */
  public static Resource getExcelResource(String[] header,
                                          List<String[]> data) {
    String headerLine = StringUtils.arrayToCommaDelimitedString(header);
    String dataLines = data.stream()
        .map(e -> Arrays.stream(e).map(ExcelUtils::handleCsvValue).toArray())
        .map(StringUtils::arrayToCommaDelimitedString)
        .collect(Collectors.joining(LINE_SEPARATOR));

    String lines = (headerLine + LINE_SEPARATOR + dataLines);
    return new ByteArrayResource(lines.getBytes(StandardCharsets.UTF_8));
  }

  public static String formatValue(Boolean value) {
    return Boolean.TRUE.equals(value) ? "是" : "否";
  }


  /**
   * 数据分页查询器.
   */
  @FunctionalInterface
  public interface DataFetcher {

    Page<String[]> fetch(Long lastId);
  }

  private static StreamingResponseBody buildResponseBody(String[] header, DataFetcher dataFetcher) {
    return outputStream -> {
      outputStream.write(
          StringUtils.arrayToCommaDelimitedString(header).getBytes(StandardCharsets.UTF_8));
      Long lastId = null;
      while (true) {
        outputStream.write(LINE_SEPARATOR.getBytes(StandardCharsets.UTF_8));
        Page<String[]> page = dataFetcher.fetch(lastId);
        List<String[]> content = page.getContent();
        if (content.isEmpty()) {
          break;
        }
        String rows = page.stream()
            .map(e -> StringUtils.arrayToCommaDelimitedString(
                Arrays.stream(e).skip(1).map(ExcelUtils::handleCsvValue).toArray()))
            .collect(Collectors.joining(LINE_SEPARATOR));
        outputStream.write(rows.getBytes(StandardCharsets.UTF_8));
        lastId = Long.valueOf(page.getContent().get(page.getContent().size() - 1)[0]);
      }
    };
  }

  private static String handleCsvValue(String cellValue) {
    return cellValue == null ? "" : "\"" + cellValue + "\"";
  }
}
